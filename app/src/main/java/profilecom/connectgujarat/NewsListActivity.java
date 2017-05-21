package profilecom.connectgujarat;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import profilecom.connectgujarat.CategoryViews.HorizontalListView;
import profilecom.connectgujarat.DataServices.NewPostCacheManager;
import profilecom.connectgujarat.Services.NewCachingPaginateService;
import profilecom.connectgujarat.Services.NewCategoryIndexService;
import profilecom.connectgujarat.Services.NewGetLatestPostService;
import profilecom.connectgujarat.Services.StoryUploadService;
import profilecom.connectgujarat.chat.ChatActivity;
import profilecom.connectgujarat.chat.Helper;
import profilecom.connectgujarat.chat.SendBirdGroupChannelListActivity;
import profilecom.connectgujarat.gcm.RegistrationIntentService;
import vn.tungdx.mediapicker.MediaItem;
import vn.tungdx.mediapicker.MediaOptions;
import vn.tungdx.mediapicker.activities.MediaPickerActivity;

public class NewsListActivity extends AppCompatActivity
        //implements ListView.OnScrollListener
{
    RelativeLayout arrowlayout, story_layout_rl, contact_layout_rl;
    ImageView arrowup;
    Button story_back, contact_back, story_upload;

    EditText contact_name, contact_email, contact_subject, contact_your_message;
    Button contact_send;

    private static HorizontalListView customHorizontalListView;
    RelativeLayout arrowlayouthll;
    ImageView arrowuphll;

    ActionBar ab;


    ImageView story_video_view;
    ImageView story_image;

    public static final int CATEGORIES_LIST_DOWNLOADED = 1234;
    public static final int OLD_POSTS_DOWNLOADED = 1010;
    public static final int NEW_POSTS_DOWNLOADED = 609;
    public static final int SLIDE_POST = 1514;

    public static ViewPager vertical_viewpager;

    public static NewPostCacheManager cacheManager;
    public static ArrayList<Integer> categoriesIds = new ArrayList<Integer>();
    public static ArrayList<String> categoriesNames = new ArrayList<String>();
    public static ArrayList<Locality> list = new ArrayList<>();

    UpdateNotifier updateNotifier;
    RelativeLayout rlSplash;
    ProgressBar pbSplash, progressBar1;
    TextView uploadding;
    RelativeLayout chatpb;


    RelativeLayout home_page_overlay_rl;
    ImageView home_page_overlay_image;
    TextView home_page_overlay_headline;
    TextView home_page_overlay_date;
    TextView home_page_overlay_category;
    TextView home_page_overlay_user;
    ImageView home_page_overlay_down;
    LinearLayout llll;


    EditText story_your_name, story_your_email, story_your_subject, story_your_story;
    LinearLayout llu;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

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

    PostsFragment postsFragment;
    HomePageFragment homepageFragment;

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // homepageFragment = new HomePageFragment();
        postsFragment = new PostsFragment();

        // adapter.addFrag(homepageFragment, "HomePage");
        adapter.addFrag(postsFragment, "Posts");


        viewPager.setAdapter(adapter);
        // viewPager.requestDisallowInterceptTouchEvent(true);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }




    /*//We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }
    private int STORAGE_PERMISSION_CODE = 23;

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == STORAGE_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }*/


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

    public void successUploaded() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Thank You! Your story/video/audio/image has been successfully submitted !! ");
        alertDialogBuilder.setPositiveButton(" OK ",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arrowlayout.setVisibility(View.INVISIBLE);
                        story_layout_rl.setVisibility(View.INVISIBLE);
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
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
         cacheManager = new NewPostCacheManager(this);


        /*if(isReadStorageAllowed()){
            //If permission is already having then showing the toast
            Toast.makeText(NewsListActivity.this,"You already have the permission",Toast.LENGTH_LONG).show();
            //Existing the method with return
            return;
        }

        //If the app has not the permission then asking for the permission
        requestStoragePermission();*/

        //FirebaseCrash.report(new Exception("My first Android non-fatal error"));

        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        uploadding = (TextView) findViewById(R.id.uploadding);
        llu = (LinearLayout) findViewById(R.id.llu);

        story_your_name = (EditText) findViewById(R.id.story_your_name);
        story_your_email = (EditText) findViewById(R.id.story_your_email);
        story_your_subject = (EditText) findViewById(R.id.story_your_subject);
        story_your_story = (EditText) findViewById(R.id.story_your_story);

        rlSplash = (RelativeLayout) findViewById(R.id.rlSplash);
        pbSplash = (ProgressBar) findViewById(R.id.pbSplash);

        rlSplash.setVisibility(View.VISIBLE);
       // pbSplash.setVisibility(View.VISIBLE);

        chatpb = (RelativeLayout) findViewById(R.id.chatpb);
        chatpb.setVisibility(View.INVISIBLE);

        ((ProgressBar) findViewById(R.id.chatpbar)).getIndeterminateDrawable().
                setColorFilter(Color.parseColor("#EF1C8F"), PorterDuff.Mode.MULTIPLY);


        customHorizontalListView =
                (HorizontalListView) findViewById(R.id.customHorizontalListView);


        cacheManager = new NewPostCacheManager(this);
        //--

        llll = (LinearLayout) findViewById(R.id.llll);
        home_page_overlay_rl = (RelativeLayout) findViewById(R.id.home_page_overlay_rl);
        home_page_overlay_image = (ImageView) findViewById(R.id.home_page_overlay_image);
        home_page_overlay_headline = (TextView) findViewById(R.id.home_page_overlay_headline);
        home_page_overlay_date = (TextView) findViewById(R.id.home_page_overlay_date);
        home_page_overlay_category = (TextView) findViewById(R.id.home_page_overlay_category);
        home_page_overlay_user = (TextView) findViewById(R.id.home_page_overlay_user);
        home_page_overlay_down = (ImageView) findViewById(R.id.home_page_overlay_down);

        //--
        // Checking if we have registered token
        final SharedPreferences sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        final boolean fcmRegistered = sharedPreferences.getBoolean("fcm_registered", false);
        if (!fcmRegistered) {
            startService(new Intent(this, RegistrationIntentService.class));
        }
        if (isNetworkAvailable() && getSP("firstcall").equals("")) {

            SP("firstcall", "true");

            pbSplash.getIndeterminateDrawable().
                    setColorFilter(Color.parseColor("#EF1C8F"), PorterDuff.Mode.MULTIPLY);
            // Get the Drawable custom_progressbar
            // Drawable draw= getDrawable(R.drawable.custom_progressbar);
            // set the drawable as progress drawable
            // pbSplash.setProgressDrawable(draw);


            Intent serviceIntent = new Intent(this, NewCategoryIndexService.class);
            startService(serviceIntent);

        } else if (isNetworkAvailable() && getSP("firstcall").equals("true") ||
                !isNetworkAvailable() && getSP("firstcall").equals("true")) {

            rlSplash.setVisibility(View.VISIBLE);
            // pbSplash.setVisibility(View.VISIBLE);
            pbSplash.getIndeterminateDrawable().
                    setColorFilter(Color.parseColor("#EF1C8F"), PorterDuff.Mode.MULTIPLY);

            //TODO load f1, f2

            EnableHomePage();


        } else {

            //TODO show dialog n close app

            /*try {
                Thread.sleep(2000);

                //finish();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }


        arrowup = (ImageView) findViewById(R.id.arrowup);

        arrowlayout = (RelativeLayout) findViewById(R.id.arrowlayout);
        story_layout_rl = (RelativeLayout) findViewById(R.id.story_layout_rl);
        contact_layout_rl = (RelativeLayout) findViewById(R.id.contact_layout_rl);

        arrowuphll = (ImageView) findViewById(R.id.arrowuphll);

        arrowlayouthll = (RelativeLayout) findViewById(R.id.arrowlayouthll);

        story_back = (Button) findViewById(R.id.story_back);
        story_upload = (Button) findViewById(R.id.story_upload);
        contact_back = (Button) findViewById(R.id.contact_back);

        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.toolbar);
        myChildToolbar.setClickable(true);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ab = getSupportActionBar();
        //ab.setDisplayHomeAsUpEnabled(true);

        ab.setCustomView(R.layout.custom_toolbar_home);
        ab.setDisplayShowCustomEnabled(true);

        myChildToolbar.findViewById(R.id.home_chooser_option).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
               /* Toast.makeText(NewsListActivity.this,
                        "Yet to implement..", Toast.LENGTH_LONG).show();*/

                        arrowlayout.setVisibility(View.VISIBLE);
                        arrowup.setOnClickListener(new View.OnClickListener() {

                            public void onClick(View view1) {
                                arrowlayout.setVisibility(View.INVISIBLE);
                            }


                        });
                    }
                });


        myChildToolbar.findViewById(R.id.home_logo_iv).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //  EnableSlidePage();
                        SlideForLatestPost();

                        // TODO
                        final Animation animShow = AnimationUtils.loadAnimation(NewsListActivity.this, R.anim.view_show);

                        home_page_overlay_rl.startAnimation(animShow);

                        home_page_overlay_rl.setVisibility(View.VISIBLE);


                    }
                });

        arrowlayout.findViewById(R.id.arrowlayout_chat).setOnClickListener
                (new View.OnClickListener() {

                    public void onClick(View view1) {

                        arrowlayout.setVisibility(View.INVISIBLE);
                        userId = getSP("user_id");
                        Log.d("userid-clickchat=", userId);

                        if (isNetworkAvailable()) {
                            if (userId.equals("")) {

                                Intent intent = new Intent(NewsListActivity.this, ChatActivity.class);
                                startActivity(intent);

                            } else {
                                chatpb.setVisibility(View.VISIBLE);
                                connect();

                            }
                        } else {
                            networkDialog();
                        }
                    }
                });


        chatpb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        arrowlayout.findViewById(R.id.arrowlayout_categories).setOnClickListener
                (new View.OnClickListener() {

                    public void onClick(View view1) {
                        arrowlayout.setVisibility(View.INVISIBLE);
                        arrowlayouthll.setVisibility(View.VISIBLE);
                    }
                });

        arrowuphll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrowlayouthll.setVisibility(View.INVISIBLE);
                arrowlayout.setVisibility(View.INVISIBLE);

            }
        });

        arrowlayout.findViewById(R.id.arrowlayout_story).setOnClickListener
                (new View.OnClickListener() {

                    public void onClick(View view1) {
                        arrowlayout.setVisibility(View.INVISIBLE);
                        story_layout_rl.setVisibility(View.VISIBLE);
                    }
                });

        story_back.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view1) {
                arrowlayout.setVisibility(View.VISIBLE);
                story_layout_rl.setVisibility(View.INVISIBLE);
                Helper.hideKeyboard(NewsListActivity.this);

            }
        });
        //  story_upload.setEnabled(true);
        //  story_upload.setClickable(true);
        story_upload.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view1) {
            /*    arrowlayout.setVisibility(View.INVISIBLE);
                story_layout_rl.setVisibility(View.INVISIBLE);*/
                //    story_upload.setEnabled(false);
                //   story_upload.setClickable(false);


                if (isNetworkAvailable()) {


                    //  story_layout_rl.setAlpha(0.95f);

                    Intent intent = new Intent(NewsListActivity.this,
                            StoryUploadService.class);

                    TextView t = ((EditText) findViewById(R.id.story_text_in));
                    if (t.getText() != null || t.getText().length() == 0)
                        decodedTxt = "new story";
                    else
                        decodedTxt = t.getText().toString();


                    if (story_your_name.getText().length() > 0 && story_your_email.getText().length() > 0) {
                        if (android.util.Patterns.EMAIL_ADDRESS.
                                matcher(story_your_email.getText().toString()).matches()) {
                            intent.putExtra("body", "");
                        /*intent.putExtra("image", decodedImg);
                        intent.putExtra("video", decodedVid);
                        intent.putExtra("audio", decodedAud);
                        intent.putExtra("text", decodedTxt);*/
                            intent.putExtra("name", story_your_name.getText().toString());
                            intent.putExtra("email", story_your_email.getText().toString());
                            intent.putExtra("subject", story_your_subject.getText() == null ? " " : story_your_subject.getText().toString());
                            intent.putExtra("story", story_your_story.getText() == null ? " " : story_your_story.getText().toString());
                            progressBar1.setVisibility(View.VISIBLE);
                            uploadding.setVisibility(View.VISIBLE);
                            llu.setVisibility(View.VISIBLE);

                            ((ImageView) findViewById(R.id.story_image)).setImageResource(R.drawable.story_def_image);

                            ((ImageView) findViewById(R.id.story_video_view)).setImageResource(R.drawable.story_def_video);

                            ((ImageView) findViewById(R.id.story_imag3)).setImageResource(R.drawable.story_def_audio);

                            ((ImageView) findViewById(R.id.story_imag4)).setImageResource(R.drawable.story_def_text);

                            ((EditText) findViewById(R.id.story_text_in)).setVisibility(View.GONE);

                            startService(intent);

                        } else
                            story_your_email.setError("Enter valid email id! ");
                    } else {
                        if (story_your_name.getText().length() == 0)
                            story_your_name.setError("Cannot be empty! ");
                        if (story_your_email.getText().length() == 0)
                            story_your_email.setError("Cannot be empty! ");
                    }


                } else {
                    networkDialog();
                }


            }
        });

      /*  story_close.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view1) {
                arrowlayout.setVisibility(View.VISIBLE);
                story_layout_rl.setVisibility(View.GONE);
            }
        });*/

        arrowlayout.findViewById(R.id.arrowlayout_contact).setOnClickListener(
                new View.OnClickListener() {

                    public void onClick(View view1) {
                        arrowlayout.setVisibility(View.INVISIBLE);
                        contact_layout_rl.setVisibility(View.VISIBLE);
                    }

                }
        );


        contact_back.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view1) {
                arrowlayout.setVisibility(View.VISIBLE);
                contact_layout_rl.setVisibility(View.INVISIBLE);
                Helper.hideKeyboard(NewsListActivity.this);

            }

        });
       /* contact_close.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view1) {
                arrowlayout.setVisibility(View.VISIBLE);
                contact_layout_rl.setVisibility(View.GONE);
            }

        });*/


        contact_name = (EditText) findViewById(R.id.contact_name);
        contact_email = (EditText) findViewById(R.id.contact_email);
        contact_subject = (EditText) findViewById(R.id.contact_subject);
        contact_your_message = (EditText) findViewById(R.id.contact_your_message);
        contact_send = (Button) findViewById(R.id.contact_send);
        contact_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!contact_name.getText().toString().equals("") &&
                        !contact_email.getText().toString().equals("") &&
                        !contact_subject.getText().toString().equals("") &&
                        !contact_your_message.getText().toString().equals("")) {

                    if (android.util.Patterns.EMAIL_ADDRESS.
                            matcher(contact_email.getText().toString()).matches()) {

                        if (isNetworkAvailable()) {

                            sendEmail(contact_subject.getText().toString(),
                                    "Sir/Madam,\n\n" +
                                            contact_your_message.getText().toString()
                                            + "\n\nThanks " + contact_name.getText().toString());
                        } else {
                            networkDialog();
                        }
                    } else {
                        Toast.makeText(NewsListActivity.this, "Invalid email ", Toast.LENGTH_LONG).show();

                    }
                    arrowlayout.setVisibility(View.INVISIBLE);


                } else {
                    contact_name.setError("Mandatory! ");

                    contact_email.setError("Mandatory! ");

                    contact_your_message.setError("Mandatory!");

                }


            }
        });


        updateNotifier = new UpdateNotifier();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("MY_ACTION");
        registerReceiver(updateNotifier, intentFilter);

        //initAll();

    }

    String userId = "";

    private void connect() {
        SendBird.connect(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {

                String nickname = "";

                SendBird.updateCurrentUserInfo(userId,
                        getSP("profpic"),
                        new SendBird.UserInfoUpdateHandler() {
                            @Override
                            public void onUpdated(SendBirdException e) {

                                // SharedPreferences.Editor editor =
                                // getPreferences(Context.MODE_PRIVATE).edit();

                                SP("user_id", userId);
                                //editor.commit();


                                Intent intent = new Intent(NewsListActivity.this,
                                        SendBirdGroupChannelListActivity.class);
                                startActivity(intent);
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
                                    // Toast.makeText(MainActivity.this, "" + e.getCode()
                                    // + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (isNetworkAvailable()) {
            SlideForLatestPost();
        }
        EnableSlideForLatestPost();


    }

    public static ArrayList<Locality> localityList;

    private class UpdateNotifier extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {

            int dataPassed = arg1.getIntExtra("newApiPosts", -99);

            // got all categories ids and names,
            // go ahead and download all posts of all categories
            if (dataPassed == CATEGORIES_LIST_DOWNLOADED)
                DownloadAllPostsOfAllCategories();

            // got  all posts of all categories
            // go ahead and display 1st category posts

            if (dataPassed == categoriesIds.size()) {
                // TODO what if no posts
                EnableHomePage();
                // PostsFragment.LoadFirstCategoryPosts();
            }

            // load new posts
            if (dataPassed == NEW_POSTS_DOWNLOADED)
                postsFragment.LoadSwipedPosts();

            // load old posts
            if (dataPassed == OLD_POSTS_DOWNLOADED)
                postsFragment.LoadPaginatedPosts();


            if (dataPassed == SLIDE_POST) {

                // localityList = arg1.getParcelableArrayListExtra("mydata");

                EnableSlideForLatestPost();
            }

            if (dataPassed == 2216) {
                //story_upload.setEnabled(true);
                //story_layout_rl.setAlpha(1.0f);

                // story_upload.setClickable(true);
                successUploaded();

                progressBar1.setVisibility(View.INVISIBLE);
                uploadding.setVisibility(View.INVISIBLE);
                llu.setVisibility(View.INVISIBLE);
                story_your_name.setText("");
                story_your_email.setText("");
                story_your_subject.setText("");
                story_your_story.setText("");


            }

        }
    }

    private void EnableHomePage() {

        //  EnableSlidePage();

        if(isNetworkAvailable() ) {
           // if (cacheManager.GetHPPost() == null)
                SlideForLatestPost();
        }
            else
                EnableSlideForLatestPost();



        // pbSplash.setVisibility(View.VISIBLE);
        rlSplash.setVisibility(View.VISIBLE);

        categoriesIds = cacheManager.GetAllCategoriesIds();

        categoriesNames = cacheManager.GetAllCategoriesNames();

        customHorizontalListView.setAdapter(
                new HAdapter(cacheManager.getFirstImageForCats(categoriesIds)));
        customHorizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                arrowlayout.setVisibility(View.INVISIBLE);
                arrowlayouthll.setVisibility(View.INVISIBLE);
                postsFragment.updateUI(position, NewsListActivity.this);

                if (home_page_overlay_rl.getVisibility() == View.VISIBLE) {

                    final Animation animHide =
                            AnimationUtils.loadAnimation(NewsListActivity.this, R.anim.view_hide);
                    home_page_overlay_rl.startAnimation(animHide);
                    home_page_overlay_rl.setVisibility(View.GONE);

                }
            }
        });


        vertical_viewpager = (ViewPager) findViewById(R.id.vertical_viewpager);
        setupViewPager(vertical_viewpager);
        //vertical_viewpager.setPageTransformer(false, new DefaultTransformer());

        /*vertical_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position==1)
                    vertical_viewpager.setEnabled(false);
                Log.d("vp onPageScrolled", ""+ position);
            }

            @Override
            public void onPageSelected(int position) {

                if(position==1)
                    vertical_viewpager.setEnabled(false);
                Log.d("vp onPageSelected", ""+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                    vertical_viewpager.setEnabled(false);
                Log.d("vp onPageScrollStateChanged", "" +state);
            }
        });*/

        // pbSplash.setVisibility(View.GONE);
        rlSplash.setVisibility(View.INVISIBLE);

    }


    private void EnableSlidePage() {

        Typeface roboto = Typeface.createFromAsset(getAssets(),
                "font/Roboto-Regular.ttf");

        NewPostCacheManager cacheManager = new NewPostCacheManager(this);
        NewsListActivity.categoriesIds = cacheManager.GetAllCategoriesIds();
        NewsListActivity.categoriesNames = cacheManager.GetAllCategoriesNames();

        home_page_overlay_rl.setVisibility(View.VISIBLE);

        if (NewsListActivity.categoriesIds.size() > 0)
            NewsListActivity.list = cacheManager.
                    GetFirstCatPosts(NewsListActivity.categoriesIds.get(0));


        if (NewsListActivity.list != null && NewsListActivity.list.size() > 0) {
            final Locality home = NewsListActivity.list.get(0);

            Picasso.with(this)
                    .load(home.getAttUrl())
                    .placeholder(R.drawable.placeholder)

                    .noFade()
                    .into(home_page_overlay_image);

            home_page_overlay_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent =
                            new Intent(NewsListActivity.this, NewsDetailsActivity.class);
                    intent.putExtra("title", home.getTitle());
                    intent.putExtra("author", home.getAuthorName());
                    intent.putExtra("date", home.getDate());
                    intent.putExtra("url", home.getAttUrl());
                    intent.putExtra("content", home.getContent());
                    intent.putExtra("cat", home.getCatid());
                    intent.putExtra("posturl", home.getPosturl());
                    intent.putExtra("postId", home.getId());
                    startActivity(intent);


                }
            });

            final Animation animHide = AnimationUtils.loadAnimation(this, R.anim.view_hide);

            llll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    home_page_overlay_rl.startAnimation(animHide);

                    home_page_overlay_rl.setVisibility(View.GONE);


                }
            });

            home_page_overlay_headline.setText(Html.fromHtml(home.getTitle()));
            home_page_overlay_headline.setTypeface(roboto);


            Date formattedDate = null;
            String finalFormattedDate = "";

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat formatter2 = new SimpleDateFormat("EEE, dd MMM yyyy");
            try {
                formattedDate = formatter.parse(home.getDate());
                finalFormattedDate = formatter2.format(formattedDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (formattedDate != null) {
                home_page_overlay_date.setText("Date : " + finalFormattedDate);
                home_page_overlay_date.setTypeface(roboto);
            }


            home_page_overlay_user.setText(home.getAuthorName());
            home_page_overlay_user.setTypeface(roboto);

            int pos = 1;
            for (int i = 0; i < NewsListActivity.categoriesIds.size(); i++) {
                if (home.getCatid() == NewsListActivity.categoriesIds.get(i)) {
                    pos = i;
                    break;
                }
            }

            home_page_overlay_category.setText(NewsListActivity.categoriesNames.get(pos));
            home_page_overlay_category.setTypeface(roboto);

            home_page_overlay_down.setVisibility(View.VISIBLE);

           /* home_page_overlay_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    home_page_overlay_rl.setVisibility(View.GONE);
                }
            });*/

        } else {
            Picasso.with(this)
                    .load(R.drawable.placeholder)
                    .noFade()
                    .into(home_page_overlay_image);
        }

    }

    private void SlideForLatestPost() {

        Intent intent = new Intent(this, NewGetLatestPostService.class);
        intent.putExtra("count", 1);

        startService(intent);
    }

    Locality home;

    private void EnableSlideForLatestPost() {


        NewsListActivity.categoriesIds = cacheManager.GetAllCategoriesIds();
        NewsListActivity.categoriesNames = cacheManager.GetAllCategoriesNames();


        home_page_overlay_rl.setVisibility(View.VISIBLE);


        //if we are offline and have recent home post
        Typeface roboto = Typeface.createFromAsset(getAssets(),
                "font/Roboto-Regular.ttf");
        home_page_overlay_headline.setText("Headline");
        home_page_overlay_headline.setTypeface(roboto);
        Date formattedDate = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
            home_page_overlay_date.setText("Date : " + dateFormat.format(date));
            home_page_overlay_date.setTypeface(roboto);
        if (cacheManager.GetHPPost() != null) {
            home = cacheManager.GetHPPost();

            setUpHomePage(home);

        } /*else
            //if we are online and we get first time home post
            if (localityList != null && localityList.size() > 0) {
                home = localityList.get(0);
                cacheManager.SaveHPPost(home);

                Log.d("hh", "." + home.toString());

                setUpHomePage(home);

            }*/ else {
                Picasso.with(this)
                        .load(R.drawable.placeholder)
                        .noFade()
                        .into(home_page_overlay_image);
            }
    }

    private void setUpHomePage(final Locality home) {

        Typeface roboto = Typeface.createFromAsset(getAssets(),
                "font/Roboto-Regular.ttf");

        Picasso.with(this)
                .load(home.getAttUrl())
                .placeholder(R.drawable.placeholder)

                .noFade()
                .into(home_page_overlay_image);

        home_page_overlay_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =
                        new Intent(NewsListActivity.this, NewsDetailsActivity.class);
                intent.putExtra("title", home.getTitle());
                intent.putExtra("author", home.getAuthorName());
                intent.putExtra("date", home.getDate());
                intent.putExtra("url", home.getAttUrl());
                intent.putExtra("content", home.getContent());
                intent.putExtra("cat", home.getCatid());
                intent.putExtra("posturl", home.getPosturl());
                intent.putExtra("postId", home.getId());
                startActivity(intent);
            }
        });

        final Animation animHide = AnimationUtils.loadAnimation(this, R.anim.view_hide);

        llll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                home_page_overlay_rl.startAnimation(animHide);

                home_page_overlay_rl.setVisibility(View.GONE);


            }
        });

        home_page_overlay_headline.setText(Html.fromHtml(home.getTitle()));
        home_page_overlay_headline.setTypeface(roboto);


        Date formattedDate = null;
        String finalFormattedDate = "";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter2 = new SimpleDateFormat("EEE, dd MMM yyyy");
        try {
            formattedDate = formatter.parse(home.getDate());
            finalFormattedDate = formatter2.format(formattedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (formattedDate != null) {
            home_page_overlay_date.setText("Date : " + finalFormattedDate);
            home_page_overlay_date.setTypeface(roboto);
        }


        home_page_overlay_user.setText(home.getAuthorName());
        home_page_overlay_user.setTypeface(roboto);

        int pos = 1;
        for (int i = 0; i < NewsListActivity.categoriesIds.size(); i++) {
            if (home.getCatid() == NewsListActivity.categoriesIds.get(i)) {
                pos = i;
                break;
            }
        }

        home_page_overlay_category.setText(NewsListActivity.categoriesNames.get(pos));
        home_page_overlay_category.setTypeface(roboto);

        home_page_overlay_down.setVisibility(View.VISIBLE);

           /* home_page_overlay_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    home_page_overlay_rl.setVisibility(View.GONE);
                }
            });*/

    }

    private void DownloadAllPostsOfAllCategories() {

        //cacheManager.exportDb();

        Intent intent = new Intent(this, NewCachingPaginateService.class);
        Calendar cal = Calendar.getInstance();
        //  cal.set(Calendar.DAY_OF_MONTH, 3);
        Date d = cal.getTime();

        categoriesIds = cacheManager.GetAllCategoriesIds();
        categoriesNames = cacheManager.GetAllCategoriesNames();

        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /*    String formedDate = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" +
                + day
                + "T" + cal.get(Calendar.HOUR_OF_DAY) + "-"
                + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.SECOND);*/

        Log.d("firsttimedate", simpledateformat.format(d).replace(" ", "T"));

        for (int i = 0; i < categoriesIds.size(); i++) {
            intent.putExtra("categoryId", categoriesIds.get(i));
            intent.putExtra("beforeDate", simpledateformat.format(d).replace(" ", "T"));
            intent.putExtra("count", 10);
            startService(intent);
        }

    }


    private class HAdapter extends BaseAdapter {
        LayoutInflater inflater;

        ArrayList<String> allUrs;

        public HAdapter(ArrayList<String> allUrsRec) {
            super();
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.allUrs = allUrsRec;
        }


        public int getCount() {
            return categoriesNames.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }


        public View getView(int position, View convertView, ViewGroup parent) {

            View retVal = convertView;


            if (retVal == null) {
                // if it's not recycled, initialize some attributes
                retVal = inflater.inflate(R.layout.viewitem, null);
            }

            TextView title = (TextView) retVal.findViewById(R.id.category_name);
            ImageView image = (ImageView) retVal.findViewById(R.id.category_image);

            title.setText(Html.fromHtml(categoriesNames.get(position)));

            Picasso.with(NewsListActivity.this)
                    .load(allUrs.get(position))
                    //.placeholder(R.drawable.placeholder)
                    .resize(140, 100)
                    .noFade()
                    .into(image);

            return retVal;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        //TODO get fresh updates automatically


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(updateNotifier);
    }


    //--------------------------------------//
    private List<MediaItem> mMediaSelectedList;

    private void addImages(MediaItem mediaItem, int a) {
      /*  LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item, null);

        ImageView imageView = (ImageView) root.findViewById(R.id.image);
        TextView textView = (TextView) root.findViewById(R.id.textView);*/

        String info = String.format("Original Uri [%s]\nOriginal Path [%s] \n\nCropped Uri [%s] \nCropped Path[%s]", mediaItem.getUriOrigin(), mediaItem.getUriCropped(), mediaItem.getPathOrigin(this), mediaItem.getPathCropped(this));
        //textView.setText(info);

        if (a == 100) {
            ImageView ig = ((ImageView) findViewById(R.id.story_video_view));
            ig.setMaxHeight(48);
            ig.setMaxWidth(72);


            try {
                decodedVid = dataDecoder(mediaItem.getPathOrigin(this));


            } catch (Throwable e) {
                decodedVid = "";
                mediaItem = null;
                Toast.makeText(this, "Capture Video again in a bit lower resolution!", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Capture Video again in a bit lower resolution!", Toast.LENGTH_LONG).show();
            }
            if (mediaItem == null || mediaItem.getUriCropped() == null) {

                Glide.with(this).load(mediaItem.getUriOrigin()).into(ig);
            } else {
                Glide.with(this).load(mediaItem.getUriCropped()).into(ig);
            }
        }
        if (a == 101) {

            if (mediaItem.getUriCropped() == null) {
                Glide.with(this).load(mediaItem.getUriOrigin()).into(((ImageView) findViewById(R.id.story_image)));
            } else {
                Glide.with(this).load(mediaItem.getUriCropped()).into(((ImageView) findViewById(R.id.story_image)));
            }
            decodedImg = dataDecoder(mediaItem.getPathOrigin(this));

        }

      /*  LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 5;
        mLinearLayout.addView(root, params);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {


            if (requestCode == 100) {
                if (resultCode == RESULT_OK) {
                    mMediaSelectedList = MediaPickerActivity
                            .getMediaItemSelected(data);
                    if (mMediaSelectedList != null) {
                        for (MediaItem mediaItem : mMediaSelectedList) {
                            addImages(mediaItem, 100);
                            Log.d(",..", mediaItem.getUriOrigin().toString());

                        }
                    } else {
                        //Log.e(TAG, "Error to get media, NULL");
                    }
                }
            }

            if (requestCode == 101) {
                if (resultCode == RESULT_OK) {
                    mMediaSelectedList = MediaPickerActivity
                            .getMediaItemSelected(data);
                    if (mMediaSelectedList != null) {
                        for (MediaItem mediaItem : mMediaSelectedList) {
                            addImages(mediaItem, 101);
                            Log.d(",..", mediaItem.getUriOrigin().toString());

                        }
                    } else {
                        //Log.e(TAG, "Error to get media, NULL");
                    }
                }
            }

           /* if (resultCode == RESULT_OK) {
                if (requestCode == RESULT_LOAD_IMG && null != data) {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    ImageView imgView = (ImageView) findViewById(R.id.story_image);
                    // Set the Image in ImageView after decoding the String
                    imgView.setImageBitmap(BitmapFactory
                            .decodeFile(imgDecodableString));

                    decodedImg = dataDecoder(imgDecodableString);


                } //else {
                    //Toast.makeText(this, "You haven't picked Image",
                 //           Toast.LENGTH_LONG).show();
               // }

                if (requestCode == RESULT_LOAD_VID && null != data) {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    decodedVid = dataDecoder(imgDecodableString);

                    *//*
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Video.Media.DATA};

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();*//*


                    // InputStream i = getAssets().open("k.mp4");
                    // Log.d("--","--"+i.toString()) ;

                    // String uriPath = "android.resource://"+"wirecamp.gujuratconnect"+"/"+R.raw.k;

                    // final VideoView imgView = (VideoView) findViewById(R.id.story_video_view);
                    // Set the Image in ImageView after decoding the String
                    // imgView.setVideoPath(i.toString());
                    //  imgView.setVideoURI(selectedImage);


                } *//*else {
                    Toast.makeText(this, "You haven't picked Video",
                            Toast.LENGTH_LONG).show();
                }*//*

                if (requestCode == RESULT_LOAD_AUD && null != data) {
                    String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
                    Uri selectedImage = data.getData();
                    Log.d("audio",selectedImage.toString());
                    String[] STAR = { "*" };

                    String[] filePathColumn = {MediaStore.Audio.Media.DATA};

                   *//* // Get the cursor
                    Cursor cursor = //managedQuery(selectedImage,STAR,selection, null, null );
                    getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    for(int i=0;i<cursor.getColumnCount();i++)
                    Log.d("cursor",".."+ cursor.getString(i));
                    //Log.d("curs",cursor.getString(1));

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();

                    decodedAud = dataDecoder(imgDecodableString);*//*

                    if(selectedImage!=null)
                        decodedAud = selectedImage.toString();
                    else
                        decodedAud = "--";


                }*/



               /* if (resultCode == RESULT_OK) {
                    if (requestCode == RESULT_LOAD_VID) {
                        Uri selectedImageUri = data.getData();

                        // OI FILE Manager
                        String filemanagerstring = selectedImageUri.getPath();

                        // MEDIA GALLERY
                        String selectedImagePath = getPath(selectedImageUri);
                        if (selectedImagePath != null) {
                            VideoView imgView = (VideoView) findViewById(R.id.story_video_view);
                            Log.d("selectedImagePath","selectedImagePath"+selectedImagePath);
                            // Set the Image in ImageView after decoding the String
                            imgView.setVideoPath(selectedImagePath);


                        }
                    }
                }
            }*/

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    private String dataDecoder(String file) {

        // String f[] = file.split("external/");
        File tempFile = new File(file/*Environment.getExternalStorageDirectory()+"/"+f[1]*/);
        String encodedString = null;

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(tempFile);
        } catch (Exception e) {
            // TODO: handle exception
        }
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
        // Log.i("encodedString=", "encodedString=" + encodedString);
        return encodedString;

    }

    protected void sendEmail(String subject, String content) {
        Log.i("Send email", "");
        //String[] TO = {"admin@connectgujurat.com"};
        //String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        boolean flag = false;
        PackageManager pm = getPackageManager();
        Intent tempIntent = new Intent(Intent.ACTION_SEND);
        tempIntent.setType("*/*");
        List<ResolveInfo> resInfo = pm.queryIntentActivities(tempIntent, 0);
        for (int i = 0; i < resInfo.size(); i++) {
            ResolveInfo ri = resInfo.get(i);
            if (ri.activityInfo.packageName.contains("android.gm")) {
                emailIntent.setComponent(new ComponentName(ri.activityInfo.packageName, ri.activityInfo.name));

                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("application/octet-stream");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"admin@connectgujurat.com"});
                //emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, content);

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    contact_layout_rl.setVisibility(View.INVISIBLE);
                    //finish();
                    Log.d("Finished sending email", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(NewsListActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
                flag = true;
                break;
            }
        }

        if (flag == false) {


            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("application/octet-stream");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"admin@connectgujurat.com"});
            //emailIntent.putExtra(Intent.EXTRA_CC, CC);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, content);


            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                contact_layout_rl.setVisibility(View.INVISIBLE);

                Log.d("Finished sending email", "");
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(NewsListActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }


        }
    }

    public void loadImagefromGallery(View view) {
       /* // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);*/

        MediaOptions.Builder builder = new MediaOptions.Builder();
        MediaOptions options = null;
        options = MediaOptions.createDefault();

        if (options != null) {
            // clearImages();

            MediaPickerActivity.open(this, 101, options);
        }

    }

    private static int RESULT_LOAD_IMG = 1;
    private static int RESULT_LOAD_VID = 2;
    private static int RESULT_LOAD_AUD = 3;
    String imgDecodableString = "";
    public static String decodedImg = "";
    public static String decodedAud = "--";
    public static String decodedVid = "";
    public static String decodedTxt = "";

    public void loadVideofromGallery(View view) {
      /*  // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(*//*Intent.ACTION_PICK,
                MediaStore.Video.Media.INTERNAL_CONTENT_URI*//*);
        galleryIntent.setType("video*//*");
        galleryIntent.setAction(Intent.ACTION_PICK);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_VID);*/
        MediaOptions.Builder builder = new MediaOptions.Builder();
        MediaOptions options = null;

        //options = builder.selectVideo().canSelectMultiVideo(true).build();
        options = builder.selectVideo().setMaxVideoDuration(10 * 1000).build();
        if (options != null) {
            // clearImages();

            MediaPickerActivity.open(this, 100, options);
        }
    }

    public void loadAudiofromGallery(View view) {
        /*// Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent();
        //new Intent(Intent.ACTION_PICK,MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
        galleryIntent.setType("audio*//*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_AUD);*/


        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Audio "), RESULT_LOAD_AUD);
    }

    public void inputTextFromTextView(View view) {
        ((ImageView) findViewById(R.id.story_imag4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((EditText) findViewById(R.id.story_text_in)).setVisibility(View.VISIBLE);
            }
        });
    }

}