package com.example.gardemanger

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var retour : Button;
    lateinit var valider : Button;
    lateinit var email : EditText;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        retour = findViewById(R.id.retour_button_forgot);
        retour.setOnClickListener(this);
        valider = findViewById(R.id.check_button);
        valider.setOnClickListener(this);
        email = findViewById(R.id.editTextEmailForgot);
    }

    override fun onClick(v: View) {
        var i: Intent;
        when (v.getId()) {
            R.id.retour_button_forgot -> {
                finish();
            }
            R.id.check_button -> {
                if (email.text.toString() == ""){
                    Toast.makeText(
                        this,
                        "Ce champ est obligatoire !",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    AsyncCheck().execute(email.text.toString())
                }
            }
            else -> {

            }
        }
    }

    private inner class AsyncCheck : AsyncTask<String, Void, String>() {
        private val pdLoading = ProgressDialog(this@ForgotPasswordActivity)
        private var co: HttpURLConnection? = null
        private var url: URL? = null
        private var idc: String? = null
        override fun onPreExecute() {
            super.onPreExecute()

            pdLoading.setMessage("Vérification de votre adresse mail...")
            pdLoading.setCancelable(false)
            pdLoading.show()
        }

        override fun doInBackground(vararg strings: String?): String {
            try {
                url = URL("http://10.0.2.2:8080/comptes/forgot-password")
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                return "false"
            }

            try {
                co = url!!.openConnection() as HttpURLConnection
                co!!.readTimeout = 15000
                co!!.connectTimeout = 15000
                co!!.requestMethod = "POST"
                co!!.setRequestProperty(
                    "Content-Type",
                    "application/json; charset=UTF-8"
                )

                co!!.doInput = true
                co!!.doOutput = true

                val requestBody = JSONObject().put("email", strings[0]).toString()

                val os: OutputStream = co!!.outputStream

                val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))

                writer.write(requestBody)
                writer.flush()
                writer.close()

                os.close()

                co!!.connect()

            } catch (e: IOException) {
                e.printStackTrace()
                return "Exception"
            }
            try {
                val responseCode = co!!.responseCode
                Log.d("HTTP_CODE", responseCode.toString())
                return if (responseCode == HttpURLConnection.HTTP_OK) {
                    val input: InputStream = co!!.inputStream
                    val reader = BufferedReader(InputStreamReader(input))
                    val result = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        result.append(line)
                    }
                    result.toString()
                } else {
                    "Unsuccessful"
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return "Exception"
            } finally {
                co?.disconnect()
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            pdLoading.dismiss()

            if (result == null) return
            Log.d("message", result)
            try {
                val json = JSONObject(result)
                val found = json.getBoolean("success")
                if (found) {
                    val idc = json.getInt("userId").toString()
                    Log.d("id", idc.toString())
                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        "Votre adresse mail existe dans notre base de données. Redirection...",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(this@ForgotPasswordActivity, NewPasswordActivity::class.java)
                    intent.putExtra("idCompte",idc)
                    intent.putExtra("mel",email.text.toString())
                    Log.i("intent",intent.toString())
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        "Votre adresse mail saisi n'existe pas dans notre base de données. Merci de vous inscrire.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ForgotPasswordActivity,
                    "Erreur de parsing JSON",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}