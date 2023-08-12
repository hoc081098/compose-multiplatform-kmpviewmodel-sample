package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.BuildKonfig
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.common.rememberKoinModules
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data.dataModule
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.domainModule
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.presentation.SearchPhotoScreen
import org.koin.dsl.module
import kotlin.jvm.JvmField

@JvmField
internal val FeatureSearchPhotoModule = module {
  includes(
    dataModule(),
    domainModule(),
  )
}

@Composable
internal fun SearchPhotoScreenWithKoin(
  modifier: Modifier = Modifier,
) {
  val loaded = rememberKoinModules(unloadModules = true) { listOf(FeatureSearchPhotoModule) }

  if (loaded) {
    SearchPhotoScreen(
      modifier = modifier
    )
  }
}

enum class BuildFlavor {
  DEV,
  PROD;

  companion object {
    val Current: BuildFlavor by lazy {
      when (BuildKonfig.FLAVOR) {
        "dev" -> DEV
        "prod" -> PROD
        else -> error("Unknown flavor ${BuildKonfig.FLAVOR}")
      }
    }
  }
}

expect fun isDebug(): Boolean