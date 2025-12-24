plugins {
    alias(libs.plugins.kstate.android.library)
}

android {
    namespace = "com.github.farzadazimipour.kstate.viewmodel"
}

dependencies {
    implementation(projects.kstate.kstateCore)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
}