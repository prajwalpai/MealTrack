# ⚡ Quick Start Guide

## 🎯 Goal
Get your Hello World app running in 15 minutes!

---

## ✅ Step-by-Step Checklist

### 1️⃣ Download Android Studio (10 min)
- [ ] Go to https://developer.android.com/studio
- [ ] Download for your OS (Windows/Mac/Linux)
- [ ] Run installer
- [ ] Choose "Standard" installation
- [ ] Wait for downloads to complete

### 2️⃣ Open This Project (2 min)
- [ ] Launch Android Studio
- [ ] Click "Open" (NOT "New Project")
- [ ] Navigate to this folder: `Android-App`
- [ ] Click "OK"
- [ ] Wait for "Gradle sync" to finish (bottom right corner)
  - First time: 5-10 minutes
  - Downloads dependencies from internet

### 3️⃣ Create Virtual Device (3 min)
- [ ] Click "Device Manager" icon (phone icon in right sidebar)
- [ ] Click "Create Device"
- [ ] Select "Phone" → "Pixel 5" → "Next"
- [ ] Select "Tiramisu" (API 33) → "Next" → "Download"
- [ ] Wait for download → "Finish"

### 4️⃣ Run the App! (1 min)
- [ ] Click green "Run" button ▶️ (top toolbar)
- [ ] Select your virtual device
- [ ] Wait for emulator to boot (1-2 min first time)
- [ ] App launches automatically!

---

## 🎉 Success!

You should see:
- A screen with "Hello World!" text
- A "Click Me!" button below it
- Clicking the button updates the text with a counter

---

## 🐛 Troubleshooting

### Problem: "Gradle sync failed"
**Solution:**
1. Check internet connection
2. File → Invalidate Caches → Invalidate and Restart
3. Try again

### Problem: "SDK not found"
**Solution:**
1. Tools → SDK Manager
2. Install "Android 13.0 (Tiramisu)"
3. Click "Apply"

### Problem: Emulator won't start
**Solution (Windows):**
1. Enable Virtualization in BIOS
2. Restart computer
3. Try again

**Solution (Mac):**
- Should work by default
- If not, try creating a different device

### Problem: "Cannot resolve symbol R"
**Solution:**
1. Build → Clean Project
2. Build → Rebuild Project
3. Wait for build to finish

---

## 📱 Using a Real Phone Instead?

### Android Phone Setup:
1. **Enable Developer Mode:**
   - Settings → About Phone
   - Tap "Build Number" 7 times
   - You'll see "You are now a developer!"

2. **Enable USB Debugging:**
   - Settings → System → Developer Options
   - Turn on "USB Debugging"

3. **Connect & Run:**
   - Connect phone via USB
   - Allow USB debugging on phone
   - Click Run ▶️ in Android Studio
   - Select your phone from list

---

## 🎓 What to Do After Running

1. **Play with the app:**
   - Click the button multiple times
   - See the counter increase

2. **Make your first change:**
   - Open `app/src/main/res/layout/activity_main.xml`
   - Find the line: `android:text="Click Me!"`
   - Change it to: `android:text="Tap Here!"`
   - Click Run ▶️ again
   - See your change!

3. **Experiment more:**
   - Change button color
   - Change text size
   - Add another TextView

4. **Read the guides:**
   - `BEGINNER_GUIDE.md` - Detailed explanations
   - `CODE_EXPLANATION.md` - Line-by-line breakdown
   - `README.md` - Full project overview

---

## 🚀 Next Steps

Once you're comfortable with Hello World:

### Stage 2: Simple Meal Logger
We'll build:
- A form to enter meal details
- Dropdown to select meal type (Breakfast/Lunch/etc.)
- Color-coded buttons (Green/Yellow/Red)
- Save meals locally
- Display list of meals

**Ready?** Let me know when you've successfully run the Hello World app!

---

## 📞 Need Help?

If you're stuck:
1. Check the error message in Android Studio (bottom panel)
2. Google the error message
3. Check Stack Overflow
4. Ask me! Describe:
   - What you're trying to do
   - What happened
   - Any error messages

---

## 🎯 Your Mission

**Run the Hello World app and click the button 10 times!**

Then come back and we'll build the Meal Logger together! 🍽️

