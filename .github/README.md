# ArticData

[![license](https://img.shields.io/github/license/Articdive/ArticDataGenerator.svg)](../LICENSE)
[![standard-readme compliant](https://img.shields.io/badge/readme%20style-standard-brightgreen.svg)](https://github.com/RichardLitt/standard-readme)

This is a data extractor and generator for Minecraft.

It works in the JVM 16 (or higher) environment it takes advantage of [Gradle](https://gradle.org/) and is written in
Java.

## Table of Contents

- [Install](#install)
- [Usage](#usage)
- [Maintainers](#maintainers)
- [Contributing](#contributing)
- [License](#license)

## Install

### Maven and Gradle

To add ArticData (not the generators!) to your project using [Maven](http://maven.apache.org/)
or [Gradle](https://gradle.org/):

Adding to a Maven Project:

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
        <version>1.18.1</version>
    </dependency>
</dependencies>
```

Adding to a Gradle Project (Kotlin DSL)

```kotlin
repositories {
    maven("https://repo.kryptonmc.org/releases")
}
dependencies {
    implementation("de.articdive:articdata:1.18.1")
}
```

## Usage

The repository artifacts include the data located [here](https://github.com/Articdive/ArticData).

They include JSON files full of useful data to do with Minecraft.

## Supported Data
We emit data in two ways:
- One JSON Object with namespaced identifiers as keys for json objects. [Example](https://raw.githubusercontent.com/Articdive/ArticData/1.18.1/1_18_1_blocks.json)
- One JSON Array with many json objects. [Example](https://raw.githubusercontent.com/Articdive/ArticData/1.18.1/1_18_1_map_colors.json)

If you require any data, open a GitHub Issue and specify the data you need.

WARNING: There is no guarantee that the format of the data will stay the same.

## Maintainers

[@Articdive](https://www.github.com/Articdive/)

## Contributing

See [the contributing file](CONTRIBUTING.md)!

## License

[MIT License © Articdive ](../LICENSE)