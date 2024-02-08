package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.koin_compose_utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import com.hoc081098.kmp.viewmodel.Closeable
import com.hoc081098.solivagant.navigation.BaseRoute
import com.hoc081098.solivagant.navigation.rememberCloseableOnRoute
import org.koin.compose.getKoin
import org.koin.core.Koin
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.module.Module

/**
 * Load and remember Modules & run CompositionKoinModuleLoader to handle scope closure
 *
 * @param unloadModules : unload loaded modules on onForgotten or onAbandoned event
 * @return true after modules are loaded, false if not.
 * @author Arnaud Giuliani
 */
@Composable
inline fun rememberKoinModulesOnRoute(
  route: BaseRoute,
  koin: Koin = getKoin(),
  unloadModules: Boolean = false,
  crossinline modules: @DisallowComposableCalls () -> List<Module> = { emptyList() },
): State<Boolean> {
  val currentUnloadModules by rememberUpdatedState(unloadModules)

  val compositionKoinModuleLoader =
    rememberCloseableOnRoute(route) {
      CompositionKoinModuleLoader(
        modules = modules(),
        koin = koin,
        unloadOnClose = currentUnloadModules,
        route = route,
      )
    }

  return compositionKoinModuleLoader.loadedState
}

@OptIn(KoinInternalApi::class)
@PublishedApi
internal class CompositionKoinModuleLoader(
  private val modules: List<Module>,
  private val koin: Koin,
  private val unloadOnClose: Boolean,
  private val route: BaseRoute,
) : Closeable {
  private val _loadedState = mutableStateOf(false)
  val loadedState: State<Boolean> get() = _loadedState

  init {
    koin.logger.debug("$this -> load modules route=$route")
    koin.loadModules(modules)
    _loadedState.value = true
  }

  override fun close() {
    if (unloadOnClose) {
      unloadModules()
    }
  }

  private fun unloadModules() {
    koin.logger.debug("$this -> unload modules route=$route")
    koin.unloadModules(modules)
    _loadedState.value = false
  }
}
