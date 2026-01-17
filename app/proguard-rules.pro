# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep line number information for debugging stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ===== Room Database =====
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keepclassmembers class * {
    @androidx.room.* <methods>;
}
-dontwarn androidx.room.paging.**

# ===== Kotlin Coroutines =====
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-dontwarn kotlinx.coroutines.**

# ===== Gson (for backup/restore) =====
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class com.prajwalpai.mealtracker.** { *; }
-keep class com.prajwalpai.mealtracker.backup.** { *; }

# Keep data classes for serialization
-keep class com.prajwalpai.mealtracker.Meal { *; }
-keep class com.prajwalpai.mealtracker.backup.BackupData { *; }
-keep class com.prajwalpai.mealtracker.goals.** { *; }

# ===== Glance Widget =====
-keep class androidx.glance.** { *; }
-keep class androidx.glance.appwidget.** { *; }

# ===== WorkManager =====
-keep class androidx.work.** { *; }
-keep class androidx.work.impl.** { *; }

# ===== Keep ViewModels =====
-keep class com.prajwalpai.mealtracker.MealViewModel { *; }

# ===== Keep Activities =====
-keep class com.prajwalpai.mealtracker.*Activity { *; }

# ===== Keep Custom Views =====
-keep class com.prajwalpai.mealtracker.MealSlotView { *; }

# ===== Keep Adapters =====
-keep class com.prajwalpai.mealtracker.*Adapter { *; }

# ===== Keep Repositories =====
-keep class com.prajwalpai.mealtracker.MealRepository { *; }

# ===== Keep Notification and Widget classes =====
-keep class com.prajwalpai.mealtracker.notifications.** { *; }
-keep class com.prajwalpai.mealtracker.widget.** { *; }
-keep class com.prajwalpai.mealtracker.goals.** { *; }
-keep class com.prajwalpai.mealtracker.backup.** { *; }

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# ===== Keep native methods =====
-keepclasseswithmembernames class * {
    native <methods>;
}

# ===== Keep Parcelables =====
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# ===== Keep Serializable classes =====
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

