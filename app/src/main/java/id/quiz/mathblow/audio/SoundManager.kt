package id.quiz.mathblow.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

/**
 * SFX pendek via SoundPool — NOL izin (resource lokal di res/raw).
 *
 * Memuat secara dinamis lewat getIdentifier() agar modul tetap meng-compile
 * walau file .ogg/.wav belum ada. Letakkan file dengan nama:
 *   res/raw/correct.ogg, res/raw/wrong.ogg, res/raw/tap.ogg, res/raw/finish.ogg
 * dan SFX otomatis aktif (tanpa ubah kode).
 */
class SoundManager(context: Context) {

    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(4)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private val ids = mutableMapOf<String, Int>()
    @Volatile var enabled: Boolean = true

    init {
        listOf("correct", "wrong", "tap", "finish").forEach { name ->
            val resId = context.resources.getIdentifier(name, "raw", context.packageName)
            if (resId != 0) ids[name] = soundPool.load(context, resId, 1)
        }
    }

    fun play(name: String) {
        if (!enabled) return
        ids[name]?.let { soundPool.play(it, 1f, 1f, 1, 0, 1f) }
    }

    fun release() = soundPool.release()
}
