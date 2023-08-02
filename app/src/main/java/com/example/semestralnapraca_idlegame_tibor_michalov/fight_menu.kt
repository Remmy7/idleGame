package com.example.semestralnapraca_idlegame_tibor_michalov

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.content.SharedPreferences
import android.graphics.Color
import android.media.MediaPlayer
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._archerLevel
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._archerWeaponLevel
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._currentExperience
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._gold
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._knightLevel
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._knightWeaponLevel
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._level
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._levelUpExperience
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._monsterHealth
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._monsterLevel
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._monsterMaxHealth
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._mysticLevel
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._mysticWeaponLevel
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._wizardLevel
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._wizardWeaponLevel
import com.example.semestralnapraca_idlegame_tibor_michalov.databinding.FragmentFightMenuBinding


/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class fight_menu : Fragment() {
    private var specialSpellCastInterval = 0
    private var ticksOfCurse = 0
    private var mysticSpellCastInterval = 5
    private var wizardSpellCastInterval = 10
    private var archerSpellCastInterval = 15
    private var knightSpellCastInterval = 20
    private var mMediaPlayer: MediaPlayer? = null
    //val sharedPreferences = activity?.getSharedPreferences("PreferenceHelper", Context.MODE_PRIVATE) //https://stackoverflow.com/questions/54744526/android-shared-preferences-inside-fragment-not-working-kotlin
    private val hideHandler = Handler()
    lateinit var mainHandler: Handler  //https://stackoverflow.com/questions/55570990/kotlin-call-a-function-every-second

    // A handler that sends a game tick (set of actions) every X milliseconds
    // Right now set to 1000ms (1 second) per tick
    // Also handles calling automatic screen upgrading, spell casting and attack on enemy
    private val updateTextTask = object : Runnable {
        override fun run() {
            specialSpellCastInterval += 0 // TODO change to 1 later
            if(specialSpellCastInterval == mysticSpellCastInterval) {
                castSpecialSpell("mystic")
                updateScreen()
                // play effect too
            } else if(specialSpellCastInterval == wizardSpellCastInterval) {
                castSpecialSpell("wizard")
                updateScreen()
            } else if(specialSpellCastInterval == archerSpellCastInterval) {
                castSpecialSpell("archer")
                updateScreen()
            } else if(specialSpellCastInterval == knightSpellCastInterval) {
                specialSpellCastInterval = 0
                castSpecialSpell("knight")
                updateScreen()
            }
            attackEnemy()
            updateScreen()
            mainHandler.postDelayed(this, 1000)
        }
    }
    // Updates text variables on the screen according to data stored in a SharedPreferences variable
    // Shows boss name and its current health
    @SuppressLint("SetTextI18n")
    private fun updateScreen() {
        val sharedPref =
            activity?.getSharedPreferences("PreferenceHelper", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            val currHealth = sharedPref.getInt(_monsterHealth, 100)
            val maxHealth = sharedPref.getInt(_monsterMaxHealth, 100)

            val healthPercentage = ((currHealth * 100) / maxHealth)
            binding.fightMenuBossNameValue.text = getString(R.string.tempboss)
            if (healthPercentage >= 70) {
                binding.fightMenuBossHealthbarValue.setTextColor(Color.GREEN);
            } else if (healthPercentage >= 30) {
                binding.fightMenuBossHealthbarValue.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.orange
                    )
                );
            } else {
                binding.fightMenuBossHealthbarValue.setTextColor(Color.RED);
            }
            //binding.fightMenuBossHealthbarValue.text = getString(R.string.fight_menu_boss_health) + healthPercentage.toString()
            binding.fightMenuBossHealthbarValue.text =
                "Health:" + getString(R.string.fight_menu_boss_health) + currHealth.toString() + " / " + maxHealth.toString()
        }
    }

    // Function handling attacks on enemy
    // Calls for calcuateDamage() function first
    // If the damage is enough to kill the enemy, monsterKill() function is called
    fun attackEnemy() {
        var calculatedDamage = calculateDamage();
        val sharedPref = activity?.getSharedPreferences("PreferenceHelper",Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(_monsterHealth, sharedPref.getInt(_monsterHealth, 0) - calculatedDamage.toInt())
            apply()
        }
        if (sharedPref.getInt(_monsterHealth, 0) <= 0) { monsterKill() }

    }
    // Function handling spell casts
    // Wizard, Archer and Knight have damaging spells, while Mystic buffs damage for
    // certain amount of game ticks
    // Damage scales off of both weapon and spell damage (legacy soon)
    fun castSpecialSpell(value : String) {
        var cursed = 1
        if (ticksOfCurse > 0) cursed = 2
        when (value) {
            "mystic" -> ticksOfCurse += 5
            "wizard" -> {
                val sharedPref = activity?.getSharedPreferences("PreferenceHelper",Context.MODE_PRIVATE)?: return
                with (sharedPref.edit()) {
                    putInt(_monsterHealth, sharedPref.getInt(_monsterHealth, 150) -
                            (((sharedPref.getInt(_wizardLevel, 150) * 20)
                                    * sharedPref.getInt(_wizardWeaponLevel, 1)) * cursed))
                    apply()
                }
            }
            "knight" -> {
                val sharedPref = activity?.getSharedPreferences("PreferenceHelper",Context.MODE_PRIVATE)
                sharedPref?.edit()?.putInt(_monsterHealth, sharedPref?.getInt(_monsterHealth, 150) -
                        (((sharedPref?.getInt(_knightLevel, 150) * 35) * sharedPref?.getInt(_knightWeaponLevel, 1)) * cursed))
                sharedPref?.edit()?.apply()
            }
            "archer" -> {
                val sharedPref = activity?.getSharedPreferences("PreferenceHelper",Context.MODE_PRIVATE)
                sharedPref?.edit()?.putInt(_monsterHealth, sharedPref?.getInt(_monsterHealth, 150) -
                        (((sharedPref?.getInt(_archerLevel, 150) * 10) * sharedPref?.getInt(_archerWeaponLevel, 1)) * cursed))
                sharedPref?.edit()?.apply()
            }
            else -> {
                val sharedPref = activity?.getSharedPreferences("PreferenceHelper",Context.MODE_PRIVATE)
                sharedPref?.edit()?.putInt(_monsterHealth,65533)
                sharedPref?.edit()?.apply()
            }

        }
    }
    // Function handling monster killing
    // It spawns a new monster with higher level and stats
    // Rewards player for killing the monster
    // Also can give player increased level if his current experience reached
    // level up threshold
    fun monsterKill() {
        val sharedPref = activity?.getSharedPreferences("PreferenceHelper",Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            val monsterLevel = sharedPref.getInt(_monsterLevel, 1)
            val increasedHp = 15;
            var newExperience = sharedPref.getInt(_currentExperience, 1) + ((50 * monsterLevel) * 12 / 10)
            while (newExperience > sharedPref.getInt(_levelUpExperience, 100)) {
                val newLevel = sharedPref.getInt(_level, 50) + 1
                putInt(_level, newLevel)
                newExperience -= sharedPref.getInt(_levelUpExperience, 100)
                putInt(_currentExperience, newExperience)
                putInt(_levelUpExperience, 100 * (newLevel * 12 / 10))
                commit()
            }
            putInt(_monsterLevel, monsterLevel + 1)

            val currHealth = sharedPref.getInt(_monsterMaxHealth, 0)
            putInt(_monsterHealth, currHealth + increasedHp)

            putInt(_monsterMaxHealth, currHealth + increasedHp)

            putInt(_gold, sharedPref.getInt(_gold, 5) + monsterLevel * 10)

            apply()
        }

    }

    // Function handling damage calculation
    // Takes both unit levels and weapon levels and with
    // specific math calculates total damage dealt that tick
    // Also handles bonus damage from curse buff
    fun calculateDamage(): Float {
        var calculatedDamage = 0f // Change the return type to Float
        val sharedPref = activity?.getSharedPreferences("PreferenceHelper", Context.MODE_PRIVATE) ?: return 1f

        val wizardDamage = sharedPref.getInt(_wizardLevel, 0).toFloat() *
                (1f + (sharedPref.getInt(_wizardWeaponLevel, 0).toFloat() / 75f))
        val archerDamage = sharedPref.getInt(_archerLevel, 0) *
                (1f + (sharedPref.getInt(_archerWeaponLevel, 0).toFloat() / 100f))
        val mysticDamage = sharedPref.getInt(_mysticLevel, 0) *
                (1f + (sharedPref.getInt(_mysticWeaponLevel, 0).toFloat() / 130f))
        val knightDamage = sharedPref.getInt(_knightLevel, 0) *
                (1f + (sharedPref.getInt(_knightWeaponLevel, 0).toFloat() / 110f))

        calculatedDamage = wizardDamage + archerDamage + mysticDamage + knightDamage

        if (ticksOfCurse > 0) {
            ticksOfCurse--
            calculatedDamage *= 1.3f // Use 1.3f to represent 13 / 10 as a float
        }

        return calculatedDamage
    }


    @Suppress("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        val flags =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        activity?.window?.decorView?.systemUiVisibility = flags
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }

    private var visible: Boolean = false
    private val hideRunnable = Runnable { hide() }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val delayHideTouchListener = View.OnTouchListener { _, _ ->
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
        false
    }


    private var _binding: FragmentFightMenuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFightMenuBinding.inflate(inflater, container, false)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        visible = true



        binding.fightMenuBackButton.setOnClickListener{ view : View ->
            view.findNavController().navigate(R.id.action_fight_menu_to_main_menu)
            }
        // Set up the user interaction to manually show or hide the system UI.
        mainHandler = Handler(Looper.getMainLooper())


    }

    override fun onResume() {
        super.onResume()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(0)
        mainHandler.post(updateTextTask)
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // Clear the systemUiVisibility flag
        activity?.window?.decorView?.systemUiVisibility = 0
        show()
        mainHandler.removeCallbacks(updateTextTask)
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun toggle() {
        if (visible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        visible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    @Suppress("InlinedApi")
    private fun show() {
        // Show the system bar
        visible = true

        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable)

        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 0

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}