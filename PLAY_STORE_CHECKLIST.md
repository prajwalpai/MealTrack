# 📋 Quick Play Store Upload Checklist

**Before uploading, complete these steps:**

---

## ✅ Pre-Upload Preparation

### 1. Change Package Name (CRITICAL)
- [ ] Change from `com.example.mealtracker` to `com.yourname.mealtracker`
- [ ] Update in `build.gradle.kts` (namespace and applicationId)
- [ ] Update package declaration in all `.kt` files
- [ ] Clean and rebuild project

### 2. Create Keystore
- [ ] Run: `keytool -genkeypair -v -storetype PKCS12 -keystore mealtrack-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias mealtrack-key`
- [ ] Save passwords securely (you'll need them forever!)
- [ ] Copy `keystore.properties.example` to `keystore.properties`
- [ ] Fill in actual passwords in `keystore.properties`

### 3. Test Release Build
- [ ] Build release: `./gradlew assembleRelease`
- [ ] Install on real device: `adb install app/build/outputs/apk/release/app-release.apk`
- [ ] Test all features:
  - [ ] App launches
  - [ ] All screens work
  - [ ] Database operations
  - [ ] Notifications
  - [ ] Widget
  - [ ] Backup/restore
  - [ ] No crashes

---

## 🎨 Store Assets Preparation

- [ ] **App Icon:** 512x512 PNG
- [ ] **Feature Graphic:** 1024x500 PNG/JPG
- [ ] **Screenshots:** At least 2, up to 8 (phone screenshots)
- [ ] **App Description:** Short (80 chars) + Full (4000 chars)
- [ ] **Privacy Policy:** Create and host online (required)

---

## 🌐 Play Console Setup

- [ ] **Create Developer Account:** https://play.google.com/console ($25 fee)
- [ ] **Create App:** Fill in name, language, type, pricing
- [ ] **Upload App Bundle:** `app-release.aab` (preferred) or `.apk`

---

## 📝 Store Listing

- [ ] App name
- [ ] Short description (80 chars)
- [ ] Full description (4000 chars)
- [ ] App icon uploaded (512x512)
- [ ] Feature graphic uploaded (1024x500)
- [ ] Screenshots uploaded (2-8)
- [ ] Privacy policy URL added
- [ ] Promotional text (optional, 80 chars)

---

## ✅ Required Sections (All Green Checkmarks)

- [ ] **App access** - Completed
- [ ] **Ads** - "No ads" selected
- [ ] **Data safety** - Form completed
- [ ] **Content rating** - Questionnaire completed
- [ ] **Target audience** - Age groups selected
- [ ] **News apps** - Not applicable
- [ ] **COVID-19** - Not applicable
- [ ] **Exports compliance** - "No" selected

---

## 🚀 Submit for Review

- [ ] All sections completed (green checkmarks)
- [ ] Release notes added
- [ ] Review all information
- [ ] Click "Start rollout to Production"
- [ ] Wait for approval (1-3 days typically)

---

## 📚 Reference

**Detailed guide:** See `PLAY_STORE_UPLOAD_GUIDE.md`

**Common Commands:**
```bash
# Generate keystore
keytool -genkeypair -v -storetype PKCS12 -keystore mealtrack-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 -alias mealtrack-key

# Build release bundle (preferred)
./gradlew bundleRelease

# Build release APK
./gradlew assembleRelease

# Install and test
adb install app/build/outputs/apk/release/app-release.apk
```

---

**Good luck! 🎉**
