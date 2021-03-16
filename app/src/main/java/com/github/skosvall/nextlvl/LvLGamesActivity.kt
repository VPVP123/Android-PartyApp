package com.github.skosvall.nextlvl

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LvLGamesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lvl_games)

        findViewById<Button>(R.id.beerpongButton).setOnClickListener {
            startGame(PlayLvLGameActivity.BEERPONG)
        }
        findViewById<Button>(R.id.gasGasButton).setOnClickListener {
            startGame(PlayLvLGameActivity.GAS_GAS)
        }
        findViewById<Button>(R.id.horseRaceButton).setOnClickListener {
            startGame(PlayLvLGameActivity.HORSE_RACE)
        }
    }
    private fun startGame(gameToStart: String){
        val intent = Intent(this, PlayLvLGameActivity::class.java)
        intent.putExtra(PlayLvLGameActivity.GAME_TO_START, gameToStart)
        startActivity(intent)
    }
}