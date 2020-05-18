package com.example.toiletfinderfixed

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_rating.*
import org.json.JSONObject

class RatingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        val sharedPref = getSharedPreferences("LoginInformation", 0) ?: return
        val idToilet = intent.getStringExtra("ID").toInt()
        val idUser = sharedPref.getInt("id_user", 0)

        Log.d("RATINGID", "$idToilet")
        Log.d("RATINGID", "$idUser")

        val rate = findViewById<RatingBar>(R.id.ratingBar)
        val review = findViewById<EditText>(R.id.etSubmitReview)


        btnSubmitRating.setOnClickListener {
            val submitRating = rate.rating.toString()
            val submitReview = review.text.toString()

//            Toast.makeText(this@RatingActivity, "${submitRating.toFloat()}", Toast.LENGTH_SHORT).show()
            Log.d("RATING", "${submitRating.toFloat()}")
            Log.d("RATINGREVIEW", submitReview)

            val url = "http://10.0.2.2/toilet_finder/rating_review/create.php"

            val params = HashMap<String, String>()
            params["id_user"] = "$idUser"
            params["id_toilet"] = "$idToilet"
            params["ratingNumber"] = submitRating
            params["ratingText"] = submitReview

            val jsonObject = JSONObject(params as Map<*, *>)
            val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                Response.Listener {response ->
                Log.d("RATINGResp", "${response["message"]}")
                    Toast.makeText(this@RatingActivity, "${response["message"]}", Toast.LENGTH_SHORT).show()
                    finish()
            }, Response.ErrorListener {error ->
                Log.d("RATINGResp", "$error")
                Toast.makeText(this@RatingActivity, "Already reviewed this toilet", Toast.LENGTH_SHORT).show()
            })
            request.retryPolicy = DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                // 0 means no retry
                0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )

            VolleySingleton.getInstance(this).addToRequestQueue(request)

        }

        ibBackRating.setOnClickListener {
            finish()
//            Intent(this, DetailActivity::class.java).also {
//                startActivity(it)
//            }
        }

    }

}