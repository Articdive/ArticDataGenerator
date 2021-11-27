group = "de.articdive.datagen"

subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    dependencies {
        // SLF4J is the base logger for most libraries.
        "implementation"("org.slf4j:slf4j-api:1.8.0-beta4")
        "implementation"("com.google.code.gson:gson:2.8.8")
        "implementation"("io.github.classgraph:classgraph:4.8.129")
    }
}

val implementedVersions = rootProject.properties["implementedVersions"].toString().split(",").map(String::trim)
tasks {
    for (implementedVersion in implementedVersions) {
        register<JavaExec>("run_$implementedVersion") {
            dependsOn(
                project(":DataGenerator:$implementedVersion").tasks.getByName<Jar>("jar"),
            )
            mainClass.set("de.articdive.articdata.datagen.DataGen")

            javaLauncher.set(
                project(":DataGenerator:$implementedVersion").extensions.getByType<JavaToolchainService>().launcherFor(
                    project(":DataGenerator:$implementedVersion").extensions.getByType<JavaPluginExtension>().toolchain
                )
            )
            var classpath: FileCollection = project.objects.fileCollection()
            // Includes all the runtime classes that the core & version require. (DataGen classes, Logger, Gson etc.)
            // This includes the core since the versioned subprojects inherit core.
            classpath += project(":DataGenerator:$implementedVersion").configurations.getByName("runtimeClasspath")
            // Includes the generators itself.
            classpath += project(":DataGenerator:$implementedVersion").tasks.getByName<Jar>("jar").outputs.files
            // We receive the runtime JAR from the parent call in the main build.gradle.kts
            setClasspath(classpath)
        }
    }
}
