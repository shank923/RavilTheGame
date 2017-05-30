package sssemil.anar.ravilthegame

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null

    var shitCounter = 0
    var rand = Random()

    private var faceView: ImageView? = null
    private var scoreTextView: TextView? = null
    private var poopView: ImageView? = null

    private var displayMetrics: DisplayMetrics = DisplayMetrics()

    internal var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        player = MediaPlayer.create(this@MainActivity, R.raw.ravileban)

        faceView = ImageView(this@MainActivity)
        scoreTextView = TextView(this@MainActivity)
        poopView = ImageView(this@MainActivity)

        faceView?.setBackgroundResource(R.drawable.face)
        poopView?.setBackgroundResource(R.drawable.poop)

        scoreTextView?.setTextColor(Color.RED)
        scoreTextView?.textSize = 25f
        scoreTextView?.setAllCaps(true)

        val relativeLayout = RelativeLayout(this)
        relativeLayout.addView(faceView)
        relativeLayout.addView(poopView)
        relativeLayout.addView(scoreTextView)
        setContentView(relativeLayout)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager!!
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        windowManager.defaultDisplay.getMetrics(displayMetrics)
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }


    override fun onAccuracyChanged(arg0: Sensor, arg1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            var newX = faceView!!.x - event.values[0] * 2
            var newY = faceView!!.y + event.values[1] * 2

            if (newX > displayMetrics.widthPixels) {
                newX = 0f
            }

            if (newY > displayMetrics.heightPixels) {
                newY = 0f
            }
            if (newX < -faceView!!.width) {
                newX = displayMetrics.widthPixels.toFloat()
            }

            if (newY < -faceView!!.height) {
                newY = displayMetrics.heightPixels.toFloat()
            }

            faceView?.x = newX
            faceView?.y = newY

            if (newX >= poopView!!.x - poopView!!.width
                    && newX <= poopView!!.x + poopView!!.width
                    && newY >= poopView!!.y - poopView!!.height
                    && newY <= poopView!!.y + poopView!!.height) {

                player?.start()
                scoreTextView!!.text = getString(R.string.shit, shitCounter++)

                poopView?.x = rand.nextInt(displayMetrics.widthPixels - poopView!!.width).toFloat()
                poopView?.y = rand.nextInt(displayMetrics.heightPixels - poopView!!.height).toFloat()
            }
        }
    }
}