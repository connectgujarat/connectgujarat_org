package profilecom.connectgujarat.chat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import profilecom.connectgujarat.FragmentSimpleLoginButton;
import profilecom.connectgujarat.R;

/**
 * SendBird Android Sample UI
 */
public class ChatActivity extends AppCompatActivity implements FBlistener, GoogleApiClient.OnConnectionFailedListener{

    private SignInButton btnSignIn;
    public static String VERSION = "3.0.2.0";
    private FragmentManager mFragmentManager;

    public static FBlistener fBlistener;
    //public static Bitmap bitmap;

    public static String profilePicturePath;
    //https://developers.facebook.com/apps/1427233657290225/settings/

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void networkDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Check network connectivity!! ");
        alertDialogBuilder.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

       /* alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });*/

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void done(String name, String profilePicturePat) {
        if(!name.equals("")){

            pbchat.setVisibility(View.VISIBLE);


            sUserId = name;
            ((EditText) findViewById(R.id.etxt_user_id)).setText(sUserId);

            //SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
            SP("user_id", sUserId);
            SP("profpic", profilePicturePat);
           // editor.commit();

            profilePicturePath = profilePicturePat;

            ((EditText) findViewById(R.id.etxt_user_id)).setEnabled(false);

            connect();
        }


    }

    private enum State {DISCONNECTED, CONNECTING, CONNECTED}

    /**
     * To test push notifications with your own appId, you should replace google-services.json with yours.
     * Also you need to set Server API Token and Sender ID in SendBird dashboard.
     * Please carefully read "Push notifications" section in SendBird Android documentation
     */
    private static final String appId = "AE15064E-A510-4EE4-9AED-4E4230871392";
    //= "A7A2672C-AD11-11E4-8DAA-0A18B21C2D82"; /* Sample SendBird Application */

    public static String sUserId;
    private String mNickname;
    public static final int INDEX_SIMPLE_LOGIN = 0;
    public static final int INDEX_CUSTOM_LOGIN = 1;
    ImageView fb_login;

    //mydevtestingacc@gmail.com
    private static final String STATE_SELECTED_FRAGMENT_INDEX = "selected_fragment_index";
    public static final String FRAGMENT_TAG = "fragment_tag";

    private void toggleFragment(int index) {
        Fragment fragment = mFragmentManager.findFragmentByTag(FRAGMENT_TAG);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        switch (index){
            case INDEX_SIMPLE_LOGIN:
                transaction.replace(android.R.id.content, new FragmentSimpleLoginButton(),FRAGMENT_TAG);
                break;
           /* case INDEX_CUSTOM_LOGIN:
                transaction.replace(android.R.id.content, new FragmentCustomLoginButton(),FRAGMENT_TAG);
                break;*/
        }
        transaction.commit();


    }

    /**
    * Call this method inside onCreate once to get your hash key
    */

    ProgressBar pbchat;
    TextView tvchat;
    View viewchat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        pbchat = (ProgressBar) findViewById(R.id.pbchat);
        pbchat.getIndeterminateDrawable().
                setColorFilter(Color.parseColor("#EF1C8F"), PorterDuff.Mode.MULTIPLY);
        //tvchat = (TextView) findViewById(R.id.tvchat);
      //  viewchat =  findViewById(R.id.viewchat);


        if(getSP("SendBirdGCMToken").length()==0){
        }


        fBlistener = (ChatActivity) this;

        fb_login = (ImageView) findViewById(R.id.fb_login);

        fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toggleFragment(INDEX_SIMPLE_LOGIN);

            }
        });


        mFragmentManager = getSupportFragmentManager();

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.toolbar);
//        myChildToolbar.setClickable(true);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        //ab.setDisplayHomeAsUpEnabled(true);

        ab.setCustomView(R.layout.custom_toolbar_chat_home);
        ab.setDisplayShowCustomEnabled(true);

        sUserId = getSP("user_id");
        mNickname = getSP("nickname");

        //SendBird.init(appId, this);

        ((Button) findViewById(R.id.btn_relogin)).setEnabled(false);

        ((Button) findViewById(R.id.btn_relogin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, SendBirdGroupChannelListActivity.class);
                startActivity(intent);
            }
        });

        /**
         * Start GCM Service.
         */
       /* Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);*/

        ((EditText) findViewById(R.id.etxt_user_id)).setText(sUserId);

        if(!sUserId.equals("")){

            if (isNetworkAvailable()) {

                connect();
            } else {
                networkDialog();
            }

        }else{
            toggleFragment(INDEX_SIMPLE_LOGIN);
        }

       /* ((EditText) findViewById(R.id.etxt_user_id)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                sUserId = s.toString();
            }
        });*/

        /*((EditText) findViewById(R.id.etxt_nickname)).setText(mNickname);
        ((EditText) findViewById(R.id.etxt_nickname)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mNickname = s.toString();
            }
        });*/

        findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btn = (Button) view;
                if (btn.getText().equals("Proceed")) {
                    connect();
                } else {
                    connect();
                }

                Helper.hideKeyboard(ChatActivity.this);
            }
        });

       /* findViewById(R.id.btn_open_channel_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SendBirdOpenChannelListActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_group_channel_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SendBirdGroupChannelListActivity.class);
                startActivity(intent);
            }
        });*/

        setState(State.DISCONNECTED);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());
    }

    private static final int RC_SIGN_IN = 007;

    public static GoogleApiClient mGoogleApiClient;

    public void SP(String key, String value) {
        SharedPreferences sharedpreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getSP(String key) {
        SharedPreferences sharedpreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        return sharedpreferences.getString(key, "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // SendBird.disconnect(null);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ((Button) findViewById(R.id.btn_relogin)).setEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * If the minimum SDK version you support is under Android 4.0,
         * you MUST uncomment the below code to receive push notifications.
         */
//        SendBird.notifyActivityResumedForOldAndroids();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /**
         * If the minimum SDK version you support is under Android 4.0,
         * you MUST uncomment the below code to receive push notifications.
         */
//        SendBird.notifyActivityPausedForOldAndroids();
    }

    private void setState(State state) {
        switch (state) {
            case DISCONNECTED:
                ((Button) findViewById(R.id.btn_connect)).setText("Proceed");
                findViewById(R.id.btn_connect).setEnabled(true);
                findViewById(R.id.btn_open_channel_list).setEnabled(false);
                findViewById(R.id.btn_group_channel_list).setEnabled(false);
                ((Button) findViewById(R.id.btn_relogin)).setEnabled(false);
                break;

            case CONNECTING:
              //  ((Button) findViewById(R.id.btn_connect)).setText("Connecting...");
                findViewById(R.id.btn_connect).setEnabled(false);
                findViewById(R.id.btn_open_channel_list).setEnabled(false);
                findViewById(R.id.btn_group_channel_list).setEnabled(false);
                break;

            case CONNECTED:
                ((Button) findViewById(R.id.btn_connect)).setText("Proceed.");
                findViewById(R.id.btn_connect).setEnabled(true);
                findViewById(R.id.btn_open_channel_list).setEnabled(true);
                findViewById(R.id.btn_group_channel_list).setEnabled(true);
                findViewById(R.id.btn_relogin).setEnabled(false);

               // tvchat.setVisibility(View.INVISIBLE);
                //viewchat.setVisibility(View.INVISIBLE);
                //pbchat.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(ChatActivity.this, SendBirdGroupChannelListActivity.class);
                startActivity(intent);

                break;
        }
    }

    private void connect() {

        //TODO progress bar
        pbchat.setVisibility(View.VISIBLE);
       // tvchat.setVisibility(View.VISIBLE);
       // viewchat.setVisibility(View.VISIBLE);

        Log.d("on connect-userid",".."+sUserId);

        SendBird.connect(sUserId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (e != null) {
                    Toast.makeText(ChatActivity.this, "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    setState(State.DISCONNECTED);
                    return;
                }

                String nickname = mNickname;

                SendBird.updateCurrentUserInfo(sUserId, profilePicturePath,
                        new SendBird.UserInfoUpdateHandler() {
                    @Override
                    public void onUpdated(SendBirdException e) {
                        if (e != null) {
                            Toast.makeText(ChatActivity.this,"" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            setState(State.DISCONNECTED);
                            return;
                        }

                       // SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
                        SP("user_id", sUserId);
                        SP("nickname", mNickname);
                       // editor.commit();



                        setState(State.CONNECTED);
                    }
                });

                if (SendBird.getPendingPushToken() == null) return;

                SendBird.registerPushTokenForCurrentUser(SendBird.getPendingPushToken(),
                        new SendBird.RegisterPushTokenWithStatusHandler() {
                    @Override
                    public void onRegistered
                            (SendBird.PushTokenRegistrationStatus pushTokenRegistrationStatus,
                             SendBirdException e) {
                        if (e != null) {
                           // Toast.makeText(MainActivity.this, "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });

        setState(State.CONNECTING);
    }

   /* private void disconnect() {
        SendBird.disconnect(new SendBird.DisconnectHandler() {
            @Override
            public void onDisconnected() {
                setState(State.DISCONNECTED);
            }
        });
    }*/


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
       // Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("call","call");


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private static final String TAG = ChatActivity.class.getSimpleName();
   // static Uri personPhotoUrl ;

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

          //  Toast.makeText(this, "yaya",Toast.LENGTH_LONG).show();
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();


            if(acct.getPhotoUrl()!=null)
                profilePicturePath = acct.getPhotoUrl().toString();
            else
                profilePicturePath = "http://baxtercoaching.com/wp-content/uploads/2013/12/facebook-default-no-profile-pic-300x300.jpg";
            String email = acct.getEmail();

            sUserId =acct.getDisplayName();


            SP("user_id", acct.getDisplayName());
            SP("profpic", profilePicturePath);
            if (isNetworkAvailable()) {


                try {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                }
                            });
                }catch (Exception e){
                    ;
                }

                connect();


            } else {
                networkDialog();
            }

            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + acct.getPhotoUrl());

           /* txtName.setText(personName);
            txtEmail.setText(email);
            Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);*/

        //    updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
          //  updateUI(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
         //   handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            //showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                   // hideProgressDialog();
                  //  handleSignInResult(googleSignInResult);
                }
            });
        }
    }
}
