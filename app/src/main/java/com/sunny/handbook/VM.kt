package com.sunny.handbook

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import androidx.savedstate.SavedStateRegistryOwner
import com.sunny.handbook.ui.viewmodels.HomeViewModel

@Composable
inline fun <reified VM : ViewModel> viewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    savedStateRegistryOwner: SavedStateRegistryOwner = LocalSavedStateRegistryOwner.current
): VM {
    return androidx.lifecycle.viewmodel.compose.viewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        factory = ViewModelFactory(
            owner = savedStateRegistryOwner,
            defaultArgs = (savedStateRegistryOwner as? NavBackStackEntry)?.arguments,
//            //remove this line if you're not using Dependency injection
//            dependencyContainer = [ACCESS YOUR DEPENDENCIES GRAPH HERE SOMEHOW],
        )
    )
}

class ViewModelFactory(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle?,
) : AbstractSavedStateViewModelFactory(
    owner,
    defaultArgs
) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return when (modelClass) {
            HomeViewModel::class.java -> HomeViewModel()
            else -> throw RuntimeException("Unknown view model $modelClass")
        } as T
    }
}