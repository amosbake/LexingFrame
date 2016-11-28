package com.lexing.lexingframe

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.lexing.lprogressbarcollection.GradientProgressBar
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick
import java.util.*
import kotlin.concurrent.timerTask

class ProgressActivity : AppCompatActivity() {
    var currentProgress = 20
    var timer: Timer? = Timer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)
        val pbSmall = find<GradientProgressBar>(R.id.pb1)
        val pbLarge = find<GradientProgressBar>(R.id.pb2)
        val btnRest = find<Button>(R.id.btnReset)
        timer!!.schedule(
                timerTask {
                    currentProgress++
                    if(currentProgress>100){
//                        timer!!.cancel()
                    }else{
                        runOnUiThread {
                            pbSmall.setProgress(currentProgress)
                            pbLarge.setProgress(currentProgress)
                        }
                    }

                },
                0,
                200)
        btnRest.onClick {
            currentProgress = 0
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

}
