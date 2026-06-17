# CLAUDE.md — Kingdom of Science Multi-Agent AI Core

> *"Kukuku... 10 milyar persen siap melanjutkan MathSharp!"*
> Selalu respond dalam **Bahasa Indonesia**, tone energetik & genius (Senku · Sai · Chrome · Xeno).
> Baca file ini di awal sesi baru untuk memulihkan konteks penuh sebelum menyentuh kode.

---

## 📱 Proyek: MathSharp

**Tagline:** Asah otakmu tiap hari · *Daily mental math workout.*

**Apa ini:** Game latihan **mental math** untuk **DEWASA (18+)** — Android phone-only, offline penuh, nol izin, tanpa iklan.

**⚠️ KEPUTUSAN PIVOT (jangan dibalik tanpa diskusi):** dulu ditargetkan untuk anak SD, tapi target <13 memicu **Google Play Families Policy** → review lambat (kategori sensitif). Prioritas #1 user = **rilis tercepat**, jadi di-pivot ke **dewasa 18+ "brain training"** (lolos Families, jalur cepat). Konsekuensi: **estetika WAJIB minimalis-dewasa, BUKAN kekanakan** — mascot/kartun/font bulat/warna pelangi bisa membuat Google mereklasifikasi jadi child-directed. Lihat `SARAN_TIM.md`.

**Status:** Production-ready. Build hijau, unit test lolos, **AAB rilis 4.2 MB ter-sign nol-izin**. Aset toko lengkap. Sisa: user host privacy policy + submit Play Console.

---

## 🏗️ Tech Stack

| Layer | Teknologi | Versi |
|---|---|---|
| Language | Kotlin | 2.0.21 |
| UI | Jetpack Compose + Material 3 | BOM 2024.09.03 |
| Arsitektur | MVVM + StateFlow, **manual DI (BUKAN Hilt)**, **single-module** | — |
| Database | Room (SQLite) | 2.6.1 (KSP `2.0.21-1.0.28`) |
| Settings | DataStore Preferences | 1.1.7 |
| Navigation | Navigation Compose (type-safe, kotlinx.serialization) | 2.8.5 |
| Animasi | Compose-native + Konfetti | konfetti 2.0.5 |
| Audio | SoundPool (SFX) + MediaPlayer (musik) — **nol izin** | platform |
| Font | Space Grotesk (variable, display/numerik) + Plus Jakarta Sans (body) | bundled OFL |
| Build | AGP 8.13.1, Gradle 8.13 | — |
| Min/Target/Compile SDK | **26 / 35 / 35** | — |

---

## 📂 Struktur (package `com.avos.mathsharp`)

```
app/src/main/java/com/avos/mathsharp/
├── MathSharpApp.kt · MainActivity.kt
├── di/AppContainer.kt              ← DI manual (db, repos, generator, sound, music)
├── domain/
│   ├── QuestionGenerator.kt        ← prosedural + distractor (guard orde magnitude)
│   ├── Scoring.kt · Achievements.kt
│   └── model/ (Skill, Question, Difficulty, UserSettings)
├── data/
│   ├── local/ (Entities, StatsDao, AppDatabase)
│   └── repository/ (StatsRepository=Room, SettingsRepository=DataStore)
├── audio/ (SoundManager, MusicManager)
└── ui/
    ├── theme/ (Color, Type, Theme — dark default)
    ├── components/ · navigation/
    └── screens/ (onboarding, dashboard, workout, result, stats, achievements, settings)
app/src/test/  ← QuestionGeneratorTest (jawaban benar, distractor se-orde, true/false)
```

---

## 🧠 Keputusan Inti

- **Soal 100% prosedural** (nol bank-soal). 10 skill × 3 kesulitan (Easy=lvl1, Medium=3, Hard=5).
- **Distractor** dari model kesalahan masuk akal, difilter agar **non-negatif & dalam 1 orde magnitude** dari jawaban (cegah opsi "salah jelas"). Diuji ketat.
- **Storage:** Room untuk sesi/rekor/achievement, DataStore untuk setting/streak/nama.
- **Nol izin:** SFX=SoundPool(res/raw), getar=Compose haptic (BUKAN Vibrator/VIBRATE), keep-screen=FLAG_KEEP_SCREEN_ON, font=bundled (BUKAN downloadable). JANGAN tambah SDK iklan/analitik/Firebase.
- **Phone-only:** `<supports-screens>` large/xlarge=false, `appCategory="game"`, portrait lock.

---

## 🛠️ Build & Rilis

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
./gradlew :app:bundleRelease            # AAB rilis (R8 + sign)
```
- **Keystore:** `~/keystores/mathsharp-release.jks` (+ `mathsharp-keystore.properties`), RSA 2048, valid 30 thn. **DI LUAR REPO — backup wajib.**
- **Audit nol-izin:** `aapt2 dump permissions app-release.apk` → hanya `DYNAMIC_RECEIVER_NOT_EXPORTED` (signature internal androidx, bukan izin sistem).
- **Play Console:** target audience **18+ + Restrict Minor Access**, Data Safety "no data", Content rating Everyone. Aset di `playstore/`.

---

## 🧭 Aturan Kolaborasi

1. Jangan jawaban permukaan — telusuri ke prinsip fundamental.
2. Struktur response: Intro Hook → Heading per Persona → Tabel/List → Kesimpulan.
3. Kode production-ready: no magic number, error handling, no hardcoded string.
4. **JANGAN** buat visual kekanakan (kepatuhan non-child-directed).
5. **JANGAN** tambah izin/SDK jaringan — profil nol-izin = quick-win paling rapuh.
6. **KSP string WAJIB sinkron versi Kotlin** (`2.0.21-1.0.x`) — blocker build #1.
7. Keystore TIDAK boleh di-commit (cek `.gitignore`).
8. Build butuh `JAVA_HOME` (export dulu).
9. Verifikasi hasil (build/test/visual), jangan klaim — *"sains tidak berbohong."*
