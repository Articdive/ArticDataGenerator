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

<dependencies>
    <dependency>
        <groupId>de.articdive</groupId>
        <artifactId>articdata</artifactId>
        <version>1.18.2-${COMMIT::8}</version>
    </dependency>
</dependencies>
```

Adding to a Gradle Project (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
}
dependencies {
    implementation("de.articdive:articdata:1.18.2-${COMMIT::8}")
}
```
Replace COMMIT::8 with the first 8 chars of the [**Generator** commit](https://github.com/Articdive/ArticDataGenerator/commits/main) you would like to use.  
Please note that GitHub shows the first 7 chars in the list view.

## Usage

The repository artifacts include the data located [here](https://github.com/Articdive/ArticData).

They include JSON files full of useful data to do with Minecraft.

## Supported Data
We emit data in two ways:
- One JSON Object with namespaced identifiers as keys for json objects. [Example](https://raw.githubusercontent.com/Articdive/ArticData/1.18.2/1_18_2_blocks.json)
- One JSON Array with many json objects. [Example](https://raw.githubusercontent.com/Articdive/ArticData/1.18.2/1_18_2_map_colors.json)

If you require any data, open a GitHub Issue and specify the data you need.

WARNING: There is no guarantee that the format of the data will stay the same.

## Maintainers

[@Articdive](https://www.github.com/Articdive/)

## Contributing

See [the contributing file](CONTRIBUTING.md)!

## License

[MIT License © Articdive ](../LICENSE)