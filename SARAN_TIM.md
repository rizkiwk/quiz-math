# 🧠 Saran Tim — Kingdom of Science Multi-Agent AI Core

> Dokumen advisory untuk proyek **quiz-math** → produk: **"Mental Math — Brain Training"** (audiens **Dewasa 18+**, non-child-directed).
> Distilasi riset + verifikasi adversarial + analisis pasar, dari 4 persona:
> 🟢 Senku (first-principles & konten) · 🔵 Sai (arsitektur kode) · 🟡 Chrome (library/DIY & build) · 🟣 Xeno (rilis & kepatuhan).
> Disusun: 16 Juni 2026. Semua klaim teknis & kebijakan diverifikasi ke dokumentasi resmi Google Play / Android / FTC.

---

## 0. Ringkasan Keputusan (TL;DR)

| Aspek | Keputusan | Alasan inti (terverifikasi) |
|---|---|---|
| **Audiens** | **Dewasa 18+** (brain-training), BUKAN anak SD | Anak <13 memicu **Families Policy** → kategori sensitif → review 3–7 hari. 18+ = lolos total, jalur tercepat |
| Positioning | "Latihan mental math harian biar otak tetap tajam — cepat, offline, tanpa daftar" | Pasar brain-training terbesar (~$9.76B→$39.37B 2033, CAGR ~19%) + reuse mesin soal prosedural |
| Bahasa & UI | **Kotlin + Jetpack Compose + Material 3 (Expressive)** | Selaras `app-senter`/`app-todo`; Compose memudahkan animasi tren |
| Izin runtime | **NOL `<uses-permission>`** | Semua fitur (suara, haptic, animasi, storage, keep-screen-on) bisa nol izin |
| Penyimpanan | **Room (SQLite)** untuk stats/sesi/achievement + **DataStore** untuk setting/profil/streak | Tepat batasan "local storage / SQLite saja" |
| Form factor | **Phone-only** (portrait, `appCategory="game"`, filter large screen) | Sesuai permintaan; games dikecualikan dari mandat large-screen A16/17 |
| Arsitektur | **Single-module `:app`**, MVVM, **manual DI (skip Hilt)** | Tercepat & paling rendah-risiko untuk ship cepat |
| minSdk / target / compile | **26 / 36 / 36** | targetSdk 36 (headroom lewat ~Agu 2026); minSdk direvisi 24→26 (adaptive icon native); verifikasi ulang saat submit |
| Output | **AAB** ter-sign + R8 (minify + shrink) + Play App Signing | Wajib AAB; profil ramping |
| Data Safety | **"No data collected / No data shared"** | Tanpa SDK iklan/analitik/jaringan |
| Deklarasi umur | **18 and over** + **Restrict Minor Access** | Satu-satunya bracket yang lolos bersih dari Families |
| Estetika | **Minimalis "wellness/produktivitas"** — dark mode, numerik tegas, statistik & streak | WAJIB hindari mascot/kartun → cegah reklasifikasi jadi child-directed |
| Timeline review realistis | **2–5 hari** (1 hari mungkin, **tidak dijanjikan** untuk app pertama) | App pertama dari akun cenderung ke ujung lebih lama |

> 📄 Lihat juga: **`BRAND_GUIDELINE.md`** (sistem desain lengkap) & **`PRIVACY_POLICY.md`** (wajib di-host sebelum submit).

---

## 1. 🟣 Xeno — Strategi Audiens & Lolos Review Tercepat

### 1.1 Kenapa anak SD membunuh target rilis cepat

Akarnya: deklarasi **"Target audience and content"** di Play Console. Pilih kelompok umur **di bawah 13** → **Families Policy aktif otomatis** → app masuk **kategori sensitif** dengan review lebih lama & scrutiny konten/visual ketat.

| Deklarasi umur | Families? | Status |
|---|---|---|
| ≤5 / 6–8 / 9–12 | ✅ Ya (primarily child) | Jalur lambat, scrutiny tinggi |
| 13–15 / 16–17 saja | ⚠️ Tidak bersih | "Dianggap memuat anak di sebagian wilayah" — ambigu |
| **18+ saja** | ❌ Tidak | **Bersih** + buka **Restrict Minor Access** |

### 1.2 Jebakan reklasifikasi (KRITIS)

Google **berhak meng-override** deklarasi umurmu berdasarkan **konten & visual** (verbatim: *"...marketing elements... such as youthful animation or young characters in the graphic assets... Google Play may reject your app."*). FTC COPPA 16 CFR 312.2 memakai uji **totalitas keadaan**: subject matter, visual content, **animated characters**, music, age of models, language, marketing.

> ⚠️ **Konsekuensi desain:** mascot ceria + font bulat + confetti warna-warni = sinyal child-directed → bisa menyeret app kembali ke bucket anak walau deklarasi 18+. **Inilah alasan estetika WAJIB minimalis-dewasa.**

### 1.3 Timeline realistis (app baru, akun Organisasi, nol izin, no-data, no-ads)

- **Resmi:** "a few hours to up to seven days (or longer in exceptional cases)"; app pertama dari akun bisa "extended review times".
- **Realistis non-anak: 2–5 hari.** ~1 hari **mungkin** tapi **bukan garansi** untuk submission pertama.
- **Akun Organisasi** dikecualikan dari gate closed-testing (12 tester × 14 hari) — itu hanya untuk akun **personal** pasca-13-Nov-2023.
- **JANGAN edit listing saat review** — SLA dihitung dari "last submitted change"; edit = app dilempar ke belakang antrean.

### 1.4 Checklist kepatuhan (profil review terbersih)

1. ✅ **Nol izin sistem** → form Data Safety paling sederhana.
2. ✅ **Privacy Policy URL publik WAJIB** (meski nol data) — gate sebelum submit. (`PRIVACY_POLICY.md` sudah disiapkan, tinggal host.)
3. ✅ **Data Safety** = "No data collected / No data shared".
4. ✅ **Content rating (IARC)** jujur → **Everyone / 3+**.
5. ✅ **Target audience = 18+** + Restrict Minor Access; ads = No; IAP = No.
6. ✅ **Store listing & ikon = bergaya dewasa/abstrak**, screenshot dari **app ASLI** (bukan mockup, bukan kartun).
7. ❌ **JANGAN** opt-in program "Designed for Families" (memicu special review discretionary).

---

## 2. 🟢 Senku — Konsep Produk & Konten Matematika (First-Principles)

**Esensi produk:** generator soal aritmetika **prosedural** yang tak terbatas — pas untuk brain-training dewasa, tanpa bank soal yang harus ditulis.

### 2.1 Cakupan materi (dewasa, framing "mental math")

| Tier | Skill (prosedural) | Contoh |
|---|---|---|
| Inti | +, −, ×, ÷ kecepatan | `47 × 8 = ?` |
| Lanjutan | Persen & diskon mental | `15% dari 80 = ?` |
| Lanjutan | Estimasi / pembulatan | `paling dekat ke 312 + 489?` |
| Lanjutan | Deret (next number) | `2, 6, 18, 54, ?` |
| Lanjutan | Urutan operasi (PEMDAS) | `6 + 4 × 3 = ?` |
| Lanjutan | Kuadrat / akar / pangkat | `√144 = ?` |
| Speed | True/False cepat | `7 × 8 = 54? (B/S)` |
| Speed | Operator/angka hilang | `6 ? 4 = 24` |

**Difficulty:** 3–5 tier adaptif (Easy → Expert) yang menaikkan magnitude angka, campuran operasi, dan batas waktu. **Harus tetap menantang di puncak** — dewasa churn kalau terasa "untuk anak".

**Mode:** `Daily Workout` (60–90 detik) · `Endless/Practice` · `Per-skill drill` · (opsional kelak) `Timed Challenge` lokal.

### 2.2 Generasi soal & logika distractor

**Keputusan: PROSEDURAL (≈100%)** — nol authoring, replay tak terbatas, AAB ramping.

```kotlin
data class Question(
    val prompt: String,       // "47 × 8 = ?"
    val correct: Int,         // 376
    val options: List<Int>,   // shuffled, mengandung correct
    val skill: String,
    val difficulty: Int       // 0..999 monotonic
)
interface QuestionGenerator { fun generate(skill: String, level: Int, rng: Random): Question }
```

**Distractor = model "kesalahan masuk akal", lalu difilter** (random murni = gampang ditebak via magnitude):
- Off-by-one/ten: `correct ± 1/2/10` · Operation confusion: untuk `a×b` sisipkan `a+b` · Carry/borrow error · Transposisi digit · Tetangga tabel perkalian.
- **Guard:** semua opsi distinct ≠ correct; **same sign & same order of magnitude**; non-negatif bila tier belum kenal negatif; 4 opsi untuk dewasa.

---

## 3. 🔵 Sai — Arsitektur Kode

**Keputusan: single-module `:app`, MVVM, manual DI. Jangan Hilt, jangan multi-module.** Ini tercepat & paling rendah-risiko untuk ship cepat (Hilt/KSP dance = blocker hari-H paling umum).

```
quiz-math/app/src/main/java/.../
├── data/      Room (db, dao, entity), DataStore, repository
├── domain/    QuestionGenerator, distractor logic, scoring/stars — pure Kotlin, unit-testable
└── ui/        Compose screens, ViewModel per screen, theme, navigation, components
```

- **DI manual:** satu `AppContainer` dibangun di `Application.onCreate()` (Room + DataStore + repo + generator). ViewModel via `ViewModelProvider.Factory`.
- **State:** `StateFlow<UiState>` + `sealed interface` (`QuizUiState.Loading/Playing/Finished`); `collectAsStateWithLifecycle()`; event one-shot via `Channel`.
- **Navigation:** `navigation-compose`, route type-safe via `kotlinx.serialization`.

### 3.1 Data model

**DataStore (Preferences)** — setting + state ringan single-row:
```kotlin
object Prefs {
    val SFX_ENABLED   = booleanPreferencesKey("sfx_enabled")     // default true
    val MUSIC_ENABLED = booleanPreferencesKey("music_enabled")   // default true
    val THEME_MODE    = intPreferencesKey("theme_mode")          // 0 system / 1 dark / 2 light
    val ONBOARDED     = booleanPreferencesKey("onboarded")
    val PLAYER_NAME   = stringPreferencesKey("player_name")
    val DAILY_STREAK  = intPreferencesKey("daily_streak")
    val LAST_PLAYED_EPOCH_DAY = longPreferencesKey("last_played_epoch_day")
}
```

**Room (SQLite)** — historis/agregasi:
```kotlin
@Entity(tableName = "skill_progress", indices = [Index(value = ["skill"], unique = true)])
data class SkillProgressEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val skill: String, val highestLevel: Int = 1,
    val bestScore: Int = 0, val bestAccuracy: Float = 0f
)

@Entity(tableName = "workout_session")   // history → personal best, grafik, streak
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val skill: String, val level: Int,
    val correct: Int, val total: Int, val score: Int,
    val avgReactionMs: Long, val durationMs: Long,
    val playedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "achievement")
data class AchievementEntity(
    @PrimaryKey val code: String,        // "streak_7", "speed_demon", "perfect_100"
    val unlocked: Boolean = false,
    val progress: Int = 0, val target: Int = 1, val unlockedAt: Long? = null
)
```
DAO expose `Flow<...>`; seed `achievement` saat `onCreate`. Profil (nama/streak) di **DataStore** (single-row, dibaca tiap layar).

### 3.2 Tech stack & versi (gaya `libs.versions.toml`, selaras siblings)

```toml
[versions]
kotlin = "2.0.21"               # selaras app-senter/app-todo
agp = "8.13.0"
composeBom = "2024.09.03"       # proven; boleh bump ke yang lebih baru untuk M3 terbaru
coreKtx = "1.16.0"
lifecycle = "2.8.7"
activityCompose = "1.9.3"
navigationCompose = "2.8.5"
room = "2.6.1"                  # KSP HARUS cocok versi Kotlin
ksp = "2.0.21-1.0.28"           # WAJIB sinkron dengan kotlin = blocker #1
datastore = "1.1.7"
splashscreen = "1.0.1"
lottie = "6.7.1"                # com.airbnb.android:lottie-compose
konfetti = "2.0.5"              # nl.dionsegijn:konfetti-compose
kotlinxSerialization = "1.7.3"
coroutines = "1.9.0"
```
**SDK:** `minSdk = 26` (Android 8.0 — adaptive icon native tanpa PNG fallback, jangkauan ~96%+; direvisi dari 24), `compileSdk = 36`, `targetSdk = 36` (wajib app baru 2026; verifikasi ulang angka di Play Console saat submit).

### 3.3 Inventori layar (lolos "minimum functionality")

`Splash → (Onboarding ringan) → Dashboard → Workout/Quiz → Result → Stats → Achievements → Settings`

| # | Layar | Inti |
|---|---|---|
| 1 | Splash | branded, `core-splashscreen` |
| 2 | Onboarding (first-run) | nama + tema; tulis DataStore |
| 3 | **Dashboard** | streak, total stats, CTA "Daily Workout", grid skill |
| 4 | **Workout/Quiz** | soal, 4 opsi, timer, progress, feedback instan (suara + animasi) |
| 5 | **Result** | skor, akurasi %, reaction time, streak update, konfeti halus |
| 6 | **Stats** | grafik akurasi/reaction, personal best, kalender streak |
| 7 | **Achievements** | grid badge locked/unlocked |
| 8 | **Settings** | sfx/music, tema, reset progress, link Privacy Policy |

> Stats + Achievements = pembeda dari "terlalu minimal" (penyebab umum reject "low functionality").

---

## 4. 🟡 Chrome — Library, Nol-Izin & Build

### 4.1 Stack animasi/UI (semua nol-izin, offline, ramping)

| Library | Versi 2026 | Koordinat Gradle | Izin | Pakai untuk |
|---|---|---|---|---|
| Lottie Compose | **6.7.1** | `com.airbnb.android:lottie-compose:6.7.1` | **Nol** (pakai `RawRes`/`Asset`, **jangan** `Url`) | Animasi vektor (idle, celebrate) |
| Konfetti Compose | **2.0.5** | `nl.dionsegijn:konfetti-compose:2.0.5` | **Nol** | Burst konfeti saat personal best |
| Compose Animation | bawaan BOM | `androidx.compose.animation` | **Nol** | Spring, count-up, shake, AnimatedContent |
| SoundPool | platform | `android.media.SoundPool` | **Nol** (SFX di `res/raw`) | SFX pendek (benar/salah/tap) |

**Compose-native menutup ~80% kebutuhan tanpa library:** bouncy button (`spring(DampingRatioMediumBouncy)`), count-up skor (`animateIntAsState`), shake jawaban salah (`Animatable` + `keyframes`), transisi soal (`AnimatedContent`), CTA pulsing (`rememberInfiniteTransition`).

**Tolak/hati-hati:** Rive (native C++ 3–6 MB+ per ABI; overkill), Accompanist (mati/upstreamed — pakai `enableEdgeToEdge` dll), Coil network module & Media3-untuk-SFX (salah alat / tarik INTERNET).

### 4.2 Vonis nol-`<uses-permission>`

| Fitur | API | Izin? | Verdict |
|---|---|---|---|
| SFX bundled | `SoundPool` (`res/raw`) | Nol | ✅ |
| Musik latar loop | `MediaPlayer` (jangan `setWakeMode`) | Nol | ✅ |
| **Haptic benar/salah** | **`LocalHapticFeedback`** (Compose) | **Nol** | ✅ pakai ini |
| ❌ Getar via `Vibrator` | `VibrationEffect` | **VIBRATE** | ❌ **DROP** (langgar nol-izin) |
| Room/DataStore/internal files | — | Nol | ✅ |
| Lottie bundled | `LottieCompositionSpec.RawRes/Asset` | Nol | ✅ |
| Font | **bundled** `res/font` (`Font(R.font.x)`) | Nol | ✅ |
| ❌ Downloadable Google Fonts | provider GMS | **INTERNET** | ❌ jangan, **bundle saja** |
| Keep screen on | **`FLAG_KEEP_SCREEN_ON`** | Nol | ✅ (bukan WakeLock) |

**Jebakan suntik-izin (HINDARI):** SDK iklan/`play-services-ads` (AD_ID + INTERNET), Firebase/GMS Analytics, Crashlytics, downloadable fonts, Lottie `.Url`, Coil network. Manifest merger ambil **UNION** semua izin lib transitif.

**Audit manifest final:**
```
./gradlew :app:processReleaseManifest
# baca app/build/intermediates/merged_manifests/release/AndroidManifest.xml → target: 0 uses-permission
# blame: app/build/outputs/logs/manifest-merger-release-report.txt
aapt dump permissions app-release.apk    # ground truth = kosong
```

### 4.3 Build ramping

`isMinifyEnabled = true` + `isShrinkResources = true` (R8). `-keep` rules untuk Room entity, kelas route `@Serializable`. Keystore di `~/keystores` (di luar repo), validity ≥ 25 tahun, **backup**. Output **`bundleRelease` → AAB ter-sign**.

---

## 5. 🎨 Sistem Desain (ringkas — detail di `BRAND_GUIDELINE.md`)

- **Estetika:** minimalis "wellness/produktivitas" (vibe Elevate/Headspace), **dark mode default**, numerik besar & tegas, statistik & streak. **NOL kartun/mascot/balon.**
- **Warna:** dark navy base + aksen **deep blue/teal**, success hijau + ikon, error coral lembut + ikon (jangan andalkan warna saja). WCAG AA.
- **Font (Google Fonts, bundled, OFL):** display/numerik **Space Grotesk**, body/UI **Plus Jakarta Sans** (fallback: Sora / Inter). Bukan font bulat kekanakan.
- **Motion:** springy/juicy tapi *elegan* — micro-interaction, count-up, shake halus, transisi 200–400 ms, hormati reduce-motion. Manfaatkan **Material 3 Expressive** (spring motion + shape morph).
- **Gamifikasi (aman, tanpa dark pattern):** streak dengan **freeze/forgive** (tanpa notif menuduh), milestone/achievement, dashboard statistik, daily challenge opsional. Hindari FOMO/leaderboard menekan.

---

## 6. 📱 Strategi Phone-Only (manifest)

Games **dikecualikan** dari mandat orientasi/resize large-screen Android 16/17 → portrait lock aman. Tidak ada kebijakan yang **memblokir** app phone-only (hanya kurangi visibilitas di tablet — itu memang tujuanmu).

```xml
<manifest xmlns:tools="http://schemas.android.com/tools" ...>
    <supports-screens
        android:smallScreens="true"
        android:normalScreens="true"
        android:largeScreens="false"
        android:xlargeScreens="false"
        android:anyDensity="true" />

    <application android:appCategory="game" ... >
        <activity
            android:name=".MainActivity"
            android:screenOrientation="portrait"
            android:resizeableActivity="false"
            android:configChanges="orientation|screenSize|screenLayout|keyboardHidden" ... />
    </application>
</manifest>
```
> Jangan pakai `<compatible-screens>` (rapuh). Verifikasi jumlah "supported devices" di Play Console → Device catalog setelah upload. Tradeoff jujur: filter ini mengecilkan basis instal — pilihan sadar sesuai permintaan phone-only.

---

## 7. ✅ Fakta Terverifikasi (lolos uji adversarial)

| Klaim | Vonis | Sumber |
|---|---|---|
| Pilih umur <13 → Families Policy aktif | ✅ | support.google.com/.../9867159 |
| 13–17 saja BUKAN exemption bersih ("includes children in some locales") | ✅ | 9867159 |
| **18+ saja = exemption bersih** + Restrict Minor Access | ✅ | 9867159 + 9893335 |
| Google override deklarasi via visual/"youthful animation" → reject | ✅ (verbatim) | 9867159 |
| COPPA 312.2 = uji totalitas (animated characters, dll) | ✅ | FTC 16 CFR 312.2 |
| Akun Organisasi dikecualikan gate 12-tester/14-hari | ✅ | 14151465 |
| Review: jam-an s/d 7 hari; app pertama bisa lebih lama | ✅ (verbatim) | 9859654 |
| Edit listing saat review = mundur antrean | ✅ (verbatim) | 9859654 |
| `setTorchMode`-style: Compose haptic TIDAK butuh VIBRATE; `Vibrator` butuh VIBRATE | ✅ | developer.android.com haptics |
| `FLAG_KEEP_SCREEN_ON` nol izin (vs WAKE_LOCK) | ✅ | developer.android.com wakelock |
| Lottie bundled & SoundPool `res/raw` = nol izin | ✅ | airbnb/lottie · media docs |
| Downloadable Google Fonts = INTERNET (bundle = nol izin) | ✅ | downloadable-fonts docs |
| targetSdk 35 wajib app baru (efektif 31 Agu 2025) | ✅ | 11926878 |
| Games dikecualikan mandat orientasi large-screen A16/17 | ✅ | android/versions/17 |

---

## 8. ⚠️ Pantangan & Koreksi Kritis

- **JANGAN target umur <13** kecuali misi edukasi anak > kecepatan rilis (terima 3–7 hari + Families).
- **JANGAN buat visual kekanakan** (mascot/kartun/font bulat/confetti tema) — bisa direklasifikasi child-directed walau deklarasi 18+.
- **JANGAN tambah SDK iklan/analitik/Firebase** — suntik AD_ID/INTERNET via manifest merger, hancurkan profil "no data".
- **JANGAN pakai `Vibrator`** (butuh VIBRATE) — pakai Compose haptic. **JANGAN downloadable fonts** — bundle.
- **KSP string WAJIB sinkron versi Kotlin** (`2.0.21-1.0.x`) — blocker build hari-H paling umum.
- **JANGAN edit store listing saat review** — mundur antrean.
- **JANGAN janji "1 hari"** — realistis 2–5 hari untuk app pertama (verifikasi ekspektasi).
- **Privacy Policy URL publik wajib** sebelum submit, meski nol data.
- **Keystore** ≥25 thn, di luar repo, **backup**.

---

## 9. 🚀 Urutan Build & Rilis (jalur tercepat, akun Organisasi)

1. **Identitas & SDK:** `applicationId`, `versionCode=1`, `versionName="1.0"`, `minSdk=26`, `target/compileSdk=36`. Build debug di HP asli.
2. **Signing & shrink:** keystore di `~/keystores`; `signingConfigs.release`; R8 `minify+shrink`; `-keep` Room & serialization → **`bundleRelease`**.
3. **Smoke-test AAB rilis:** `bundletool build-apks --mode=universal` → install HP asli → jalankan flow penuh (sekaligus ambil **screenshot asli**).
4. **Host Privacy Policy** (paralel — gating): publish `PRIVACY_POLICY.md` ke URL publik (GitHub Pages/domain).
5. **Play Console — buat app:** bahasa default `id`, kategori **Education** atau **Games > Educational/Puzzle**, gratis.
6. **Store listing aset ASLI:** ikon 512×512 (abstrak/numerik, **bukan kartun**), feature graphic 1024×500, screenshot dari build rilis (min 2, ideal 4–8: Dashboard, Workout, Result, Stats). Deskripsi Indonesia "mental math/brain training".
7. **Content rating (IARC):** jujur → Everyone / 3+.
8. **Data Safety:** "No data collected / No data shared".
9. **Target audience:** **18+** + Restrict Minor Access; ads = No; IAP = No; permissions = none (verifikasi manifest).
10. **Release:** upload AAB ke **Production**, rollout 100%, **submit sekali** — jangan edit saat review.
11. **Tunggu ~2–5 hari.** Konsistensi langkah 4/8/9 = kunci pass cepat.

---

## 10. 🔜 Langkah Berikutnya (siap eksekusi)

1. Scaffold proyek `:app` (build.gradle.kts + `libs.versions.toml` + manifest nol-izin + theme M3 dark + nav).
2. Implementasi `domain/QuestionGenerator` + distractor + unit test.
3. Implementasi Room + DataStore + repository.
4. UI: Dashboard → Workout → Result → Stats → Achievements → Settings.
5. Polish: SoundPool SFX, Lottie/konfeti, haptic, animasi springy.
6. Build AAB rilis + host Privacy Policy + lengkapi Play Console.

---

*Kukuku... Sains tidak pernah berbohong. Pivot ke audiens dewasa = jalur tercepat yang terverifikasi, bukan tebakan. Tinggal eksekusi. — Kingdom of Science* 🧪
