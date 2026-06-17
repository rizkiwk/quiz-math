# MathSharp — Mental Math Brain Training 🧠

Game latihan **mental math** untuk **dewasa (18+)**. Android, phone-only, **nol izin**, **offline penuh**, **tanpa iklan / tanpa data keluar device**.

> Audiens dewasa dipilih secara sengaja agar **lolos Google Play Families Policy → review tercepat**. Lihat `SARAN_TIM.md` untuk analisis lengkap & `BRAND_GUIDELINE.md` untuk sistem desain.

---

## ✅ Status (terverifikasi)

| Verifikasi | Hasil |
|---|---|
| `:app:assembleDebug` | ✅ BUILD SUCCESSFUL |
| `:app:testDebugUnitTest` | ✅ Lolos (mesin soal + scoring) |
| `:app:bundleRelease` (R8 + lintVitalRelease) | ✅ **AAB 4.2 MB**, ter-sign (font brand + SFX + musik) |
| Izin sistem (debug & release) | ✅ **NOL** (hanya signature internal androidx — tak muncul di Play) |
| Penyimpanan | ✅ Room (SQLite) + DataStore, lokal |

> ⚠️ **Keystore:** dev keystore sudah dibuat di `~/keystores/mathsharp-release.jks` (valid 30 thn, di luar repo).
> Password ada di `~/keystores/mathsharp-keystore.properties`. **BACKUP file ini** — hilang = tak bisa update app
> (kecuali reset upload key via Play App Signing). Ganti password sebelum rilis produksi bila perlu.

---

## 🏗️ Tech Stack

Kotlin 2.0.21 · Jetpack Compose + Material 3 · Room (SQLite, KSP) · DataStore · Navigation Compose (type-safe) · Konfetti · MVVM + manual DI · single-module.

minSdk 26 · target/compileSdk 35 · AGP 8.13.1 · Gradle 8.13.

## 📂 Struktur

```
app/src/main/java/com/avos/mathsharp/
├── MathSharpApp.kt · MainActivity.kt
├── di/AppContainer.kt              ← DI manual
├── domain/                         ← QuestionGenerator (prosedural + distractor), Scoring, Achievements
│   └── model/                      ← Skill, Question, UserSettings
├── data/
│   ├── local/                      ← Room: Entities, StatsDao, AppDatabase
│   └── repository/                 ← StatsRepository (Room), SettingsRepository (DataStore)
├── audio/SoundManager.kt           ← SFX SoundPool (nol izin)
└── ui/
    ├── theme/                      ← Color, Type, Theme (dark default)
    ├── components/                 ← AnswerButton, SkillTile, StatPill, StarRow
    ├── navigation/                 ← Routes + NavHost
    └── screens/                    ← dashboard, workout, result, stats, achievements, settings
app/src/test/                       ← QuestionGeneratorTest
```

## ▶️ Build

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew :app:assembleDebug          # APK debug
./gradlew :app:testDebugUnitTest      # unit test
./gradlew :app:bundleRelease          # AAB rilis (butuh keystore di ~/keystores)
```

## 🔜 Sebelum rilis

1. ✅ **Font brand sudah ter-bundle** (`res/font/`: Space Grotesk variable + Plus Jakarta Sans 4 weight, OFL). Display/numerik = Space Grotesk, body = Plus Jakarta Sans.
2. ✅ **SFX placeholder sudah ada** (`res/raw/`: correct/wrong/tap/finish.wav, tone sintetik). Ganti dengan SFX kustom bila mau.
3. ✅ **Ikon Play 512×512 sudah ada** (`playstore/ic_launcher_playstore.png`).
4. ✅ Keystore sudah dibuat (`~/keystores/`), `bundleRelease` terbukti jalan.
5. **Host halaman privasi** → deploy `privacy-site/index.html` ke GitHub Pages/Netlify/domain (single-file, self-contained, bilingual ID/EN, dark+light), salin URL-nya ke Play Console. (Sumber teks: `PRIVACY_POLICY.md`.)
6. Lengkapi Play Console (target audience **18+ + Restrict Minor Access**), upload AAB + aset, submit.

### 🎨 Aset Play Store (siap di `playstore/`)
- `ic_launcher_playstore.png` — ikon 512×512 (32-bit)
- `feature_graphic.png` — feature graphic 1024×500
- `screenshots/` — **6 screenshot HP 1080×2400** dari app rilis (onboarding, dashboard, workout, result, stats, achievements)
- `STORE_LISTING.md` — judul + deskripsi singkat & lengkap (ID & EN) + metadata
- `../privacy-site/index.html` — **halaman privasi web** ber-desain (dark/light, bilingual, animasi) siap host
- 🎵 Musik latar (`res/raw/music.wav`, loop 8 dtk seamless) + 4 SFX — semua placeholder sintetik, ganti bila mau.

> Screenshot di-capture otomatis dari emulator Medium_Phone (tema gelap). Stats menampilkan 1 sesi 0/10 (artefak pengujian) — regenerate bila ingin angka pristine.

*Kingdom of Science · Kukuku.* 🧪
