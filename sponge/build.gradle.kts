import org.spongepowered.gradle.plugin.config.PluginLoaders

plugins {
    id("java")
    alias(libs.plugins.sponge)
}

dependencies {
    implementation(project(":common"))
    compileOnly(libs.packetevents.sponge)
}

sponge {
    apiVersion(libs.versions.sponge.api.get())

    loader {
        name(PluginLoaders.JAVA_PLAIN)
        version(version.toString())
    }

    license("UNLICENSED")

    plugin(property("id") as String) {
        displayName(property("name") as String)
        entrypoint("wtf.gacek.nosignatures.nosignatures.NoSignaturesSponge")
        description(property("description") as String)

        links {
            homepage(property("website") as String)
            source(property("vcs") as String)
            issues(property("issues") as String)
        }

        contributor(property("author") as String) {
            description("Author")
        }

        dependency("spongeapi") {
            loadOrder(org.spongepowered.plugin.metadata.model.PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
    }
}