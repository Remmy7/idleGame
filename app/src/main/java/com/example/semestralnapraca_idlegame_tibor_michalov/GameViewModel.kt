package com.example.semestralnapraca_idlegame_tibor_michalov

import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    //main game
    private var _level: UInt = 5u
    val level: UInt
        get() = _level
    private var _monsterLevel: UInt = 1u
    private var _currentExperience: ULong = 0u
    private var _levelupExperience: ULong = 100u
    private var _gold: ULong = 600u
    val gold: ULong
        get() = _gold
    private var _legacyMoney: UInt = 2u
    val legacyMoney: UInt
        get() = _legacyMoney
    //private var _currentDpsDisplay: Long = 0


    //boss
    private var _bossFrequency: UInt = 25u; // frequency of boss spawning, 10 = every 10 monsters
    private var _bossHealthMultiplier: UInt = 10u
    private var _bossExperienceMultiplier: UInt = 5u
}