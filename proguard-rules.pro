# Jetpack Compose
-keep class androidx.compose.** { *; }

# Kotlin Serialization
-keepclassmembers class ** {
    *** Companion;
}
-keepclasseswithmembers class ** {
    kotlinx.serialization.SerializationStrategy <;
    kotlinx.serialization.DeserializationStrategy <;
}
-keep,includedescriptorclasses class com.mysynthetic.agent.data.model.**$$serializer { *; }
-keepclasseswithmembers class com.mysynthetic.agent.data.model.** {
    *** *;
}

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclasseswithmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# General
-keep public class com.mysynthetic.agent.MainActivity { *; }
-keep public class com.mysynthetic.agent.** { *; }

# Logging
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
