package com.example.gardemanger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

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
                    Toast.makeText(this, "Merci de renseigner obligatoirement votre adresse mail et le mot de passe.", Toast.LENGTH_SHORT).show()
                    return
                }
            }

            else -> {

            }
        }
    }
}