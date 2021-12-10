# ArticData

[![license](https://img.shields.io/github/license/Articdive/ArticData.svg)](../LICENSE)
[![standard-readme compliant](https://img.shields.io/badge/readme%20style-standard-brightgreen.svg)](https://github.com/RichardLitt/standard-readme)

This is a data extractor and generator for Minecraft.

It works in the JVM 16 (or higher) environment it takes advantage of [Gradle](https://gradle.org/) and is written in
Java. The generators for this data are located [here](https://github.com/Articdive/ArticDataGenerator).

## Table of Contents

- [Install](#install)
- [Usage](#usage)
- [Supported Data](#supported-data)
@TOC@
- [Maintainers](#maintainers)
- [Contributing](#contributing)
- [License](#license)

## Install

### Maven and Gradle

To add ArticData (not the generators!) to your project using [Maven](http://maven.apache.org/)
or [Gradle](https://gradle.org/):

RAdding to a Maven Project:

```xml

<repositories>
    <repository>
        <id>krypton-repo</id>
        <url>https://repo.kryptonmc.org/releases</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>de.articdive</groupId>
        <artifactId>articdata</artifactId>
        <version>@mcVersion@-SNAPSHOT</version>
    </dependency>
</dependencies>
```

Adding to a Gradle Project (Kotlin DSL)

```kotlin
repositories {
    maven("https://repo.kryptonmc.org/releases")
}
dependencies {
    implementation("de.articdive:articdata:@mcVersion@-SNAPSHOT")
}
```

## Usage

The repository artifacts include the data located [here](https://github.com/Articdive/ArticData).

They include JSON files full of useful data to do with Minecraft.

## Supported Data
We emit data in two ways:
- One JSON Object with namespaced identifiers as keys for json objects. [Example](https://raw.githubusercontent.com/Articdive/ArticData/1.17.1/1_17_1_blocks.json)
- One JSON Array with many json objects. [Example](https://raw.githubusercontent.com/Articdive/ArticData/1.17.1/1_17_1_map_colors.json)

If you require any data, open a GitHub Issue and specify the data you need.

WARNING: There is no guarantee that the format of the data will stay the same.

@content_documentation@
## Maintainers

[@Articdive](https://www.github.com/Articdive/)

## Contributing

See [the contributing file](https://github.com/Articdive/ArticDataGenerator/blob/main/.github/CONTRIBUTING.md)!

## License
Data is licensed under the [Minecraft EULA](https://account.mojang.com/documents/minecraft_eula).
The rest (Format, Generators, etc.) is licensed under the [MIT License © Articdive](../LICENSE).