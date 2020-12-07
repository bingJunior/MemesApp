package com.bingjunior.memesapp

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bingjunior.memesapp.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var currentImageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        loadMeme()

        //Share Meme Button
        binding.shareBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "$currentImageUrl")
            val chooser = Intent.createChooser(intent, "Share this meme using: ")
            startActivity(chooser)
        }

        //Next Meme Button
        binding.nextBtn.setOnClickListener {
            loadMeme()
        }
    }

    //Load Meme Function
    private fun loadMeme() {
        binding.progressBar.visibility = View.VISIBLE

        // Instantiate the RequestQueue.
        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    currentImageUrl = response.getString("url")
                    Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable> {

                        override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                        ): Boolean {
                            binding.progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                        ): Boolean {
                            binding.progressBar.visibility = View.GONE
                            return false
                        }

                    }).into(binding.ivImg)
                },
                { error ->
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                })

        //Add request to request queue
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}
