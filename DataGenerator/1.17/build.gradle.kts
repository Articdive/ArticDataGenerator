plugins {
    id("org.spongepowered.gradle.vanilla")
}

dependencies {
    implementation(project(":DataGenerator:1.16.5"))
    implementation(project(":DataGenerator:core"))
}

configurations {
    runtimeClasspath {
        // We want to exclude the 1.17 JAR from the runtime, we include the version we want in the run options.
        exclude("net.minecraft", "server")
    }
}

minecraft {
    version("1.17")
    platform(org.spongepowered.gradle.vanilla.repository.MinecraftPlatform.SERVER)
}