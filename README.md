# KUtils

**KUtils** is a modular utility library built to enhance Kotlin development, particularly for Minecraft plugins using [PaperMC](https://papermc.io), but also suitable for general-purpose use. It provides helpful Kotlin extensions, DSLs, and abstractions to speed up development across multiple domains.

---

## 🧩 Modules

| Module            | Description                                    |
|-------------------|------------------------------------------------|
| `core`            | General-purpose Kotlin utilities               |
| `core-i18n`       | Internationalization utilities for Kotlin      |
| `paper`           | PaperMC-specific plugin utilities              |
| `paper-ux`        | Tools for player messaging and UX enhancements |
| `paper-inventory` | Simplified inventory GUI handling for Paper    |
| `paper-commands`  | CommandAPI utilities and enhancements          |

---

## 📦 Installation

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.InvalidJoker.KUtils:core:$jitpackVersion")
    implementation("com.github.InvalidJoker.KUtils:core-i18n:$jitpackVersion")
    implementation("com.github.InvalidJoker.KUtils:paper:$jitpackVersion")
    implementation("com.github.InvalidJoker.KUtils:paper-ux:$jitpackVersion")
    implementation("com.github.InvalidJoker.KUtils:paper-inventory:$jitpackVersion")
    implementation("com.github.InvalidJoker.KUtils:paper-commands:$jitpackVersion")
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