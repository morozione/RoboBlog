package com.morozione.roboblog.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.morozione.roboblog.MainActivity
import com.morozione.roboblog.R
import com.morozione.roboblog.entity.User
import com.morozione.roboblog.mvp.presenter.LoginPresenter
import com.morozione.roboblog.mvp.view.LoginView
import com.morozione.roboblog.utils.bind

class LoginActivity : MvpAppCompatActivity(), LoginView {

    private val mLogin by bind<Button>(R.id.login)
    private val bRegistration by bind<Button>(R.id.registration)
    private val mEmail by bind<EditText>(R.id.email)
    private val mPassword by bind<EditText>(R.id.password)

    @InjectPresenter
    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setListeners()
        checkOnLoginning()
    }

    private fun checkOnLoginning() {
        if (presenter.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setListeners() {
        mLogin.setOnClickListener {
            singIn()
        }
        bRegistration.setOnClickListener {
            singUp()
        }
    }

    private fun singIn() {
        val login = mEmail.text.toString()
        val password = mPassword.text.toString()
        if (!login.isEmpty() && !password.isEmpty()) {
            presenter.singIn(login, password)
        } else {
            Toast.makeText(this, "Input available data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun singUp() {
        val login = mEmail.text.toString()
        val password = mPassword.text.toString()
        if (!login.isEmpty() && !password.isEmpty()) {
            presenter.singUp(login, password)
        } else {
            Toast.makeText(this, "Input available data", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAuthorizationResult(isSuccess: Boolean) {
        if (isSuccess) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onRegistrationResult(isSuccess: Boolean) {
        if (isSuccess) {
            presenter.saveUser(getFilledUser())
        }
    }

    override fun onSavedUserSuccess(isSuccess: Boolean) {
        if (isSuccess) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onError() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }

    private fun getFilledUser(): User {
        val user = User()
        user.email = mEmail.text.toString()

        return user
    }
}
