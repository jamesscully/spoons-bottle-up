package com.scullyapps.spoonsbottleup.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.activities.viewmodel.ProductsActivityViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ProductsActivity : AppCompatActivity() {

    val viewModel : ProductsActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        lifecycleScope.launch {

        }
    }
}