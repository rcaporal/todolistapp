package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.example.todolist.api.ClientAPI
import com.example.todolist.data.Task
import com.example.todolist.data.TokenPreferences
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker
import com.facebook.Profile
import com.facebook.login.LoginManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    lateinit var adapter: TasksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = TasksAdapter(this,
            onCheckBoxClick = { position, task ->
                updateTask(position, task)
            },
            onDeleteClick = { position, task ->
                MaterialDialog(this).show {
                    title(R.string.delete)
                    title(R.string.delete_message)
                    positiveButton {
                        deleteTask(position, task.id)
                    }
                    negativeButton { }
                }
            })

        recyclerTasks.adapter = adapter
        val manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        recyclerTasks.layoutManager = manager

        showHideEmptyTaskText()

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
                negativeButton { }
            }
        }

        getTasks()

    }

    override fun onResume() {
        super.onResume()
        updateTextUserName()
    }

    private fun updateTextUserName() {
        val profile = Profile.getCurrentProfile()
        textUserName.text = if (profile != null) "${profile.firstName} ${profile.lastName}" else ""
    }

    private fun getTasks() {
        ClientAPI.getService(this)?.getTasks()?.enqueue(
            object : Callback<MutableList<Task>> {

                override fun onResponse(call: Call<MutableList<Task>>, response: Response<MutableList<Task>>) {
                    if (response.isSuccessful) {
                        response.body()?.let { taskList ->
                            adapter.updateTaskList(taskList)
                        }
                    } else {
                        showSnackBar(response.message())
                    }
                }

                override fun onFailure(call: Call<MutableList<Task>>, t: Throwable) {
                    t.message?.let { showSnackBar(it) }
                }


            })

        object : AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(oldAccessToken: AccessToken?, newAccessToken: AccessToken?) {
                if (newAccessToken == null) {
                    logOut()
                }
            }
        }
    }

    private fun updateTask(position: Int, task: Task) {
        ClientAPI.getService(this)?.postUpdateTask(task.id, task)?.enqueue(
            object : Callback<Task> {

                override fun onResponse(call: Call<Task>, response: Response<Task>) {
                    if (response.isSuccessful) {
                        response.body()?.let { taskUpdated ->
                            adapter.updateTask(position, taskUpdated)
                        }
                    } else {
                        showSnackBar(response.message())
                    }
                }

                override fun onFailure(call: Call<Task>, t: Throwable) {
                    t.message?.let { showSnackBar(it) }
                }
            }
        )
    }

    private fun deleteTask(position: Int, taskId: String) {
        ClientAPI.getService(this)?.postDeleteTask(taskId)?.enqueue(
            object : Callback<ResponseBody> {

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        adapter.deleteTask(position)
                        showHideEmptyTaskText()
                    } else {
                        showSnackBar(response.message())
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    t.message?.let { showSnackBar(it) }
                }


            }
        )
    }

    private fun addTask(taskText: String) {
        val task = Task(taskText)
        ClientAPI.getService(this)?.postCreateTask(task)?.enqueue(
            object : Callback<Task> {

                override fun onResponse(call: Call<Task>, response: Response<Task>) {
                    if (response.isSuccessful) {
                        response.body()?.let { task ->
                            adapter.addTask(task)
                            recyclerTasks.smoothScrollToPosition(adapter.itemCount)
                            showHideEmptyTaskText()
                        }
                    } else {
                        showSnackBar(response.message())
                    }
                }

                override fun onFailure(call: Call<Task>, t: Throwable) {
                    t.message?.let { showSnackBar(it) }
                }


            }
        )
    }

    private fun showHideEmptyTaskText() {
        textNoTasks.visibility = if (adapter.itemCount > 0) View.GONE else View.VISIBLE
    }

    private fun logOut() {
        TokenPreferences.clear(this)
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(rootMainLayout, message, Snackbar.LENGTH_LONG).show()
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
