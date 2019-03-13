package com.example.memorama

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import com.example.games.R
import kotlinx.android.synthetic.main.activity_memorama2.*

class MemoramaActivity : AppCompatActivity() {
    private var cards_imgs : IntArray = IntArray(12)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memorama2)
        val rv = recyclerView1
        rv.setHasFixedSize(true)
        var grid = GridLayoutManager(this, 4)
        rv.layoutManager = grid

        generateCards()
        for (img in cards_imgs) {
            Log.d("MEMORAMA", img.toString())
        }

        val chips = ArrayList<Chip>()
        for (i  in 0..12)
            chips.add(Chip(R.mipmap.ic_launcher))
        var  adapter = MemoramaAdapter(chips, cards_imgs)
        rv.adapter = adapter
    }

    private fun generateCards() {
        var possible : ArrayList<Int> = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        var cards = intArrayOf(R.drawable.card1, R.drawable.card2, R.drawable.card3,
            R.drawable.card4, R.drawable.card5, R.drawable.card6)
        for (card in cards) {
            var card_a_index = Math.floor(Math.random() * possible.size).toInt()
            var card_a = possible[card_a_index]
            possible.removeAt(card_a_index)
            var card_b_index = Math.floor(Math.random() * possible.size).toInt()
            var card_b = possible[card_b_index]
            possible.removeAt(card_b_index)

            cards_imgs[card_a - 1] = card
            cards_imgs[card_b - 1] = card
        }
    }
}
