plugins {
    kotlin("jvm") version "1.7.21"
    id("org.openjfx.javafxplugin") version "0.0.13"
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
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
        classpath("com.android.tools.build:gradle:7.0.3")
    }
}

javafx {
    modules("javafx.base", "javafx.controls", "javafx.fxml")
}

allprojects {
    repositories {
        mavenCentral()
    }
}
