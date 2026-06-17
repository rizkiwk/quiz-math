package com.avos.mathsharp.audio

import android.content.Context
import android.media.MediaPlayer

/**
 * Musik latar looping via MediaPlayer — NOL izin (resource lokal `res/raw/music`).
 * Hanya berbunyi jika diaktifkan DAN app di foreground. Aman bila file belum ada
 * (getIdentifier == 0 → no-op).
 */
class MusicManager(context: Context) {

    private val appContext = context.applicationContext
    private var player: MediaPlayer? = null
    private var enabled = true
    private var foreground = false

    fun setEnabled(value: Boolean) {
        enabled = value
        sync()
    }

    fun onForeground() {
        foreground = true
        sync()
    }

    fun onBackground() {
        foreground = false
        sync()
    }

    private fun sync() {
        if (enabled && foreground) {
            if (player == null) {
                val id = appContext.resources.getIdentifier("music", "raw", appContext.packageName)
                if (id != 0) {
                    player = MediaPlayer.create(appContext, id)?.apply {
                        isLooping = true
                        setVolume(0.5f, 0.5f)
                    }
                }
            }
            player?.takeIf { !it.isPlaying }?.start()
        } else {
            player?.takeIf { it.isPlaying }?.pause()
        }
    }

    fun release() {
        player?.release()
        player = null
    }
}
