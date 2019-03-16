package com.example.memorama

import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.games.R
import kotlinx.android.synthetic.main.renglon.view.*

class MemoramaAdapter(val chips: ArrayList<Chip>):

    RecyclerView.Adapter<MemoramaAdapter.ChipViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ChipViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.renglon, p0, false)
        return ChipViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  chips.size
    }

    override fun onBindViewHolder(p0: ChipViewHolder, p1: Int) {
        p0.imageView.setImageResource(chips[p1].idImage)
        p0.turnImage = chips[p1].idTurnImage
    }

    var score = 0
    var selectedChip1:ImageView? = null
    var selectedChip2:ImageView? = null

    var turnImage1 = 0
    var turnImage2 = 0

    fun jugar(item : ImageView, turnImage: Int){
        if (selectedChip1 == null) {
            selectedChip1 = item
            turnImage1 = turnImage
        }
        else if (selectedChip2 == null) {
            selectedChip2 = item
            turnImage2 = turnImage
        }
    }

    fun checarPares(view : View){
        if (selectedChip1 != null && selectedChip2 != null){
            if (turnImage1 == turnImage2) {
                score++
                clean()
                if(score == 6)
                    Toast.makeText(view.context, "Ganaste", Toast.LENGTH_LONG).show()
            }
            else{
                Handler().postDelayed({
                    selectedChip1!!.setImageResource(R.mipmap.ic_launcher)
                    selectedChip2!!.setImageResource(R.mipmap.ic_launcher)
                    clean()
                },500)
            }
        }
    }

    fun clean(){
        selectedChip1 = null
        selectedChip2 = null
        turnImage1 = -2
        turnImage2 = -1
    }


    inner class ChipViewHolder(item : View) : RecyclerView.ViewHolder(item) {
        val imageView = item.findViewById<ImageView>(R.id.chip)
        var turnImage : Int = -1

        init {
            item.setOnClickListener {
                imageView.setImageResource(turnImage)
                jugar(imageView, turnImage)
                checarPares(item)
            }
        }
    }

}
