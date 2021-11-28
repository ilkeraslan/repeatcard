package it.ilker.repeatcard.ui.directories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.ilker.repeatcard.db.directory.Directory
import me.ilker.design.Directory

@ExperimentalMaterialApi
@Composable
internal fun Directories(
    modifier: Modifier = Modifier,
    directories: List<Directory>
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = directories,
            key = { directory -> directory.id }
        ) { directory ->
            Directory(
                modifier = Modifier.fillMaxWidth(),
                name = directory.title
            )
        }
    }
}

/**
 * Previews
 */
private val directories = listOf(
    Directory(
        id = 1,
        title = "English",
        creationDate = null
    ),
    Directory(
        id = 2,
        title = "Italian",
        creationDate = null
    ),
    Directory(
        id = 3,
        title = "Spanish",
        creationDate = null
    )
)

@ExperimentalMaterialApi
@Preview(
    backgroundColor = 0xFFFFFF,
    showBackground = true
)
@Composable
private fun DirectoriesPreview() {
    Directories(
        modifier = Modifier.fillMaxWidth(),
        directories = directories
    )
}
