plugins {
    id("org.spongepowered.gradle.vanilla") version "0.2"
}

dependencies {
    implementation(project(":DataGenerator:1.16.5"))
    implementation(project(":DataGenerator:core"))
}

minecraft {
    version("1.17")
    platform(org.spongepowered.gradle.vanilla.repository.MinecraftPlatform.SERVER)
}