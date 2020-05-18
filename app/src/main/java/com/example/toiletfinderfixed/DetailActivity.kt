package com.example.toiletfinderfixed

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val sharedPref = getSharedPreferences("LoginInformation", 0) ?: return
        val id = intent.getStringExtra("ID").toInt()
        val isAdminFromPref = sharedPref.getInt("isAdmin", 0)

//        if(isAdminFromPref == 0){
//            ibDeleteReview.visibility = View.GONE
//        }

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val urlRating = "http://10.0.2.2/toilet_finder/rating_review/read.php"
        val paramsRating = HashMap<String, String>()
        paramsRating["id_toilet"] = "$id"

        val jsonArrayRating = JSONObject(paramsRating as Map<*, *>)
        val requestRating = JsonObjectRequest(Request.Method.POST, urlRating, jsonArrayRating,
            Response.Listener{response ->
                var ratings = response.getJSONArray("ratings")
                var toiletList = mutableListOf<Toilet>()
                //Log.d("TESTjson", ratings.getJSONObject(0).toString())
                for (i in 0 until ratings.length()) {
                    toiletList.add(
                        Toilet(
                            "${ratings.getJSONObject(i)["username"]}",
                            "${ratings.getJSONObject(i)["ratingNumber"]}",
                            "${ratings.getJSONObject(i)["ratingText"]}",
                            "${ratings.getJSONObject(i)["date"]}",
                            "${ratings.getJSONObject(i)["id_rating"]}"
                        )
                    )
                    Log.d("111111", "$response")
                }

                val adapter = ReviewsAdapter(toiletList)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)

            }, Response.ErrorListener {error->
                Log.d("11111", "$error")
            })
        requestRating.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            // 0 means no retry
            0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
            1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        VolleySingleton.getInstance(this).addToRequestQueue(requestRating)


        ibBackDetail.setOnClickListener{
            finish()
//            Intent(this, MapsActivity::class.java).also{
//                startActivity(it)
//            }
        }

        btnReview.setOnClickListener {
            val usernameFromSharedPref = sharedPref.getString("username", "")
            if(usernameFromSharedPref == ""){
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                }
            } else {
                Intent(this, RatingActivity::class.java).also {
                    it.putExtra("ID", "$id")
//                    it.putExtra("id_user", "$id_user")
                    startActivity(it)
                }
            }
        }

        Log.d("MAPINFO", "$id")
        val url = "http://10.0.2.2/toilet_finder/toilet/readDetail.php"
        val params = HashMap<String, String>()
        params["id_toilet"] = "$id"

        val jsonObject = JSONObject(params as Map<*, *>)
        val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
            Response.Listener { response ->
                tvNamaToilet.text = "${response["nama"]}"
                tvOperationalTime.text = "Operational Time : ${response["jam_buka"]} - ${response["jam_tutup"]}"
                tvToiletGender.text = "Gender : ${response["gender"]}"
                tvToiletType.text = "Toilet type : ${response["tipe"]}"
                Log.d("RESPNAMA", "$response")
            }, Response.ErrorListener {


            })
        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            // 0 means no retry
            0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
            1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        VolleySingleton.getInstance(this).addToRequestQueue(request)

    }


}