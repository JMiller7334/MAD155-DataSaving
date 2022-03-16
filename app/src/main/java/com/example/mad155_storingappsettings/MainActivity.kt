package com.example.mad155_storingappsettings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Switch

class MainActivity : AppCompatActivity() {

    lateinit var fieldName: EditText
    lateinit var fieldClassType: EditText
    lateinit var fieldClassNumber: EditText
    lateinit var myBar: SeekBar

    // Global Variables
    val DATASTORE = "MySharedPref" //this is the name of my datastore/pref file - it is just a string.
    val PREF_DARK_MODE = "dark_theme"
    val PREF_SEEKBAR = "seekbar_progress"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fieldName = findViewById(R.id.fieldName)
        fieldClassType = findViewById(R.id.fieldType)
        fieldClassNumber = findViewById(R.id.fieldNumber)
        myBar = findViewById(R.id.seekBar)

        //Data retrieving
        val SP = getSharedPreferences(DATASTORE, MODE_PRIVATE)
        val seekProgress = SP.getInt(PREF_SEEKBAR, 0)
        myBar.progress = seekProgress

        val useDarkMode = SP.getBoolean(PREF_DARK_MODE, false)
        if (useDarkMode){
            Log.d("test", "switch enabled")
        }

        val toggleTheme = findViewById<Switch>(R.id.modeSwitch)
        toggleTheme.isChecked = useDarkMode
        toggleTheme.setOnCheckedChangeListener { view, isChecked -> changeTheme(isChecked) }
    }

    private fun changeTheme(isChecked:Boolean){
        //write to the datastore
        val SP = getSharedPreferences(DATASTORE, MODE_PRIVATE)
        val changes = SP.edit()
        changes.apply{
            putBoolean(PREF_DARK_MODE, isChecked)
            apply()
        }
        // this refreshes the screen thus firing the onCreate function
        val intent = intent
        finish()
        startActivity(intent)
    }

    override fun onResume() { //generated stub; this is a lifecycle functions.
        super.onResume()
        // SP is retrieving the data saved(if any) Mode Private is recommended open mode in most cases.
        val SP = getSharedPreferences(DATASTORE, MODE_PRIVATE)
        val keyName = SP.getString("name", "") //retreiving data from SP(the datastore) using the key. 2nd paramater is what returns if no value found.
        val keyType = SP.getString("classType", "")
        val keyNumber = SP.getInt("classNumber", 0)

        //assign retreived data to the UI screen elements/widgets
        fieldName!!.setText(keyName) //Note the !! is because this could be null - think of it like an optional
        fieldClassType!!.setText(keyType)
        fieldClassNumber!!.setText(keyNumber.toString())


    }

    override fun onPause() { //again generated lifecycle stub - fires when the app slips into the background or when its manually shut down.
        super.onPause()
        // opened in private mode for writing
        val SP = getSharedPreferences(DATASTORE, MODE_PRIVATE)
        val myEdit = SP.edit()

        //writing to the datastore/file
        myEdit.putString("name", fieldName!!.text.toString()) //this is key value pair - writing to key: Name
        myEdit.putString("classType", fieldClassType!!.text.toString())
        myEdit.putInt("classNumber", fieldClassNumber!!.text.toString().toInt()) //how the int is converted
        myEdit.putInt(PREF_SEEKBAR, myBar.progress.toString().toInt())
        // using commit() is an option, going to use apply() in this case.
        myEdit.apply() //save chnages
    }
}