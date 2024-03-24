package com.ol.gpt.image.generator.ui.base

import android.app.Application
import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ol.gpt.image.generator.util.Const.REQUEST_DELAY
import kotlinx.coroutines.*
import org.koin.java.KoinJavaComponent

open class BaseViewModel constructor(
    application: Application,
    private val coroutineScope: CoroutineScope
) : AndroidViewModel(application), CoroutineScope by coroutineScope {

    protected val context: Context by lazy { getApplication<Application>().applicationContext }

    val isLoading = MutableLiveData<Boolean>()
    val getSuccessData = MutableLiveData<Boolean?>()

    fun clearSuccessData() {
        getSuccessData.postValue(null)
    }


    // use this to set delay only for Loading Live Data
    protected fun getJobDelay(): Job = coroutineScope.launch {
        setDelay()
        isLoading.postValue(true)
    }

    var error: String = ""

    //Additional delay used to wait when screen will animate properly
    private suspend fun setDelay() {
        delay(REQUEST_DELAY)
    }

    protected fun getString(@StringRes stringRes: Int): String {
        return context.getString(stringRes)
    }

    protected fun getString(@StringRes stringRes: Int, vararg formatArgs: Any): String {
        return context.getString(stringRes, *formatArgs)
    }

    override fun onCleared() {
        coroutineScope.coroutineContext.cancelChildren()
        super.onCleared()
    }
}