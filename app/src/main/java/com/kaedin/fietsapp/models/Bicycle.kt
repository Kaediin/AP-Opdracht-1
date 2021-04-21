package com.kaedin.fietsapp.models

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService


class Bicycle : Vehicle(){
    override fun greet(context: Context, msg: String) {
        super.greet(context, msg)
    }

    fun ring(context: Context){
        Toast.makeText(context, "RING RING...", Toast.LENGTH_SHORT).show()
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v!!.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
            Thread.sleep(250)
            v.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            v!!.vibrate(250)
            Thread.sleep(250)
            v.vibrate(250)
        }
    }
}