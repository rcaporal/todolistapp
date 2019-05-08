package com.example.todolist

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.data.Task

class TasksAdapter(activity: Activity,
                   val onCheckBoxClick: (position: Int, isChecked: Boolean)->Unit,
                   val onDeleteClick: (position: Int)->Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val taskList = mutableListOf<Task>()
    private val layoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as TaskViewHolder
        val task = taskList[position]

        holder.taskText.text = task.text
        holder.checkBox.isChecked = task.completed

        showStrikeThrough(holder.taskText, task.completed)

        holder.checkBox.setOnClickListener {
            onCheckBoxClick(position, !task.completed)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick(position)
        }

    }

    fun updateTask(position: Int, isChecked: Boolean){
        val task = taskList[position]
        task.completed = isChecked
        notifyItemChanged(position)
    }

    fun deleteTask(position: Int){
        taskList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount - position)
    }

    fun updateTaskList(tasks: MutableList<Task>){
        this.taskList.clear()
        this.taskList.addAll(tasks)
        notifyDataSetChanged()
    }

    private fun showStrikeThrough(textView: TextView, show: Boolean){
        textView.paintFlags = if (show){
            textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            textView.paintFlags  and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskText: TextView = itemView.findViewById(R.id.textTask)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkboxTask)
        val deleteButton: View = itemView.findViewById(R.id.deleteTask)
    }

}