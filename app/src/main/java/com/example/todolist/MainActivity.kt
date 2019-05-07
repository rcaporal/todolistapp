package com.example.todolist

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.example.todolist.data.TokenPreferences
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_task.*


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

        checkboxTask.setOnCheckedChangeListener { buttonView, isChecked ->
            showStrikeThrough(textTask, isChecked)
        }

    }

    private fun logOut() {
        TokenPreferences.clear(this)
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

    private fun showStrikeThrough(textView: TextView, show: Boolean){
        textView.paintFlags = if (show){
            textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            textView.paintFlags  and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}
