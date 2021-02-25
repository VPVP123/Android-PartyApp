package com.github.skosvall.nextlvl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class PlayLvLGamesActivity : AppCompatActivity() {
    companion object{
        const val GAME_TO_START = "gameToPlay"
        const val BEERPONG = "beerpong"
        const val GAS_GAS = "gasGas"
        const val HORSE_RACE = "horseRace"
    }

    val db = FirebaseFirestore.getInstance()

    val getLvLGamesBeerPong = db.collection("lvlGamesData").document("beerpong").collection("english").document("texts")
    val getLvLGamesGasGas = db.collection("lvlGamesData").document("gasGas").collection("english").document("texts")
    val getLvLGamesHourseRace = db.collection("lvlGamesData").document("horseRace").collection("english").document("texts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_lvl_games)

        val gameToPlay = intent.getStringExtra(GAME_TO_START)

        when (intent.getStringExtra(GAME_TO_START)){
            BEERPONG -> startBeerpong()
            GAS_GAS -> startGasGas()
            HORSE_RACE -> startHorseRace()
        }
    }
    private fun startBeerpong(){
        //Insert everything from firestore in fragment
        getLvLGamesBeerPong.get()
                .addOnSuccessListener { document ->
                    if(document != null){
                        Log.d("exist", "DocumentSnapshot data: ${document.data}")

                        supportFragmentManager.beginTransaction()
                                .add(R.id.PlayLvLGamesFrameLayout, LvLGameFragment.newInstance(document.getString("title") as String,
                                        document.getString("shortDescription") as String,
                                        document.getString("sectionOneText") as String,
                                        document.getString("sectionTwoText") as String,
                                        document.getString("sectionThreeText") as String))
                                .commit()
                    }else{
                        Log.d("noExist", "No document found")
                    }
                }
                .addOnFailureListener {exception ->
                    Log.d("errorDB", "get failed with ", exception)

                }
    }
    private fun startGasGas(){
        //Insert everything from firestore in fragment
        getLvLGamesGasGas.get()
                .addOnSuccessListener { document ->
                    if(document != null){
                        Log.d("exist", "DocumentSnapshot data: ${document.data}")

                        supportFragmentManager.beginTransaction()
                                .add(R.id.PlayLvLGamesFrameLayout, LvLGameFragment.newInstance(document.getString("title") as String,
                                        document.getString("shortDescription") as String,
                                        document.getString("sectionOneText") as String,
                                        document.getString("sectionTwoText") as String,
                                        document.getString("sectionThreeText") as String))
                        .commit()
                    }else{
                        Log.d("noExist", "No document found")
                    }
                }
                .addOnFailureListener {exception ->
                    Log.d("errorDB", "get failed with ", exception)

                }
    }
    private fun startHorseRace(){
        //Insert everything from firestore in fragment
        getLvLGamesHourseRace.get()
                .addOnSuccessListener { document ->
                    if(document != null){
                        Log.d("exist", "DocumentSnapshot data: ${document.data}")

                        supportFragmentManager.beginTransaction()
                                .add(R.id.PlayLvLGamesFrameLayout, LvLGameFragment.newInstance(document.getString("title") as String,
                                        document.getString("shortDescription") as String,
                                        document.getString("sectionOneText") as String,
                                        document.getString("sectionTwoText") as String,
                                        document.getString("sectionThreeText") as String))
                                        .commit()
                    }else{
                        Log.d("noExist", "No document found")
                    }
                }
                .addOnFailureListener {exception ->
                    Log.d("errorDB", "get failed with ", exception)

                }
    }
}