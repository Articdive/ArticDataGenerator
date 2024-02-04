group = "de.articdive"
version = "1.0"

allprojects {
    version = "1.0"
}

plugins {
    id("org.spongepowered.gradle.vanilla") version "0.2.1-SNAPSHOT" apply false
}

val supportedVersions = project.properties["supportedVersions"].toString().split(",").map(String::trim)

tasks {
    for (mcVersion in supportedVersions) {
        val compileVersions = getVersionsRequiredForCompile(mcVersion)
        if (!compileVersions.contains(mcVersion)) {
            compileVersions.add(mcVersion)
        }
        val implementedVersion = compileVersions[0]

        val outputPath =
            (findProperty("output") ?: rootDir.resolve("ArticData").resolve(mcVersion).absolutePath) as String

        // Copy the data in /includedFiles to the directories
        // Does some filtering to update versions and specific documentation.
        register<Copy>("copyExt_$mcVersion") {
            from(rootDir.resolve("includedFiles")) {
                filter(
                    org.apache.tools.ant.filters.ReplaceTokens::class, "tokens" to mapOf(
                        "mcVersion" to mcVersion,
                        "mcVersionGit" to mcVersion + if (rootDir.resolve("git_reference_id.txt").exists()) rootDir.resolve("git_reference_id.txt").readText(Charsets.UTF_8)
                            .trim() else "4661a768" // This should fail the GitHub CI, as the repo does not redeploy versions.
                    )
                )
                exclude("gradle/wrapper/gradle-wrapper.jar")
            }
            // The JAR gets corrupted when the version filter runs over it.
            from(rootDir.resolve("includedFiles")) {
                include("gradle/wrapper/gradle-wrapper.jar")
            }
            into(File(outputPath))
            filteringCharset = "UTF-8"
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
            doLast {
                val outputFile = File(outputPath)
                val readmeFile = outputFile.resolve(".github").resolve("README.md")
                var README = readmeFile.readText(Charsets.UTF_8)
                README = README.replace(
                    "@content_documentation@",
                    outputFile.resolve("CONTENT_DOCUMENTATION.md").readText(Charsets.UTF_8)
                )
                README = README.replace("@TOC@", outputFile.resolve("TOC.md").readText(Charsets.UTF_8))
                readmeFile.writeText(README, Charsets.UTF_8)
            }
        }

        register("generateData_$mcVersion") {
            group = "articdata"
            description = "Generate game data for Minecraft $mcVersion."

            // After using VanillaGradle the build process has significantly been simplified.
            // Run the DataGenerator
            dependsOn(
                project(":DataGenerator").tasks.getByName<JavaExec>("run_$implementedVersion")
                    .setArgsString("$mcVersion $outputPath")
                    // This gets a configuration containing the correct version's server JAR for the runtime classpath.
                    // Located in VersionHolder just so that it does not have to be defined in every subproject individually.
                    .classpath(project(":DataGenerator:VersionHolder").configurations.getByName("runtimeServer_$mcVersion"))
            ).finalizedBy("copyExt_$mcVersion")
        }
    }
}


// Returns a List of versions required to get data for the specified version.
fun getVersionsRequiredForCompile(version: String): ArrayList<String> {
    // IMPORTANT: THE FIRST RETURNED VERSION IS THE GENERATOR VERSION (IMPLEMENTED VERSION)
    when (version) {
        "1.16.3",
        "1.16.4",
        "1.16.5" -> {
            return arrayListOf("1.16.3")
        }
        // 1.17 (uses some 1.16.3 generators)
        "1.17",
        "1.17.1" -> {
            return arrayListOf("1.17", "1.16.3")
        }
        // 1.18 (uses some 1.16.3, 1.17 generators
        "1.18",
        "1.18.1" -> {
            return arrayListOf("1.18", "1.17", "1.16.3")
        }
        // 1.18.2 is special because of changes to Biome categories :(.
        "1.18.2" -> {
            return arrayListOf("1.18.2", "1.18", "1.17", "1.16.3")
        }

        "1.19",
        "1.19.1",
        "1.19.2" -> {
            return arrayListOf("1.19", "1.18.2", "1.18", "1.17", "1.16.3")
        }

        "1.19.3" -> {
            return arrayListOf("1.19.3")
        }

        "1.19.4" -> {
            return arrayListOf("1.19.4", "1.19.3")
        }

        "1.20",
        "1.20.1",
        -> {
            return arrayListOf("1.20", "1.19.4", "1.19.3")
        }

        "1.20.2" -> {
            return arrayListOf("1.20.2", "1.20", "1.19.4", "1.19.3")
        }

        "1.20.3",
        "1.20.4" -> {
            return arrayListOf("1.20.3", "1.20.2", "1.20", "1.19.4", "1.19.3")
        }
        // Attempt with 1.16.3
        else -> {
            return arrayListOf("1.16.3")
        }
    }
}
