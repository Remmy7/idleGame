package com.example.semestralnapraca_idlegame_tibor_michalov

import androidx.lifecycle.ViewModel
import kotlin.text.Typography.times

class GameViewModel : ViewModel() {
    //main game
    private var _level: UInt = 5u
    val level: UInt get() = _level

    private var _monsterLevel: UInt = 1u
    val monsterLevel: UInt get() = _monsterLevel

    private var _currentExperience: ULong = 0u
    val currentExperience: ULong get() = _currentExperience

    private var _levelupExperience: ULong = 100u
    val levelupExperience: ULong get() = _levelupExperience

    private var _gold: ULong = 600u
    val gold: ULong get() = _gold

    private var _legacyMoney: UInt = 2u
    val legacyMoney: UInt get() = _legacyMoney

    private var _monsterHealth: Long = 100
    val monsterHealth: Long get() = _monsterHealth

    //private var _currentDpsDisplay: Long = 0


    //boss
    private var _bossFrequency: UInt = 25u; // frequency of boss spawning, 10 = every 10 monsters
    private var _bossHealthMultiplier: UInt = 10u
    private var _bossExperienceMultiplier: UInt = 5u

    fun damageEnemy() {
        _monsterHealth -= 20
        if (_monsterHealth <= 0) {
            _monsterHealth = 100
            increaseGold(500u)
            increaseExperience(500u)
        }
    }

    private fun increaseGold(amount: UInt) {
        _gold += amount
    }

    private fun increaseExperience(amount: ULong) {
        _currentExperience += amount
        if (_currentExperience > _levelupExperience) { levelUp() }
    }
    private fun levelUp() {
        _level++;
        _currentExperience -= _levelupExperience
        _levelupExperience *= 12UL/10UL  // for some reason 1.1UL doesn't work?
        if (_currentExperience > _levelupExperience) { levelUp() }
    }
    private fun increaseLegacy(amount: UInt) {
        _legacyMoney += amount
    }

}