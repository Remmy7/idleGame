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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this,navController, drawerLayout)
        
    }

    object PreferenceHelper { //https://www.journaldev.com/234/android-sharedpreferences-kotlin
        val _level = "level"
        val _currentExperience = "currentExperience"
        val _levelUpExperience = "levelUpExperience"
        val _gold = "gold"
        val _legacy = "legacy"
        val _monsterLevel = "monsterLevel"
        val _monsterHealth = "monsterHealth"

        val _wizardLevel = "wizardLevel"
        val _wizardWeaponLevel = "WizardWeaponLevel"
        val _archerLevel = "archerLevel"
        val _archerWeaponLevel = "archerWeaponLevel"
        val _knightLevel = "knightLevel"
        val _knightWeaponLevel = "knightWeaponLevel"
        val _mysticLevel = "mysticLevel"
        val _mysticWeaponLevel = "mysticWeaponLevel"

        inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
            val editMe = edit()
            operation(editMe)
            editMe.apply()
        }

        var SharedPreferences.level
            get() = getInt(_level, 1)
            set(value) {
                editMe {
                    it.putInt(_level, value)
                }
            }
        var SharedPreferences.gold
            get() = getInt(_gold, 1)
            set(value) {
                editMe {
                    it.putInt(_gold, value)
                }
            }
        var SharedPreferences.legacy
            get() = getInt(_legacy, 1)
            set(value) {
                editMe {
                    it.putInt(_legacy, value)
                }
            }

        var SharedPreferences.currentExperience
            get() = getInt(_currentExperience, 1)
            set(value) {
                editMe {
                    it.putInt(_currentExperience, value)
                }
            }
        var SharedPreferences.levelUpExperience
            get() = getInt(_levelUpExperience, 100)
            set(value) {
                editMe {
                    it.putInt(_levelUpExperience, value)
                }
            }
        var SharedPreferences.monsterHealth
            get() = getInt(_monsterHealth, 100)
            set(value) {
                editMe {
                    it.putInt(_monsterHealth, value)
                }
            }

        var SharedPreferences.wizardLevel
            get() = getInt(_wizardLevel, 1)
            set(value) {
                editMe {
                    it.putInt(_wizardLevel, value)
                }
            }
        var SharedPreferences.wizardWeaponLevel
            get() = getInt(_wizardWeaponLevel, 1)
            set(value) {
                editMe {
                    it.putInt(_wizardWeaponLevel, value)
                }
            }
        var SharedPreferences.archerLevel
            get() = getInt(_archerLevel, 1)
            set(value) {
                editMe {
                    it.putInt(_archerLevel, value)
                }
            }
        var SharedPreferences.archerWeaponLevel
            get() = getInt(_archerWeaponLevel, 1)
            set(value) {
                editMe {
                    it.putInt(_archerWeaponLevel, value)
                }
            }
        var SharedPreferences.knightLevel
            get() = getInt(_knightLevel, 1)
            set(value) {
                editMe {
                    it.putInt(_knightLevel, value)
                }
            }
        var SharedPreferences.knightWeaponLevel
            get() = getInt(_knightWeaponLevel, 1)
            set(value) {
                editMe {
                    it.putInt(_knightWeaponLevel, value)
                }
            }
        var SharedPreferences.mysticLevel
            get() = getInt(_mysticLevel, 1)
            set(value) {
                editMe {
                    it.putInt(_mysticLevel, value)
                }
            }
        var SharedPreferences.mysticWeaponLevel
            get() = getInt(_mysticWeaponLevel, 1)
            set(value) {
                editMe {
                    it.putInt(_mysticWeaponLevel, value)
                }
            }




    }

}