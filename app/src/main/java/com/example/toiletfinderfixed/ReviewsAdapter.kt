package com.example.toiletfinderfixed

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_recycle_view.view.*
import org.json.JSONObject

class ReviewsAdapter(var todos: List<Toilet>) : RecyclerView.Adapter<ReviewsAdapter.MyViewHolder>(){
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_recycle_view, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.apply{
            val sharedPref = context.getSharedPreferences("LoginInformation", 0)
            val isAdminFromPref = sharedPref.getInt("isAdmin", 0)
            Log.d("ADDD", "$isAdminFromPref")
            if(isAdminFromPref == 0) {
                ibDeleteReview.visibility = View.GONE
            }

            Log.d("DELETE", todos[position].id_rating)

            ibDeleteReview.setOnClickListener {
                val url = "http://10.0.2.2/toilet_finder/rating_review/delete.php"
                val params = HashMap<String, String>()
                params["id_rating"] = todos[position].id_rating
                val jsonObject = JSONObject(params as Map<*, *>)
                val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    Response.Listener { response ->
                        Toast.makeText(context, "${response["message"]}", Toast.LENGTH_SHORT).show()

                    }, Response.ErrorListener { error ->
                        Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
                    })
                request.retryPolicy = DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    // 0 means no retry
                    0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                    1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )

                VolleySingleton.getInstance(context).addToRequestQueue(request)

            }

            tvUsername.text = todos[position].username
            tvRatingNumber.text = todos[position].ratingNumber
            tvReviewText.text = todos[position].reviewText
            tvDate.text = todos[position].date
        }
    }
}