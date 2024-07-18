package com.example.quotes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.quotes.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getQuote()

        binding.btn.setOnClickListener {
            getQuote()
        }
    }
    private fun getQuote(){
        setInProgress(true)

        GlobalScope.launch {
            try {
               val response= RetrofitInstance.quoteApi.getRandomQuotes()
                runOnUiThread {
                    setInProgress(false)
                    response.body()?.first()?.let{
                        setUI(it)
                    }
                }
            }catch (e:Exception){
                runOnUiThread {
                    setInProgress(false)
                    Toast.makeText(applicationContext,"Something went Wrong",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setUI(quote:QuoteModel){
        binding.quoteTv.text=quote.q
        binding.authorTv.text=quote.a
    }

    private fun setInProgress(inProgress:Boolean){
        if(inProgress){
            binding.progressBar.visibility= View.VISIBLE
            binding.btn.visibility= View.GONE
        }
        else{
            binding.progressBar.visibility= View.GONE
            binding.btn.visibility= View.VISIBLE
        }
    }
}
