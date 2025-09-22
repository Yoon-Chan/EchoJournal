package com.chan.echojournal.echos.presentation.util

import org.junit.Assert.assertEquals
import org.junit.Test


class AmplitudeNormalizerTest {

    @Test
    fun sourceLowerThanTargetSize() {
        val source = listOf<Float>(0f, 0.1f, 0f)

        val result = AmplitudeNormalizer.normalize(
            source,
            27f,
            2f,
            1f
        )

        assertEquals(result, listOf<Float>(
//            0.0f, 0.025f, 0.05f, 0.075f, 0.1f, 0.075f, 0.05f, 0.025f, 0.0f
            0.25f, 0.25f, 0.25f, 0.25f, 0.25f, 0.25f, 0.25f, 0.25f, 0.25f
        ))
    }
}