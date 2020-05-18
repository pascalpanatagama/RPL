package com.example.toiletfinderfixed

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import kotlinx.android.synthetic.main.activity_toilet_request.*
import org.json.JSONArray


class ToiletRequestActivity : AppCompatActivity() {

    private lateinit var recyclerViewRequest: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

        override fun onCreate(savedInstanceState : Bundle? ){
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_toilet_request)

            ibBackReq.setOnClickListener {
                finish()
            }

            recyclerViewRequest = findViewById<RecyclerView>(R.id.rvRequest)
//            viewManager = LinearLayoutManager(this)

            val url = "http://10.0.2.2/toilet_finder/toilet/readToiletRequest.php"

            val jsonArray = JSONArray()
            val request = JsonArrayRequest(Request.Method.POST, url, jsonArray,
                Response.Listener { response->
                    var toiletRequestList = mutableListOf<RequestToilet>()
                    for(i in 0 until response.length()){
                        toiletRequestList.add(
                            RequestToilet(
                                "${response.getJSONObject(i)["nama"]}",
                                "${response.getJSONObject(i)["gender"]}",
                                "${response.getJSONObject(i)["tipe"]}",
                                "${response.getJSONObject(i)["jam_buka"]} - ${response.getJSONObject(i)["jam_tutup"]}",
                                "${response.getJSONObject(i)["latitude"]}",
                                "${response.getJSONObject(i)["longitude"]}",
                                "${response.getJSONObject(i)["id_toilet"]}"
                            )
                        )
                    }
                    Log.d("REQUEST", "$toiletRequestList")
//                    Log.d("REQUEST", "${response.getJSONObject(0)["nama"]}")
//                    viewAdapter = RequestToiletAdapter(toiletRequestList)
//                    recyclerView.apply{
//
//                        layoutManager = viewManager
//
//                        adapter = viewAdapter
//
//                    }
                    val adapter = RequestToiletAdapter(this, toiletRequestList)
                    recyclerViewRequest.adapter = adapter
                    recyclerViewRequest.layoutManager = LinearLayoutManager(this)

                }, Response.ErrorListener {error->
                    Log.d("ERRORREQ", "$error")
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