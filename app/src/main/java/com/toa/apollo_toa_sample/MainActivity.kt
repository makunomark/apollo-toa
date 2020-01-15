package com.toa.apollo_toa_sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.toa.apollo_toa.ApolloToa
import okhttp3.OkHttpClient
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {

    val BASE_URL = "https://rickandmortyapi.com/graphql"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val okHttpClient = OkHttpClient.Builder().build()

        val apolloClient = ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttpClient)
            .build()

        apolloClient.query(
            GetCharactersQuery.builder().build()
        ).enqueue(object : ApolloCall.Callback<GetCharactersQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                e.printStackTrace()
            }

            override fun onResponse(response: Response<GetCharactersQuery.Data>) {
                val characters = response.data()?.characters
                val results: MutableList<GetCharactersQuery.Result>? = characters?.results
                val chars = ApolloToa().convertList(results, Character::class)
                println("------------ ${chars.size} characters retrieved")
            }
        })
    }
}
class Character{
    var id: String? = ""
    var name: String? = ""
    var status: String? = ""
}
