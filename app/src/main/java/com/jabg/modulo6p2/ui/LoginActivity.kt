package com.jabg.modulo6p2.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jabg.modulo6p2.R
import com.jabg.modulo6p2.databinding.ActivityLoginBinding
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.jabg.modulo6p2.utils.message
import com.jabg.modulo6p2.utils.setupHideKeyboardOnTouch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        setupHideKeyboardOnTouch(binding.root,this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        screenSplash.setKeepOnScreenCondition {false}

        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser != null){
            actionLoginSuccessful()
        }

        binding.btnLogin.setOnClickListener {
            if (validateFields()){
                binding.progressBar.visibility = View.VISIBLE
                authenticateUser(email, password)
            }
        }

        binding.btnRegistrarse.setOnClickListener {
            if (validateFields()){
                binding.progressBar.visibility = View.VISIBLE
                createUser(email, password)
            }
        }

        binding.tvRestablecerPassword.setOnClickListener {
            resetPassword()
        }
    }

    private fun validateFields(): Boolean {
        email = binding.tietEmail.text.toString().trim()
        password = binding.tietContrasena.text.toString().trim()

        if (email.isEmpty()) {
            binding.tietEmail.error = getString(R.string.error_required_email)
            binding.tietEmail.requestFocus()
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tietEmail.error = getString(R.string.error_invalid_email_format)
            binding.tietEmail.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            binding.tietContrasena.error = getString(R.string.error_required_password)
            binding.tietContrasena.requestFocus()
            return false
        } else if (password.length < 6) {
            binding.tietContrasena.error = getString(R.string.error_password_length)
            binding.tietContrasena.requestFocus()
            return false
        }

        return true
    }

    private fun handleErrors(task: Task<AuthResult>) {
        var errorCode = ""

        try {
            errorCode = (task.exception as FirebaseAuthException).errorCode
        } catch (e: Exception) {
            e.printStackTrace()
        }

        when (errorCode) {
            "ERROR_INVALID_EMAIL" -> {
                val error = getString(R.string.error_invalid_email)
                message(error)
                binding.tietEmail.error = error
                binding.tietEmail.requestFocus()
            }
            "ERROR_WRONG_PASSWORD" -> {
                val error = getString(R.string.error_wrong_password)
                message(error)
                binding.tietContrasena.error = error
                binding.tietContrasena.requestFocus()
                binding.tietContrasena.setText("")
            }
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                message(getString(R.string.error_existing_account))
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                val error = getString(R.string.error_email_in_use)
                message(error)
                binding.tietEmail.error = error
                binding.tietEmail.requestFocus()
            }
            "ERROR_USER_TOKEN_EXPIRED" -> {
                message(getString(R.string.error_token_expired))
            }
            "ERROR_USER_NOT_FOUND" -> {
                message(getString(R.string.error_user_not_found))
            }
            "ERROR_WEAK_PASSWORD" -> {
                message(getString(R.string.error_weak_password))
                binding.tietContrasena.error = getString(R.string.error_password_strength)
                binding.tietContrasena.requestFocus()
            }
            "NO_NETWORK" -> {
                message(getString(R.string.error_no_network))
            }
            else -> {
                message(getString(R.string.error_auth_failed))
            }
        }
    }

    private fun actionLoginSuccessful() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun authenticateUser(user: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(user, password).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {
                message(getString(R.string.auth_success))
                actionLoginSuccessful()
            } else {
                binding.progressBar.visibility = View.GONE
                handleErrors(authResult)
            }
        }
    }

    private fun createUser(user: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(user, password).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {
                firebaseAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                    message(getString(R.string.email_verification_sent))
                }?.addOnFailureListener {
                    message(getString(R.string.email_verification_failed))
                }

                message(getString(R.string.registration_success))
                actionLoginSuccessful()
            } else {
                binding.progressBar.visibility = View.GONE
                handleErrors(authResult)
            }
        }
    }

    private fun resetPassword() {
        val resetMail = EditText(this)
        resetMail.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        resetMail.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))

         MaterialAlertDialogBuilder(this, R.style.CustomAlertDialog)
            .setTitle(getString(R.string.reset_password_title))
            .setMessage(getString(R.string.reset_password_message))
            .setView(resetMail)
            .setPositiveButton(getString(R.string.send)) { _, _ ->
                val email = resetMail.text.toString()
                if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                        message(getString(R.string.password_reset_link_sent))
                    }.addOnFailureListener {
                        message(getString(R.string.password_reset_link_failed))
                    }
                } else {
                    message(getString(R.string.invalid_email_input))
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

}