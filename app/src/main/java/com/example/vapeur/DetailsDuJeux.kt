package com.example.vapeur

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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class DetailsDuJeux : Fragment() {

    val gson = Gson()

    // Déclarer les variables pour RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var gameDataList = ArrayList<GameData>()

    interface SteamInfosAPI {
        @GET("appdetails")
        suspend fun getGameInfos(@Query("appids") appid: Int): Response<JsonObject>
    }

    // Définir la classse de données pour les jeux
    data class GameData(
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

        // Initialise RecyclerView
        recyclerView = RecyclerView(requireContext())
        viewManager = LinearLayoutManager(context)
        viewAdapter = GameDetailsAdapter(gameDataList)

        recyclerView = recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        val appid = arguments?.getInt("appid")

        //Seconde API
        val retrofit2 = Retrofit.Builder()
            .baseUrl("https://store.steampowered.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .build()

        // Obtenir une instance de l'interface de l'API
        val steamInfosAPI = retrofit2.create(SteamInfosAPI::class.java)

        suspend fun fetchGameInfos(appid: Int): Response<JsonObject> {
            println(appid)
            return steamInfosAPI.getGameInfos(appid)
        }

        GlobalScope.launch {
            try {
                val response = fetchGameInfos(appid ?: 0)
                if (response.isSuccessful) {
                    val responseJson = response.body()!!
                    val appidData = responseJson.getAsJsonObject("$appid")

                    if (appidData.get("success").asBoolean) {


                        val data = appidData.getAsJsonObject("data")
                        val gameInfosPrice = data?.getAsJsonObject("price_overview")

                        val gameData = GameData(
                            name = data.get("name").asString ?: "N/A",
                            publishers = gameInfosPrice?.get("final_formatted")?.asString ?: "gratuit",
                            final_formatted = data.get("publishers").asJsonArray[0].asString,
                            header_image = data?.get("header_image")?.asString ?: "N/A"
                        )

                        gameDataList.add(gameData)

                        val gameName = data.get("name").asString ?: "N/A"
                        gameData.name = gameName ?: "N/A"

                        val gamePrice = gameInfosPrice?.get("final_formatted")?.asString ?: "gratuit"
                        gameData.final_formatted = gamePrice ?: "gratuit"

                        val gamePublishers = data.get("publishers").asJsonArray[0].asString
                        val publisherList = ArrayList<String>()
                        if (gamePublishers != null) {
                            if (gamePublishers.isNotEmpty()) {
                                publisherList.add(gamePublishers)
                            }
                        }
                        val publisherString = publisherList.joinToString(", ")
                        gameData.publishers = publisherString

                        val gameImage = data?.get("header_image")?.asString ?: "N/A"
                        gameData.header_image = gameImage ?: "N/A"

                    }
                    viewAdapter = GameDetailsAdapter(gameDataList)
                    recyclerView.adapter = viewAdapter
                }
            } catch (e: Exception) {
                println(e)
            }
        }

        this@DetailsDuJeux.gameDataList.addAll(gameDataList)
        viewAdapter.notifyDataSetChanged()
        return view
    }

    companion object {

    }
}