package com.example.dice

import android.widget.SeekBar

fun SeekBar.setOnSeekBarChangeListener(function: (seekBar: SeekBar?, progress: Int, fromUser: Boolean) -> Unit) {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) =
            function(seekBar, progress, fromUser)

        override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
        override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
    })
}

