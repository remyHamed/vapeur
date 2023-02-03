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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        var header_image: String,
        var background: String
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
            val response = fetchGameInfos(appid ?: 0)
            if (response.isSuccessful) {
                val json = response.body()

                val data = json?.getAsJsonObject(appid.toString())
                val gameInfos = data?.getAsJsonObject("data")

                val name = gameInfos?.get("name")?.asString ?: "N/A"
                val publishers = gameInfos?.getAsJsonArray("publishers")?.joinToString { it.asString } ?: "N/A"
                val final_formatted = gameInfos?.get("final_formatted")?.asString ?: "N/A"
                val header_image = gameInfos?.get("header_image")?.asString ?: "N/A"
                val background = gameInfos?.get("background")?.asString ?: "N/A"

                withContext(Dispatchers.Main) {
                    gameDataList.add(
                        GameData(
                            name = name,
                            publishers = publishers,
                            final_formatted = final_formatted,
                            header_image = header_image,
                            background = background
                        )
                    )
                    viewAdapter.notifyDataSetChanged()
                }
            }
        }

        return recyclerView
    }

}