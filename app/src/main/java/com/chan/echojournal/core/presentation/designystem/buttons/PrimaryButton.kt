package com.chan.echojournal.core.presentation.designystem.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chan.echojournal.core.presentation.designystem.theme.EchoJournalTheme
import com.chan.echojournal.core.presentation.designystem.theme.buttonGradient

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
        ),
        modifier = modifier
            .background(
                brush = if (enable) {
                    MaterialTheme.colorScheme.buttonGradient
                } else {
                    SolidColor(MaterialTheme.colorScheme.surfaceVariant)
                },
                shape = CircleShape
            )
    ) {
        leadingIcon?.invoke()

        if (leadingIcon != null) {
            Spacer(modifier = Modifier.width(6.dp))
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = if (enable) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline
        )
    }
}

@Preview
@Composable
private fun PrimaryButtonPreview() {
    EchoJournalTheme {
        PrimaryButton(
            text = "heelo World!",
            onClick = {},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null
                )
            }
        )
    }
}

@Preview
@Composable
private fun PrimaryDisabledButtonPreview() {
    EchoJournalTheme {
        PrimaryButton(
            text = "heelo World!",
            onClick = {},
            enable = false,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null
                )
            }
        )
    }
}