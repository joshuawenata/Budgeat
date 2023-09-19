package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class AdapterWithButton<T>(
    private val context: Context,
    private val items: List<T>,
    private val itemLayoutResId: Int,
    private val bindView: (View, T) -> Unit,
    private val onItemClick: (T) -> Unit,
    private val onItemButtonClickListener: (T) -> Unit
) : RecyclerView.Adapter<AdapterWithButton<T>.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: T) {
            bindView(itemView, item)

            // Set an onClickListener for the item view
            itemView.setOnClickListener {
                onItemClick(item)
            }

            // Set an onClickListener for the button within the item view
            val button = itemView.findViewById<ImageButton>(R.id.card_add_stock_button) // Replace with your button ID
            button.setOnClickListener {
                onItemButtonClickListener(item)
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
