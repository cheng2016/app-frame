-keepattributes Signature
-keepattributes *Annotation*
-keep class com.cds.iot.data.dto.** { *; }
-dontwarn com.tencent.mm.opensdk.**
-keep class com.tencent.mm.opensdk.** { *; }
-keep class com.google.mlkit.** { *; }
-dontwarn com.google.mlkit.**
-keep class androidx.camera.** { *; }

