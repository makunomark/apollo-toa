package com.toa.apollo_toa_sample

import android.os.Bundle
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
//                Log.d(javaClass.simpleName, results.toString())

//                results?.forEach {
//                    //                    val character = Character(
////                        it.id(), it.name, it.status
////                    )
//
//
//
//                }


                if(results != null)
                ApolloToa().convert(results[0], Character::class)
            }
        })
    }
}

data class Character(
    private val id: String?,
    private val name: String?,
    private val status: String?
)
