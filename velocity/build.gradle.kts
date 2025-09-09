plugins {
    id("java")
    alias(libs.plugins.run.velocity)
    alias(libs.plugins.blossom)
}

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    implementation(project(":common"))
    compileOnly(libs.velocity.api)
    annotationProcessor(libs.velocity.api)
    compileOnly(libs.packetevents.velocity)
}

sourceSets {
    main {
        blossom {
            javaSources {
                property("id",          rootProject.property("id") as String)
                property("name",        rootProject.property("name") as String)
                property("version",     rootProject.version as String)
                property("description", rootProject.property("description") as String)
                property("author",      rootProject.property("author") as String)
            }
        }
    }
}

tasks.runVelocity {
    downloadPlugins {
        modrinth("packetevents", "${libs.versions.packetevents.get()}+${project.name}")
    }

    velocityVersion(libs.versions.velocity.api.get())
}

tasks.withType(xyz.jpenilla.runtask.task.AbstractRun::class) {
    javaLauncher = javaToolchains.launcherFor {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(21)
    }
    jvmArgs("-XX:+AllowEnhancedClassRedefinition")
}