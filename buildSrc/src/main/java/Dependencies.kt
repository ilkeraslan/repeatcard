@Suppress("MemberVisibilityCanBePrivate")
object Versions {

    //app
    const val versionCode = 2 //used for local builds, CI uses codes used for published versions
    const val versionName = "0.1"

    // platform
    const val buildTools = "29.0.2"
    const val kotlin = "1.3.61"
    const val minSdk = 21
    const val targetSdk = 29

    // 3rd party
    const val detekt = "1.4.0"
    const val koin = "2.1.3"
    const val lifecycle_version = "2.2.0-rc03"
}

@Suppress("unused")
object Libs {
    const val gson = "com.google.code.gson:gson:2.8.6"
}
