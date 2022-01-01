plugins {
    id("maven-publish")
}

tasks {
    register<Jar>("packageVersion") {
        archiveBaseName.set("articdata")
        archiveVersion.set("@mcVersion@")

        destinationDirectory.set(layout.buildDirectory.dir("dist"))
        from(project.rootProject.rootDir)
        // Exclude any gradle files
        exclude("/.gradle/", "/build/", "/gradle/", "/gradlew", "/gradlew.bat", "/build.gradle.kts", "/settings.gradle.kts", "/.github", "/LICENSE")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "de.articdive"
            artifactId = "articdata"
            version = "@mcVersion@"

            artifact(tasks.getByName("packageVersion"))
        }
    }
    repositories {
        maven {
            name = "krypton-repo"
            url = uri("https://repo.kryptonmc.org/releases")
            credentials {
                username = System.getenv("KRYPTON_REPO_CREDS_USR")
                password = System.getenv("KRYPTON_REPO_CREDS_PSW")
            }
        }
    }
}