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
import android.widget.TextView
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

class NewPasswordActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var valider: Button;
    lateinit var retour: Button;
    lateinit var mdp : EditText;
    lateinit var mdpr : EditText;
    lateinit var mel : TextView;
    lateinit var display : Button;
    lateinit var displayr : Button;
    lateinit var extras : Bundle;
    var showPwd = false;
    var showReconfirmPwd = false;
    var id = "";
    var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_password)
        valider = findViewById(R.id.change_pwd_button);
        valider.setOnClickListener(this);
        retour = findViewById(R.id.retour_button_new_password);
        retour.setOnClickListener(this);
        display = findViewById(R.id.newpwddisplaybutton);
        displayr = findViewById(R.id.newpwdrdisplaybutton);
        display.setOnClickListener(this)
        displayr.setOnClickListener(this)
        mel = findViewById(R.id.labelmel);
        mdp = findViewById(R.id.editTextNewPasswordForgot);
        mdpr = findViewById(R.id.editTextNewPasswordReconfirmForgot);
        extras = intent.extras!!
        id = extras.getString("idCompte").toString()
        Log.i("extras",id)
        email = extras.getString("mel").toString();
        mel.setText("Mail : "+email);
    }

    override fun onClick(v: View) {
        var i: Intent;
        when (v.getId()) {
            R.id.retour_button_new_password -> {
                finish();
            }

            R.id.newpwddisplaybutton -> {
                if (showPwd) {
                    mdp.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    showPwd = false;
                } else {
                    mdp.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    showPwd = true;
                }
            }

            R.id.newpwdrdisplaybutton -> {
                if (showReconfirmPwd) {
                    mdpr.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    showReconfirmPwd = false;
                } else {
                    mdpr.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    showReconfirmPwd = true;
                }
            }

            R.id.change_pwd_button -> {
                if (mdp.text.toString().equals("") || mdpr.text.toString().equals("")){
                    Toast.makeText(this, "Tous les champs sont obligatoires.", Toast.LENGTH_SHORT).show()
                    return;
                }
                if (mdp.text.toString() != mdpr.text.toString()){
                    Toast.makeText(this, "Les 2 mots de passe saisis sont différents. Veuillez recommencer.", Toast.LENGTH_SHORT).show()
                    return;
                }
                if (mdp.text.toString().length < 8 || mdp.text.toString().length > 50){
                    Toast.makeText(this, "Le mot de passe doit contenir entre 8 et 50 caractères. Nombre : "+mdp.text.toString().length, Toast.LENGTH_SHORT).show()
                    return;
                }
                AsyncChangePwd().execute(mdp.text.toString())
            }

            else -> {

            }
        }
    }

    private inner class AsyncChangePwd : AsyncTask<String, Void, String>() {
        private val pdLoading = ProgressDialog(this@NewPasswordActivity)
        private var co: HttpURLConnection? = null
        private var url: URL? = null
        private var idc: String? = null
        override fun onPreExecute() {
            super.onPreExecute()

            pdLoading.setMessage("Changement du mot de passe...")
            pdLoading.setCancelable(false)
            pdLoading.show()
        }

        override fun doInBackground(vararg strings: String?): String {
            try {
                url = URL("http://10.0.2.2:8080/comptes/"+id.toString())
                Log.i("url","http://10.0.2.2:8080/comptes/"+id.toString())
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                return "false"
            }

            try {
                co = url!!.openConnection() as HttpURLConnection
                co!!.readTimeout = 15000
                co!!.connectTimeout = 15000
                co!!.requestMethod = "PATCH"
                co!!.setRequestProperty(
                    "Content-Type",
                    "application/json; charset=UTF-8"
                )

                co!!.doInput = true
                co!!.doOutput = true

                val requestBody = JSONObject().put("mdp", strings[0]).toString()

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
                return if (responseCode in 200..299) {
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
            if (result == "Unsuccessful" || result == "Exception" || result == "false") {
                Toast.makeText(
                    this@NewPasswordActivity,
                    "Une erreur s'est produite lors de la modification du mot de passe. Veuillez réessayer ultérieurement.",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            Toast.makeText(
                this@NewPasswordActivity,
                "Votre mot de passe a été modifié avec succès.",
                Toast.LENGTH_LONG
            ).show()

            val intent = Intent(this@NewPasswordActivity, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            AsyncMailAuto().execute(email,"Mot de passe réinitialisé","Bonjour,\n\n Nous vous confirmons que votre mot de passe a été réinitialisé. Vous pouvez désormais vous reconnecter avec votre nouveau mot de passe.\n\nCordialement,\n\nGarde-manger");
            startActivity(intent)
        }
    }

    private inner class AsyncMailAuto : AsyncTask<String, Void, String>() {
        private val pdLoading = ProgressDialog(this@NewPasswordActivity)
        private var co: HttpURLConnection? = null
        private var url: URL? = null
        private var idc: String? = null
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg strings: String?): String {
            try {
                url = URL("http://10.0.2.2:8080/sendEmail")
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

                val requestBody = JSONObject()
                    .put("to", strings[0])
                    .put("subject", strings[1])
                    .put("text", strings[2])
                    .toString()

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
                return if (responseCode in 200..299) {
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
            if (result == "Unsuccessful" || result == "Exception" || result == "false") {
                Toast.makeText(
                    this@NewPasswordActivity,
                    "Vous pouvez désormais vous connecter avec votre adresse email et votre mot de passe, mais nous n'avons pas pu vous envoyer un email de confirmation.",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            Toast.makeText(
                this@NewPasswordActivity,
                "Nous vous avons envoyé un email de confirmation. Vous pouvez désormais vous connecter avec votre adresse email et votre mot de passe.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}