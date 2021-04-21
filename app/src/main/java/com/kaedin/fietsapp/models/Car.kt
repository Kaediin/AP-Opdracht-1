package com.kaedin.fietsapp.models

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast

class Car : Vehicle(){
    override fun greet(context: Context, msg: String) {
        super.greet(context, msg)
    }

    fun honk(context: Context){
        Toast.makeText(context, "HONK!...", Toast.LENGTH_SHORT).show()
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v!!.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
            Thread.sleep(750)
            v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            v!!.vibrate(700)
            Thread.sleep(250)
            v.vibrate(700)
        }
    }
}