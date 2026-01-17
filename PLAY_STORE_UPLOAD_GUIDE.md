# 📱 Google Play Store Upload Guide - MealTrack

Complete step-by-step guide to upload your MealTrack app to the Google Play Store.

---

## ⚠️ IMPORTANT PRE-UPLOAD CHECKLIST

### Before You Start:
- [ ] **Change package name** (Currently: `com.example.mealtracker` → Should be: `com.yourname.mealtracker` or `com.yourdomain.mealtracker`)
- [ ] **Test the release build** thoroughly on a real device
- [ ] **Prepare app icons** (512x512 for Play Store)
- [ ] **Take screenshots** (at least 2, up to 8)
- [ ] **Prepare app description** and promotional text
- [ ] **Set up Google Play Developer account** ($25 one-time fee)

---

## 📋 STEP 1: Change Package Name (CRITICAL)

**Important:** Google Play Store will reject apps with `com.example.*` package name.

### Option A: Android Studio (Recommended)

1. **Right-click on `com.example.mealtracker`** in Project view
2. Select **Refactor → Rename Package**
3. Enter new name: `com.yourname.mealtracker` (or your domain)
4. Check **"Search in comments and strings"**
5. Check **"Rename module"**
6. Click **Refactor**
7. Update `build.gradle.kts`:
   ```kotlin
   namespace = "com.yourname.mealtracker"
   applicationId = "com.yourname.mealtracker"
   ```
8. **Sync Gradle** and rebuild

### Option B: Manual (If Refactor doesn't work)

1. Create new package: `com.yourname.mealtracker`
2. Move all `.kt` files to new package
3. Update all `package` declarations in Kotlin files
4. Update `AndroidManifest.xml` if needed
5. Update `build.gradle.kts` namespace and applicationId
6. Clean and rebuild

---

## 🔐 STEP 2: Create Keystore for App Signing

**CRITICAL:** You need a keystore to sign your release APK. Keep this file safe - you'll need it for all future updates!

### Generate Keystore via Terminal:

```bash
cd /Users/prajwalpai/Documents/GitHub-Personal/MealTrack

# Generate keystore
keytool -genkeypair -v -storetype PKCS12 -keystore mealtrack-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias mealtrack-key
```

**You'll be prompted for:**
- **Keystore password:** (Create a strong password - save it!)
- **Key password:** (Press Enter to use same as keystore)
- **Your name:** Your name or company name
- **Organizational Unit:** (e.g., Development)
- **Organization:** Your company name (or your name)
- **City:** Your city
- **State:** Your state/province
- **Country Code:** Two-letter code (e.g., US, IN)

**Example:**
```
Keystore password: [Enter strong password]
Key password: [Press Enter]

What is your first and last name?
  [Unknown]: John Doe
What is the name of your organizational unit?
  [Unknown]: Development
What is the name of your organization?
  [Unknown]: Your Company Name
What is the name of your City or Locality?
  [Unknown]: San Francisco
What is the name of your State or Province?
  [Unknown]: California
What is the two-letter country code for this unit?
  [Unknown]: US
```

### Verify Keystore was created:
```bash
ls -la mealtrack-release-key.jks
```

---

## 🔧 STEP 3: Configure Signing in build.gradle.kts

1. **Create `keystore.properties` file** (already created - see below)
2. **Update `app/build.gradle.kts`** (see below)

### Create keystore.properties:

**Location:** `app/keystore.properties` (create if not exists)

```properties
storePassword=YOUR_KEYSTORE_PASSWORD
keyPassword=YOUR_KEY_PASSWORD
keyAlias=mealtrack-key
storeFile=../mealtrack-release-key.jks
```

⚠️ **IMPORTANT:** 
- Replace `YOUR_KEYSTORE_PASSWORD` and `YOUR_KEY_PASSWORD` with actual passwords
- Add `keystore.properties` to `.gitignore` (don't commit passwords!)

---

## 📦 STEP 4: Update build.gradle.kts for Release

The build.gradle.kts has been updated with signing configuration. Key changes:

1. **Signing configuration** added
2. **ProGuard enabled** for release (optimizes and obfuscates code)
3. **Release build type** configured

**After updating, sync Gradle.**

---

## 🏗️ STEP 5: Build Release APK/AAB

### Option A: Build App Bundle (AAB) - **RECOMMENDED**

Google Play prefers App Bundles (.aab) over APKs:

```bash
# In Android Studio:
Build → Generate Signed Bundle / APK → Android App Bundle → Next
```

**Or via Terminal:**
```bash
cd /Users/prajwalpai/Documents/GitHub-Personal/MealTrack
./gradlew bundleRelease
```

**Output location:**
```
app/build/outputs/bundle/release/app-release.aab
```

### Option B: Build APK (Alternative)

```bash
# In Android Studio:
Build → Generate Signed Bundle / APK → APK → Next
```

**Or via Terminal:**
```bash
./gradlew assembleRelease
```

**Output location:**
```
app/build/outputs/apk/release/app-release.apk
```

---

## 🧪 STEP 6: Test Release Build

**CRITICAL:** Always test the release build before uploading!

### Test on Device:

1. **Install release APK on a real device:**
   ```bash
   adb install app/build/outputs/apk/release/app-release.apk
   ```

2. **Test everything:**
   - [ ] App launches correctly
   - [ ] All screens work
   - [ ] Database operations work
   - [ ] Notifications work
   - [ ] Widget works
   - [ ] Backup/restore works
   - [ ] No crashes

3. **Test on multiple devices** if possible (different Android versions)

---

## 🎨 STEP 7: Prepare Play Store Assets

### Required Assets:

#### 1. **App Icon** (Required)
- **Size:** 512 x 512 pixels
- **Format:** PNG (32-bit)
- **Location:** `app/src/main/res/mipmap-xxxhdpi/ic_launcher.png` (already have)

#### 2. **Feature Graphic** (Required)
- **Size:** 1024 x 500 pixels
- **Format:** PNG or JPG
- **Content:** App name, logo, key features
- **Tip:** Use Canva or Figma to create

#### 3. **Screenshots** (Required - at least 2)
- **Phone screenshots:** At least 2, up to 8
- **Sizes needed:**
  - 16:9 or 9:16 aspect ratio
  - Minimum width: 320px
  - Maximum width: 3840px
- **Recommended sizes:**
  - Phone: 1080 x 1920 (portrait) or 1920 x 1080 (landscape)
  - Tablet (optional): 1200 x 1600 (portrait) or 1600 x 1200 (landscape)

**Screenshot ideas:**
1. Daily Dashboard (main screen)
2. Add Meal screen
3. Weekly Summary with charts
4. Monthly Summary
5. Goals screen
6. Widget on home screen
7. Settings screen

**How to take screenshots:**
- Use Android Studio's **Layout Inspector** → **Take Screenshot**
- Or use device's screenshot feature

#### 4. **App Description** (Required)
- **Short description:** 80 characters max
- **Full description:** 4000 characters max
- **Promotional text:** 80 characters max (optional, shown on store listing)

#### 5. **Privacy Policy** (Required for apps with user data)
- **Since you collect meal data:** Create a privacy policy
- **Can use:** https://www.privacypolicygenerator.info/
- **Host it:** On GitHub Pages, your website, or any free hosting

---

## 🌐 STEP 8: Set Up Google Play Console

### 8.1 Create Developer Account

1. **Go to:** https://play.google.com/console
2. **Sign in** with Google account
3. **Pay $25 one-time registration fee**
4. **Complete developer profile**

### 8.2 Create New App

1. **Click "Create app"**
2. **Fill in:**
   - **App name:** MealTrack (or your preferred name)
   - **Default language:** English (United States)
   - **App or Game:** App
   - **Free or Paid:** Free (or Paid)
   - **Declarations:** Check appropriate boxes
3. **Click "Create app"**

---

## 📤 STEP 9: Upload App to Play Console

### 9.1 Go to Production (or Internal Testing)

1. **In Play Console:** Left sidebar → **Production** (or **Testing → Internal testing**)
2. **Click "Create new release"**

### 9.2 Upload App Bundle/APK

1. **Click "Browse files"**
2. **Select:** `app-release.aab` (or `.apk`)
3. **Add release name:** `1.0 (1)` (or `Initial Release`)
4. **Add release notes:**
   ```
   Initial release of MealTrack!
   
   Features:
   - Track daily meals (Breakfast, Lunch, Evening Snacks, Dinner)
   - Health categorization (Healthy, Neutral, Junk)
   - Weekly and monthly summaries
   - Goals and streak tracking
   - Meal reminders
   - Home screen widget
   - Backup and restore functionality
   ```

### 9.3 Review and Rollout

1. **Review** all information
2. **Click "Save"**
3. **Click "Review release"**
4. **Click "Start rollout to Production"** (or Internal testing)

---

## 📝 STEP 10: Complete Store Listing

### 10.1 Main Store Listing

**Fill in all required fields:**

1. **App name:** MealTrack
2. **Short description:** (80 chars max)
   ```
   Track your daily meals with health categorization, statistics, and goals. Simple and beautiful meal tracking app.
   ```

3. **Full description:** (4000 chars max)
   ```
   MealTrack is a beautiful, feature-rich Android app for tracking your daily meals with health categorization and insights.
   
   🌟 KEY FEATURES:
   
   • Daily Meal Tracking
   Track 4 meals per day: Breakfast, Lunch, Evening Snacks, and Dinner
   
   • Health Categorization
   Categorize meals as Healthy 🟢, Neutral ⚪, or Junk 🔴
   
   • Statistics & Analytics
   View weekly summaries with trends and monthly insights
   
   • Goals & Achievements
   Set healthy eating goals and track your progress with streaks
   
   • Smart Notifications
   Customizable meal reminders to help you stay on track
   
   • Home Screen Widget
   Quick access to today's meals and weekly progress (3 sizes)
   
   • Backup & Restore
   Export your data to JSON and restore across devices
   
   🎨 MODERN DESIGN
   • Material Design 3 interface
   • Dark mode support
   • Smooth animations
   • Intuitive navigation
   
   Start tracking your meals today and build healthier eating habits!
   ```

4. **App icon:** Upload 512x512 icon
5. **Feature graphic:** Upload 1024x500 graphic
6. **Screenshots:** Upload 2-8 screenshots
7. **Phone screenshots:** Upload phone screenshots
8. **Promotional text:** (Optional, 80 chars)
   ```
   Track meals, achieve goals, and build healthy eating habits!
   ```

### 10.2 Privacy Policy

1. **Go to:** Store presence → Main store listing
2. **Scroll to:** Privacy Policy
3. **Add URL:** Your privacy policy URL
   - Example: `https://yourusername.github.io/mealtrack/privacy-policy.html`
   - Or use GitHub Pages

### 10.3 Content Rating

1. **Fill out questionnaire:**
   - Does your app contain user-generated content? → **No** (or Yes if applicable)
   - Does your app contain ads? → **No**
   - Does your app allow users to communicate? → **No**
   - Continue with questionnaire
2. **Get rating certificate** (usually automatic)

### 10.4 Target Audience & Content

1. **Select:** Appropriate target audience (All ages, etc.)
2. **Complete:** Content rating questionnaire
3. **Upload:** Required certificates if needed

---

## ✅ STEP 11: Complete Required Sections

### Required Sections (Play Console):

1. ✅ **App access** - Select "All functionality is available without restrictions"
2. ✅ **Ads** - Select "No, my app doesn't contain ads"
3. ✅ **Data safety** - Complete data safety form
4. ✅ **Content rating** - Complete questionnaire
5. ✅ **Target audience** - Select appropriate age groups
6. ✅ **News apps** - Not applicable (unless your app is a news app)
7. ✅ **COVID-19 contact tracing** - Not applicable
8. ✅ **Exports compliance** - Select "No" (unless your app has restrictions)

### Data Safety Form:

Since you collect meal data, you need to disclose:

1. **What data do you collect?**
   - Select: "App activity"
   - Describe: "Meal tracking data (meal names, types, categories, dates)"

2. **How is data used?**
   - Select: "App functionality"

3. **Is data shared?**
   - Select: "No" (unless you share data)

4. **Data security:**
   - Select: "Data is encrypted in transit"
   - Select: "Users can request deletion of data"

---

## 🚀 STEP 12: Submit for Review

### 12.1 Final Checklist

Before submitting, verify:

- [ ] All required sections completed (green checkmarks)
- [ ] App bundle/APK uploaded
- [ ] Store listing complete (icon, screenshots, description)
- [ ] Privacy policy URL added
- [ ] Content rating complete
- [ ] Data safety form completed
- [ ] Target audience selected
- [ ] Release notes added

### 12.2 Submit

1. **Go to:** Production → Review
2. **Click:** "Start rollout to Production"
3. **Confirm:** Review and confirm

### 12.3 Wait for Review

- **First-time apps:** Usually 1-3 days
- **Updates:** Usually a few hours to 1 day
- **You'll receive email** when approved or if there are issues

---

## 📊 STEP 13: Monitor Release

### After Submission:

1. **Check Play Console** for status updates
2. **Respond to any issues** Google flags
3. **Monitor reviews** after launch
4. **Track analytics** in Play Console

---

## 🔄 STEP 14: Future Updates

### When releasing updates:

1. **Update version code:**
   ```kotlin
   versionCode = 2  // Increment by 1 each time
   versionName = "1.1"
   ```

2. **Build new release:**
   ```bash
   ./gradlew bundleRelease
   ```

3. **Upload to Play Console:**
   - Go to Production → Create new release
   - Upload new `.aab` file
   - Add release notes
   - Submit for review

---

## 🆘 TROUBLESHOOTING

### Common Issues:

#### 1. **Keystore Password Error**
- **Problem:** "Keystore password was incorrect"
- **Solution:** Check `keystore.properties` file - ensure passwords match

#### 2. **Package Name Rejected**
- **Problem:** "Package name contains 'example'"
- **Solution:** Change package name from `com.example.*` to your own

#### 3. **Upload Failed**
- **Problem:** Upload fails or times out
- **Solution:** 
  - Check internet connection
  - Try uploading from different network
  - Use Chrome browser (recommended)

#### 4. **Rejected for Permissions**
- **Problem:** App rejected for permission usage
- **Solution:** 
  - Ensure permissions are necessary
  - Update privacy policy if needed
  - Explain permission usage in description

#### 5. **ProGuard Issues**
- **Problem:** App crashes after enabling ProGuard
- **Solution:** 
  - Add keep rules in `proguard-rules.pro`
  - Test release build thoroughly
  - Disable ProGuard if needed (not recommended)

---

## 📚 ADDITIONAL RESOURCES

### Google Play Resources:
- [Play Console Help](https://support.google.com/googleplay/android-developer/)
- [App Bundle Guide](https://developer.android.com/guide/app-bundle)
- [Play Store Listing Best Practices](https://developer.android.com/distribute/best-practices/launch/store-listing)

### Tools:
- [Privacy Policy Generator](https://www.privacypolicygenerator.info/)
- [Canva](https://www.canva.com/) - For feature graphics
- [App Icon Generator](https://icon.kitchen/) - For app icons

---

## ✅ FINAL CHECKLIST

Before submitting:

- [ ] Package name changed (not `com.example.*`)
- [ ] Keystore created and configured
- [ ] Release build tested on device
- [ ] App icon (512x512) ready
- [ ] Feature graphic (1024x500) ready
- [ ] Screenshots (at least 2) ready
- [ ] App description written
- [ ] Privacy policy created and hosted
- [ ] All Play Console sections completed
- [ ] App bundle/APK uploaded
- [ ] Release notes added

---

## 🎉 SUCCESS!

Once your app is approved, it will be live on the Google Play Store!

**Good luck with your launch! 🚀**

---

**Need help?** Check the troubleshooting section or Google Play Console support.
