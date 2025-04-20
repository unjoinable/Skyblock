# 🌌 Skyblock — A Modular Hypixel-Inspired Java Skyblock Engine

[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://openjdk.org/)
[![Build Status](https://img.shields.io/badge/Build-Stable-brightgreen.svg)]()
[![License](https://img.shields.io/github/license/unjoinable/skyblock.svg)](LICENSE)

A highly modular and extensible Skyblock engine built for custom Minecraft-like game experiences. Inspired by Hypixel Skyblock, this project is designed with future-proofing and clean architecture at its core.

---

## 🚀 Features

- ✅ **Component-Based Item System**  
  Define item behavior using clean, decoupled components.

- 📦 **Centralized Registry System**  
  Register items, components, and other game objects with ease.

- 🧠 **Stat System**  
  Modify and calculate item stats through modifiers and base profiles.

- 🧱 **Minestom Integration**               
  Lightweight and modern Minecraft server API support.

- 🧰 **Serialization & JSON Configs**  
  Deserialize items directly from JSON for easy configuration.

---

## 🧩 Architecture Overview

### 🔧 Components

The heart of the item system — each item is composed of independent, immutable components that handle:
- Display names, lore, rarity
- Item material and category
- Base stats and stat modifiers

```java
interface Component {}
interface LoreComponent extends Component {
    List<Component> generateLore(ComponentContainer container);
}
```

### 🗃️ Registries

Base `Registry<K, V>` class provides thread-safe storage for any kind of game object. Items and components are registered using keys like `String` IDs.

```java
register("aspect_of_the_end", new SkyblockItem(...));
```

---

## 📁 File Structure

```
skyblock/
├── item/               # Item definition & components
│   ├── component/      # Component system
│   ├── enums/          # Item categories & rarities
├── registry/           # Base registry + item/component registration
├── stats/              # Stat system and modifiers
├── utils/              # Shared utilities
```

---

## 📦 Getting Started

### 🧪 Prerequisites

- Java 17+
- Gradle or IntelliJ with Gradle support

### 🛠️ Build

```bash
./gradlew build
```

### 📄 Run / Load Items

Ensure `skyblock_items.json` is present in the root directory. Items are loaded via `ItemRegistry.init()`.

---

## 🧠 Planned Features

- 🔮 Custom crafting & recipe systems  
- 🧙 RPG-style abilities & skill trees  
- 🗺️ Island/world generation
- 📊 GUI menu systems
- 💾 Database support for player data

---

## 🤝 Contributing

Pull requests are welcome! If you're interested in improving the architecture, adding new systems, or fixing bugs, feel free to fork and create a PR.

---

## 📜 License

Licensed under Apache. See [`LICENSE`](LICENSE) for details.

---

## 🧭 Inspiration

- [Hypixel Skyblock](https://hypixel.net/skyblock)
- [Minestom](https://github.com/Minestom/Minestom)
- [ECS (Entity Component System)](https://en.wikipedia.org/wiki/Entity_component_system)

---

> *Crafted with code, designed for scale.*
