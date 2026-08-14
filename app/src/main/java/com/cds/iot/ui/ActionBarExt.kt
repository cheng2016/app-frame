package com.cds.iot.ui

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import com.cds.iot.R

fun View.setupActionBar(
    titleText: String,
    showBack: Boolean = true,
    onBack: (() -> Unit)? = null,
) {
    findViewById<TextView>(R.id.title)?.text = titleText
    findViewById<View>(R.id.back_button)?.apply {
        isVisible = showBack
        setOnClickListener { onBack?.invoke() }
    }
}
