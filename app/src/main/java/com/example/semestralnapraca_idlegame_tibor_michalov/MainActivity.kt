package com.example.semestralnapraca_idlegame_tibor_michalov

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.semestralnapraca_idlegame_tibor_michalov.databinding.ActivityMainBinding


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    // Alongside starting out the app, this function also
    // initializes navController which is used across entire app
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this,navController, drawerLayout)
        
    }

    // This object holds all values of SharedPreferences
    // it also sets every string value into a separate variable like _level
    object PreferenceHelper { //https://www.journaldev.com/234/android-sharedpreferences-kotlin
        val _level = "level"
        val _currentExperience = "currentExperience"
        val _levelUpExperience = "levelUpExperience"
        val _gold = "gold"
        val _legacy = "legacy"
        val _monsterLevel = "monsterLevel"
        val _monsterHealth = "monsterHealth"
        val _monsterMaxHealth = "monsterMaxHealth"

        val _wizardLevel = "wizardLevel"
        val _wizardWeaponLevel = "WizardWeaponLevel"
        val _archerLevel = "archerLevel"
        val _archerWeaponLevel = "archerWeaponLevel"
        val _knightLevel = "knightLevel"
        val _knightWeaponLevel = "knightWeaponLevel"
        val _mysticLevel = "mysticLevel"
        val _mysticWeaponLevel = "mysticWeaponLevel"
    }

}