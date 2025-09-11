import net.kyori.blossom.BlossomExtension

plugins {
    id("base")
    alias(libs.plugins.blossom)
    alias(libs.plugins.shadow)
}

group = "wtf.gacek.nosignatures"
version = "1.0.0"
description = property("description") as String

subprojects {
    apply(plugin = "java")
    apply(plugin = rootProject.libs.plugins.blossom.get().pluginId)
    apply(plugin = rootProject.libs.plugins.shadow.get().pluginId)

    group = rootProject.group
    version = rootProject.version
    description = rootProject.description

    repositories { // every subproject uses PacketEvents, so this lets apply repositories everywhere at once
        mavenCentral()
        maven("https://repo.codemc.io/repository/maven-releases/")
        maven("https://repo.codemc.io/repository/maven-snapshots/")
    }

    configure<SourceSetContainer> {
        named("main") {
            configure<BlossomExtension> {
                resources {
//                    trimNewlines = false

                    property("id",          rootProject.property("id") as String)
                    property("name",        rootProject.property("name") as String)
                    property("version",     rootProject.version as String)
                    property("description", rootProject.property("description") as String)
                    property("author",      rootProject.property("author") as String)
                    property("website",     rootProject.property("website") as String)
                }
            }
        }
    }

    tasks.named("build") {
        dependsOn("shadowJar")
    }

    tasks.withType<AbstractArchiveTask> {
        // Move into root build, instead of everything being inside their own folder
        destinationDirectory.set(rootProject.layout.buildDirectory)
        // Prepend the rootProject name to every subproject
        archiveBaseName = "${rootProject.name}-${project.name}"
    }
}

tasks.register("printVersion") {
    doLast {
        println(project.version)
    }
}
