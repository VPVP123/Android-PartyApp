package com.github.skosvall.nextlvl

import android.os.Parcel
import android.os.Parcelable
import java.util.stream.IntStream.range

class DareOrDrinkQuestion(var question: String?) : Parcelable{
    private var nrOfPlayers: Int = 0

    constructor(parcel: Parcel) : this(parcel.readString()) {
        nrOfPlayers = parcel.readInt()
    }

    init{
        if(question?.filter { it == '1' }!!.count() > 0){
            nrOfPlayers++
        }
        if(question?.filter { it == '2' }!!.count() > 0){
            nrOfPlayers++
        }
    }

    fun getNrOfPlayersRequired(): Int{
        return nrOfPlayers
    }

    fun getCompleteQuestion(playerNames: List<String>): String?{
        var questionToReturn = question
        for(i in range(0, nrOfPlayers)){
            questionToReturn = questionToReturn?.replace((i+1).toString(), playerNames[i])
        }
        return questionToReturn
    }

    override fun toString() = ""
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(question)
        parcel.writeInt(nrOfPlayers)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DareOrDrinkQuestion> {
        override fun createFromParcel(parcel: Parcel): DareOrDrinkQuestion {
            return DareOrDrinkQuestion(parcel)
        }

        override fun newArray(size: Int): Array<DareOrDrinkQuestion?> {
            return arrayOfNulls(size)
        }
    }
}
