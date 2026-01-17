# 📝 Code Explanation - Line by Line

Let's break down every single line of code so you understand exactly what's happening!

---

## 🧠 MainActivity.kt - The Brain

```kotlin
package com.example.mealtracker
```
**What it means:** This file belongs to the `com.example.mealtracker` package (like a folder for organizing code).

```kotlin
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
```
**What it means:** Import (bring in) code from Android libraries that we need:
- `AppCompatActivity` - Base class for app screens
- `Bundle` - Saves app state
- `Button` - Button UI element
- `TextView` - Text display element

```kotlin
class MainActivity : AppCompatActivity() {
```
**What it means:** 
- Create a new class called `MainActivity`
- `:` means "inherits from" or "is a type of"
- `AppCompatActivity()` is the parent class (gives us screen functionality)

```kotlin
    private var clickCount = 0
```
**What it means:**
- `private` - Only this class can access this variable
- `var` - Variable (can change)
- `clickCount` - Name of the variable
- `= 0` - Starts at zero
- This keeps track of how many times the button was clicked

```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
```
**What it means:**
- `override` - We're replacing the parent's version of this function
- `fun` - Declares a function (a block of code that does something)
- `onCreate` - Function name (runs when activity is created)
- `savedInstanceState: Bundle?` - Parameter (can be null, indicated by `?`)

```kotlin
        super.onCreate(savedInstanceState)
```
**What it means:**
- `super` - Refers to the parent class (AppCompatActivity)
- Call the parent's `onCreate` first (required!)

```kotlin
        setContentView(R.layout.activity_main)
```
**What it means:**
- `setContentView` - Sets what the screen looks like
- `R.layout.activity_main` - Points to `activity_main.xml` file
- This loads your UI design

```kotlin
        val textView = findViewById<TextView>(R.id.textView)
        val button = findViewById<Button>(R.id.button)
```
**What it means:**
- `val` - Value (cannot change once set)
- `findViewById` - Find a UI element by its ID
- `<TextView>` - Tell Kotlin what type of element it is
- `R.id.textView` - The ID from XML (remember `android:id="@+id/textView"`?)
- Now we have references to our UI elements!

```kotlin
        button.setOnClickListener {
```
**What it means:**
- `setOnClickListener` - Set up code to run when button is clicked
- `{` - Start of the code block (lambda function)

```kotlin
            clickCount++
```
**What it means:**
- `clickCount++` - Increase clickCount by 1 (same as `clickCount = clickCount + 1`)

```kotlin
            textView.text = "Hello World!\nButton clicked $clickCount times"
```
**What it means:**
- `textView.text` - The text property of the TextView
- `=` - Set it to...
- `"Hello World!\n..."` - A string (text)
- `\n` - New line (like pressing Enter)
- `$clickCount` - Insert the value of clickCount into the string
- This updates what the user sees!

```kotlin
        }
    }
}
```
**What it means:** Close the click listener, close onCreate, close the class.

---

## 🎨 activity_main.xml - The UI

```xml
<?xml version="1.0" encoding="utf-8"?>
```
**What it means:** This is an XML file, version 1.0, using UTF-8 encoding.

```xml
<androidx.constraintlayout.widget.ConstraintLayout
```
**What it means:** 
- Root element (container for everything)
- `ConstraintLayout` - A flexible layout system
- Elements inside are positioned relative to each other

```xml
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
```
**What it means:** 
- `xmlns` - XML namespace (defines where attributes come from)
- `android:` - Standard Android attributes
- `app:` - Custom app attributes (like constraints)
- `tools:` - Design-time only attributes

```xml
    android:layout_width="match_parent"
    android:layout_height="match_parent"
```
**What it means:**
- `layout_width` - How wide the element is
- `match_parent` - Fill the entire parent (whole screen)
- Same for height

```xml
    android:padding="16dp"
```
**What it means:**
- `padding` - Space inside the element
- `16dp` - 16 density-independent pixels (scales on different screens)

```xml
    tools:context=".MainActivity">
```
**What it means:**
- Tells Android Studio this layout is used by MainActivity
- Helps with autocomplete and previews

```xml
    <TextView
        android:id="@+id/textView"
```
**What it means:**
- Create a TextView element
- Give it an ID of "textView"
- `@+id/` means "create a new ID"

```xml
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
```
**What it means:**
- `wrap_content` - Only as big as the text inside

```xml
        android:text="Hello World!"
        android:textSize="24sp"
        android:textStyle="bold"
        android:gravity="center"
```
**What it means:**
- `text` - What text to display
- `textSize` - Size in scale-independent pixels
- `textStyle` - Make it bold
- `gravity` - Center the text within the TextView

```xml
        app:layout_constraintBottom_toTopOf="@+id/button"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
```
**What it means:** Position constraints:
- Bottom edge is above the button
- Left edge aligns with parent's left
- Right edge aligns with parent's right
- Top edge aligns with parent's top
- Result: Centered horizontally, in upper half of screen

```xml
    <Button
        android:id="@+id/button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Click Me!"
        android:textSize="18sp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView" />
```
**What it means:** Similar to TextView, but:
- It's a Button
- Top edge is below the TextView
- Bottom edge aligns with parent's bottom
- Result: Centered horizontally, in lower half of screen

---

## 🎯 How It All Works Together

1. **App starts** → Android creates MainActivity
2. **onCreate runs** → Loads activity_main.xml
3. **UI appears** → TextView shows "Hello World!", Button shows "Click Me!"
4. **User clicks button** → onClick code runs
5. **Counter increases** → clickCount goes from 0 to 1
6. **Text updates** → TextView now shows "Hello World!\nButton clicked 1 times"
7. **Repeat** → Each click increases the counter

---

## 🔄 The Flow

```
MainActivity.kt          activity_main.xml
     |                         |
     |-- onCreate() ---------> Load layout
     |                         |
     |<-- findViewById() ----- TextView (id: textView)
     |<-- findViewById() ----- Button (id: button)
     |                         |
     |-- setOnClickListener -> Button
     |                         |
User clicks button <----------+
     |
     |-- clickCount++
     |-- textView.text = "..."
     |
     +--> Screen updates!
```

---

## 💡 Key Takeaways

1. **Kotlin code** controls behavior (what happens)
2. **XML layout** controls appearance (what you see)
3. **IDs** connect them together
4. **findViewById** is the bridge between code and UI
5. **Listeners** respond to user actions

---

**Next:** Try modifying the code! Change text, add another button, experiment!

