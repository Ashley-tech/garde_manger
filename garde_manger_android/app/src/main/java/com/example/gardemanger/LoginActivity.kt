package com.example.gardemanger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

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
                    /*val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
                    AsyncConnect().execute(
                        email.text.toString(),
                        mdp.text.toString()
                    )*/
                }
            }
            R.id.quit_button -> {
                finish();
            }
        }
    }
}