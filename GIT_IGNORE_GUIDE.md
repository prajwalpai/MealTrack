# 🔒 What Should and Shouldn't Be in Your GitHub Repo

## ❌ NEVER COMMIT TO GITHUB (Security Risk!)

### 🔴 CRITICAL - Must Ignore:

1. **Keystore Files** (MUST IGNORE!)
   - `*.jks` (Java KeyStore)
   - `*.keystore` (KeyStore files)
   - `keystore.properties` (Contains passwords!)
   - **Why:** Anyone with these files can sign apps as you! They can publish malicious updates.

2. **local.properties**
   - Contains your local Android SDK path
   - **Why:** Personal paths, unnecessary for others

3. **Build Outputs** (Already ignored)
   - `app/build/` folder
   - `*.apk`, `*.aab` files
   - `*.dex` files
   - **Why:** Generated files, huge size, regeneratable

4. **IDE Files** (Personal Settings)
   - `.idea/` folder (most of it)
   - `*.iml` files
   - **Why:** Personal IDE settings, machine-specific

5. **System Files**
   - `.DS_Store` (macOS)
   - `Thumbs.db` (Windows)
   - **Why:** Operating system files, unnecessary

### 🟡 SHOULD IGNORE (Best Practice):

1. **Gradle Cache**
   - `.gradle/` folder
   - **Why:** Large, regeneratable, personal cache

2. **Log Files**
   - `*.log`
   - **Why:** Temporary, can be large

3. **Temporary Files**
   - `*.tmp`, `*.bak`, `*.swp`
   - **Why:** Temporary files from editors

---

## ✅ SHOULD COMMIT TO GITHUB:

### ✅ Source Code (Always commit)
- `app/src/` - All your Kotlin code
- `app/src/main/res/` - Resources (layouts, drawables, etc.)
- `app/src/main/AndroidManifest.xml`
- All `.kt` files in `app/src/main/java/`

### ✅ Build Configuration (Always commit)
- `build.gradle.kts` (root and app level)
- `settings.gradle.kts`
- `gradle.properties` (project-level, NOT local.properties)
- `gradle/wrapper/` - Gradle wrapper files
- `app/proguard-rules.pro` - ProGuard rules

### ✅ Documentation (Always commit)
- `README.md` - Main readme
- `CONTRIBUTING.md` - Contribution guide
- `LICENSE` - License file
- `Documentation/` folder - All documentation
- `PLAY_STORE_UPLOAD_GUIDE.md` - Play Store guide
- `PLAY_STORE_CHECKLIST.md` - Play Store checklist

### ✅ Templates (Safe to commit)
- `keystore.properties.example` - Template (no real passwords)
- `.github/` - GitHub templates if you have them

### ✅ Project Files
- `.gitignore` - Obviously! 😄
- Project images/logos (like `MealTrack.png`)

---

## 🎯 CURRENT STATUS CHECK:

Let me help you verify what's currently in your repo that shouldn't be:

### Files to Check:

1. **Build folders:**
   ```bash
   # Check if app/build is ignored (should be)
   ls -la app/build  # Should exist locally but be ignored
   ```

2. **Keystore files:**
   ```bash
   # Check if keystore exists (should be ignored if it does)
   ls -la *.jks *.keystore keystore.properties 2>/dev/null
   ```

3. **local.properties:**
   ```bash
   # Check if local.properties exists (should be ignored)
   ls -la local.properties
   ```

---

## 📊 SIZE CHECK:

Large folders that shouldn't be committed:

1. **`app/build/`** - Can be 100MB+ (already ignored ✅)
2. **`.gradle/`** - Can be 500MB+ (already ignored ✅)
3. **`build/`** (root) - Generated files (already ignored ✅)

---

## ⚠️ CURRENT ISSUE IN YOUR .gitignore:

I noticed you **commented out** the keystore ignore rules:

```gitignore
# Keystore files
# Uncomment the following lines if you do not want to check your keystore files in.
#*.jks
#*.keystore
```

### ⚠️ **THIS IS A SECURITY RISK!**

**You MUST uncomment these lines!** If you accidentally commit a keystore file:
- Anyone can download it
- They can sign apps with your identity
- They can publish malicious updates to your Play Store app
- You'll have to revoke and recreate everything

**Recommended fix:** Keep keystore files ALWAYS ignored, no exceptions!

---

## ✅ RECOMMENDED .gitignore ADDITIONS:

Add these to your `.gitignore` to be extra safe:

```gitignore
# Keystore files - ALWAYS IGNORE (SECURITY!)
*.jks
*.keystore
keystore.properties

# macOS files (better pattern)
.DS_Store
**/.DS_Store

# Temporary files
*.tmp
*.bak
*.swp
*~.nib

# Editor backup files
*~
.*.swp

# OS files
Thumbs.db
.DS_Store

# Kotlin metadata
*.kotlin_module
*.kotlin_metadata
```

---

## 🧪 HOW TO CHECK WHAT WILL BE COMMITTED:

Before committing, check what Git sees:

```bash
# See what Git will track (files not in .gitignore)
git status

# See all files Git is tracking
git ls-files

# Check if a specific file would be ignored
git check-ignore -v filename.jks
```

---

## 🔍 VERIFY YOUR .gitignore WORKS:

Test that sensitive files are ignored:

```bash
# Create a test keystore (if you haven't created real one yet)
touch test-keystore.jks

# Check if Git ignores it
git status
# Should NOT show test-keystore.jks

# Clean up test
rm test-keystore.jks
```

---

## ✅ FINAL CHECKLIST:

Before pushing to GitHub:

- [ ] `.gitignore` includes `*.jks` and `*.keystore`
- [ ] `.gitignore` includes `keystore.properties`
- [ ] `.gitignore` includes `local.properties`
- [ ] `.gitignore` includes `app/build/`
- [ ] `.gitignore` includes `.gradle/`
- [ ] Run `git status` - no sensitive files listed
- [ ] No keystore files in `git ls-files` output
- [ ] `app/build/` folder not in `git ls-files`
- [ ] `local.properties` not in `git ls-files`

---

## 🆘 IF YOU ALREADY COMMITTED SENSITIVE FILES:

**If you already pushed keystore files to GitHub:**

1. **IMMEDIATELY:**
   - Remove the keystore files from Git:
     ```bash
     git rm --cached *.jks *.keystore keystore.properties
     git commit -m "Remove sensitive keystore files"
     git push
     ```

2. **THEN:**
   - **Generate NEW keystore** (old one is compromised)
   - **Update app signing** with new keystore
   - **Rotate Play Store keys** if already published

3. **PREVENT FUTURE:**
   - Add to `.gitignore` immediately
   - Never commit keystore files again

---

## 📝 SUMMARY:

### ✅ DO COMMIT:
- Source code (`.kt` files)
- Resources (layouts, drawables)
- Build configs (`build.gradle.kts`, `proguard-rules.pro`)
- Documentation (`.md` files)
- Git configs (`.gitignore`)

### ❌ DON'T COMMIT:
- **Keystore files** (`*.jks`, `*.keystore`, `keystore.properties`) - **CRITICAL!**
- Build outputs (`app/build/`, `*.apk`, `*.aab`)
- Local configs (`local.properties`)
- IDE files (`.idea/`, `*.iml`)
- System files (`.DS_Store`)

---

**Remember: When in doubt, add it to `.gitignore`! It's better to ignore too much than to accidentally commit sensitive files.**
