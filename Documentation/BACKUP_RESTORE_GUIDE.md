# 💾 Backup & Restore Guide

## Overview

The Meal Tracker app now includes a comprehensive backup and restore system that allows you to:
- **Export** all your meal data to a JSON file
- **Import** data from backup files
- **Merge** backups with existing data or **replace** everything
- **Transfer** data between devices
- **Save** backups to device storage or cloud services (Google Drive, Dropbox, etc.)

---

## 🚀 Quick Start

### Creating a Backup

1. Open the app
2. Go to **Settings** (gear icon)
3. Tap **💾 Backup & Restore**
4. Tap **📤 Create Backup**
5. Choose where to save the file
6. Done! Your data is backed up

### Restoring a Backup

1. Open the app
2. Go to **Settings** → **💾 Backup & Restore**
3. Tap **📥 Restore Backup**
4. Select your backup file
5. Choose **Merge** or **Replace All**
6. Confirm and restore!

---

## 📤 Creating Backups

### What Gets Backed Up?

Your backup includes:
- ✅ All meal entries (name, type, category, date, timestamp)
- ✅ Complete meal history
- ✅ Backup metadata (version, timestamp, meal count)

### Backup File Format

Backups are saved as JSON files with this naming format:
```
MealTracker_Backup_YYYY-MM-DD_HH-MM-SS.json
```

Example: `MealTracker_Backup_2024-01-16_14-30-45.json`

### Where to Save Backups

You can save backups to:
- 📱 **Device Storage** - Local files on your phone
- ☁️ **Google Drive** - Cloud storage (recommended)
- 📦 **Dropbox** - Cloud storage
- 💻 **Computer** - Transfer via USB
- 📧 **Email** - Send to yourself

**💡 Tip:** Save to cloud storage for automatic sync across devices!

---

## 📥 Restoring Backups

### Two Restore Options

#### 1. **Merge** (Recommended)
- Adds backup data to your existing meals
- Keeps all current data
- No data loss
- Best for: Combining data from multiple devices

#### 2. **Replace All** (⚠️ Caution)
- Deletes ALL current meals
- Replaces with backup data
- Cannot be undone
- Best for: Fresh start or switching devices

### Backup Preview

Before restoring, you'll see:
- 📊 Total meals in backup
- 🟢 Healthy meal count
- ⚪ Neutral meal count
- 🔴 Junk meal count
- 📅 Days tracked
- 🕐 Backup creation date

### Validation

The app automatically validates backups to ensure:
- ✅ Valid JSON format
- ✅ Correct data structure
- ✅ All required fields present
- ✅ No corrupted data

---

## 🔄 Common Use Cases

### 1. Regular Backups
**Scenario:** Protect your data from loss

**Steps:**
1. Create weekly backups
2. Save to Google Drive
3. Keep multiple versions

### 2. Switching Devices
**Scenario:** Moving to a new phone

**Steps:**
1. Create backup on old device
2. Save to cloud storage
3. Install app on new device
4. Restore backup (Replace All)

### 3. Combining Data
**Scenario:** Merging data from multiple devices

**Steps:**
1. Create backup on Device A
2. Transfer to Device B
3. Restore backup (Merge)
4. All data combined!

### 4. Sharing with Family
**Scenario:** Share meal tracking with family member

**Steps:**
1. Create backup
2. Share file via email/messaging
3. Family member restores (Merge)

---

## 📊 Backup Statistics

After creating a backup, you'll see:
- **Total Meals** - Number of meals backed up
- **Healthy Meals** - Count of healthy meals 🟢
- **Neutral Meals** - Count of neutral meals ⚪
- **Junk Meals** - Count of junk meals 🔴
- **Days Tracked** - Number of unique days

---

## 🛡️ Best Practices

### Regular Backups
- ✅ Create backups **weekly**
- ✅ Save to **cloud storage**
- ✅ Keep **multiple versions**
- ✅ Test restores occasionally

### File Management
- 📁 Organize backups by date
- 🏷️ Use descriptive names
- 🗑️ Delete old backups (keep last 3-5)
- ☁️ Enable cloud auto-sync

### Before Major Changes
Create a backup before:
- 📱 Switching devices
- 🔄 App updates
- 🗑️ Deleting old data
- 🔧 Troubleshooting issues

---

## ⚠️ Important Notes

### Data Safety
- **Merge** is safer than **Replace All**
- Always preview backup before restoring
- Keep backups in multiple locations
- Test backups periodically

### File Compatibility
- Backups work across all devices
- JSON format is universal
- Future app versions will support old backups
- No data loss during updates

### Privacy
- Backups contain your meal data
- Store securely (password-protected cloud)
- Don't share publicly
- Delete backups when no longer needed

---

## 🔧 Troubleshooting

### Backup Failed
**Problem:** Can't create backup

**Solutions:**
- Check storage space
- Grant file permissions
- Try different save location
- Restart app

### Restore Failed
**Problem:** Can't restore backup

**Solutions:**
- Verify file is valid JSON
- Check file isn't corrupted
- Ensure file is from Meal Tracker app
- Try re-downloading backup

### Invalid Backup File
**Problem:** "Invalid backup file" error

**Solutions:**
- Ensure file is from Meal Tracker
- Check file wasn't edited manually
- Verify file extension is .json
- Try creating new backup

### Missing Data After Restore
**Problem:** Some meals missing

**Solutions:**
- Check if you used "Replace All" instead of "Merge"
- Verify backup file has all data
- Create new backup and compare
- Contact support if issue persists

---

## 📱 Technical Details

### File Format
```json
{
  "version": 1,
  "timestamp": 1705420800000,
  "meals": [
    {
      "id": 1,
      "name": "Oatmeal",
      "type": "Breakfast",
      "category": "Healthy",
      "date": "2024-01-16",
      "timestamp": 1705420800000
    }
  ],
  "appVersion": "1.0"
}
```

### Backup Size
- Average: 1-5 KB per 100 meals
- 1 year of data: ~20-50 KB
- Very efficient storage

---

## 🎯 Summary

✅ **Easy to use** - Simple 3-step process  
✅ **Safe** - Validation and preview before restore  
✅ **Flexible** - Merge or replace options  
✅ **Universal** - Works across all devices  
✅ **Reliable** - JSON format, no data loss  

**Never lose your meal tracking data again!** 💪

---

**Questions?** Check the main documentation or contact support.

**Happy backing up!** 💾✨

