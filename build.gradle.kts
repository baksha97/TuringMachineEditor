plugins {
    id("org.openjfx.javafxplugin") version "0.0.13"
}

java.sourceSets["main"].java {
    srcDir("src/main")
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

//mainClassName='src.Main'
//jar {
//    manifest {
//        attributes 'Main-Class': 'src.Main'
//    }
//    from {
//        configurations.compile.collect { it.isDirectory() ? it : zipTree(it) }
//    }
//}
