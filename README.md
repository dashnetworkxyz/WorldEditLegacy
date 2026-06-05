# WorldEditLegacy [![Build Status](https://jenkins.dashnetwork.xyz/job/WorldEditLegacy/badge/icon)](https://jenkins.dashnetwork.xyz/job/WorldEditLegacy/)
This is a fork of WorldEdit 6.1.10, with changes to actually compile.

## Highlights
- Support for Forge 1.8.9, 1.9.4, 1.10.2, 1.11.2, & 1.12.2 on latest version
- Updated to Gradle 9

## Using
You can get the latest version from [releases](https://github.com/dashnetworkxyz/WorldEditLegacy/releases)

## Compiling
Building is still done with `gradlew build` but there are some new quirks.

The switch to Gradle 9 means JDK 17 or newer is now required to compile.<br>
However, the compiled JAR is still compatible with Java 8.

By default, the Forge mod will be built for 1.12.2.<br>
You will need to do one of the following for other Forge versions

```
gradlew build :worldedit-forge -PmcVer=1.8.9
gradlew build :worldedit-forge -PmcVer=1.9.4
gradlew build :worldedit-forge -PmcVer=1.10.2
gradlew build :worldedit-forge -PmcVer=1.11.2
```
