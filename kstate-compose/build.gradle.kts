plugins {
    alias(libs.plugins.kstate.android.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.github.farzadazimipour.kstate.compose"
}

dependencies {
    implementation(projects.kstate.kstateCore)

    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.compose.runtime)
}