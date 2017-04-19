package profilecom.connectgujarat;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import profilecom.connectgujarat.R;
import profilecom.connectgujarat.chat.ChatActivity;


/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentSimpleLoginButton extends Fragment {

    private TextView mTextDetails;
    private CallbackManager mCallbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;

   static URL imageURL;
    public static String getFacebookProfilePicture(String userID){
        try {
             imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
       /* Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return imageURL.toString();
    }


   private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("VIVZ", "onSuccess");
            /*Profile profile = null;

            if(Profile.getCurrentProfile() == null) {
                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                        // profile2 is the new profile
                        profile = profile2;
                        Log.v("facebook - profile", profile2.getFirstName());
                        mProfileTracker.stopTracking();
                    }
                };
                // no need to call startTracking() on mProfileTracker
                // because it is called by its constructor, internally.
            }
            else {
                 profile = Profile.getCurrentProfile();
                Log.v("facebook - profile", profile.getFirstName());
            }

          //  AccessToken accessToken = loginResult.getAccessToken();
            // Profile profile = Profile.getCurrentProfile();

            //Log.d("prof",profile.toString());
            Log.d("id",profile.getId());

            //https://graph.facebook.com/1177799048933718/picture?type=large
           // Bitmap bitmap  = getFacebookProfilePicture(profile.getId());
            String profilePicturePath  = getFacebookProfilePicture(profile.getId());

            mTextDetails.setText(constructWelcomeMessage(profile));

           mButtonLogin.setVisibility(View.INVISIBLE);
            Log.d("profile-name",profile.getName());
            Log.d("profilePicturePath",profilePicturePath);


            if(profile !=null)
                MainActivity.fBlistener.done(profile.getName(),profilePicturePath);
            else
                MainActivity.fBlistener.done("j", null);

        */}


        @Override
        public void onCancel() {
            Log.d("VIVZ", "onCancel");
        }

        @Override
        public void onError(FacebookException e) {
            Log.d("VIVZ", "onError " + e);
        }
    };


    public FragmentSimpleLoginButton() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // factory creates object - hides creation logic
        mCallbackManager = CallbackManager.Factory.create();

        // register BR to LBM & starts tracking & BR receives prof change, token change intent broadcast
        setupTokenTracker();
        setupProfileTracker();

        mTokenTracker.startTracking();
        mProfileTracker.startTracking();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_simple_login_button, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupTextDetails(view);
        setupLoginButton(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        mTextDetails.setText(constructWelcomeMessage(profile));
    }

    @Override
    public void onStop() {
        super.onStop();
        mTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if you don't add following block,
        // your registered `FacebookCallback` won't be called
       /* if (mCallbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }*/
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setupTextDetails(View view) {
        mTextDetails = (TextView) view.findViewById(R.id.text_details);
    }

    private void setupTokenTracker() {
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.d("setupTokenTracker", "" + currentAccessToken);
            }
        };
    }

    private void setupProfileTracker() {
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                if(currentProfile!=null) {
                    Log.d("setupProfileTracker", "" + currentProfile.getId());


                    String profilePicturePath = getFacebookProfilePicture(currentProfile.getId());

                    if (currentProfile != null)
                        ChatActivity.fBlistener.done(currentProfile.getName(), profilePicturePath);
                    else
                        ChatActivity.fBlistener.done("-", "-");

                    //  mTextDetails.setText(constructWelcomeMessage(currentProfile));
                }
            }
        };
    }

    public boolean isPackageExisted(String targetPackage){
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = getActivity().getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }

    LoginButton mButtonLogin;
    private void setupLoginButton(View view) {
         mButtonLogin = (LoginButton) view.findViewById(R.id.login_button);
        mButtonLogin.setFragment(this);
        mButtonLogin.setVisibility(View.VISIBLE);

      /*  if(isPackageExisted("com.facebook.katana")) {

            //  mButtonLogin.setCompoundDrawables(null, null, null, null);

            mButtonLogin.setReadPermissions("user_friends");
            mButtonLogin.registerCallback(mCallbackManager, mFacebookCallback);
        }else{
            Toast.makeText(getActivity(), "Install facebook app! ", Toast.LENGTH_LONG).show();
        }*/
    }

    public String constructWelcomeMessage(Profile profile) {
        StringBuffer stringBuffer = new StringBuffer();
        if (profile != null) {
            stringBuffer.append("Welcome " + profile.getName());
        }
        Log.d("constructWelcomeMessage", stringBuffer.toString()+"..");
        return stringBuffer.toString();
    }

}
