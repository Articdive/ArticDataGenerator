rootProject.name = "ArticData"
// DataGenerator
include("DataGenerator")

include("DataGenerator:core")
findProject(":DataGenerator:core")?.name = "core"

include("DataGenerator:1.16.5")
findProject(":DataGenerator:1.16.5")?.name = "1.16.5"

include("DataGenerator:1.17")
findProject(":DataGenerator:1.17")?.name = "1.17"

include("DataGenerator:1.18")
findProject(":DataGenerator:1.18")?.name = "1.18"

pluginManagement {
    repositories {
        maven(url = "https://repo.spongepowered.org/repository/maven-public/")
    }
}
