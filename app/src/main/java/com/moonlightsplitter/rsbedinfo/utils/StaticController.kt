package com.moonlightsplitter.rsbedinfo.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class StaticController {

    fun dateFormatted(date: String?): String {
        var result = ""
        val sdf1 = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val sdf2 = SimpleDateFormat("d MMM yyyy h:mm a")
        try {
            val parseDate = sdf1.parse(date)
            val timestamp = parseDate.time
            result = sdf2.format(Date(timestamp))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return result
    }

    fun testB(): String {
        return "Anjay"
    }
}