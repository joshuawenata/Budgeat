package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.`object`.Menu

class AdapterCheckout<T>(
    private val context: Context,
    private val items: List<T>,
    private val itemLayoutResId: Int,
    private val bindView: (View, T, Int) -> Unit,
    private val onItemClick: (T) -> Unit,
) : RecyclerView.Adapter<AdapterCheckout<T>.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: T) {
            bindView(itemView, item, adapterPosition)

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
}
