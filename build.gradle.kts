import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.dagger.hilt.android") version "2.46" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.5.0" apply false
    id("org.jlleitschuh.gradle.ktlint-idea") version "11.5.0"
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<KtlintExtension> {
        version.set("0.49.1")
    }
}
