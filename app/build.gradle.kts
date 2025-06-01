import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.androidx.navigation.safeargs)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.jabg.modulo6p2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.jabg.modulo6p2"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if(localPropertiesFile.exists()){
            localProperties.load(localPropertiesFile.inputStream())
        }

        val mapsApiKey = localProperties.getProperty("MAPS_API_KEY")

        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //Splash
    implementation(libs.androidx.core.splashscreen)
    //Corrutinas con alcance del ViewModel
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    //LiveData (opcional para proyectos legacy)
    implementation (libs.androidx.lifecycle.livedata.core.ktx)
    //Kotlin extensions para instanciar viewmodels sin parámetros desde activities con un delegado
    implementation (libs.activity.ktx)
    //Kotlin extensions para instanciar viewmodels sin parámetros desde fragments con un delegado
    implementation (libs.androidx.fragment.ktx)
    //Para retrofit y Gson
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    //Adicional para el interceptor (sireve para conocer el status de la peticion)
    implementation(libs.logging.interceptor)
    //Glide
    implementation(libs.glide)
    //Para las corrutinas con alcance lifecycle (opcional)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    //Imágenes con bordes redondeados
    implementation(libs.roundedimageview)
    //Navegacion
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)
    //ShimmerFacebook
    implementation(libs.shimmer)
    //Videos en YouTube
    implementation(libs.core)
    implementation(libs.firebase.auth)
    // Google Maps
    implementation("com.google.android.gms:play-services-maps:19.2.0")


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}