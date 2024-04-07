package com.example.nasapodrecyclerview
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

  //  private lateinit var imageView: ImageView
    private lateinit var podList: MutableList<PODItem>
    private lateinit var rvPod: RecyclerView
    private lateinit var urls : MutableList<String>
    private var currentPage = 1
    private var totalPages = 20 // Start with a high value

    private val baseUrl = "https://api.nasa.gov/planetary/apod"
    private val apiKey = "OldBxtzzlUfJcHrmifQcn0bDAtjIOorryctniRMf"
    private lateinit var appName : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

     //   imageView = findViewById(R.id.imageView)
        appName = findViewById(R.id.appName)
        rvPod = findViewById(R.id.recyclerView)
        rvPod.layoutManager = LinearLayoutManager(this)
        rvPod.addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL))

        podList = mutableListOf()
        val adapter = PODAdapter(podList)
        rvPod.adapter = adapter
        appName.setText("NASA PODS")

        loadAllPhotos()
    }

    private fun loadAllPhotos() {
        val calendar = Calendar.getInstance()
        val todayDate = calendar.time

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDate = "2023-12-31" // Displaying photos from December 31, 2023

        val startCalendar = Calendar.getInstance()
        startCalendar.time = sdf.parse(startDate)

        while (calendar >= startCalendar && currentPage <= totalPages) {
            val formattedDate = sdf.format(startCalendar.time)
            loadPhotoForDate(formattedDate)
            startCalendar.add(Calendar.DATE, 1) // Move to the next day
        }
    }

    private fun loadPhotoForDate(selectedDate: String) {
        val client = AsyncHttpClient()
        val url = "$baseUrl?api_key=$apiKey&date=$selectedDate"

        client.get(url, object : TextHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: String?) {
                responseString?.let {
                    try {
                        val response = JSONObject(it)
                        handlePhotoResponse(response)
                    } catch (e: JSONException) {
                        Log.e("com.example.nasapodrecyclerview.MainActivity", "JSON Exception: ${e.localizedMessage}")
                    }
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                Log.e("com.example.nasapodrecyclerview.MainActivity", "Request failed with status code: $statusCode")
            }
        })
    }

    private fun handlePhotoResponse(response: JSONObject) {
        try {
            val imageUrl = response.getString("url")
            val title = response.getString("title")
            val description = response.getString("explanation")

            val podItem = PODItem(imageUrl, title, description)

            podList.add(podItem)
            rvPod.adapter?.notifyItemInserted(podList.size - 1)
        } catch (e: JSONException) {
            Log.e("MainActivity", "JSON Exception: ${e.localizedMessage}")
        }
    }

}
