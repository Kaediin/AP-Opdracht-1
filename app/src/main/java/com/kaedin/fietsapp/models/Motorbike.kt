package com.kaedin.fietsapp.models

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast

class Motorbike : Vehicle(){
    /**
     * @param context is used if showing a Toast message
     * @see Toast
     * @param msg is the message shown in the toast
     * @see Toast.setText
     *
     * This function can be overridden if needed
     */
    override fun greet(context: Context, msg: String) {
        super.greet(context, msg)
    }
    /**
     * @param context is used to make a Toast message
     * This function shows a toast message and vibrates the device 6 short times
     * like an angry motor driver :D
     */
    fun honk(context: Context){
        Toast.makeText(context, "HONK!...", Toast.LENGTH_SHORT).show()
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v!!.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
            Thread.sleep(250)
            v.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
            v.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
            Thread.sleep(250)
            v.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
            v.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
            Thread.sleep(250)
            v.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
            v.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
            Thread.sleep(250)
            v.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
            v.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
            Thread.sleep(250)
            v.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            v!!.vibrate(700)
            Thread.sleep(250)
            v.vibrate(700)
        }
    }
}