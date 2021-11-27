plugins {
    id("org.spongepowered.gradle.vanilla")
}

dependencies {
    implementation(project(":DataGenerator:core"))
}

configurations {
    runtimeClasspath {
        // We want to exclude the 1.16.5 JAR from the runtime, we include the version we want in the run options.
        exclude("net.minecraft", "server")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
        vendor.set(JvmVendorSpec.ADOPTOPENJDK)
    }
}

minecraft {
    version("1.16.5")
    platform(org.spongepowered.gradle.vanilla.repository.MinecraftPlatform.SERVER)
}