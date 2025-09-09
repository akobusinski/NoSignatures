plugins {
    id("java")
    alias(libs.plugins.run.waterfall)
}

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(project(":common"))
    compileOnly(libs.bungeecord.api)
    compileOnly(libs.packetevents.bungeecord)
}

tasks.runWaterfall {
    downloadPlugins {
        modrinth("packetevents", "${libs.versions.packetevents.get()}+${project.name}")
    }

    waterfallVersion(libs.versions.minecraft.get())
}

tasks.withType(xyz.jpenilla.runtask.task.AbstractRun::class) {
    javaLauncher = javaToolchains.launcherFor {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(21)
    }
    jvmArgs("-XX:+AllowEnhancedClassRedefinition")
}