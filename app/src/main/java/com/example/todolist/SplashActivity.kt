package com.example.todolist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.data.TokenPreferences
import com.facebook.AccessToken


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val accessToken: AccessToken? = TokenPreferences.getToken(this)
        checkAccessToken(accessToken)

    }

    private fun checkAccessToken(accessToken: AccessToken?) {
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        val intent = if (isLoggedIn) {
            Intent(this, MainActivity::class.java)
        } else {
            TokenPreferences.clear(this)
            Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()
    }


}
