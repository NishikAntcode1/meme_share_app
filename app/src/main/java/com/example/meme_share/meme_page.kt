package com.example.meme_share

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.meme_share.databinding.ActivityMainBinding
import com.example.meme_share.network.RetrofitInstance
import com.example.meme_share.network.responseDataClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.meme_share.databinding.ActivityMemePageBinding

class meme_page : AppCompatActivity() {

    // "https://meme-api.com/gimme"
    lateinit var binding:ActivityMemePageBinding
    var currentImageUrl : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()
        binding.nextButton.setOnClickListener { getData() }
        binding.shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "Hey, Checkout this cool meme I got from Reddit $currentImageUrl")
            val chooser = Intent.createChooser(intent, "Share this meme using...")
            startActivity(chooser)
        }
    }

    private fun getData(){

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait....")
        progressDialog.show()

        RetrofitInstance.apiInterface.getData().enqueue(object : Callback<responseDataClass?> {
            override fun onResponse(
                call: Call<responseDataClass?>,
                response: Response<responseDataClass?>
            ) {
                binding.memeTitleTextView.text = response.body()?.title
                binding.authorTextView.text = "meme by ${response.body()?.author}"
                currentImageUrl = response.body()?.url
                Glide.with(this@meme_page)
                    .load(currentImageUrl)
                    .into(binding.memeImageView);
                progressDialog.dismiss()
            }

            override fun onFailure(call: Call<responseDataClass?>, t: Throwable) {
                Toast.makeText(this@meme_page,"${t.localizedMessage}", Toast.LENGTH_SHORT ).show()
                progressDialog.dismiss()
            }
        })
    }
}