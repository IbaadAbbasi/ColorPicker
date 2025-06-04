# 🎨 ColorPalettePicker

A lightweight and customizable Color Picker Dialog built using **Jetpack Compose**, designed for Android apps to easily pick and preview colors — including HEX, RGB, Alpha, and Material Design shades.

---

## ✨ Features

- 🧩 Fullscreen Color Picker Dialog
- 🎚 Hue, Saturation, Brightness, and Alpha sliders
- 🎨 Predefined Material Design Color Swatches
- 🔁 Show current and previously selected color
- 🟦 Real-time HEX and RGB values
- ⚙️ Optional: Show/hide color value preview
- 💡 Easy to integrate into any Compose-based project

---

## 📦 How to Use

### Step 1: Add JitPack to `settings.gradle` (Project level)

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
dependencies {
    implementation 'com.github.IbaadAbbasi:ColorPicker:0.0.1'
}
ColorPickerDialog(
    showColorValues = true,
    onOkClick = { hex, rgb ->
        // Use selected HEX and RGB values
    },
    onDismissRequest = {
        // Handle dismiss
    }
)
