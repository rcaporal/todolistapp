package com.example.todolist.api

import com.example.todolist.data.Task
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProtocolAPI  {

    companion object {
        val API_URL: String get() = "http://10.10.0.60:3000"
    }

    @GET("/api/tasks")
    fun getTasks(): Call<MutableList<Task>>

    @POST("/api/tasks/{task_id}/completed")
    fun postUpdateTask(@Path("task_id") id:String, @Body task: Task): Call<Task>

    @POST("/api/tasks")
    fun postCreateTask(@Body task: Task): Call<Task>

    @POST("/api/tasks/{task_id}/delete")
    fun postDeleteTask(@Path("task_id") id:String): Call<ResponseBody>

}