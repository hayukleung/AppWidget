package com.hayukleung.appwidget.yylx

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.util.Log
import android.util.SparseIntArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * @author liangxiaxu@leyaoyao.com
 */
object Sound {

    private var mSoundPool: SoundPool? = null

    private val mSoundIdPool: SparseIntArray = SparseIntArray(1)

    // ---------------------------------------------------------------------------------------------

    private val TAG = Sound::class.java.name

    /**
     * 扫码完毕声效
     */
    private const val SOUND_YYLX = 0x0004

    // -----------------------------------------------------------------------------------------

    /**
     * 初始化
     *
     * @param context Context
     */
    @SuppressLint("ObsoleteSdkInt")
    internal fun init(context: Context) {

        mSoundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder().setMaxStreams(5)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setLegacyStreamType(AudioManager.STREAM_SYSTEM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .build()
                ).build()
        } else {
            SoundPool(5, AudioManager.STREAM_SYSTEM, 5)
        }
        try {
            mSoundIdPool.put(SOUND_YYLX, mSoundPool!!.load(context.assets.openFd("sound/notification_searching_03.wav"), 1))
        } catch (e: IOException) {
            Log.e(TAG, e.message ?: "")
        }
    }

    // ---------------------------------------------------------------------------------------------

    private fun playSound(
        context: Context,
        leftVolume: Float,
        rightVolume: Float,
        callback: Callback
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            if (null == mSoundPool) {
                init(context)
            }
            delay(100)
            mSoundPool!!.play(mSoundIdPool[SOUND_YYLX], leftVolume, rightVolume, 0, 0, 1f)
            delay(200)
            callback.onPlayFinish()
        }
    }


    fun playSoundScanFinish(context: Context, callback: Callback) {
        playSound(context, 1f, 1f, callback)
    }

    fun release() {
        mSoundPool?.release()
    }
}
