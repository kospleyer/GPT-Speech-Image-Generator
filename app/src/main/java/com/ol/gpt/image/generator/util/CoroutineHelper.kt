package com.ol.gpt.image.generator.util

import com.brunn.patientapp.util.converters.JSONConverter
import org.json.JSONException
import retrofit2.Response
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


object CoroutineHelper {
    suspend fun <T : Any> getSuspendCoroutine(response: Response<T>): T =
        suspendCoroutine { continuation ->
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                continuation.resume(data)
            } else {
                try {
                    val errorString = response.errorBody()!!.string()
                    val message = JSONConverter.getServerMessage(errorString)
                    continuation.resumeWithException(Exception(message, Throwable(errorString)))
                    Timber.e("Server responses with error: ${response.message()}")
                } catch (jsonException: JSONException) {
                    Timber.e(response.message())
                    continuation.resumeWithException(Exception())
                } catch (jsonException: Exception) {
                    Timber.e(response.message())
                    continuation.resumeWithException(Exception())
                }
            }
        }
}
