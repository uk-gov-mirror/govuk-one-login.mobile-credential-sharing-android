import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`

    // list of 3rd party plugins required by the custom plugins
    listOf(
        libs.plugins.kotlin.android,
        libs.plugins.roborazzi,
    ).forEach { dependency ->
        alias(dependency) apply false
    }

    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.detekt) apply false
}

// Force safe versions for vulnerable transitive dependencies
configurations.configureEach {
    resolutionStrategy.eachDependency {
        when (requested.group) {
            "io.netty" -> useVersion(libs.versions.netty.get())
            "ch.qos.logback" -> useVersion(libs.versions.logback.get())
            "org.jdom" -> useVersion(libs.versions.jdom2.get())
            "org.bitbucket.b_c" -> useVersion(libs.versions.jose4j.get())
            "com.google.guava" -> if (requested.name == "guava") {
                useVersion(libs.versions.guava.get())
            }
            "com.google.android.gms" -> if (requested.name == "play-services-basement") {
                useVersion(libs.versions.play.services.basement.get())
            }
            "org.apache.commons" -> if (requested.name == "commons-lang3") {
                useVersion(libs.versions.commons.lang3.get())
            }
            "org.apache.httpcomponents" -> if (requested.name == "httpclient") {
                useVersion(libs.versions.httpclient.get())
            }
        }
    }
}

dependencies {
    listOf(
        libs.detekt.gradle,
        libs.junit,
        libs.org.robolectric,
        libs.sonarqube.gradle,
        libs.spotless.gradle,
    ).forEach(::api)

    listOf(
        libs.com.android.tools.build.gradle,
        libs.com.google.devtools.ksp.gradle,
        libs.org.jetbrains.kotlin.gradle,
        libs.roborazzi.gradle,
        libs.uk.gov.pipelines.plugins,
        libs.metro.gradle.plugin,
    ).forEach(::implementation)
}

kotlin {
    compilerOptions {
        apiVersion.set(KotlinVersion.KOTLIN_2_2)
        jvmTarget.set(JvmTarget.JVM_21)
    }
}
