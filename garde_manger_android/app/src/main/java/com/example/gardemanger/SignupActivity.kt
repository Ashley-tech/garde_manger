package com.example.gardemanger

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
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

class SignupActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var signup : Button;
    lateinit var retour : Button;
    lateinit var display : Button;
    lateinit var mdp : EditText;
    lateinit var displayr : Button;
    lateinit var mdp_reconfirm : EditText;
    lateinit var email : EditText;
    lateinit var emailr : EditText;
    lateinit var ln : EditText;
    lateinit var fn : EditText;
    lateinit var naissance : EditText;
    lateinit var adresse : EditText;
    lateinit var adresse_comp : EditText;
    lateinit var cp : EditText;
    lateinit var ville : EditText;
    lateinit var pays : EditText;
    lateinit var fonction : EditText;
    var showPwd = false;
    var showReconfirmPwd = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        retour = findViewById(R.id.retour_button_signup)
        retour.setOnClickListener(this)
        display = findViewById(R.id.pwddisplaybuttonsignup)
        displayr = findViewById(R.id.pwdreconfirmdisplaybuttonsignup)
        display.setOnClickListener(this)
        displayr.setOnClickListener(this)
        mdp = findViewById(R.id.editTextNewPassword)
        mdp_reconfirm = findViewById(R.id.editTextNewPasswordReconfirm)
        signup = findViewById(R.id.validate_signup_button)
        signup.setOnClickListener(this)
        email = findViewById(R.id.editTextEmail)
        emailr = findViewById(R.id.editTextEmailReconfirm)
        ln = findViewById(R.id.editTextLN)
        fn = findViewById(R.id.editTextFN)
        naissance = findViewById(R.id.editTextDN)
        adresse = findViewById(R.id.editTextAddress)
        adresse_comp = findViewById(R.id.editTextAddressComplement)
        cp = findViewById(R.id.editTextCP)
        ville = findViewById(R.id.editTextCity)
        pays = findViewById(R.id.editTextPays)
        fonction = findViewById(R.id.editTextFunction)
    }

    override fun onClick(v: View) {
        var i : Intent;
        when(v.getId()) {
            R.id.retour_button_signup -> {
                finish();
            }

            R.id.pwddisplaybuttonsignup -> {
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

            R.id.pwdreconfirmdisplaybuttonsignup -> {
                if (showReconfirmPwd) {
                    mdp_reconfirm.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    showReconfirmPwd = false;
                } else {
                    mdp_reconfirm.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    showReconfirmPwd = true;
                }
            }

            R.id.validate_signup_button -> {
                if (mdp.text.toString() == "" || email.text.toString() == "" || mdp_reconfirm.text.toString() == "" || emailr.text.toString() == "" || ln.text.toString() == "" || fn.text.toString() == "" || naissance.text.toString() == ""){
                    Toast.makeText(this, "Merci de renseigner obligatoirement tous les champs avec *.", Toast.LENGTH_SHORT).show()
                    return
                }
                if (!email.text.toString().equals(emailr.text.toString())){
                    Toast.makeText(this, "Les 2 emails saisis sont différents. Veuillez recommencer.", Toast.LENGTH_SHORT).show()
                    return
                }
                if (!mdp.text.toString().equals(mdp_reconfirm.text.toString())){
                    Toast.makeText(this, "Les 2 mots de passe saisis sont différents. Veuillez recommencer.", Toast.LENGTH_SHORT).show()
                    return
                }
                var regexD = Regex("^\\d{4}-\\d{2}-\\d{2}$")
                if(!regexCheck(regexD,naissance.text.toString())) {
                    Toast.makeText(this, "Votre date de naissance ne respecte pas le format AAAA-MM-DD", Toast.LENGTH_SHORT).show()
                    return
                }
                AsyncCheck().execute(email.text.toString())
            }

            else -> {

            }
        }
    }

    fun regexCheck(re: Regex, str: String):Boolean{
        return re.matches(str)
    }

    private inner class AsyncCheck : AsyncTask<String, Void, String>() {
        private val pdLoading = ProgressDialog(this@SignupActivity)
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
                    Toast.makeText(
                        this@SignupActivity,
                        "Votre adresse mail saisi existe dans notre base de données. Merci de renseigner une autre adresse mail",
                        Toast.LENGTH_LONG
                    ).show()
                    return;
                }
                AsyncSignup().execute(
                    ln.text.toString(),
                    fn.text.toString(),
                    email.text.toString(),
                    mdp.text.toString(),
                    naissance.text.toString(),
                    adresse.text.toString(),
                    adresse_comp.text.toString(),
                    cp.text.toString(),
                    ville.text.toString(),
                    pays.text.toString(),
                    fonction.text.toString()
                )
            } catch (e: Exception) {
                Toast.makeText(
                    this@SignupActivity,
                    "Erreur de parsing JSON",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private inner class AsyncSignup : AsyncTask<String, Void, String>() {
        private val pdLoading = ProgressDialog(this@SignupActivity)
        private var co: HttpURLConnection? = null
        private var url: URL? = null
        private var idc: String? = null
        override fun onPreExecute() {
            super.onPreExecute()

            pdLoading.setMessage("Inscription en cours...")
            pdLoading.setCancelable(false)
            pdLoading.show()
        }

        override fun doInBackground(vararg strings: String?): String {
            try {
                url = URL("http://10.0.2.2:8080/comptes")
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
                    .put("nom", strings[0])
                    .put("prenom", strings[1])
                    .put("email", strings[2])
                    .put("mdp", strings[3])
                    .put("dateNaissance", strings[4])
                    .put("adresse", strings[5])
                    .put("adresseComp", strings[6])
                    .put("cp", strings[7])
                    .put("ville", strings[8])
                    .put("pays", strings[9])
                    .put("fonction", strings[10])
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
                    this@SignupActivity,
                    "Une erreur s'est produite lors de l'inscription. Veuillez réessayer ultérieurement.",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            Toast.makeText(
                this@SignupActivity,
                "Votre inscription a été effectué avec succès.",
                Toast.LENGTH_LONG
            ).show()
            AsyncMailAuto().execute(email.text.toString(),"Inscription effectué","Bonjour,\n\n Nous vous confirmons votre inscription pour faire le suivi de votre stock d'aliments.\n\nCordialement,\n\nGarde-manger")
            finish()
        }
    }

    private inner class AsyncMailAuto : AsyncTask<String, Void, String>() {
        private val pdLoading = ProgressDialog(this@SignupActivity)
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
                    this@SignupActivity,
                    "Vous pouvez désormais vous connecter avec votre adresse email et votre mot de passe, mais nous n'avons pas pu vous envoyer un email de confirmation.",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            Toast.makeText(
                this@SignupActivity,
                "Nous vous avons envoyé un email de confirmation. Vous pouvez désormais vous connecter avec votre adresse email et votre mot de passe.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}