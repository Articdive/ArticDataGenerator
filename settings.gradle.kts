rootProject.name = "ArticDataGenerator"
// DataGenerator
include("DataGenerator")

include("DataGenerator:VersionHolder")

include("DataGenerator:core")

include("DataGenerator:1.16.3")

include("DataGenerator:1.17")

include("DataGenerator:1.18")

include("DataGenerator:1.18.2")

pluginManagement {
    repositories {
        maven(url = "https://repo.spongepowered.org/repository/maven-public/")
    }
}
