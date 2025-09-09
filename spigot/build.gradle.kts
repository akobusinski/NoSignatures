plugins {
    id("java")
    alias(libs.plugins.blossom)
    alias(libs.plugins.run.paper)
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
}

dependencies {
    implementation(project(":common"))
    compileOnly(libs.spigot.api)
    compileOnly(libs.packetevents.spigot)
}

tasks.runServer {
    downloadPlugins {
        modrinth("packetevents", "${libs.versions.packetevents.get()}+${project.name}")
    }

    minecraftVersion(libs.versions.minecraft.get())
}

tasks.withType(xyz.jpenilla.runtask.task.AbstractRun::class) {
    javaLauncher = javaToolchains.launcherFor {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(21)
    }
    jvmArgs("-XX:+AllowEnhancedClassRedefinition")
}