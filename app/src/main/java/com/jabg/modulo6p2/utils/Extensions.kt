package com.jabg.modulo6p2.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast

fun Activity.message(message: String, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(
        this,
        message,
        duration
    ).show()
}

@SuppressLint("ClickableViewAccessibility")
fun setupHideKeyboardOnTouch(view: View, activity: Activity) {
    // Set up touch listener for non-text box views to hide keyboard.
    if (view !is EditText) {
        view.setOnTouchListener { _, _ ->
            hideKeyboard(activity)
            false
        }
    }

    // If a layout container, iterate over children and seed recursion.
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val innerView = view.getChildAt(i)
            setupHideKeyboardOnTouch(innerView, activity)
        }
    }
}

private fun hideKeyboard(activity : Activity) {
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = activity.currentFocus
    view?.let {
        imm.hideSoftInputFromWindow(it.windowToken, 0)
        it.clearFocus()
    }
}