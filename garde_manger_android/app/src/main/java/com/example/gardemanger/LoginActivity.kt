package com.example.gardemanger

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
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

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var signup : Button;
    lateinit var login : Button;
    lateinit var display : Button;
    lateinit var mdp : EditText;
    lateinit var email : EditText;
    lateinit var quitter : Button;
    var passwordVisible = false;
    public val READ_TIMEOUT : Int = 15000
    public val CONNECTION_TIMEOUT : Int = 15000
    lateinit var forgot : Button;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        signup = findViewById(R.id.signup_button)
        signup.setOnClickListener(this)
        login = findViewById(R.id.login_button)
        login.setOnClickListener(this)
        display = findViewById(R.id.displaypwdloginb);
        display.setOnClickListener(this);
        email = findViewById(R.id.editTextMelLogin);
        mdp = findViewById(R.id.editTextPassword)
        forgot = findViewById(R.id.forgot_button)
        forgot.setOnClickListener(this);
        quitter = findViewById(R.id.quit_button)
        quitter.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        var i: Intent;
        when (p0.getId()) {
            R.id.displaypwdloginb -> {
                if (passwordVisible) {
                    mdp.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    passwordVisible = false;
                } else {
                    mdp.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    passwordVisible = true;
                }
            }
            R.id.signup_button -> {
                i = Intent(this,SignupActivity::class.java);
                startActivity(i);
            }
            R.id.forgot_button -> {
                i = Intent(this,ForgotPasswordActivity::class.java)
                startActivity(i)
            }
            R.id.login_button -> {
                if (mdp.text.toString() == "" || email.text.toString() == ""){
                    Toast.makeText(this, "Merci de renseigner obligatoirement votre adresse mail et le mot de passe.",Toast.LENGTH_SHORT)
                } else {
                    //val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
                    AsyncConnect().execute(
                        email.text.toString(),
                        mdp.text.toString()
                    )
                }
            }
            R.id.quit_button -> {
                finish();
            }
        }
    }

    private inner class AsyncConnect : AsyncTask<String, Void, String>() {
        private val pdLoading = ProgressDialog(this@LoginActivity)
        private var co: HttpURLConnection? = null
        private var url: URL? = null
        private var idc: String? = null
        override fun onPreExecute() {
            super.onPreExecute()

            pdLoading.setMessage("Connexion...")
            pdLoading.setCancelable(false)
            pdLoading.show()
        }

        override fun doInBackground(vararg strings: String?): String {
            try {
                url = URL("http://10.0.2.2:8080/login")
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

                val requestBody = JSONObject().put("email", strings[0]).put("password", strings[1]).toString()

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
                        this@LoginActivity,
                        "L'adresse email et le mot de passe saisi corespond à un compte. Redirection...",
                        Toast.LENGTH_LONG
                    ).show()
                    /*val intent = Intent(this@LoginActivity, NewPasswordActivity::class.java)
                    intent.putExtra("idCompte",idc)
                    intent.putExtra("mel",email.text.toString())
                    startActivity(intent)*/
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "L'adresse email et le mot de passe saisi ne correspond à aucun compte.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@LoginActivity,
                    "Erreur de parsing JSON",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}