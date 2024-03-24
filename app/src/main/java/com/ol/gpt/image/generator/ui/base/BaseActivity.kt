package com.ol.gpt.image.generator.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ol.gpt.image.generator.R
import com.ol.gpt.image.generator.ui.dialog.progress_bar.ProgressBarDialog

open class BaseActivity : AppCompatActivity() {

    private var progressBarDialog: ProgressBarDialog? = null

    override fun onBackPressed() {
        hideProgressBarDialog()
        if (supportFragmentManager.backStackEntryCount > 1) {
            super.onBackPressed()
        } else {
            finish()
        }
    }

    protected fun replaceFragment(fragment: Fragment, bundle: Bundle? = null) {
        if (bundle != null) fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_left,
                R.anim.slide_out_right,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    protected fun addFragment(fragment: Fragment, bundle: Bundle? = null) {
        val fragmentName = fragment.javaClass.simpleName
        val name = getCurrentFragment()?.javaClass?.simpleName ?: ""
        if (name != fragmentName) {
            if (bundle != null) fragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
                .add(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    protected fun getCurrentFragment(): Fragment? =
        supportFragmentManager.findFragmentById(R.id.fragment_container)

    fun showProgressBarDialog() {
            if (progressBarDialog == null) {
                progressBarDialog = ProgressBarDialog()
                progressBarDialog!!.show(supportFragmentManager, null)
            }
    }

    fun hideProgressBarDialog() {
            progressBarDialog?.dismiss()
            progressBarDialog = null
    }

    open fun clearBackStack() {
        while (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    // remove fragments until the right fragment found
    open fun backTo(
        firstFragment: Fragment,
        secondFragment: Fragment? = null,
        changeTab: Boolean = false
    ) {
        val mainFragmentName = getFragmentName(firstFragment)
        val secondFragmentName = getFragmentName(secondFragment ?: firstFragment)
        var currentName = getFragmentName(getCurrentFragment())
        while (
            (mainFragmentName != currentName && secondFragmentName != currentName)
            && supportFragmentManager.backStackEntryCount > 1
        ) {
            supportFragmentManager.popBackStackImmediate()
            currentName = getFragmentName(getCurrentFragment())
        }
        val fragment = getCurrentFragment()
        if (changeTab) {
            if (fragment is BaseFragment) {
                fragment.updateData()
            }
        }
    }

    private fun getFragmentName(fragment: Fragment?): String =
        fragment?.javaClass?.simpleName ?: ""

    open fun removeFragmentsFromBackStack(count: Int) {
        for (i in 1..count) {
            supportFragmentManager.popBackStackImmediate()
        }
    }
}