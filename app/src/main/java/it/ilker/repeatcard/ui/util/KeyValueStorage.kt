package it.ilker.repeatcard.ui.util

import android.content.Context
import android.content.SharedPreferences

interface KeyValueStorage {
    fun contains(key: String): Boolean
    fun remove(key: String)

    fun getString(key: String, default: String? = null): String?
    fun getInt(key: String, default: Int): Int
    fun getBoolean(key: String, default: Boolean): Boolean
    fun getLong(key: String, default: Long): Long
    fun getFloat(key: String, default: Float): Float
    fun putString(key: String, value: String)
    fun putInt(key: String, value: Int)
    fun putBoolean(key: String, value: Boolean)
    fun putLong(key: String, value: Long)
    fun putFloat(key: String, value: Float)
}

class KeyValueStorageFactory private constructor() {
    companion object {
        fun build(context: Context, name: String): KeyValueStorage =
            Preferences(context.getSharedPreferences(name, Context.MODE_PRIVATE))
    }
}

class Preferences(private val sharedPreferences: SharedPreferences) : KeyValueStorage {

    override fun contains(key: String): Boolean = sharedPreferences.contains(key)

    override fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    override fun getString(key: String, default: String?): String? =
        sharedPreferences.getString(key, default)

    override fun getInt(key: String, default: Int) =
        sharedPreferences.getInt(key, default)

    override fun getBoolean(key: String, default: Boolean) =
        sharedPreferences.getBoolean(key, default)

    override fun getLong(key: String, default: Long) =
        sharedPreferences.getLong(key, default)

    override fun getFloat(key: String, default: Float) =
        sharedPreferences.getFloat(key, default)

    override fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun putInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    override fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    override fun putLong(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    override fun putFloat(key: String, value: Float) {
        sharedPreferences.edit().putFloat(key, value).apply()
    }
}
