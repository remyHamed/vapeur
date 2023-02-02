package com.example.stime

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vapeur.R

class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val rank: TextView = itemView.findViewById(R.id.rank)
    val name: TextView = itemView.findViewById(R.id.name)
    val publishers: TextView = itemView.findViewById(R.id.publishers)
    val final_formatted: TextView = itemView.findViewById(R.id.final_formatted)
    val header_image: ImageView = itemView.findViewById(R.id.header_image)
    val button_item: Button = itemView.findViewById(R.id.button_item)
}