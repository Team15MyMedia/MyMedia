package com.example.mymedia.main

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes

// ViewModel 에서 간접적으로 받아봄
interface ContextProvider {

    fun getSharedPreferences(): SharedPreferences

    fun getString(@StringRes stringRes: Int): String

}

// View 에서 주입함
class ContextProviderImpl(
    private val context: Context
) : ContextProvider {

    override fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences("name", Context.MODE_PRIVATE)
    }

    override fun getString(stringRes: Int): String {
        return context.getString(stringRes)
    }
}
