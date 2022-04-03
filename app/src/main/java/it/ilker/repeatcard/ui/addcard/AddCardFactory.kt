package it.ilker.repeatcard.ui.addcard

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import it.ilker.repeatcard.navigation.NavFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.ilker.design.Error
import me.ilker.design.Loading
import org.koin.androidx.compose.viewModel

@ExperimentalPermissionsApi
@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
object AddCardFactory : NavFactory {
    @Composable
    override fun Create(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController
    ) {
        val vm by viewModel<AddCardViewModel>()
        val state = vm.state.collectAsState().value
        val externalStoragePermissionState = rememberPermissionState(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        when (externalStoragePermissionState.status) {
            is PermissionStatus.Denied -> SideEffect {
                externalStoragePermissionState.launchPermissionRequest()
            }
        }

        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicturePreview()
        ) { bitmap -> vm.setImage(bitmap) }

        when {
            state.error != null -> Error(
                modifier = Modifier.fillMaxSize()
            )
            state.isLoading -> Loading(
                modifier = Modifier.fillMaxSize()
            )
            state.cardCreated -> navController.popBackStack()
            else -> AddCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp),
                title = state.title,
                onTitleChanged = vm::setTitle,
                onSelectImage = { launcher.launch() },
                selectedImage = state.image?.asImageBitmap(),
                onAdded = vm::addCard
            ) { navController.popBackStack() }
        }
    }
}
