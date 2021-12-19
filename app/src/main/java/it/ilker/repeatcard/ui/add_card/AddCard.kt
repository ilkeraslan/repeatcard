package it.ilker.repeatcard.ui.add_card

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AddCard(
    modifier: Modifier = Modifier,
    onAdded: (String) -> Unit = {}
) {
    val textState = remember { mutableStateOf(TextFieldValue(annotatedString = AnnotatedString(""))) }

    OutlinedTextField(
        value = textState.value,
        onValueChange = {
            textState.value = it
        }
    )

    OutlinedButton(onClick = { onAdded(textState.value.annotatedString.text) }) {
        Text(text = "Add")
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
    AddCard()
}
