plugins {
    kotlin("jvm") version "1.7.20"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.jetbrains.compose") version "1.2.1"
}

sourceSets {
    main {
        java {
            srcDirs("src/main/java")
            srcDirs("src/main/kotlin")
        }
        resources {
            srcDirs("src/resources")
        }
    }
    test {
        java {
            srcDirs("src/test/java")
            srcDirs("src/test/kotlin")
        }
        resources {
            srcDirs("src/resources")
        }
    }
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
        classpath("com.android.tools.build:gradle:7.0.3")
    }
}

dependencies {
    implementation(compose.desktop.currentOs)
}

javafx {
    modules("javafx.base", "javafx.controls", "javafx.fxml")
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}
