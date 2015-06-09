# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:/Users/franklin.michael/AppData/Local/Android/android-studio/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#-keep class android.support.v4.app.** { *; }
#-keep interface android.support.v4.app.** { *; }
#-keepattributes *Annotation*
#-keepattributes JavascriptInterface
#-keep public class com.payu.sdk.ProcessPaymentActivity$PayUJavaScriptInterface
#-keep public class * implements com.payu.sdk.ProcessPaymentActivity$PayUJavaScriptInterface
#-keepclassmembers class com.payu.sdk.ProcessPaymentActivity$PayUJavaScriptInterface {
#    <methods>;
#}
#
#-keepclassmembers class * {
#    @android.webkit.JavascriptInterface <methods>;
#}
#
#-keepclassmembers class com.payu.custombrowser.** {
#    *;
#}
#
#-keepattributes Signature
#
#-keepattributes InnerClasses
#
#
#-optimizations !field/removal/writeonly,!field/marking/private,!class/merging/*,!code/allocation/variable
#
#-optimizationpasses 50

-keep class com.payu.sdk.** {
 *;
}

-keepattributes InnerClasses, Exceptions

-keepclassmembers class * {
 @android.webkit.JavascriptInterface <methods>;
}

-keepclassmembers class com.payu.custombrowser.** {
 *;
}

-keepattributes Signature

-keep class com.payu.sdk.ProcessPaymentFragment {
  protected void startPaymentProcessActivity(com.payu.sdk.PayU.PaymentMode, com.payu.sdk.Params);
}

-keepattributes *Annotation*
-keepattributes JavascriptInterface
-keep public class com.payu.sdk.ProcessPaymentActivity$PayUJavaScriptInterface
-keep public class * implements com.payu.sdk.ProcessPaymentActivity$PayUJavaScriptInterface
-keepclassmembers class com.payu.sdk.ProcessPaymentActivity$PayUJavaScriptInterface {
    <methods>;
}
