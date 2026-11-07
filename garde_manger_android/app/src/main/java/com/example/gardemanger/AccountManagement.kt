package com.example.gardemanger

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class AccountManagement(private val co: Context) {

    private var sP: SharedPreferences =
        co.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var editor: SharedPreferences.Editor = sP.edit()

    companion object {
        const val PREF_NAME = "User_login"
        const val COMPTE_ID = "compte_id"
        const val COMPTE_EMAIL = "compte_email"
    }

    fun isAccountLogin(): Boolean {
        return sP.getString(COMPTE_ID, null) != null &&
                sP.getString(COMPTE_EMAIL, null) != null
    }

    fun getCompteId(): String? {
        return sP.getString(COMPTE_ID, null)
    }

    fun getCompteEmail(): String? {
        return sP.getString(COMPTE_EMAIL, null)
    }

    fun accountSessionUpdate(
        compteId: String,
        compteEmail: String
    ) {
        editor.putString(COMPTE_ID, compteId)
        editor.putString(COMPTE_EMAIL, compteEmail)
        editor.apply()
    }

    fun userDetails(): HashMap<String, String?> {
        val account = HashMap<String, String?>()
        account[COMPTE_ID] = sP.getString(COMPTE_ID, null)
        account[COMPTE_EMAIL] = sP.getString(COMPTE_EMAIL, null)
        return account
    }

    fun updateInfo(
        compteId: String,
        compteEmail: String
    ) {
        editor.clear().apply()
        //editor.commit()

        editor.putString(COMPTE_ID, compteId)
        editor.putString(COMPTE_EMAIL, compteEmail)
        editor.apply()
    }

    /*fun accountSessionManage(
        compteId: String,
        compteEmail: String
    ) {
        editor.putString(COMPTE_ID, compteId)
        editor.putString(COMPTE_EMAIL, compteEmail)
        editor.apply()

        val intent = Intent(co, MainMenuActivity::class.java)
        intent.putExtra(COMPTE_ID, compteId)
        intent.putExtra(COMPTE_EMAIL, compteEmail)

        co.startActivity(intent)

        (co as LoginActivity).finish()
    }

    fun checkLogin() {
        if (!isAccountLogin()) {
            val intent = Intent(co, LoginActivity::class.java)
            co.startActivity(intent)
            if (co is MainMenuActivity) {
                co.finish()
            }
        }
    }

    fun logout() {
        editor.clear()
        editor.apply()
        val intent = Intent(co, LoginActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        co.startActivity(intent)
    }

    fun deleteAccount() {
        editor.clear().apply()
        val intent = Intent(co, LoginActivity::class.java)
        co.startActivity(intent)
        (co as ConfirmDeleteAccountActivity).finish()
    }*/
}