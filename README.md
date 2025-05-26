# KUtils

**KUtils** is a modular utility library built to enhance Kotlin development, particularly for Minecraft plugins using [PaperMC](https://papermc.io), but also suitable for general-purpose use. It provides helpful Kotlin extensions, DSLs, and abstractions to speed up development across multiple domains.

---

## 🧩 Modules

| Module            | Description                                    |
| ----------------- | ---------------------------------------------- |
| `core`            | General-purpose Kotlin utilities               |
| `paper`           | PaperMC-specific plugin utilities              |
| `paper-ux`        | Tools for player messaging and UX enhancements |
| `paper-inventory` | Simplified inventory GUI handling for Paper    |

---

## 📦 Installation

### Option 1: **Using Github Repo** (Stable)

```kotlin
repositories {
    maven("https://maven.pkg.github.com/InvalidJoker/KUtils")
}

dependencies {
    implementation("de.joker.kutils:core:$version")
    implementation("de.joker.kutils:paper:$version")           // Optional
    implementation("de.joker.kutils:paper-ux:$version")        // Optional
    implementation("de.joker.kutils:paper-inventory:$version") // Optional
}
```

> Replace `$version` with the latest version from the [Releases](https://github.com/InvalidJoker/KUtils/releases).

---

### Option 2: **Using JitPack** (Latest commit or GitHub release)

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.InvalidJoker.KUtils:core:$jitpackVersion")
    implementation("com.github.InvalidJoker.KUtils:paper:$jitpackVersion")
    implementation("com.github.InvalidJoker.KUtils:paper-ux:$jitpackVersion")
    implementation("com.github.InvalidJoker.KUtils:paper-inventory:$jitpackVersion")
}
```

> Replace `$jitpackVersion` with a commit hash.
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