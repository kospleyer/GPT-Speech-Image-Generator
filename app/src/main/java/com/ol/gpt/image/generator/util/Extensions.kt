package com.ol.gpt.image.generator.util

import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.util.*

object Extensions {

    fun View.showKeyboard() {
        this.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, 0)
    }

    fun Context.getLocaleStringResource(
        requestedLocale: Locale?,
        resourceId: Int,
    ): String {
        val result: String
        val config =
            Configuration(resources.configuration)
        config.setLocale(requestedLocale)
        result = createConfigurationContext(config).getText(resourceId).toString()

        return result
    }
}