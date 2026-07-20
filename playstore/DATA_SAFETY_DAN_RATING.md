# ✅ Jawaban Form "App Content" Play Console — MathBlow (`id.quiz.mathblow`)

> Panduan jawaban presisi untuk setiap form di **Play Console → Policy and programs → App content**.
> Semua jawaban berbasis perilaku app yang sudah diverifikasi dari source code: **offline, nol izin,
> tanpa SDK jaringan/analitik/pihak ketiga**.

---

## 1. Privacy Policy
- **Privacy policy URL:** `〔URL hasil hosting PRIVACY_POLICY.md〕` (wajib diisi, meski tanpa data).

---

## 2. Data Safety (paling penting)

**Pertanyaan: Does your app collect or share any of the required user data types?** → **NO.**

Alasan: seluruh data (skor, statistik, streak, nama opsional) dibuat & disimpan **lokal**
(Room/DataStore); aplikasi **tidak punya jaringan** → **tidak ada data yang dikirim ke pengembang
atau pihak ketiga**.

| Bagian | Jawaban |
|---|---|
| Data **collected** | **None** |
| Data **shared** | **None** |
| Encrypted in transit | N/A (tak ada transit data) |
| Cara user minta hapus data | **Uninstall app / reset di dalam app** (data 100% lokal) |

---

## 3. Ads
- **Does your app contain ads?** → **NO.** *(Deklarasi form wajib & faktual — bukan teks pemasaran publik, jadi aman.)*

---

## 4. Content Rating (kuesioner IARC)
Jawab semua kategori sensitif dengan **No** (tanpa kekerasan, seksual, judi, narkoba, dll).
- App = game latihan mental math. **Tidak ada** konten matang.
- **Hasil rating yang diharapkan:** **Everyone / Semua Umur.**

---

## 5. Target Audience & Content  ⚠️ (PALING KRUSIAL untuk MathBlow)
- **Target age group:** centang **18 and over** SAJA + aktifkan **Restrict Minor Access**.
- **Jangan** centang kelompok usia di bawah 18 → memicu **Families Policy** (kategori sensitif, review lambat).
- **Appeal to children?** → **No.**

> 🔴 **Risiko #1 MathBlow = reklasifikasi child-directed.** Game matematika rawan dianggap menarik bagi
> anak. Pertahankan estetika **dewasa-minimalis** (dark, font geometrik, tanpa mascot/kartun/warna pelangi)
> agar Google **tidak menimpa** deklarasi 18+ → ini penentu kecepatan review.

---

## 6. Financial / fitur sensitif lain
| Form | Jawaban |
|---|---|
| Financial features | **Tidak** (bukan app keuangan) |
| Government app | **No** |
| Health / medical | **No** |
| News app | **No** |
| Pembuatan akun di app | **Tidak ada** — akses langsung tanpa pendaftaran |
| App access (kredensial untuk reviewer) | **Tidak perlu** — seluruh fitur dapat diakses langsung |

---

## 7. Ringkasan "profil bersih"
| Sinyal review | Status MathBlow |
|---|---|
| Izin sistem | **0** |
| Data dikumpulkan/dibagikan | **None** |
| Monetisasi | **Gratis** (tanpa IAP) |
| Child-directed | **Tidak** (18+ + Restrict Minor Access) |
| Minimum functionality | **Kaya fitur** (10 skill × 3 level, statistik, achievement) |

> Profil bersih + audiens 18+ tegas = peluang lolos review tercepat. Catatan jujur: risiko child-directed
> bersifat tail-risk yang tak bisa dinolkan untuk app matematika (lihat SARAN_TIM).
