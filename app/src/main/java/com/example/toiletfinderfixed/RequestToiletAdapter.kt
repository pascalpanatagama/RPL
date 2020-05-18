package com.example.toiletfinderfixed

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_rv_toilet_req.view.*
import org.json.JSONObject

class RequestToiletAdapter(var ctx: Context, var todosReq : List<RequestToilet>) : RecyclerView.Adapter<RequestToiletAdapter.MyViewHolder>(){
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestToiletAdapter.MyViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.activity_rv_toilet_req, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todosReq.size
    }

    override fun onBindViewHolder(holder: RequestToiletAdapter.MyViewHolder, position: Int) {
        holder.itemView.apply{
//            Log.d("TOILETID", "${todosReq[position].id_toilet}")

            ibDeleteReq.setOnClickListener {
                val url = "http://10.0.2.2/toilet_finder/toilet_finder/toilet/delete.php"
                val params = HashMap<String, String>()
                params["id_toilet"] = todosReq[position].id_toilet

                val jsonObject = JSONObject(params as Map<*, *>)
                val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    Response.Listener{response->
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

            ibApproveReq.setOnClickListener {
                val urlApprove = "http://10.0.2.2/toilet_finder/toilet/update.php"
                val paramsApprove = HashMap<String, String>()
                paramsApprove["id_toilet"] = todosReq[position].id_toilet

                val jsonObjectApprove = JSONObject(paramsApprove as Map<*, *>)
                val requestApprove = JsonObjectRequest(Request.Method.POST, urlApprove, jsonObjectApprove,
                    Response.Listener { response ->
                        Toast.makeText(context, "${response["message"]}", Toast.LENGTH_SHORT).show()
                        val intent = Intent(ctx, MapsActivity::class.java).apply {  }
                        ctx.startActivity(intent)
                    }, Response.ErrorListener {error ->
                        Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
                    })

                requestApprove.retryPolicy = DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    // 0 means no retry
                    0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                    1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )

                VolleySingleton.getInstance(context).addToRequestQueue(requestApprove)
            }

            tvNamaToiletReq.text = todosReq[position].nama
            tvJamOperasionalReq.text = todosReq[position].jam_operasional
            tvGenderReq.text = todosReq[position].gender
            tvTipeReq.text = todosReq[position].tipe
            tvLatitude.text = todosReq[position].latitude
            tvLongitude.text = todosReq[position].longitude

        }
    }


}