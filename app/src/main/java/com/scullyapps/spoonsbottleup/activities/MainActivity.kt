package com.scullyapps.spoonsbottleup.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import com.scullyapps.spoonsbottleup.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        // db.updateListOrder(0,0);

        setupButtons()
    }

    private fun setupButtons() {
        val start = Intent()
        mmenu_button_start.setOnClickListener {
            start.setClass(this, CountActivity::class.java)
            startActivity(start)
        }
        mmenu_button_settings.setOnClickListener {
            start.setClass(this, SettingsActivity::class.java)
            startActivity(start)
        }
        mmenu_about_button.setOnClickListener { Toast.makeText(this, "Implement me!", Toast.LENGTH_SHORT).show() }
        mmenu_button_exit.setOnClickListener { exitProcess(0) }
    }
}