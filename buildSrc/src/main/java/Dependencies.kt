@Suppress("MemberVisibilityCanBePrivate")
object Versions {

    //app
    const val versionCode = 6 //used for local builds, CI uses codes used for published versions
    const val versionName = "1.1.4"

    // platform
    const val buildTools = "30.0.2"
    const val kotlin = "1.5.31"
    const val minSdk = 24
    const val targetSdk = 31

    // 3rd party
    const val coroutines = "1.5.2"
    const val detekt = "1.7.4"
    const val koin = "3.1.3"
    const val lifecycle_version = "2.2.0"
    const val roomVersion = "2.4.0-alpha04"
    const val accompanist = "0.24.2-alpha"
}

object BuildLibs {
    const val detekt = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.detekt}"
    const val firebase_crashlytics = "com.google.firebase:firebase-crashlytics-gradle:2.4.1"
    const val gradle_android = "com.android.tools.build:gradle:7.0.4"
    const val gradle_versions = "com.github.ben-manes:gradle-versions-plugin:0.27.0"
    const val google_services = "com.google.gms:google-services:4.3.4"
    const val jacoco = "org.jacoco:org.jacoco.core:0.8.4"
    const val kotlin_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlin_serialization = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
    const val kotlinx_serialization = "org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.1"
    const val sonarqube = "org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:2.8"
}

@Suppress("unused")
object Libs {
    const val accompanistInsets = "com.google.accompanist:accompanist-insets:${Versions.accompanist}"
    const val accompanistPermissions = "com.google.accompanist:accompanist-permissions:${Versions.accompanist}"
    const val androidx_appcompat = "androidx.appcompat:appcompat:1.3.0"
    const val androidx_constraint = "androidx.constraintlayout:constraintlayout:2.0.0-alpha3"
    const val androidx_kotlin_extensions = "androidx.core:core-ktx:1.7.0"
    const val androidx_legacy_support = "androidx.legacy:legacy-support-v4:1.0.0"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val date = "com.jakewharton.threetenabp:threetenabp:1.2.2"
    const val glide = "com.github.bumptech.glide:glide:4.11.0"
    const val firebase_ads = "com.google.firebase:firebase-ads:18.3.0"
    const val firebase_analytics = "com.google.firebase:firebase-analytics:18.0.0"
    const val firebase_crashlytics = "com.google.firebase:firebase-crashlytics:17.3.0"
    const val google_material = "com.google.android.material:material:1.1.0-alpha07"
    const val koin = "io.insert-koin:koin-core:${Versions.koin}"
    const val koinCompose = "io.insert-koin:koin-androidx-compose:${Versions.koin}"
    const val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"
    const val kotlin_serialization = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
    const val kotlinx_serialization = "org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.1"
    const val kotlinx_serialization_json = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1"
    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val lifecycle_extensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle_version}"
    const val lifecycle_viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle_version}"
    const val liquidSwipe = "com.github.Chrisvin:LiquidSwipe:1.3"
    const val lottie = "com.airbnb.android:lottie:3.4.0"
    const val multidex = "com.android.support:multidex:1.0.3"
    const val navigation = "androidx.navigation:navigation-ui-ktx:2.2.1"
    const val navigation_fragment = "androidx.navigation:navigation-fragment-ktx:2.2.1"
    const val pageIndicator = "com.romandanylyk:pageindicatorview:1.0.3@aar"
    const val room_compiler = "androidx.room:room-compiler:${Versions.roomVersion}"
    const val room_runtime = "androidx.room:room-runtime:${Versions.roomVersion}"
    const val room_rx = "androidx.room:room-rxjava2:${Versions.roomVersion}"
    const val rx_android = "io.reactivex.rxjava2:rxandroid:2.1.1"
    const val rx_java = "io.reactivex.rxjava2:rxjava:2.2.17"
    const val room_ktx = "androidx.room:room-ktx:${Versions.roomVersion}"
    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val ucrop = "com.github.yalantis:ucrop:2.2.4"
    const val viewpager2 = "androidx.viewpager2:viewpager2:1.0.0-rc01"
}

object AndroidTestLibs {
    const val espresso_core = "androidx.test.espresso:espresso-core:3.2.0"
    const val junit = "androidx.test.ext:junit:1.1.2"
}

object TestLibs {
    const val assertk = "com.willowtreeapps.assertk:assertk:0.17"
    const val core_testing = "android.arch.core:core-testing:1.1.1"
    const val hamcrest = "org.hamcrest:hamcrest-library:2.2"
    const val junit = "junit:junit:4.13"
    const val junitJupiterEngine = "org.junit.jupiter:junit-jupiter-engine:5.7.0"
    const val junitJupiterApi = "org.junit.jupiter:junit-jupiter-api:5.7.0"
    const val junitVintageEngine = "org.junit.vintage:junit-vintage-engine:5.7.0"
    const val mockk = "io.mockk:mockk:1.10.2"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
    const val koin_test = "org.koin:koin-test:${Versions.koin}"
    const val kotlin_test = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    const val mockito_kotlin = "com.nhaarman:mockito-kotlin:1.6.0"
    const val room_testing = "androidx.room:room-testing:${Versions.roomVersion}"
}
