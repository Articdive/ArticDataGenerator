plugins {
    id("org.spongepowered.gradle.vanilla")
}

dependencies {
    implementation(project(":DataGenerator:core"))
    implementation(project(":DataGenerator:1.19.3"))
    implementation(project(":DataGenerator:1.20"))
}

configurations {
    runtimeClasspath {
        // We want to exclude the 1.18 JAR from the runtime, we include the version we want in the run options.
        exclude("net.minecraft", "server")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

minecraft {
    version("1.20.2")
    platform(org.spongepowered.gradle.vanilla.repository.MinecraftPlatform.SERVER)
    runs {
        server()
    }
}