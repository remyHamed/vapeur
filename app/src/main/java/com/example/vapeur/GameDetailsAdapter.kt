package com.example.vapeur

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GameDetailsAdapter(private val games: ArrayList<DetailsDuJeux.GameData>) : RecyclerView.Adapter<GameDetailsAdapter.InfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_info, parent, false)
        return InfoViewHolder(view)
    }

    class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val publishers: TextView = itemView.findViewById(R.id.publishers)
        val final_formatted: TextView = itemView.findViewById(R.id.final_formatted)
        val header_image: ImageView = itemView.findViewById(R.id.header_image)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        val game = games[position]
        holder.name.text = "${game.name}"
        holder.publishers.text = "Editeur: ${game.publishers}"
        holder.final_formatted.text =" ${game.final_formatted}"
        Glide.with(holder.itemView.context)
            .load(game.header_image)
            .into(holder.header_image)
    }

    override fun getItemCount(): Int {
        return games.size
    }

}