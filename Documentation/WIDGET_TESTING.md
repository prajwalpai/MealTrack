# 🧪 Widget Testing Guide

## How to Test the Widget

### 1. **Build and Install the App**
```bash
# In Android Studio
Build > Make Project
Run > Run 'app'
```

### 2. **Add the Widget to Home Screen**

#### Method 1: Long Press
1. Long press on your home screen
2. Tap "Widgets"
3. Find "MealTrack" widget
4. Drag it to your home screen

#### Method 2: Widget Picker (Android 12+)
1. Long press on home screen
2. Tap "Widgets"
3. Search for "MealTrack"
4. Tap and drag to place

### 3. **Test Widget Sizes**

#### Small Widget Test:
1. Add widget to home screen
2. Resize to smallest size (2x1)
3. **Expected:** Should show only "➕ Add Meal" button
4. **Tap button:** Should open MainActivity

#### Medium Widget Test:
1. Resize widget to medium (4x2)
2. **Expected:** Should show:
   - "🍽️ Meal Tracker" header
   - "X meals today" count
   - "🟢 X  ⚪ X  🔴 X" category breakdown
   - "➕ Add Meal" button
3. **Tap widget background:** Should open DailyDashboardActivity
4. **Tap button:** Should open MainActivity

#### Large Widget Test:
1. Resize widget to large (4x3)
2. **Expected:** Should show:
   - "🍽️ Meal Tracker" header
   - Today section with meal count and categories
   - Divider line
   - "This Week" section with weekly meal count
   - "➕ Add Meal" button
3. **Tap widget background:** Should open DailyDashboardActivity
4. **Tap button:** Should open MainActivity

### 4. **Test Widget Updates**

#### Test Auto-Update:
1. Note current widget data
2. Wait 30 minutes
3. **Expected:** Widget should refresh automatically

#### Test Manual Update (Add Meal):
1. Note current meal count on widget
2. Open app and add a new meal
3. Return to home screen
4. **Expected:** Widget should show updated count immediately

#### Test Manual Update (Edit Meal):
1. Note current category breakdown
2. Open app and edit a meal's category
3. Return to home screen
4. **Expected:** Widget should show updated categories

#### Test Manual Update (Delete Meal):
1. Note current meal count
2. Open app and delete a meal
3. Return to home screen
4. **Expected:** Widget should show decreased count

### 5. **Test Edge Cases**

#### No Meals Today:
1. Delete all meals for today
2. **Expected Widget Shows:**
   - "0 meals today"
   - "🟢 0  ⚪ 0  🔴 0"
   - "0 meals this week" (large widget)

#### One Meal:
1. Add exactly one meal
2. **Expected Widget Shows:**
   - "1 meals today" (or "1 meal today" if you fix grammar)
   - Correct category count (e.g., "🟢 1  ⚪ 0  🔴 0")

#### Multiple Widgets:
1. Add 2-3 widgets to home screen
2. Add a meal
3. **Expected:** All widgets update simultaneously

#### Different Days:
1. Add meals for today
2. Change device date to tomorrow
3. **Expected:** Widget shows "0 meals today"

### 6. **Test Click Actions**

#### Widget Background Click:
1. Tap anywhere on widget (except button)
2. **Expected:** Opens DailyDashboardActivity
3. **Verify:** Shows today's meals

#### Quick Add Button Click:
1. Tap "➕ Add Meal" button
2. **Expected:** Opens MainActivity
3. **Verify:** Can add a new meal
4. After saving, return to home screen
5. **Verify:** Widget updated with new meal

### 7. **Test Visual Appearance**

#### Light Wallpaper:
1. Set a light-colored wallpaper
2. **Expected:** Widget text is readable
3. **Expected:** White background with subtle shadow

#### Dark Wallpaper:
1. Set a dark-colored wallpaper
2. **Expected:** Widget text is still readable
3. **Expected:** Widget stands out from background

#### Different Screen Densities:
Test on different devices if available:
- Phone (normal density)
- Tablet (high density)
- Small phone (low density)

### 8. **Performance Testing**

#### Battery Impact:
1. Add widget to home screen
2. Use phone normally for a day
3. Check battery stats
4. **Expected:** Widget should have minimal battery impact

#### Memory Usage:
1. Add multiple widgets
2. Check app memory usage in Settings
3. **Expected:** No significant memory increase

#### Update Speed:
1. Add a meal
2. Time how long until widget updates
3. **Expected:** Updates within 1-2 seconds

## Common Issues & Solutions

### Widget Not Showing Up
- **Solution:** Rebuild project and reinstall app
- **Check:** Widget is registered in AndroidManifest.xml

### Widget Not Updating
- **Solution:** Remove and re-add widget
- **Check:** WidgetUpdateHelper is being called

### Widget Shows Wrong Data
- **Solution:** Clear app data and restart
- **Check:** Database queries are correct

### Widget Crashes
- **Solution:** Check logcat for errors
- **Check:** All required permissions are granted

### Widget Layout Broken
- **Solution:** Check XML layout files
- **Check:** All referenced resources exist

## Debugging Tips

### View Logcat:
```bash
# Filter for widget logs
adb logcat | grep MealWidget
```

### Check Widget IDs:
```bash
# List all widgets
adb shell dumpsys appwidget
```

### Force Widget Update:
```kotlin
// In code
WidgetUpdateHelper.updateWidgets(context)
```

### Manual Broadcast:
```bash
# Send update broadcast
adb shell am broadcast -a android.appwidget.action.APPWIDGET_UPDATE
```

## Success Criteria

✅ Widget appears in widget picker  
✅ Widget can be added to home screen  
✅ Widget can be resized  
✅ Widget shows correct data  
✅ Widget updates automatically  
✅ Widget updates when meals change  
✅ Click actions work correctly  
✅ Widget works on different screen sizes  
✅ Widget has minimal battery impact  
✅ Widget looks good on light/dark wallpapers  

---

**Happy Testing!** 🎉

If you find any bugs, please document them and create an issue.

