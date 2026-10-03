plugins {
    id("com.android.application")
}

android {
    namespace = "com.zfloatd.albioncraftmaster"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.zfloatd.albioncraftmaster"
        minSdk = 24
        targetSdk = 35
        versionCode = 2
        versionName = "1.1.0"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.activity:activity-ktx:1.10.1")
}
