package com.example.dice101
import androidx.appcompat.app.AlertDialog // for AlertDialog.Builder


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.PopupWindow

class NewGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game)

        fun startNewGame() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val startButton = findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener {
            startNewGame()
        }
        val button = findViewById<Button>(R.id.about_button)

        button.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("I Mojitha Gunaratne - w1867439\n" +
                    "confirm that I understand what plagiarism is and have read and\n" +
                    "understood the section on Assessment Offences in the Essential\n" +
                    "Information for Students. The work that I have submitted is\n" +
                    "entirely my own. Any work from other authors is duly referenced\n" +
                    "and acknowledged.")
            builder.setCancelable(true)


            builder.setPositiveButton("OK") { dialog, which ->

            }

            val dialog = builder.create()
            dialog.show()
        }


    }



        }
