package com.toa.apollo_toa_sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.toa.apollo_toa.ApolloToa
import com.toa.apollo_toa.ApolloToaImpl
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {
    lateinit var apolloClient: ApolloClient
    lateinit var apolloToa: ApolloToa

    val BASE_URL = "https://rickandmortyapi.com/graphql"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val okHttpClient = OkHttpClient.Builder().build()

        apolloClient = ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttpClient)
            .build()
        apolloToa = ApolloToaImpl()

        getEpisodes()
    }

    fun getCharacters() {
        apolloClient.query(
            GetCharactersQuery.builder().build()
        ).enqueue(object : ApolloCall.Callback<GetCharactersQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                e.printStackTrace()
            }

            override fun onResponse(response: Response<GetCharactersQuery.Data>) {
                val characters = response.data()?.characters
                val results: MutableList<GetCharactersQuery.Result>? = characters?.results

                val chars = apolloToa.convertList(results, Character::class)
                println("------------ ${chars.size} characters retrieved")
            }
        })
    }

    fun getEpisodes() {
        apolloClient.query(GetEpisodesQuery.builder().build())
            .enqueue(object : ApolloCall.Callback<GetEpisodesQuery.Data>() {
                override fun onResponse(response: Response<GetEpisodesQuery.Data>) {
                    val episodes = response.data()?.episodes
                    val results = episodes?.results
                    val eps = apolloToa.convertList(results, Episode::class)
                    println("------------ ${eps.size} episodes retrieved")
                }

                override fun onFailure(e: ApolloException) {
                    e.printStackTrace()
                }
            })
    }
}

data class Character(
    var id: String? = "",
    var name: String? = "",
    var status: String? = ""
)

data class Episode(
    var id: String? = "",
    var name: String? = "",
    var episode: String? = "",
    var airDate: String? = ""
)
