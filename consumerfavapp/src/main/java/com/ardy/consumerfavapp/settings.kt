package com.ardy.consumerfavapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import com.ardy.consumerfavapp.alarm.AlarmReceiver
import com.ardy.consumerfavapp.databinding.ActivitySettingsBinding

class settings : AppCompatActivity() {
    private lateinit var binding : ActivitySettingsBinding
    private lateinit var alarmManager: AlarmReceiver
    private lateinit var sharedPreferences: SharedPreferences

    companion object{
        const val PREFERENCES = "preference"
        const val KEY_ALARM = "Alarm"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmManager= AlarmReceiver()
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        binding.btnLanguage.setOnClickListener{

    val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
    startActivity(mIntent)

}


        checkSwitchState()
        binding.btnSetting.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked){
                alarmManager.setRepeatingAlarm(this,AlarmReceiver.EXTRA_TYPE,"Let's find User popular user On Github!")
            }
            else {alarmManager.cancelAlarm(this)
            }
            saveChanges(isChecked)
        }
    }


    private fun checkSwitchState() {
        binding.btnSetting.isChecked = sharedPreferences.getBoolean(KEY_ALARM, false)
    }
    private fun saveChanges(value: Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_ALARM,value)
        editor.apply()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_git, menu)
        val  search : MenuItem = menu.findItem(R.id.search);
        search.setVisible(false);

        val  settings : MenuItem = menu.findItem(R.id.setting);
        settings.setVisible(false);
        var actionBar = getSupportActionBar()

        if (actionBar != null) {


            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        if (item.itemId == R.id.fav) {
            val intent = Intent(this@settings , Favorite::class.java)
            startActivity((intent))
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            android.R.id.home -> {
                Findah()
            }

        }
    }

    private fun Findah() {

        val move =  Intent (this@settings, Favorite::class.java)

        startActivity(move)
    }

}