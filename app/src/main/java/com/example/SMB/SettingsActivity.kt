package com.example.SMB

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.settings_activity.*


class SettingsActivity : AppCompatActivity() {

    private lateinit var sp: SharedPreferences
    private lateinit var spedit: SharedPreferences.Editor

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        sp = getSharedPreferences("Prefs", MODE_PRIVATE)
        spedit = sp.edit()





        val sw1 = findViewById<Switch>(R.id.theme_switch)
        val sw2 = findViewById<Switch>(R.id.font_switch)

        if (sp.getBoolean("darkMode", true)) {
            sw1.isChecked = true

        }

        if (sp.getBoolean("bigFont", true)) {
            sw2.isChecked = true

        }
        sw1?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                delegate.applyDayNight()
                spedit.putBoolean("darkMode", true)
                spedit.apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                delegate.applyDayNight()
                spedit.putBoolean("darkMode", false)
                spedit.apply()
            }
        }

        sw2?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                //delegate.applyDayNight()
                spedit.putBoolean("bigFont", true)
                spedit.apply()

            } else {


                spedit.putBoolean("bigFont", false)
                spedit.apply()
            }
        }




    }

    override fun onStart(){
        super.onStart()
    }
}



