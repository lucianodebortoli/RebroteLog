package org.luciano.rebrotelog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.IOException
import okhttp3.*

class MainActivity : AppCompatActivity() {

    private val TAG = "RebroteTAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun logToSheets(emisor: String, receptor: String, monto: String, detalle: String)
    {
        val url = "https://script.google.com/macros/s/AKfycbyRKdBzm3WBNfp0c9J7oJyTlKmCQNIuR548dHRCX86lcaTSMEs_j-nwyA/exec"
        val requestURL = url+"?emisor="+emisor+"&receptor="+receptor+"&monto="+monto+"&detalle="+detalle
        val request = Request.Builder().url(requestURL).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                Log.i(TAG, "Sent a GET request")
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.i(TAG, "Failed to send GET request")
            }
        })
    }

}