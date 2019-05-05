package com.example.todolist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val accessToken = AccessToken.getCurrentAccessToken()
        checkAccessToken(accessToken)

    }

    private fun checkAccessToken(accessToken: AccessToken?) {
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        val intent = if (isLoggedIn) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()
    }


}
