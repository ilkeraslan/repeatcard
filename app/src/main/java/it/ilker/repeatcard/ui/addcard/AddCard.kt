package it.ilker.repeatcard.ui.addcard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AddCard(
    modifier: Modifier = Modifier,
    onSelectImage: () -> Unit = {},
    onImageSelected: ImageBitmap? = null,
    onAdded: (String) -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    val textState = remember {
        mutableStateOf(
            TextFieldValue(annotatedString = AnnotatedString(""))
        )
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.SpaceBetween) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = textState.value,
            onValueChange = {
                textState.value = it
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        onImageSelected?.let { bitmap ->
            Image(
                modifier = Modifier.align(CenterHorizontally),
                bitmap = bitmap,
                contentDescription = ""
            )
        } ?: kotlin.runCatching {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Color.LightGray,
                    contentColor = Color.White
                ),
                onClick = onSelectImage
            ) {
                Text(text = "Select an image")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Color.Red,
                    contentColor = Color.White
                ),
                onClick = onBackPressed
            ) {
                Text(text = "Cancel")
            }

            Spacer(modifier = Modifier.width(10.dp))

            OutlinedButton(
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Color.Green,
                    contentColor = Color.White
                ),
                onClick = { onAdded(textState.value.annotatedString.text) }
            ) {
                Text(text = "Add")
            }
        }
    }
}

/**
 * Previews
 */
@ExperimentalMaterialApi
@Preview(
    backgroundColor = 0xFFFFFF,
    showBackground = true
)
@Composable
private fun AddCardPreview() {
    AddCard(
        modifier = Modifier.fillMaxSize(),
    )
}
