rootProject.name = "ArticData"
include("Deobfuscator")
// DataGenerator
include("DataGenerator")

include("DataGenerator:core")
findProject(":DataGenerator:core")?.name = "core"

include("DataGenerator:1.16.5")
findProject(":DataGenerator:1.16.5")?.name = "1.16.5"

include("DataGenerator:1.17")
findProject(":DataGenerator:1.17")?.name = "1.17"