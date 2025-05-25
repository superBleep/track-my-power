package com.afca.trackmypower.helpers.bindingAdapters

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("string", "args", requireAll = false)
fun formatText(textView: TextView, string: String?, args: Array<Any>?) {
    if (string == null) return

    val text = if (args != null)
        String.format(string, *args)
    else
        string

    textView.text = text
}
