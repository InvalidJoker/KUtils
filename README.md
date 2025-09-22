# KUtils

**KUtils** is a modular utility library built to enhance Kotlin development, particularly for Minecraft plugins using [PaperMC](https://papermc.io), but also suitable for general-purpose use. It provides helpful Kotlin extensions, DSLs, and abstractions to speed up development across multiple domains.

---

## 🧩 Modules

| Module            | Description                                    |
|-------------------|------------------------------------------------|
| `core`            | General-purpose Kotlin utilities               |
| `core-i18n`       | Internationalization utilities for Kotlin      |
| `redis`           | Redis client utilities for Kotlin              |
| `adventure`       | Adventure API utilities for Minecraft          |
| `paper`           | PaperMC-specific plugin utilities              |
| `paper-ux`        | Tools for player messaging and UX enhancements |
| `paper-inventory` | Simplified inventory GUI handling for Paper    |
| `paper-commands`  | CommandAPI utilities and enhancements          |
| `paper-mineskin`  | Integration with MineSkin for player skins     |
| `paper-luckperms` | LuckPerms API utilities for permissions        |
| `velocity`        | Utilities for Velocity proxy development       |

---

## 📦 Installation

![Latest Version](https://img.shields.io/maven-metadata/v?label=kutils-core&metadataUrl=https://maven.fsqrt.org/releases/de/joker/kutils/core/maven-metadata.xml)


```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("dev.invalidjoker.kutils:core:$jitpackVersion")
    implementation("dev.invalidjoker.kutils:core-i18n:$jitpackVersion")
    
    implementation("dev.invalidjoker.kutils:adventure:$jitpackVersion")
    
    implementation("dev.invalidjoker.kutils:paper:$jitpackVersion")
    implementation("dev.invalidjoker.kutils:paper-ux:$jitpackVersion")
    implementation("dev.invalidjoker.kutils:paper-luckperms:$jitpackVersion")
    implementation("dev.invalidjoker.kutils:paper-inventory:$jitpackVersion")
    implementation("dev.invalidjoker.kutils:paper-commands:$jitpackVersion")
    implementation("dev.invalidjoker.kutils:paper-mineskin:$jitpackVersion")
    
    implementation("dev.invalidjoker.kutils:velocity:$jitpackVersion")
}
```

> Replace `$jitpackVersion` with a commit hash or release.
> See: [JitPack Page](https://jitpack.io/#InvalidJoker/KUtils)

---

## 🤝 Contributing

We welcome:

* Issues and feature requests
* Pull requests
* Documentation improvements

👉 [GitHub Repository](https://github.com/InvalidJoker/KUtils)

---

## 🙏 Acknowledgments

* [PaperMC](https://papermc.io/) — for the plugin development foundation
* [Fruxz](https://github.com/TheFruxz) — for inspiration and supportive libraries
* [KSpigot](https://github.com/jakobkmar/KSpigot) — for initial ideas and the entire gui system