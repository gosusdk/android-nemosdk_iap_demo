Nemo SDK for Android (Goodgle Play's billing integration)
========================

FEATURES
--------
* Login
* Billing
* Tracking

INSTALLATION
------------

1. In your root-level (project-level) Gradle file `<project>/build.gradle`, add more plugins dependency to your `build.gradle` file:

```gradle
dependencies {
    // ...
    // google grpc
    classpath "com.google.protobuf:protobuf-gradle-plugin:0.9.1"
    // google service (use firebase tracking): optional
    classpath 'com.google.gms:google-services:4.3.3'
}
```	
2. In your module (app-level) Gradle file `<project>/<app-module>/build.gradle`, add more plugins dependency to your `build.gradle` file:

```gradle
// google service plugin (use firebase tracking): optional
apply plugin: 'com.google.gms.google-services'

android {
  //...
  defaultConfig {
    // ...
    manifestPlaceholders = [
      'appAuthRedirectScheme': '[redirect_uri]'
      'appAuthRedirectCallback': '/callback'
    ]
  }
}
dependencies {
    // ...
    // Nemo Login only
    implementation files('libs/nemosdk_login.aar')
    implementation("com.squareup.okio:okio:3.2.0")
    implementation 'net.openid:appauth:0.11.1'

    // Nemo Billings dependencies & GRPC protobuf
    //for in app billing
    implementation 'com.android.billingclient:billing:6.0.1'
    // GRPC Deps
    implementation 'io.grpc:grpc-okhttp:1.54.1'
    implementation 'io.grpc:grpc-protobuf-lite:1.55.1'
    implementation 'io.grpc:grpc-stub:1.55.1'

    // Nemo Tracking only: Optional
    implementation files('libs/nemosdk_tracking.aar')
    implementation 'com.appsflyer:af-android-sdk:6.3.2'
    implementation 'com.android.installreferrer:installreferrer:2.2'
    implementation platform('com.google.firebase:firebase-bom:31.1.0')
    implementation 'com.google.firebase:firebase-analytics:21.2.0'
    implementation 'com.google.firebase:firebase-messaging:23.1.0'
}
```	
// IF used tracking add plugin
Move config file (google-services.json) into the module (app-level) root directory of your app.
```
app/
  google-services.json
```

3. Add auth_config.json file to folder main/assets
```json
{
  "client_id": "demo-id.nemoverse",
  "redirect_uri": "demo-id.android:/callback",
  "end_session_redirect_uri": "demo-id.android:/callback",
  "authorization_scope": "openid email profile",
  "nemo_url": "https://gid.nemoverse.io",
  "game_client_id": "clientID-billing",
  "sdk_signature": "secrect-key-billng"
}
```


USAGE NEMO LOGIN SDK
--------------------
1. Initialize configuration for NemoSDK
---
```java
  protected void onCreate(Bundle savedInstanceState)()
  {
    // ...
    //Initialize SDK
    NemoSDK nemoSDK = new NemoSDK();

    nemoSDK.sdkInitialize(activity, new IGameOauthListener() {
        @Override
        public void onLoginSuccess(String access_token, String id_token) {
        }

        @Override
        public void onLoginFail(String msg, String code) {
        }

        @Override
        public void onLogoutSuccess() {
        }

        @Override
        public void onLogoutFail() {
        }
    });
  }

  //Add function
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    //...
    if(nemoSDK != null){
        nemoSDK.onActivityResult(requestCode, resultCode, data);
    }
  }
  @Override
  protected void onStart() {
    super.onStart();
    // ...
    if(nemoSDK != null){
        nemoSDK.onStart();
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
    //...
    if(nemoSDK != null){
        nemoSDK.onStop();
    }
  }
  @Override
  protected void onDestroy() {
    super.onDestroy();
    //...
    if(nemoSDK != null){
        nemoSDK.onDestroy();
    }
  }
```

2. Nemo SDK Basic Functions
---
```java
//Login
nemoSDK.login();

//getUserInfo  (return json)
nemoSDK.getUserInfo();

//Logout
nemoSDK.logout();


```
3. Paying on Google Play with NemoSDK
---
```java
public void call_billing()
{
    IAPGameData iapGameData = new IAPGameData();
    iapGameData.roleId = "role-id";
    iapGameData.serverId = "S1";
    iapGameData.amount = "20000";
    iapGameData.productID = "vn.devapps.100kc";
    iapGameData.productName = "100 diamonds";
    iapGameData.extraInfo = "vn.devapps.100kc|2000|xyz";
    iapGameData.orderID = "";
    nemoSDK.showIAP(iapGameData, new IAPDelegate() {
        @Override
        public void onSuccess(String code, String message) {
            
        }

        @Override
        public void onFail(String action, String code, String message) {
          /**
           * Action detail:
           *    - initFailed: order initialization on server failed
           *    - onBillingConnectFailed: google play billing connection failed
           *    - onIapBeforePurchaseFailed: google play billing initialization failed
           *    - onIapPurchaseFailed: payment with google play billing failed
           *    - verifyFailed: Payment verification with server failed
           *    - verifySucceed: Payment verification with server is successful, but can't send diamond
           */
        }
    });
}
```

USAGE NEMO TRACKING SDK
--------------------
To utilize a feature listed above include the appropriate listed below in your `AdroidManifest.xml` file.
```xml
<application android:supportsRtl="true">
 <receiver android:name="com.appsflyer.MultipleInstallBroadcastReceiver" android:exported="true" >
    <intent-filter>
      <action android:name="com.android.vending.INSTALL_REFERRER" />
    </intent-filter>
 </receiver>
 <receiver android:name="com.appsflyer.SingleInstallBroadcastReceiver" android:exported="true" >
  <intent-filter>
	<action android:name="com.android.vending.INSTALL_REFERRER" />
  </intent-filter>
 </receiver>
</application>
```

```java
//Initialize SDK 

Tracking.getInstance(activity).start("af_key");
Tracking.getInstance(activity).trackingStartTrialEventAF()
Tracking.getInstance(activity).trackLoginEventAF()
Tracking.getInstance(activity).trackingStartTrialEventCustomAF(String jsonContent) 
/* example: 
jsonContent = {"event": "event_name", "params": {"key": "value", "key2": "value2"} }
*/
```
