plugins {
    id("java")
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.blossom)
}

dependencies {
    implementation(project(":common"))
    shadow(project(":common"))
    compileOnly(libs.packetevents.fabric)

    minecraft("com.mojang:minecraft:${libs.versions.minecraft.get()}")
    mappings("net.fabricmc:yarn:${libs.versions.fabric.yarn.get()}:v2")

    modCompileOnly("net.fabricmc:fabric-loader:${libs.versions.fabric.loader.get()}")
    modCompileOnly("net.fabricmc.fabric-api:fabric-api:${libs.versions.fabric.api.get()}")
}

sourceSets {
    main {
        blossom {
            resources {
                property("fabric_loader_version", rootProject.libs.versions.fabric.loader.get())
                property("minimum_minecraft_version", "1.19")
            }
        }
    }
}

tasks.shadowJar {
    configurations = emptyList()

    from(project(":common").tasks.jar.get().archiveFile.map { zipTree(it) })
}