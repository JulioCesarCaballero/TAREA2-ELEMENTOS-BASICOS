plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.gmail.juliocesar64914.interfazconviews"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.gmail.juliocesar64914.interfazconviews"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)

    // Navegación, fragments y ViewModel
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)

    // Listas, pestañas deslizables y actualizar arrastrando
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.swiperefresh)

    // Imágenes desde URL
    implementation(libs.coil.views)
    implementation(libs.coil.network)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}