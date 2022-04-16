plugins {
    id("org.spongepowered.gradle.vanilla")
}

val supportedVersions = rootProject.properties["supportedVersions"].toString().split(",").map(String::trim)
configurations {
    for (mcVersion in supportedVersions) {
        create("runtimeServer_$mcVersion")
    }
}

dependencies {
    for (mcVersion in supportedVersions) {
        var vanillaReferenceVersion = mcVersion
        val a = rootProject.properties[mcVersion + "_alias"]
        if (a != null) {
            vanillaReferenceVersion = a.toString();
        }
        "runtimeServer_$mcVersion"("net.minecraft:server:$vanillaReferenceVersion")
    }
}

minecraft {
    // Will be mostly ignored but it has to be defined (VanillaGradle wants it for whatever reason)
    version("1.16.3")
    platform(org.spongepowered.gradle.vanilla.repository.MinecraftPlatform.SERVER)
}