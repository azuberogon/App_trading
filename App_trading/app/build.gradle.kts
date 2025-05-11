import org.jetbrains.dokka.gradle.DokkaTask

// Este bloque define los plugins de Gradle que se aplican a este módulo.
// Los plugins añaden funcionalidades específicas al proceso de construcción.
plugins {
    // Plugin de aplicación Android: Indica que este módulo es una aplicación Android ejecutable.
    // Define tareas como compilar código, empaquetar APKs, etc.
    alias(libs.plugins.android.application)
    // Plugin de Kotlin para Android: Permite usar el lenguaje Kotlin en tu proyecto Android.
    // Añade tareas para compilar código Kotlin.
    alias(libs.plugins.kotlin.android)
    // Aquí estaba comentado un alias para el plugin de Google Services. Si la línea de abajo está activa, esta no es necesaria.
//    alias(libs.plugins.googleService) apply false
    // Plugin de Dokka: Genera documentación API a partir de los comentarios de tu código Kotlin.
    id("org.jetbrains.dokka") version "1.9.10" // Especifica la versión del plugin Dokka
    // Plugin de Google Services: Obligatorio para integrar Firebase y otros servicios de Google en tu app.
    // Este plugin procesa el archivo `google-services.json` para configurar tu proyecto.
    id("com.google.gms.google-services")
    // Plugin Kotlin KAPT (Kotlin Annotation Processing Tool): Permite usar procesadores de anotaciones
    // escritos en Java o Kotlin con tu código Kotlin. Es necesario para bibliotecas como Room y Glide.
    id("kotlin-kapt")

}

android {
    // Define el namespace de tu aplicación. Es el nombre único que identifica tu paquete principal.
    namespace = "com.example.app_trading"
    // Especifica la versión del API de Android (SDK) con la que se compilará tu código.
    compileSdk = 35

    // Configuración por defecto para todos los tipos de construcción (debug, release, etc.)
    defaultConfig {
        // El ID único de la aplicación. Se usa para identificar tu app en Google Play Store
        // y en el sistema Android. Debe ser único globalmente.
        applicationId = "com.example.app_trading"
        // La versión mínima del API de Android que soporta tu aplicación.
        minSdk = 24
        // La versión del API de Android con la que se probará tu aplicación.
        // Debes probar tu app en un dispositivo/emulador con esta versión para asegurar la compatibilidad.
        targetSdk = 35
        // Un número entero que representa la versión interna de la aplicación.
        // Se usa para las actualizaciones (debe ser creciente).
        versionCode = 1
        // Un nombre de versión visible para los usuarios (por ejemplo, "1.0", "Beta 2").
        versionName = "1.0"

        // El corredor de pruebas de instrumentación por defecto.
        // Se usa para ejecutar pruebas que requieren un dispositivo o emulador real.
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Define las diferentes configuraciones para construir tu aplicación (build types).
    buildTypes {
        // Configuración para la versión de 'release' (la que se suele publicar).
        release {
            // Indica si se debe minimizar el código (eliminar código no usado, acortar nombres).
            // Aquí está desactivado (false). En una app real, suele estar activado.
            isMinifyEnabled = false
            // Especifica los archivos de reglas para ProGuard o R8,
            // que se usan para minimizar, ofuscar y optimizar el código.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), // Reglas por defecto de Android
                "proguard-rules.pro" // Tus reglas personalizadas
            )
        }
        // (La configuración de 'debug' suele ser la por defecto si no se especifica aquí)
    }
    // Configuración para la compilación de código Java.
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11 // La versión del lenguaje Java que usas en tu código fuente.
        targetCompatibility = JavaVersion.VERSION_11 // La versión del bytecode Java generado.
    }
    // Configuración específica para la compilación de código Kotlin.
    kotlinOptions {
        jvmTarget = "11" // Especifica la versión de la máquina virtual Java (JVM) a la que apunta el código compilado.
    }
    // Habilita funcionalidades específicas en el proceso de construcción.
    buildFeatures {
        viewBinding = true // Habilita View Binding, una forma segura y fácil de interactuar con las vistas del layout.
    }
}

// Configura la tarea de Dokka.
tasks.withType<DokkaTask>().configureEach {
    // Define el directorio donde se generará la documentación.
    outputDirectory.set(file("docs"))
}

// Este bloque declara las dependencias de tu módulo. Son las librerías externas que tu proyecto utiliza.
dependencies {

    // Dependencias principales de AndroidX: Componentes que reemplazan las antiguas Support Libraries.
    implementation(libs.androidx.core.ktx) // Contiene extensiones de Kotlin para funciones básicas del sistema.
    implementation(libs.androidx.appcompat) // Proporciona compatibilidad con versiones anteriores para la barra de acción y temas.
    implementation(libs.material) // Librería de Google con componentes de Material Design (botones, barras, etc.).
    implementation(libs.androidx.activity) // Componentes para manejar Activities (como el resultado de otra actividad).
    implementation(libs.androidx.constraintlayout) // Para usar ConstraintLayout, un layout flexible y potente.
    implementation(libs.androidx.annotation) // Anotaciones que ayudan a detectar errores y mejorar la calidad del código.
    implementation(libs.androidx.lifecycle.livedata.ktx) // Extensiones de Kotlin para LiveData, una clase observable que mantiene un ciclo de vida consciente.
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // Extensiones de Kotlin para ViewModel, que almacena y gestiona datos relacionados con la UI de manera que sobreviven a cambios de configuración.
    implementation(libs.firebase.database) // Firebase Realtime Database (base de datos NoSQL en tiempo real).
    // Dependencias de pruebas unitarias (se ejecutan localmente en la JVM)
    testImplementation(libs.junit) // El framework de pruebas unitarias JUnit.
    // Dependencias de pruebas de instrumentación (se ejecutan en un dispositivo o emulador)
    androidTestImplementation(libs.androidx.junit) // Implementación de JUnit para pruebas de instrumentación.
    androidTestImplementation(libs.androidx.espresso.core) // Espresso, un framework para pruebas de UI.

    // Dependencias de Firebase declaradas usando el archivo libs.versions.toml
    // Esto centraliza la gestión de versiones.
    implementation(libs.firebaseAuth) // Firebase Authentication (para gestionar usuarios, registro, login).
    implementation(libs.firebaseStore) // Asumo que es Cloud Firestore, la base de datos NoSQL de Firebase (más moderna que Realtime Database).
    implementation(libs.firebaseStorage) // Firebase Storage (para almacenar y servir archivos de usuario como imágenes).

    // Dependencias de Retrofit, OkHttp y Gson para llamadas a APIs REST y manejo de JSON.
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Cliente HTTP para interactuar con APIs REST.
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Un plugin para Retrofit que usa Gson para convertir JSON a objetos Kotlin/Java.
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3") // Un interceptor para OkHttp que registra información de las peticiones HTTP, útil para depuración.
    implementation("com.squareup.okhttp3:okhttp:4.9.3") // El cliente HTTP subyacente que usa Retrofit.
    implementation ("com.google.code.gson:gson:2.8.9") // Librería Gson para trabajar con JSON (serialización/deserialización).

    // Dependencia para crear gráficos
    implementation("com.github.PhilJay:MPAndroidChart:3.1.0") // Una librería popular para dibujar gráficos (líneas, barras, pastel, etc.).

    // Dependencia de Material Design. Tienes dos versiones declaradas (1.9.0 y 1.10.0).
    // Lo ideal es tener solo una, la más reciente, para evitar conflictos.
    implementation ("com.google.android.material:material:1.9.0")

   
    //Firebase tutorial
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))
    //analisis de firebase
    implementation("com.google.firebase:firebase-analytics")
    //autenticacion de firebase
    implementation("com.google.firebase:firebase-auth:23.2.0")
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))
    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")
    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:21.3.0")


    implementation ("com.google.android.material:material:1.10.0")
    implementation("com.airbnb.android:lottie:6.1.0")


    // Dependencia de Coil
    implementation("io.coil-kt:coil:2.6.0") // O la última versión estable

    //gift para el splashScreen
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    implementation ("androidx.room:room-runtime:2.5.2")
    kapt ("androidx.room:room-compiler:2.5.2")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

}

