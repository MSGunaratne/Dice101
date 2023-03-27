package com.example.dice101

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {


    private lateinit var playerDice: List<ImageView>
    private lateinit var computerDice: List<ImageView>
    private lateinit var scoreView: TextView
    private lateinit var winScore: TextView
    private lateinit var diceSelection1: CheckBox
    private lateinit var diceSelection2: CheckBox
    private lateinit var diceSelection3: CheckBox
    private lateinit var diceSelection4: CheckBox
    private lateinit var diceSelection5: CheckBox
    private var rollCount: Int = 0
    private var playerTotal: Int = 0
    private var computerTotal: Int = 0
    private var computerWins: Int = 0
    private var playerWins: Int = 0
    private val playerDiceValues = mutableListOf(0, 0, 0, 0, 0)
    private val computerDiceValues = mutableListOf(0, 0, 0, 0, 0)
    private val keepDice = mutableListOf(false, false, false, false, false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scoreView = findViewById(R.id.scoreView)
        scoreView.text = resources.getString(R.string.score_0, playerTotal, computerTotal)
        winScore = findViewById(R.id.winScore)
        winScore.text = resources.getString(R.string.wins, playerWins, computerWins)


        playerDice = listOf(
            findViewById(R.id.dice_view1),
            findViewById(R.id.dice_view2),
            findViewById(R.id.dice_view3),
            findViewById(R.id.dice_view4),
            findViewById(R.id.dice_view5)
        )
        computerDice = listOf(
            findViewById(R.id.dice_image1),
            findViewById(R.id.dice_image2),
            findViewById(R.id.dice_image3),
            findViewById(R.id.dice_image4),
            findViewById(R.id.dice_image5)
        )

        diceSelection1 = findViewById(R.id.checkBox1)
        diceSelection2 = findViewById(R.id.checkBox2)
        diceSelection3 = findViewById(R.id.checkBox3)
        diceSelection4 = findViewById(R.id.checkBox4)
        diceSelection5 = findViewById(R.id.checkBox5)


        fun displayResult(message: String, color: String) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(Html.fromHtml("<font color='$color'>$message</font>", Html.FROM_HTML_MODE_LEGACY))
            builder.setCancelable(false)

            val dialog = builder.create()
            dialog.show()
        }

        fun winGame() {

            playerTotal += sumDiceValues(playerDiceValues)
            computerTotal += sumDiceValues(computerDiceValues)
            scoreView.text = resources.getString(R.string.score_0, playerTotal, computerTotal)

            rollCount = 0

            val winningTotal = winTotal()
            if (playerTotal >= winningTotal && playerTotal > computerTotal) {
                displayResult("You Win", "#00FF00")
                playerWins++
                winScore.text = resources.getString(R.string.wins, playerWins, computerWins)
            } else if (computerTotal >= winningTotal && computerTotal > playerTotal) {
                displayResult("Computer Wins", "#FF0000")
                computerWins++
                winScore.text = resources.getString(R.string.wins, playerWins, computerWins)
            } else if (computerTotal >= winningTotal && computerTotal == playerTotal) {
                rollCount = 2
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    resetImages()
                }, 1000)
                Log.d("TAG", "playerTotal = $playerTotal")
                Log.d("TAG", "computerTotal = $computerTotal")
            }
        }

        val tossButton: Button = findViewById(R.id.toss)
        tossButton.setOnClickListener {

            fun updateComputerRandom() {
                if ((1..6).random() > (1..6).random()) {
                    updateComputerDice(rollDice() as MutableList<Int>)
                }
            }

            when (rollCount) {
                0 -> {
                    updateDiceValues()
                    rollCount++
                }
                1 -> {
                    for (i in 0 until 5) {
                        if (!keepDice[i]) {
                            playerDiceValues[i] = rollDice().first()
                        }
                    }
                    updatePlayerDice(playerDiceValues)
                    updateComputerRandom()
                    rollCount++
                }
                2 -> {
                    for (i in 0 until 5) {
                        if (!keepDice[i]) {
                            playerDiceValues[i] = rollDice().first()
                        }
                    }
                    updatePlayerDice(playerDiceValues)
                    updateComputerRandom()
                    winGame()
                }
            }
        }


        val scoreButton: Button = findViewById(R.id.Score)
        scoreButton.setOnClickListener {
            winGame()
        }


        findViewById<ImageView>(R.id.dice_view1).setOnClickListener {
            diceSelection1.isChecked = !diceSelection1.isChecked
        }
        findViewById<ImageView>(R.id.dice_view2).setOnClickListener {
            diceSelection2.isChecked = !diceSelection2.isChecked
        }
        findViewById<ImageView>(R.id.dice_view3).setOnClickListener {
            diceSelection3.isChecked = !diceSelection3.isChecked
        }
        findViewById<ImageView>(R.id.dice_view4).setOnClickListener {
            diceSelection4.isChecked = !diceSelection4.isChecked
        }
        findViewById<ImageView>(R.id.dice_view5).setOnClickListener {
            diceSelection5.isChecked = !diceSelection5.isChecked
        }

        diceSelection1.setOnCheckedChangeListener { _, isChecked ->
            keepDice[0] = isChecked
        }
        diceSelection2.setOnCheckedChangeListener { _, isChecked ->
            keepDice[1] = isChecked
        }
        diceSelection3.setOnCheckedChangeListener { _, isChecked ->
            keepDice[2] = isChecked
        }
        diceSelection4.setOnCheckedChangeListener { _, isChecked ->
            keepDice[3] = isChecked
        }
        diceSelection5.setOnCheckedChangeListener { _, isChecked ->
            keepDice[4] = isChecked
        }


    }

    private fun rollPlayerDice(): MutableList<Int> {
        val diceValuesNew = rollDice()
        for (i in 0 until 5) {
            playerDiceValues[i] = diceValuesNew[i]
        }
        for (i in playerDice.indices) {
            playerDice[i].setImageResource(getDicePlayer(playerDiceValues[i]))
        }
        return playerDiceValues
    }

    private fun updatePlayerDice(diceValues: MutableList<Int>) {
        for (i in playerDice.indices) {
            playerDice[i].setImageResource(getDiceImage(diceValues[i]))
        }
    }

    private fun updateDiceValues() {
        val playerDiceValuesNew = rollPlayerDice()
        val computerDiceValuesNew = rollComputerDice()
        updatePlayerDice(playerDiceValuesNew)
        updateComputerDice(computerDiceValuesNew)

    }

    private fun rollComputerDice(): MutableList<Int> {
        val diceValuesNew = rollDice()
        for (i in 0 until 5) {
            computerDiceValues[i] = diceValuesNew[i]
        }
        for (i in computerDice.indices) {
            computerDice[i].setImageResource(getDicePlayer(computerDiceValues[i]))
        }
        return computerDiceValues
    }

    private fun updateComputerDice(diceValues: MutableList<Int>) {
        for (i in computerDice.indices) {
            computerDice[i].setImageResource(getDiceComputer(diceValues[i]))
        }
    }


    private fun resetImages() {
        playerDice[0].setImageResource(R.drawable.pdice1)
        playerDice[1].setImageResource(R.drawable.pdice2)
        playerDice[2].setImageResource(R.drawable.pdice3)
        playerDice[3].setImageResource(R.drawable.pdice4)
        playerDice[4].setImageResource(R.drawable.pdice5)
        computerDice[0].setImageResource(R.drawable.dice1)
        computerDice[1].setImageResource(R.drawable.dice2)
        computerDice[2].setImageResource(R.drawable.dice3)
        computerDice[3].setImageResource(R.drawable.dice4)
        computerDice[4].setImageResource(R.drawable.dice5)

        for (i in 0 until 5) {
            playerDiceValues[i] = 0
            computerDiceValues[i] = 0
        }

    }

    private fun sumDiceValues(diceList: MutableList<Int>): Int {
        var sum = 0
        for (value in diceList) {
            sum += value
        }
        return sum
    }
}


private fun rollDice(): List<Int> {
    val diceValues = mutableListOf<Int>()
    for (i in 1..5) {
        diceValues.add(Random.nextInt(6) + 1)
    }
    return diceValues
}


private fun getDicePlayer(diceValue: Int): Int {

    return when (diceValue) {
        1 -> R.drawable.pdice1
        2 -> R.drawable.pdice2
        3 -> R.drawable.pdice3
        4 -> R.drawable.pdice4
        5 -> R.drawable.pdice5
        else -> R.drawable.pdice6
    }

}

private fun getDiceComputer(diceValue: Int): Int {

    return when (diceValue) {
        1 -> R.drawable.dice1
        2 -> R.drawable.dice2
        3 -> R.drawable.dice3
        4 -> R.drawable.dice4
        5 -> R.drawable.dice5
        else -> R.drawable.dice6
    }

}

private fun getDiceImage(value: Int): Int {
    return when (value) {
        1 -> R.drawable.pdice1
        2 -> R.drawable.pdice2
        3 -> R.drawable.pdice3
        4 -> R.drawable.pdice4
        5 -> R.drawable.pdice5
        6 -> R.drawable.pdice6
        else -> throw IllegalArgumentException("Invalid dice value: $value")
    }
}

private fun winTotal(value: Int = 101): Int {
    return value
}






