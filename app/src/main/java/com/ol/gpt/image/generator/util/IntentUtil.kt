package com.ol.gpt.image.generator.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.ol.gpt.image.generator.R
import timber.log.Timber


object IntentUtil {

    private const val EMAIL_TYPE = "message/rfc822"

    fun performPhoneCall(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        try {
            context.startActivity(intent)
        } catch (ex: Exception) {
            Timber.e("perform phone call failed: $ex")
        }
    }

    fun openBrowser(context: Context, url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = if (!url.startsWith("http://") && !url.startsWith("https://"))
            Uri.parse("http://$url")
        else
            Uri.parse(url)
        try {
            context.startActivity(i)
        } catch (ex: Exception) {
            Timber.e("open browser failed: $ex")
        }
    }

    fun openMap(context: Context, address: String) {
        val mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address))
        val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        try {
            context.startActivity(mapIntent)
        } catch (ex: Exception) {
            Timber.e("open map failed: $ex")
        }
    }

    fun openGmailApp(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addCategory(Intent.CATEGORY_APP_EMAIL)
            context.startActivity(intent)
        } catch (ex: Exception) {
            Toast.makeText(
                context,
                R.string.error_occurred,
                Toast.LENGTH_SHORT
            ).show()
            Timber.e(ex)
        }
    }

    fun openGmailSendLetter(context: Context, email: String) {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            intent.type = EMAIL_TYPE
            context.startActivity(Intent.createChooser(intent, "Choose Email Client..."))
        } catch (e: Exception) {
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }
}