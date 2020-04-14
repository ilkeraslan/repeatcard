@Suppress("MemberVisibilityCanBePrivate")
object Versions {

    //app
    const val versionCode = 2 //used for local builds, CI uses codes used for published versions
    const val versionName = "0.1"

    // platform
    const val buildTools = "29.0.2"
    const val kotlin = "1.3.70"
    const val minSdk = 21
    const val targetSdk = 29

    // 3rd party
    const val detekt = "1.4.0"
    const val koin = "2.1.3"
    const val lifecycle_version = "2.2.0-rc03"
}

object BuildLibs {
    const val detekt = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.detekt}"
    const val gradle_android = "com.android.tools.build:gradle:3.6.1"
    const val gradle_versions = "com.github.ben-manes:gradle-versions-plugin:0.20.0"
    const val jacoco = "org.jacoco:org.jacoco.core:0.8.4"
    const val kotlin_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val sonarqube = "org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:2.8"
}

@Suppress("unused")
object Libs {
    const val date = "com.jakewharton.threetenabp:threetenabp:1.2.2"
    const val google_material = "com.google.android.material:material:1.1.0-alpha07"
    const val gson = "com.google.code.gson:gson:2.8.6"
    const val koin = "org.koin:koin-android:${Versions.koin}"
    const val koinViewModel = "org.koin:koin-android-viewmodel:${Versions.koin}"
    const val lifecycle_extensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle_version}"
    const val lifecycle_viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle_version}"
    const val timber = "com.jakewharton.timber:timber:4.7.1"
}
