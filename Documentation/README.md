# Meal Tracker - Android Learning Project

Welcome to your Android development journey! This project will take you from Hello World to a fully functional Meal Tracker app.

## 🎯 Learning Stages

### Stage 1: Hello World & Basics (CURRENT)
- ✅ Project structure created
- ✅ Basic UI with TextView and Button
- ✅ Click event handling
- 🔄 Next: Run the app!

### Stage 2: Simple Meal Logger
- Meal entry form
- Color-coded categories (Healthy/Neutral/Junk)
- Local data storage
- List display

### Stage 3: Full Meal Tracker
- Database integration
- Daily meal structure (4 meals/day)
- Weekly & monthly summaries
- Edit/delete functionality

## 🚀 Getting Started

### Prerequisites
1. **Download Android Studio**: https://developer.android.com/studio
2. **System Requirements**:
   - Windows: 8GB RAM minimum, 16GB recommended
   - macOS: 8GB RAM minimum, 16GB recommended
   - 8GB of available disk space

### Installation Steps

1. **Install Android Studio**
   - Download from the link above
   - Run the installer
   - Follow the setup wizard (install all recommended components)
   - This will install:
     - Android SDK
     - Android SDK Platform
     - Android Virtual Device (Emulator)

2. **Open This Project**
   - Launch Android Studio
   - Click "Open" (not "New Project")
   - Navigate to this folder and select it
   - Wait for Gradle sync to complete (5-10 minutes first time)

3. **Set Up an Emulator**
   - Click "Device Manager" in Android Studio
   - Click "Create Device"
   - Select "Pixel 5" or any phone
   - Select System Image: "Tiramisu" (API 33) or latest
   - Click "Finish"

4. **Run the App**
   - Click the green "Run" button (▶️) in the toolbar
   - Select your emulator
   - Wait for the app to launch!

## 📱 What You'll See

### App Screenshots

<div align="center">

#### Main Dashboard
<img src="screenshots/Main-Screen.png" width="50%" alt="Main Screen">
*The daily dashboard showing meal slots for Breakfast, Lunch, Evening Snacks, and Dinner*

#### Meal Logger
<img src="screenshots/Meal-Logger.png" width="50%" alt="Meal Logger">
*Add or edit meals with health categorization*

#### Weekly Insights
<img src="screenshots/Weekly-Insights.png" width="50%" alt="Weekly Insights">
*Weekly statistics and trends visualization*

#### Monthly Insights
<img src="screenshots/Monthly-Insights.png" width="50%" alt="Monthly Insights">
*Monthly eating patterns and analysis*

#### Goals and Achievements
<img src="screenshots/Goals%20and%20Achievements.png" width="50%" alt="Goals and Achievements">
*Track goals, streaks, and achievements*

</div>

## 📚 Project Structure Explained

```
Android-App/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/prajwalpai/mealtracker/
│   │       │   └── MainActivity.kt          # Your main code
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   └── activity_main.xml    # UI design
│   │       │   ├── values/
│   │       │   │   ├── strings.xml          # Text resources
│   │       │   │   ├── colors.xml           # Color definitions
│   │       │   │   └── themes.xml           # App styling
│   │       └── AndroidManifest.xml          # App configuration
│   └── build.gradle.kts                     # App dependencies
├── build.gradle.kts                         # Project configuration
└── settings.gradle.kts                      # Project settings
```

## 🆘 Troubleshooting

### "Gradle sync failed"
- Check your internet connection
- Click "File" → "Invalidate Caches" → "Invalidate and Restart"

### "SDK not found"
- Go to "Tools" → "SDK Manager"
- Install "Android 13.0 (Tiramisu)" or latest

### Emulator won't start
- Enable virtualization in BIOS (for Windows)
- Ensure you have enough RAM available

## 📖 Next Steps

Once you successfully run the Hello World app:
1. Explore the code in `MainActivity.kt`
2. Modify the button text in `activity_main.xml`
3. Change colors in `colors.xml`
4. We'll move to Stage 2: Building the Meal Logger!

## 🎓 Learning Resources

- [Android Developer Guide](https://developer.android.com/guide)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Material Design](https://material.io/design)

---

**Ready to start?** Follow the installation steps above and let me know when you've successfully run the app!

