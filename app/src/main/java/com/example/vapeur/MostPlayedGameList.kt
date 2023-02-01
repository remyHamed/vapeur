package com.example.vapeur

import GameAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import kotlinx.android.synthetic.main.fragment_most_played_game_list.*
import kotlinx.android.synthetic.main.game_item.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.*

class MostPlayedGameList : Fragment() {
    val gson = Gson()

    // Déclarer les variables pour RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var gameDataList = ArrayList<GameData>()

    interface SteamChartsAPI {
        @GET("GetMostPlayedGames/v1/")
        suspend fun getMostPlayedGames(): Response<JsonObject>
    }

    interface SteamInfosAPI {
        @GET("appdetails")
        suspend fun getGameInfos(@Query("appids") appid: Int): Response<JsonObject>
    }

    // Définir la classse de données pour les jeux
    data class GameData(
        var rank: Int,
        var appid: Int,
        var last_week_rank: Int,
        var peak_in_game: Int,
        var name: String,
        var publishers: String,
        var final_formatted: String,
        var header_image: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        // lien adapter
        val view = inflater.inflate(R.layout.fragment_most_played_game_list, container, false)
        viewManager = LinearLayoutManager(context)
        viewAdapter = GameAdapter(gameDataList)

        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        //Première API

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.steampowered.com/ISteamChartsService/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .build()

        val steamChartsAPI = retrofit.create(SteamChartsAPI::class.java)

        suspend fun fetchMostPlayedGames(): Response<JsonObject> {
            return steamChartsAPI.getMostPlayedGames()
        }

        //Seconde API

        val retrofit2 = Retrofit.Builder()
            .baseUrl("https://store.steampowered.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .build()

        // Obtenir une instance de l'interface de l'API
        val steamInfosAPI = retrofit2.create(SteamInfosAPI::class.java)

        suspend fun fetchGameInfos(appid: Int): Response<JsonObject> {
            return steamInfosAPI.getGameInfos(appid)
        }

        GlobalScope.launch {
            try {
                val response = fetchMostPlayedGames()
                withContext(Dispatchers.Main){
                    if (response.isSuccessful) {
                        var json = response.body()
                        val dataArray = json?.getAsJsonObject("response")?.getAsJsonArray("ranks")
                        val gameDataList = gson.fromJson(dataArray, Array<GameData>::class.java).toList()
                        println("poulet")
                        for(gameData in gameDataList) {
                            json = fetchGameInfos(gameData.appid).body()
                            val data = json?.getAsJsonObject(gameData.appid.toString())
                            val gameInfos = data?.getAsJsonObject("data")
                            val gameName = gameInfos?.get("name")?.asString ?: "N/A"
                            val gamePublishers = gameInfos?.getAsJsonArray("publishers")
                            val gameInfosPrice = gameInfos?.getAsJsonObject("price_overview")
                            val gamePrice = gameInfosPrice?.get("final_formatted")?.asString ?: "gratuit"
                            gameData.name = gameName ?: "N/A"
                            gameData.final_formatted = gamePrice ?: "gratuit"

                            val publisherList = ArrayList<String>()
                            if (gamePublishers != null) {
                                for (i in 0 until gamePublishers.size()) {
                                    publisherList.add(gamePublishers.get(i).asString)
                                }
                            }
                            val publisherString = publisherList.joinToString(", ")
                            gameData.publishers = publisherString
                            val gameImage = gameInfos?.get("header_image")?.asString ?: "N/A"
                            gameData.header_image = gameImage ?: "N/A"

                        }
                        // Mise à jour de la liste
                        viewAdapter = GameAdapter(gameDataList)
                        recyclerView.adapter = viewAdapter
                    }
                }
            } catch (e: Exception) {
                println(e)
                // erreurs
            }
        }

        this@MostPlayedGameList.gameDataList.addAll(gameDataList)
        viewAdapter.notifyDataSetChanged()
        //return inflater.inflate(R.layout.fragment_most_played_game_list, container, false)
        return view
    }

    companion object {

    }
}