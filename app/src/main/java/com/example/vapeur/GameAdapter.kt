import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stime.GameViewHolder
import com.example.vapeur.DetailsDuJeux
import com.example.vapeur.MostPlayedGameList
import com.example.vapeur.R
import com.example.vapeur.databinding.FragmentDetailsDuJeuxBinding


class GameAdapter(private val games: List<MostPlayedGameList.GameData>, private val fragment: Fragment) : RecyclerView.Adapter<GameViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_item, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.rank.text = "Classement: ${game.rank}"
        holder.name.text = "${game.name}"
        holder.publishers.text = "Editeur: ${game.publishers}"
        holder.final_formatted.text =" ${game.final_formatted}"
        Glide.with(holder.itemView.context)
            .load(game.header_image)
            .into(holder.header_image)
        Glide.with(holder.itemView.context)
            .load(game.background)
            .into(holder.background)

        holder.button_item.setOnClickListener{
            fragment.findNavController().navigate(R.id.DetailsDuJeux, bundleOf("appid" to game.appid))
        }
    }

    override fun getItemCount(): Int {
        return games.size
    }
}