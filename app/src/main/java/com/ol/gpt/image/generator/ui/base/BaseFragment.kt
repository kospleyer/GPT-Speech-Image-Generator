package com.ol.gpt.image.generator.ui.base

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ol.gpt.image.generator.ui.main.MainActivity

open class BaseFragment : Fragment() {


    override fun onStart() {
        super.onStart()
        hideKeyboard()
    }

    fun getMainActivity(): MainActivity? {
        return activity as? MainActivity
    }

    fun getBaseActivity(): BaseActivity? {
        return activity as? BaseActivity
    }

    /*
    Implement in fragments that need to update data after onBackPressed()
     */
    open fun updateData() {}

    protected fun hideKeyboard() {
        requireActivity().currentFocus?.let { view ->
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    protected fun showToastLong(stringId: Int) {
        Toast.makeText(
            requireContext(),
            getString(stringId),
            Toast.LENGTH_LONG
        ).show()
    }
}