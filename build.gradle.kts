plugins {
    id("com.android.application") version "7.2.2" apply false
    id("com.android.library") version "7.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.3.0" apply false
    id("io.gitlab.arturbosch.detekt") version "1.11.1"
    id("org.jlleitschuh.gradle.ktlint") version "9.3.0"
    id("com.github.ben-manes.versions") version "0.29.0"
}

subprojects {
    apply {
        plugin("org.jlleitschuh.gradle.ktlint")
        plugin("io.gitlab.arturbosch.detekt")
    }
    ktlint {
        version.set("0.37.2")
        verbose.set(true)
        android.set(true)
    }
    detekt {
        reports {
            html {
                enabled = true
                destination = file("build/reports/detekt.html")
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
