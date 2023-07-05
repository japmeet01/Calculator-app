package com.example.calculatorapp
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.calculatorapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var calcViewModel: CalculatorViewModel
    private lateinit var binding:ActivityMainBinding
    private var tvInput: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        tvInput = findViewById(R.id.tvInput)
        calcViewModel = ViewModelProvider(this).get(CalculatorViewModel::class.java)
        calcViewModel.getInputLiveData().observe(this) { input ->
            tvInput?.text = input
        }
        binding.calculatorViewModel=calcViewModel
        binding.lifecycleOwner=this
    }
}

