# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class com.technonext.payment.view.WebActivity {
   public *;
}

-keepclassmembers class com.technonext.payment.* {
   public *;
}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#-keep class com.technonext.payment.adapter.* { *; }
#-keep class com.technonext.payment.api.* { *; }
#-keep class com.technonext.payment.di.* { *; }
#-keep class com.technonext.payment.factory.* { *; }
#-keep class com.technonext.payment.fragment.* { *; }
#-keep class com.technonext.payment.model.* { *; }
#-keep class com.technonext.payment.receiver.* { *; }
#-keep class com.technonext.payment.repository.* { *; }
#-keep class com.technonext.payment.utils.* { *; }
#-keep class com.technonext.payment.view.* { *; }
#-keep class com.technonext.payment.viewmodel.* { *; }

-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes LineNumberTable,SourceFile