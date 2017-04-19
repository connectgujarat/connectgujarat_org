package profilecom.connectgujarat;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.sendbird.android.SendBird;

import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import profilecom.connectgujarat.gcm.RegistrationIntentService;


@ReportsCrashes(formKey = "", // will not be used
        mailTo = "anandmr062011.@gmail.com",
        customReportContent = {ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA,
                ReportField.STACK_TRACE, ReportField.LOGCAT},
        mode = ReportingInteractionMode.TOAST,
        resToastText = profilecom.connectgujarat.R.string.crash)

public class ConnectGujurat extends Application {


    @Override
    public void onCreate() {
        super.onCreate();



        // The following line triggers the initialization of ACRA
         // ACRA.init(this);

        initAppSdks();

    }

    private void initAppSdks(){

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        printKeyHash();

        SendBird.init(appsId, this);

        //Start GCM Service.
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }

    private static final String appsId =
            //"AE15064E-A510-4EE4-9AED-4E4230871392";//old
            //"32E89024-55EA-4A99-890D-DD8DDEBD8C68";//new
            "044C5E31-D21E-4626-828F-A5E5FFDC8CA6";

    public void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().
                    getPackageInfo("profilecom.connectgujarat", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        SendBird.disconnect(null);

    }
}
