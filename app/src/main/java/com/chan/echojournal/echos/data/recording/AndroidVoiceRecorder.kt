package com.chan.echojournal.echos.data.recording

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import com.chan.echojournal.echos.domain.recording.RecordingDetails
import com.chan.echojournal.echos.domain.recording.VoiceRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class AndroidVoiceRecorder(
    private val context: Context,
    private val applicationScope: CoroutineScope
) : VoiceRecorder {

    companion object {
        private const val TEMP_FILE_PREFIX = "temp_recording"
        private const val MAX_AMPLITUDE_VALUE = 26_000L
    }

    private val singleThreadDispatcher = Dispatchers.Default.limitedParallelism(1)

    private val _recordingDetails = MutableStateFlow(RecordingDetails())
    override val recordingDetails: StateFlow<RecordingDetails> = _recordingDetails.asStateFlow()

    private var tempFile = generateTempFile()

    private var recorder: MediaRecorder? = null
    private var isRecording: Boolean = false
    private val amplitudes = mutableListOf<Float>()
    private var isPaused: Boolean = false

    private var durationJob: Job? = null
    private var amplitudeJob: Job? = null

    override fun start() {
        if (isRecording) {
            return
        }

        try {
            resetSession()

            tempFile = generateTempFile()
            recorder = newMediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128 * 1000)
                setAudioSamplingRate(44100)
                setOutputFile(tempFile.absolutePath)

                prepare()
                start()
            }

            isRecording = true
            isPaused = false
            startTrackingDuration()
            startTrackingAmplitudes()
        } catch (e: IOException) {
            Timber.e(e, "Failed to start recording")
            recorder?.release()
            recorder = null
        }
    }

    private fun startTrackingDuration() {
        durationJob = applicationScope.launch {
            var lastTime = System.currentTimeMillis()
            while (isRecording && !isPaused) {
                delay(10L)
                val currentTime = System.currentTimeMillis()
                val elapsedTime = currentTime - lastTime

                _recordingDetails.update {
                    it.copy(
                        durations = it.durations + elapsedTime.milliseconds
                    )
                }
                lastTime = System.currentTimeMillis()
            }
        }
    }

    private fun startTrackingAmplitudes() {
        amplitudeJob = applicationScope.launch {
            while (isRecording) {
                val amplitude = getAmplitude()
                withContext(singleThreadDispatcher) {
                    amplitudes.add(amplitude)
                }
                delay(10L)
            }
        }
    }

    private fun getAmplitude(): Float {
        return if (isRecording) {
            try {
                val maxAmplitude = recorder?.maxAmplitude
                val amplitudeRatio = maxAmplitude?.takeIf { it > 0f }?.run {
                    (this / MAX_AMPLITUDE_VALUE.toFloat()).coerceIn(0f, 1f)
                }
                amplitudeRatio ?: 0f
            } catch (e: Exception) {
                Timber.e(e, "Faile to retrieve current amplitude.")
                0f
            }
        } else 0f
    }

    private fun generateTempFile(): File {
        val id = UUID.randomUUID().toString()
        return File(
            context.cacheDir,
            "${TEMP_FILE_PREFIX}_${id}.mp4"
        )
    }

    private fun newMediaRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }
    }

    private fun resetSession() {
        _recordingDetails.update { RecordingDetails() }
        applicationScope.launch(singleThreadDispatcher) {
            amplitudes.clear()
            cleanup()
        }
    }

    private fun cleanup() {
        Timber.d("Cleaning up voice recorder resources")
        recorder = null
        isRecording = false
        isPaused = false
        amplitudeJob?.cancel()
        durationJob?.cancel()
    }

    override fun pause() {
        if (!isRecording || isPaused) {
            return
        }
        recorder?.pause()
        isPaused = true
        durationJob?.cancel()
        amplitudeJob?.cancel()
    }

    override fun stop() {
        try {
            recorder?.apply {
                stop()
                release()
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to stop recording")
        } finally {
            _recordingDetails.update {
                it.copy(
                    amplitudes = amplitudes.toList(),
                    filePath = tempFile.absolutePath
                )
            }
            cleanup()
        }
    }

    override fun resume() {
        if (!isRecording || !isPaused) {
            return
        }
        recorder?.resume()
        isPaused = false
        startTrackingDuration()
        startTrackingAmplitudes()
    }

    override fun cancel() {
        stop()
        tempFile.delete() // 임시 파일 폐기
        resetSession()
    }
}