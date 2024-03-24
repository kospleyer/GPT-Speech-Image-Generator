package com.brunn.patientapp.util.converters

import org.json.JSONObject

object JSONConverter {
    fun getServerMessage(jsonStr: String): String = JSONObject(jsonStr).getString("message")
}