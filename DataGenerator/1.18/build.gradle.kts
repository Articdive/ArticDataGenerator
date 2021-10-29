plugins {
    id("org.spongepowered.gradle.vanilla")
}

dependencies {
    implementation(project(":DataGenerator:1.16.5"))
    implementation(project(":DataGenerator:1.17"))
    implementation(project(":DataGenerator:core"))
}

configurations {
    runtimeClasspath {
        // We want to exclude the 1.18 JAR from the runtime, we include the version we want in the run options.
        exclude("net.minecraft", "server")
    }
}

minecraft {
    version(rootProject.properties["1.18_alias"].toString())
    platform(org.spongepowered.gradle.vanilla.repository.MinecraftPlatform.SERVER)
}