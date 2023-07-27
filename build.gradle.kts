import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.46" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.5.0" apply false
    id("org.jlleitschuh.gradle.ktlint-idea") version "11.5.0"
    id("com.google.devtools.ksp") version "1.9.0-1.0.12" apply false
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<KtlintExtension> {
        version.set("0.49.1")
    }
}
