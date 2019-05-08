package com.example.todolist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.example.todolist.data.Task
import com.example.todolist.data.TokenPreferences
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var adapter: TasksAdapter

    private val tasks = mutableListOf(
        Task("Tarefa 1 ", false),
        Task("Tarefa 2 ", true),
        Task("Tarefa 3 ", false),
        Task("Tarefa 4 ", true),
        Task("Tarefa 5 ", false),
        Task("Tarefa 6 ", true),
        Task("Tarefa 7 ", false),
        Task("Tarefa 8 ", true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = TasksAdapter(this,
            onCheckBoxClick = {position, isChecked ->
                adapter.updateTask(position, isChecked)
            },
            onDeleteClick = {position ->
                MaterialDialog(this).show {
                    title(R.string.delete)
                    title(R.string.delete_message)
                    positiveButton {
                        adapter.deleteTask(position)
                    }
                    negativeButton {  }
                }
            })

        recyclerTasks.adapter = adapter
        val manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        recyclerTasks.layoutManager = manager

        adapter.updateTaskList(tasks)

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
}
