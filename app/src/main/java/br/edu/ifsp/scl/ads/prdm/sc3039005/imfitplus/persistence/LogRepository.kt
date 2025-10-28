package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.persistence

import android.content.Context
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.LogItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit

object LogRepository {

    private const val LOG_PREFS = "LogPrefs"
    private const val LOG_KEY = "LogList"
    private val gson = Gson()

    fun getAllLogs(context: Context): MutableList<LogItem> {
        val prefs = context.getSharedPreferences(LOG_PREFS, Context.MODE_PRIVATE)
        val json = prefs.getString(LOG_KEY, null)

        if (json == null) {
            return mutableListOf()
        }

        val type = object : TypeToken<MutableList<LogItem>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveLogItem(context: Context, newItem: LogItem) {
        val logList = getAllLogs(context)

        logList.add(0, newItem)

        val json = gson.toJson(logList)

        val prefs = context.getSharedPreferences(LOG_PREFS, Context.MODE_PRIVATE)
        prefs.edit { putString(LOG_KEY, json) }
    }
}