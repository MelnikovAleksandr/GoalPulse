-repackageclasses

-renamesourcefileattribute SourceFile

-keepnames @com.squareup.moshi.JsonClass class *
-if @com.squareup.moshi.JsonClass class *
-keep class <1>JsonAdapter {
    <init>(...);
    <fields>;
}
-if @com.squareup.moshi.JsonClass class **$*
-keep class <1>_<2>JsonAdapter {
    <init>(...);
    <fields>;
}

-keepnames class * implements androidx.navigation3.runtime.NavKey
-keepclassmembers class * implements androidx.navigation3.runtime.NavKey {
    <init>(...);
    static ** Companion;
    public static ** INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}
-if class * implements androidx.navigation3.runtime.NavKey
-keep class <1>$$serializer {
    *;
}
