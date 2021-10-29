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
    var eulaCheck = false
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
            val outputFile = File(outputPath)
            val contentDocumentation = outputFile.resolve("CONTENT_DOCUMENTATION.md")
            val toc = outputFile.resolve("TOC.md")
            from(rootDir.resolve("${rootProject.projectDir}/includedFiles")) {
                filter(
                    org.apache.tools.ant.filters.ReplaceTokens::class, "tokens" to mapOf(
                        "mcVersion" to mcVersion,
                        "content_documentation" to if (contentDocumentation.exists()) contentDocumentation.readText(
                            Charsets.UTF_8
                        ) else "",
                        "TOC" to if (toc.exists()) toc.readText(Charsets.UTF_8) else ""
                    )
                )
                exclude("gradle/wrapper/gradle-wrapper.jar", "CONTENT_DOCUMENTATION.md", "TOC.md")
            }
            // The JAR gets corrupted when the version filter runs over it.
            from(rootDir.resolve("${rootProject.projectDir}/includedFiles")) {
                include("gradle/wrapper/gradle-wrapper.jar")
            }
            into(File(outputPath))
            filteringCharset = "UTF-8"
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }

        register("generateData_$mcVersion") {
            if (!eulaCheck) {
                logger.warn("Mojang requires all source-code and mappings used to be governed by the Minecraft EULA.")
                logger.warn("Please read the Minecraft EULA located at https://account.mojang.com/documents/minecraft_eula.")
                logger.warn("In order to agree to the EULA you must create a file called eula.txt with the text 'eula=true'.")
                val eulaTxt = File("${rootProject.projectDir}/eula.txt")
                logger.warn("The file must be located at '${eulaTxt.absolutePath}'.")
                if ((eulaTxt.exists() && eulaTxt.readText(Charsets.UTF_8)
                        .equals("eula=true", true)) || project.properties["eula"].toString().toBoolean()
                ) {
                    logger.warn("")
                    logger.warn("The EULA has been accepted and signed.")
                    logger.warn("")
                } else {
                    throw GradleException("Data generation has been halted as the EULA has not been signed.")
                }
                logger.warn("It is unclear if the data from the data generator also adhere to the Minecraft EULA.")
                logger.warn("Please consult your own legal team!")
                logger.warn("All data is given independently without warranty, guarantee or liability of any kind.")
                logger.warn("The data may or may not be the intellectual property of Mojang Studios.")
                logger.warn("")
                eulaCheck = true
            }
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
        "1.16",
        "1.16.1",
        "1.16.2",
        "1.16.3",
        "1.16.4",
        "1.16.5" -> {
            return arrayListOf("1.16.5")
        }
        // 1.17 (uses some 1.16.5 generators)
        "1.17",
        "1.17.1" -> {
            return arrayListOf("1.17", "1.16.5")
        }
        // 1.18 (uses some 1.16.5, 1.17 generators
        "1.18" -> {
            return arrayListOf("1.18", "1.17", "1.16.5")
        }
        // Attempt with 1.16.5
        else -> {
            return arrayListOf("1.16.5")
        }
    }
}
