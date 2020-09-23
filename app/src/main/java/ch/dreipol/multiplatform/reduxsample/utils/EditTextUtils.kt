package ch.dreipol.multiplatform.reduxsample.utils

import android.widget.EditText

fun EditText.setNewText(newText: String?) {
    if (text?.toString().orEmpty() != newText.orEmpty()) {
        setText(newText)
    }
}