package com.example.semestralnapraca_idlegame_tibor_michalov

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._archerLevel
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._archerWeaponLevel
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._currentExperience
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._gold
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._knightLevel
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._knightWeaponLevel
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._legacy
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._level
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._levelUpExperience
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._monsterHealth
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._monsterLevel
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._mysticLevel
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._mysticWeaponLevel
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._wizardLevel
import com.example.semestralnapraca_idlegame_tibor_michalov.MainActivity.PreferenceHelper._wizardWeaponLevel
import com.example.semestralnapraca_idlegame_tibor_michalov.databinding.FragmentLegacyMenuBinding

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class legacy_menu : Fragment() {
    private val hideHandler = Handler()

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
    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
        fullscreenContentControls?.visibility = View.VISIBLE
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

    private var dummyButton: Button? = null
    private var fullscreenContent: View? = null
    private var fullscreenContentControls: View? = null

    private var _binding: FragmentLegacyMenuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLegacyMenuBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        visible = true

        binding.legacyMenuBackButton.setOnClickListener{ view : View ->
            view.findNavController().navigate(R.id.action_legacy_menu_to_main_menu)
        }
        binding.legacyMenuGameRestartButton.setOnClickListener{restartGame()}
        updateScreen()
        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent?.setOnClickListener { toggle() }

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        dummyButton?.setOnTouchListener(delayHideTouchListener)
    }
    // Function that fully restarts players progress
    // While also awarding him legacy, according to level he reached
    // Legacy will be used for future upgrades
    private fun restartGame() {
        val sharedPref = activity?.getSharedPreferences("PreferenceHelper", Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(_legacy, sharedPref.getInt(_legacy, 5) + (sharedPref.getInt(_level, 1) / 100))
            putInt(_level, 1)
            putInt(_currentExperience, 0)
            putInt(_levelUpExperience, 100)
            putInt(_monsterLevel, 1)
            putInt(_monsterHealth, 15)
            putInt(_gold, 20000)
            putInt(_wizardLevel, 1)
            putInt(_wizardWeaponLevel, 1)
            putInt(_mysticLevel, 1)
            putInt(_mysticWeaponLevel, 1)
            putInt(_archerLevel, 1)
            putInt(_archerWeaponLevel, 1)
            putInt(_knightLevel, 1)
            putInt(_knightWeaponLevel, 1)

            apply()
            updateScreen()
        }
    }

    // Updates text variables on the screen according to data stored in a SharedPreferences variable
    // Also pre-calculates how legacy will player get after reset
    private fun updateScreen() {
        val sharedPref = activity?.getSharedPreferences("PreferenceHelper", Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            binding.legacyMenuLegacyText.text = getString(R.string.legacy_text) + sharedPref.getInt(_legacy, 1).toString() +
                    " + " + (sharedPref.getInt(_level, 1) / 100).toString()
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(0)
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // Clear the systemUiVisibility flag
        activity?.window?.decorView?.systemUiVisibility = 0
        show()
    }

    override fun onDestroy() {
        super.onDestroy()
        dummyButton = null
        fullscreenContent = null
        fullscreenContentControls = null
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
        fullscreenContentControls?.visibility = View.GONE
        visible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    @Suppress("InlinedApi")
    private fun show() {
        // Show the system bar
        fullscreenContent?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        visible = true

        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable)
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY.toLong())
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