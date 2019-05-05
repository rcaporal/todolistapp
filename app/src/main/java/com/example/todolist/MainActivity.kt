package com.example.todolist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonLogout.setOnClickListener {
            showLogoutMessage()
        }

        val tokenListener = object : AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(oldAccessToken: AccessToken?, newAccessToken: AccessToken?) {
                if (newAccessToken == null) {
                    logOut()
                }
            }
        }

    }

    private fun logOut() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLogoutMessage() {
        MaterialDialog(this).show {
            title(R.string.sair)
            message(R.string.deseja_sair_mensagem)
            positiveButton { LoginManager.getInstance().logOut() }
            negativeButton { }
        }
    }
}
