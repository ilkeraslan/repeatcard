package it.ilker.repeatcard.ui.addcard

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.ilker.repeatcard.R

@Composable
fun AddCard(
    modifier: Modifier = Modifier,
    onSelectImage: () -> Unit = {},
    selectedImage: ImageBitmap? = null,
    onAdded: (String, Bitmap?) -> Unit = { _, _ -> },
    onBackPressed: () -> Unit = {}
) {
    val textState = remember {
        mutableStateOf(
            TextFieldValue(annotatedString = AnnotatedString(""))
        )
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = textState.value,
            onValueChange = {
                textState.value = it
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            selectedImage = selectedImage,
            onSelectImage = onSelectImage
        )

        Spacer(modifier = Modifier.height(20.dp))

        Buttons(
            modifier = Modifier.fillMaxWidth(),
            onBackPressed = onBackPressed,
            onAdded = onAdded,
            textState = textState,
            selectedImage = selectedImage
        )
    }
}

@Composable
private fun ColumnScope.Image(
    modifier: Modifier = Modifier,
    selectedImage: ImageBitmap?,
    onSelectImage: () -> Unit
) {
    selectedImage?.let { bitmap ->
        Image(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .align(CenterHorizontally),
            bitmap = bitmap,
            contentDescription = stringResource(id = R.string.flashcardImage)
        )
    } ?: runCatching {
        OutlinedButton(
            modifier = modifier.fillMaxWidth(),
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = Color.LightGray,
                contentColor = Color.White
            ),
            onClick = onSelectImage
        ) {
            Text(text = stringResource(id = R.string.select_image))
        }
    }
}

@Composable
private fun Buttons(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onAdded: (String, Bitmap?) -> Unit = { _, _ -> },
    textState: MutableState<TextFieldValue>,
    selectedImage: ImageBitmap?
) {
    Row(
        modifier = modifier,
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
            Text(text = stringResource(id = R.string.cancel))
        }

        Spacer(modifier = Modifier.width(10.dp))

        OutlinedButton(
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = Color.Green,
                contentColor = Color.White
            ),
            onClick = {
                onAdded(
                    textState.value.annotatedString.text,
                    selectedImage?.asAndroidBitmap()
                )
            }
        ) {
            Text(text = stringResource(id = R.string.add))
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
