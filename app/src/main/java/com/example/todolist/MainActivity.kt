package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.example.todolist.data.Task
import com.example.todolist.data.TokenPreferences
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var adapter: TasksAdapter

    private val tasks = mutableListOf<Task>(
//        Task("Tarefa 1 ", false),
//        Task("Tarefa 2 ", true),
//        Task("Tarefa 3 ", false),
//        Task("Tarefa 4 ", true),
//        Task("Tarefa 5 ", false),
//        Task("Tarefa 6 ", true),
//        Task("Tarefa 7 ", false),
//        Task("Tarefa 8 ", true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = TasksAdapter(this,
            onCheckBoxClick = {position, isChecked ->
                updateTask(position, isChecked)
            },
            onDeleteClick = {position ->
                MaterialDialog(this).show {
                    title(R.string.delete)
                    title(R.string.delete_message)
                    positiveButton {
                        deleteTask(position)
                    }
                    negativeButton {  }
                }
            })

        recyclerTasks.adapter = adapter
        val manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        recyclerTasks.layoutManager = manager

        showHideEmptyTaskText()

        adapter.updateTaskList(tasks)

        buttonLogout.setOnClickListener {
            showLogoutMessage()
        }

        fabAddTask.setOnClickListener {
            MaterialDialog(this).show {
                title(R.string.add_task)
                input()
                positiveButton(R.string.add) {
                    val text = getInputField().text.toString()
                    addTask(text)
                }
                negativeButton {  }
            }
        }

        val tokenListener = object : AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(oldAccessToken: AccessToken?, newAccessToken: AccessToken?) {
                if (newAccessToken == null) {
                    logOut()
                }
            }
        }

    }

    private fun updateTask(position: Int, checked: Boolean) {
        adapter.updateTask(position, checked)
    }

    private fun deleteTask(position: Int) {
        adapter.deleteTask(position)
        showHideEmptyTaskText()
    }

    private fun addTask(taskText: String){
        adapter.addTask(taskText)
        recyclerTasks.smoothScrollToPosition(tasks.size)
        showHideEmptyTaskText()
    }

    private fun showHideEmptyTaskText(){
        textNoTasks.visibility = if (adapter.itemCount > 0) View.GONE else View.VISIBLE
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
