package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class Adapter<T>(
    private val context: Context,
    private var items: List<T>,
    private val itemLayoutResId: Int,
    private val bindView: (View, T) -> Unit,
    private val onItemClick: (T) -> Unit,
) : RecyclerView.Adapter<Adapter<T>.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: T) {
            bindView(itemView, item)

            // Set an onClickListener for the item view
            itemView.setOnClickListener {
                onItemClick(item)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(itemLayoutResId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun filterList(filteredData: List<T>) {
        items = filteredData
        notifyDataSetChanged()
    }
}
