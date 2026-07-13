# 🎨 Brand Guideline — quiz-math ("Mental Math / Brain Training")

> Sistem desain untuk audiens **Dewasa 18+**, estetika **minimalis "wellness/produktivitas"**.
> Prinsip utama: **terlihat dewasa & premium, BUKAN kekanakan** — ini juga syarat kepatuhan (cegah reklasifikasi child-directed). Disusun 16 Juni 2026 oleh Kingdom of Science.

---

## 1. Brand Direction

- **Nama (kandidat, register dewasa):** *MathSharp* · *Sharpen* · *Brain Reps* · *NumberFit* · *Tally — Mental Math* · pola pemenang: **"[Brand] — Mental Math Brain Training"**.
- **Tone of voice:** ringkas, percaya diri, berbasis data. "Sharpen", "Focus", "Streak", "Personal best", "Stats". Hindari bahasa kekanakan ("ayo bisa!", "hebat sekali adik!").
- **Personality:** tajam, tenang, modern, sedikit "techy/precision". Vibe: Elevate × Headspace × kalkulator premium.
- **Ikon:** mark **abstrak/numerik** (mis. glyph "×" atau bracket minimalis dalam lingkaran gradient blue→teal). **TANPA** karakter, wajah, mata, atau kartun.

---

## 2. Color System (WCAG AA)

> Aturan: base **calm-cool** (navy/blue/teal) untuk fokus & rendah-cemas; aksen hanya pada elemen interaktif & reward. Korektness **jangan andalkan warna saja** — selalu + ikon + label.

### 🌙 Dark (default)
| Role | HEX | Catatan |
|---|---|---|
| Background | `#0E1117` | Near-black navy (bukan hitam pekat) |
| Surface | `#161B26` | Kartu |
| Surface Variant | `#1F2630` | Tile, inactive |
| Primary | `#4C8DFF` | Electric blue — CTA, fokus |
| On-Primary | `#06121F` | |
| Secondary | `#2DD4BF` | Teal — aksi sekunder |
| Tertiary | `#A78BFA` | Violet — tema/unlockable |
| Success (benar) | `#34D399` | + ikon check + label |
| Error (salah) | `#FB7185` | Coral lembut + ikon + copy lembut |
| Streak/Reward | `#F59E0B` | Gold — streak, personal best |
| On-Surface (teks) | `#E6EAF2` | ~14:1 di background |
| On-Surface Variant | `#9AA4B2` | Teks sekunder |
| Outline | `#2A323D` | |

### ☀️ Light
| Role | HEX |
|---|---|
| Background | `#F6F8FB` |
| Surface | `#FFFFFF` |
| Surface Variant | `#EDF1F7` |
| Primary | `#2563EB` |
| On-Primary | `#FFFFFF` |
| Secondary | `#0D9488` |
| Tertiary | `#7C3AED` |
| Success | `#059669` |
| Error | `#E11D48` |
| Streak/Reward | `#D97706` |
| On-Surface | `#111827` |
| On-Surface Variant | `#4B5563` |
| Outline | `#D1D9E6` |

---

## 3. Typography

**Pairing (Google Fonts, OFL, BUNDLE di `res/font` — nol izin):**
- **Display / Headline / NUMERIK soal & skor → `Space Grotesk`** — modern, "techy", angka distinctif & tegas. Hero font math app.
- **Body / UI / label → `Plus Jakarta Sans`** — netral, sangat legible, dewasa.
- **Fallback:** Space Grotesk → Sora → sans-serif; Plus Jakarta Sans → Inter → Roboto.

> ❌ Jangan Fredoka/Baloo/Comic — terbaca kekanakan & berisiko child-directed.

**Skala (Material 3):**

| Token | Font | Size / LineHeight | Pakai |
|---|---|---|---|
| displayLarge | Space Grotesk | 48 / 56, w600 | Angka soal besar `47 × 8` |
| headlineMedium | Space Grotesk | 28 / 36, w600 | Judul layar |
| titleLarge | Plus Jakarta Sans | 22 / 28, w600 | Section |
| bodyLarge | Plus Jakarta Sans | 16 / 24, w400 | Teks utama |
| labelLarge | Plus Jakarta Sans | 14 / 20, w600 | Tombol/chip |
| numberDisplay (custom) | Space Grotesk | 36 / 44, w700, **tabular figures** | Skor/streak count-up |

---

## 4. Layout, Sizing & UX

- **Touch target:** min 56dp, ideal 64dp untuk tombol jawaban (dewasa, tapi tetap nyaman 1 tangan).
- **Spacing scale:** 4 / 8 / 12 / 16 / 24 / 32dp. Padding layar 16–24dp. Gap antar opsi ≥12dp.
- **Corner radius:** 16dp kartu, 20–24dp tombol pill, 28dp sheet. (Material 3 Expressive shape.)
- **Satu fokus per layar:** satu soal, satu CTA dominan.
- **Bottom nav max 3–4:** Play · Stats · Achievements · Settings.
- **Feedback instan** tiap tap (<150 ms): visual + SFX + haptic.
- **Timer = bagian inti** (brain-training = speed), tapi visual tenang (bukan merah berkedip panik).
- **NO dark pattern:** tanpa FOMO, tanpa notif menuduh, tanpa pay-to-win.

---

## 5. Motion (springy tapi elegan)

| Efek | API Compose | Spec |
|---|---|---|
| Press tombol | `animateFloatAsState` scale | `spring(dampingRatio=0.55f, stiffness=Spring.StiffnessMediumLow)`; scale 0.96 |
| Count-up skor/streak | `animateIntAsState` / `Animatable` | 600–900 ms easing emphasized |
| Jawaban benar | scale-pop + `Success` glow + konfeti halus (Konfetti) | 300 ms |
| Jawaban salah | shake (`Animatable` offset `keyframes` ±8dp ×3) | 250 ms, lalu snap |
| Transisi layar | `AnimatedContent` slide + fade | 250–350 ms |
| CTA idle | `rememberInfiniteTransition` alpha/scale halus | loop lembut |

- Durasi 200–400 ms, 60fps, **hormati reduce-motion** (ganti bounce → fade).
- Manfaatkan **Material 3 Expressive**: spring motion system + shape-morph.

---

## 6. Gamifikasi (aman, retensi tinggi)

| Mekanik | Implementasi | Catatan |
|---|---|---|
| **Streak harian** | counter + kalender | **WAJIB freeze/forgive** 1 hari; **TANPA** notif menuduh |
| **Milestone/achievement** | badge unlock | "Speed Demon", "Perfect 100", "7-Day Streak" |
| **Dashboard statistik** | akurasi %, reaction time, personal best, grafik | inti daya tarik dewasa |
| **Daily Workout** | sesi 60–90 detik | habit loop |
| Adaptive difficulty | 3–5 tier | naikkan magnitude + waktu |

❌ Hindari: leaderboard menekan, loot box variable-ratio, countdown urgensi, notif loss-framed.

---

## 7. Accessibility

- [ ] Kontras AA: 4.5:1 teks, 3:1 teks besar/UI (palet di atas sudah memenuhi).
- [ ] Korektness: warna **+ ikon + label** (success=hijau+check, error=coral+retry) — colorblind-safe.
- [ ] Hormati skala font OS + toggle "Teks Besar" in-app; layout reflow.
- [ ] Reduce-motion: ganti bounce/konfeti dengan fade.
- [ ] Suara **suportif, bukan wajib** (jalan penuh saat mute; ada backup visual + haptic).
- [ ] TalkBack: content description di semua elemen interaktif.

---

## 8. Compose Theme (sketsa)

```kotlin
private val DarkColors = darkColorScheme(
    primary = Color(0xFF4C8DFF), onPrimary = Color(0xFF06121F),
    secondary = Color(0xFF2DD4BF), tertiary = Color(0xFFA78BFA),
    background = Color(0xFF0E1117), surface = Color(0xFF161B26),
    surfaceVariant = Color(0xFF1F2630),
    onBackground = Color(0xFFE6EAF2), onSurface = Color(0xFFE6EAF2),
    onSurfaceVariant = Color(0xFF9AA4B2), outline = Color(0xFF2A323D),
    error = Color(0xFFFB7185),
)
// + LightColors (lihat tabel §2). Success & Streak/Gold sebagai extra color tokens
// (CompositionLocal/companion) karena bukan slot standar M3.

@Composable
fun QuizMathTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = QuizMathTypography,   // Space Grotesk + Plus Jakarta Sans
        content = content
    )
}
```

---

## 9. Do / Don't

| ✅ DO | ❌ DON'T |
|---|---|
| Dark mode default, navy + aksen blue/teal | Palet pelangi primer "mainan" |
| Angka besar Space Grotesk, statistik | Mascot, wajah, mata googly |
| Ikon abstrak/numerik | Ikon kartun/karakter anak |
| Copy "Sharpen / Streak / Stats" | "Ayo adik / belajar seru / sekolah" |
| Konfeti halus sebagai reward | Confetti/balon sebagai tema utama |
| Screenshot store = UI stats asli | Screenshot mockup/ilustrasi anak |

> Setiap pelanggaran kolom kanan = sinyal child-directed → risiko reklasifikasi Families & review lambat.
