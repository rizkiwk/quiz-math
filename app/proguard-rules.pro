# ── MathBlow R8 / ProGuard rules ───────────────────────────────────────────

# Room: entitas & DAO diakses via refleksi oleh generated code
-keep class id.quiz.mathblow.data.local.** { *; }
-keep class androidx.room.** { *; }
-dontwarn androidx.room.paging.**

# kotlinx.serialization — route navigation type-safe & serializer
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class **$$serializer { *; }
-keepclasseswithmembers class id.quiz.mathblow.ui.navigation.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class id.quiz.mathblow.ui.navigation.**$$serializer { *; }

# Kotlin metadata & coroutines
-keep class kotlin.Metadata { *; }
-dontwarn kotlinx.coroutines.**

# Konfetti
-dontwarn nl.dionsegijn.konfetti.**
