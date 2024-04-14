package com.newoverride.notas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.newoverride.notas.databinding.HomeViewBinding

class MainActivity : AppCompatActivity() {

    private var binding: HomeViewBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeViewBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }
}