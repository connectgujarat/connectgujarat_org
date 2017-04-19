package profilecom.connectgujarat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import profilecom.connectgujarat.R;
import profilecom.connectgujarat.Services.CommentAddService;
import profilecom.connectgujarat.Services.CommentDetails;
import profilecom.connectgujarat.Services.CommentRetreiveService;
import profilecom.connectgujarat.chat.Helper;

public class NewsDetailsActivity extends AppCompatActivity implements
        TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {

    public static List<CommentDetails> allCommentsFromServer = new ArrayList<CommentDetails>();
    static String author = "";
    static int postId = -99;

    public static final String MY_LOCAL_ACTION = "my_action";
    UpdateNotifier updateNotifier;
    //TODO implement scroll, hide/unhide toolbar
    TextView textNewsHeadline;
    TextView textDate;
    TextView textBy;
    ImageView newsImageView;
    NewsDetailsListAdapter newsDetailsListAdapter;
    ListView mNewsListView;
    TextView content;
    TextView category_name;

    RelativeLayout leave_comment_rl;
    TextView leave_comment_name, leave_comment_email, leave_comment_your_comment;
    FloatingActionButton add_comment_fab;

    Button leave_comment_close;
    Button comment_back_btn;
    private TextToSpeech tts;

    Toolbar newDetailsToolbar;
    ScrollView scrollViewDetails;
    boolean speak;
    ProgressBar progressBar1;
    ActionBar ab;
    ListView listview_comment;
    Intent intent = null;

    LinearLayout ll_comment;

    void togglePanel(){
        ll_comment.setVisibility(View.VISIBLE);
        add_comment_fab.setImageResource(R.drawable.send);

    }
    String res2 ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details_layout);

         SP("movetoDetails", "true");

        updateNotifier = new UpdateNotifier();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MY_LOCAL_ACTION);
        registerReceiver(updateNotifier, intentFilter);

        postId = getIntent().getExtras().getInt("postId");

        scrollViewDetails = (ScrollView) findViewById(R.id.scrollViewDetails);
        if (tts == null)
            tts = new TextToSpeech(this, this);

        SimpleDateFormat formatter;
        SimpleDateFormat formatter2;

        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter2 = new SimpleDateFormat("EEE, dd MMM yyyy");

        ll_comment = (LinearLayout) findViewById(R.id.ll_comment);


        add_comment_fab = (FloatingActionButton) findViewById(R.id.add_comment_fab);
        leave_comment_rl = (RelativeLayout) findViewById(R.id.leave_comment_rl);
        leave_comment_name = (TextView) findViewById(R.id.leave_comment_name);
        leave_comment_email = (TextView) findViewById(R.id.leave_comment_email);
        leave_comment_your_comment = (TextView) findViewById(R.id.leave_comment_your_comment);

        add_comment_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_comment.setVisibility(View.VISIBLE);

                add_comment_fab.setImageResource(R.drawable.send);

                if (!leave_comment_name.getText().toString().equals("") &&
                        !leave_comment_email.getText().toString().equals("") &&
                        !leave_comment_your_comment.getText().toString().equals("")) {

                    if (android.util.Patterns.EMAIL_ADDRESS.
                            matcher(leave_comment_email.getText().toString()).matches()) {
                        add_comment_fab.setImageResource(R.drawable.send);

                        if (isNetworkAvailable()) {

                            if (getSP("author_name").equals("") && getSP("author_email").equals("")) {

                                SP("author_name", leave_comment_name.getText().toString());
                                SP("author_email", leave_comment_email.getText().toString());

                              //  add_comment_fab.setImageResource(R.drawable.send);

                            } else {
                               // add_comment_fab.setImageResource(R.drawable.send);

                            }

                            intent = new Intent(NewsDetailsActivity.this,
                                    CommentAddService.class);

                            intent.putExtra("postId", postId);
                            intent.putExtra("author_name", leave_comment_name.getText().toString());
                            intent.putExtra("author_email", leave_comment_email.getText().toString());
                            intent.putExtra("content", leave_comment_your_comment.getText().toString());

                            startService(intent);

                            Toast.makeText(NewsDetailsActivity.this,
                                    "Comment has been posted, will be approved soon! ", Toast.LENGTH_LONG).show();

                            togglePanel();

                        } else {
                            networkDialog();
                            togglePanel();
                        }
                    } else {
                        Toast.makeText(NewsDetailsActivity.this, "Invalid email ", Toast.LENGTH_LONG).show();

                    }


                } else {
                    leave_comment_name.setError("Mandatory! ");

                    leave_comment_email.setError("Mandatory! ");

                    leave_comment_your_comment.setError("Mandatory!");

                }

            }
        });



        if (!getSP("author_name").equals("") && !getSP("author_email").equals("")) {
            leave_comment_name.setEnabled(false);
            leave_comment_email.setEnabled(false);

            leave_comment_name.setText(getSP("author_name"));
            leave_comment_email.setText(getSP("author_email"));

        }


        leave_comment_close = (Button) findViewById(R.id.leave_comment_close);
        comment_back_btn = (Button) findViewById(R.id.comment_back_btn);

        // my_child_toolbar is defined in the layout file
        newDetailsToolbar = (Toolbar) findViewById(R.id.newDetailsToolbar);
        newDetailsToolbar.setClickable(true);
        setSupportActionBar(newDetailsToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ab = getSupportActionBar();
        //ab.setDisplayHomeAsUpEnabled(true);
        ab.setCustomView(R.layout.custom_toolbar_details);
        ab.setDisplayShowCustomEnabled(true);
        Typeface roboto = Typeface.createFromAsset(getAssets(),
                "font/Roboto-Regular.ttf");
        //use this.getAssets if you are calling from an Activity
        // txtView.setTypeface(roboto);



   /* TODO swipe up/ down hide/show toolbar
    ab.hide();

        scrollViewDetails.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        ab.show();
                }

                switch(event.getAction()){
                    case MotionEvent.ACTION_SCROLL:

                       // event.get
                        ab.hide();
                }
                return false;
            }
        });
 */

        newDetailsToolbar.findViewById(R.id.i0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Get ListView object from xml
        listview_comment = (ListView) findViewById(R.id.listview_comment);
        listview_comment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                togglePanel();

                return false;
            }
        });

        newDetailsToolbar.findViewById(R.id.detail_post_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.SECOND,cal.get(Calendar.SECOND)+20);
                Date d = cal.getTime();
                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Log.d("commentCurrentime", simpledateformat.format(d).replace(" ", "T"));


                leave_comment_rl.setVisibility(View.VISIBLE);

                Intent intent = new Intent(NewsDetailsActivity.this,
                        CommentRetreiveService.class);

                intent.putExtra("post", postId);
                intent.putExtra("after", simpledateformat.format(d).replace(" ", "T"));
                intent.putExtra("per_page", 99);

                startService(intent);

            }
        });

        final String posturl = getIntent().getExtras().getString("posturl");

        newDetailsToolbar.findViewById(R.id.detail_post_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, posturl);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this site!");
                startActivity(Intent.createChooser(intent, "Share"));

            }
        });

        // speak = false;
        newDetailsToolbar.findViewById(R.id.detail_texttospeech).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!tts.isSpeaking()) {

                    String str = getIntent().getExtras().getString("title")
                            +"                                             "
                            +(getIntent().getExtras().getString("content").split("var "))[0];

                    str =Html.fromHtml(str).toString();

                    Log.d("str","str=> "+str.length());
                    if(str.length()>=4000){
                    String res = str.substring(0, 3996);
                    res2 = str.substring(3997, str.length()-1);
                    speak(res);
                        flag = true;
                    //speak(res2);
                    }else
                    speak(str);
                    (newDetailsToolbar.findViewById(R.id.detail_texttospeech)).setAlpha(1.0f);

                } else {
                    (newDetailsToolbar.findViewById(R.id.detail_texttospeech)).setAlpha(0.3f);

                    tts.stop();
                }


            /*
                if(!speak){
                    (myChildToolbar.findViewById(R.id.detail_texttospeech)).setAlpha(1.0f);
                    speakOut();
                    speak= true;
                   // progressBar1.setVisibility(View.VISIBLE);
                }
                else{
                    speak= false;
                   // progressBar1.setVisibility(View.INVISIBLE);

                    (myChildToolbar.findViewById(R.id.detail_texttospeech)).setAlpha(0.3f);
                    if(tts.isSpeaking()) {
                        tts.stop();
                    }*/
            }
        });

        comment_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leave_comment_rl.setVisibility(View.GONE);
                Helper.hideKeyboard(NewsDetailsActivity.this);


            }
        });

        textNewsHeadline = (TextView) findViewById(R.id.list_news_text);
        textBy = (TextView) findViewById(R.id.list_news_user);
        textDate = (TextView) findViewById(R.id.list_news_date);
        newsImageView = (ImageView) findViewById(R.id.list_news_image);
        content = (TextView) findViewById(R.id.content);
        category_name = (TextView) findViewById(R.id.category_name);

        //Log.d("content",getIntent().getExtras().getString("content"));
        //Log.d("content",(getIntent().getExtras().getString("content").split("var "))[0]);


        textNewsHeadline.setText(Html.fromHtml(
                getIntent().getExtras().getString("title")));
        textNewsHeadline.setTypeface(roboto);

        author = getIntent().getExtras().getString("author");

        textBy.setText("By : " +
                        getIntent().getExtras().getString("author")
        );
        textBy.setTypeface(roboto);


        Date formattedDate = null;
        String finalFormattedDate = "";

        try {
            formattedDate = formatter.parse(getIntent().getExtras().getString("date"));

            //Log.d("datess","act date="+localuoposts.get(position).getDate()
            //     +"---formattedDate"+formattedDate);
            finalFormattedDate = formatter2.format(formattedDate);
            //Log.d("datess","finalFormattedDate date="+finalFormattedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        textDate.setText("Date : " + finalFormattedDate);
        textDate.setTypeface(roboto);

        String url = getIntent().getExtras().getString("url");
        if (url == null)
            url = "http://i.img";
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .noFade()
                .into(newsImageView);

        //String s[] = ;

        content.setText(
                Html.fromHtml((getIntent().getExtras().getString("content").split("var "))[0]));
        content.setTypeface(roboto);

        int catid = getIntent().getExtras().getInt("cat");

        int pos = 1;

        for (int i = 0; i < NewsListActivity.categoriesIds.size(); i++) {
            if (catid == NewsListActivity.categoriesIds.get(i)) {
                pos = i;
                break;
            }
        }

        category_name.setText(NewsListActivity.categoriesNames.get(pos));

        category_name.setTypeface(roboto);

    }


   /* private void speakOut() {
    //    progressBar1.setVisibility(View.VISIBLE);

        //   String text = txtText.getText().toString();


        tts.speak(content.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

        while (!tts.isSpeaking()) {

           // progressBar1.setVisibility(View.GONE);
        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        tts.shutdown();
        Helper.hideKeyboard(this);
        SP("movetoDetails", "");

//        progressBar1.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (tts.isSpeaking())
            tts.stop();

        

        Helper.hideKeyboard(this);

        /*if (tts.isSpeaking()) {
            tts.stop();
            progressBar1.setVisibility(View.INVISIBLE);
            (myChildToolbar.findViewById(R.id.detail_texttospeech)).setAlpha(0.3f);

        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        //	if(tts.isSpeaking())
        // speakOut();

    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();

        unregisterReceiver(updateNotifier);


        /*if (tts.isSpeaking()) {
            tts.stop();
            progressBar1.setVisibility(View.INVISIBLE);
        }*/
    }


    private void speak(String text) {
        if (text != null) {
            HashMap<String, String> myHashAlarm = new HashMap<String, String>();
            myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "SOME MESSAGE");
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, myHashAlarm);
            tts.setSpeechRate(0.8f);

        }
    }

    // Fired after mThs initialization
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setOnUtteranceCompletedListener(this);
        }
    }

    boolean flag = false;
    // It's callback
    public void onUtteranceCompleted(String utteranceId) {
        Log.i("onUtteranceCompleted", "onUtteranceCompleted" + utteranceId);

        if(res2!=null && res2.length()>0&& flag == true) {
            flag = false;
            speak(res2);
        }else{

            (newDetailsToolbar.findViewById(R.id.detail_texttospeech)).setAlpha(0.3f);
            tts.stop();
        }


    }

    private class UpdateNotifier extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {


            int comments = arg1.getIntExtra("comments", -99);


            if (comments == 100)
                LoadAllComments();


            if (comments == 1000)
                LoadAllFailed();

            if (comments == 200)
                CommentAddedSuccessfully();

            if (comments == 404)
                CommentFailedToAdd();


        }
    }

    void LoadAllFailed() {

       networkDialog();
    }

    void CommentAddedSuccessfully() {

       /* allCommentsFromServer.add(0, new CommentDetails(0, 0, author, "", "", ""));
        commentAdapter.notifyDataSetChanged();*/

     /*   Calendar cal = Calendar.getInstance();
        Date d = cal.getTime();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.d("commentCurrentime", simpledateformat.format(d).replace(" ", "T"));

        Intent intent = new Intent(NewsDetailsActivity.this,
                CommentRetreiveService.class);

        intent.putExtra("post", postId);
        intent.putExtra("after", simpledateformat.format(d).replace(" ", "T"));
        intent.putExtra("per_page", 99);

        startService(intent);*/

        ll_comment.setVisibility(View.VISIBLE);

    }

    void CommentFailedToAdd() {

        Toast.makeText(NewsDetailsActivity.this, " Failed to add your comment! ", Toast.LENGTH_LONG).show();
        ll_comment.setVisibility(View.VISIBLE);

    }

    CommentAdapter commentAdapter;

    void LoadAllComments() {
        commentAdapter = new CommentAdapter();
        // Assign adapter to ListView
        listview_comment.setAdapter(commentAdapter);

    }

    private class CommentAdapter extends BaseAdapter {

        LayoutInflater inflater;
        Typeface roboto;
       SimpleDateFormat formatter2,formatter;

        public CommentAdapter() {
            super();

            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter2 = new SimpleDateFormat("dd MMM yyyy HH:mm");

            roboto = Typeface.createFromAsset(getAssets(),
                    "font/Roboto-Regular.ttf");

            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        public int getCount() {
            return allCommentsFromServer.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }


        public View getView(int position, View convertView, ViewGroup parent) {


            View commentView = convertView;
            TextView comment_name;
            TextView comment_content;
            TextView comment_date;

            if (commentView == null) {
                // if it's not recycled, initialize some attributes
                commentView = inflater.inflate(R.layout.comment_item, null);
            }

            comment_name = (TextView) commentView.findViewById(R.id.comment_name);
            comment_content = (TextView) commentView.findViewById(R.id.comment_content);
            comment_date = (TextView) commentView.findViewById(R.id.comment_date);
            comment_content.setTypeface(roboto);

            comment_name.setText(allCommentsFromServer.get(position).getAuthorname());
            comment_name.setTypeface(roboto);

            comment_content.setText(allCommentsFromServer.get(position).getCommentContent());
            comment_content.setTypeface(roboto);


            Date formattedDate = null;
            String finalFormattedDate = "";

            try {
                formattedDate = formatter.parse(allCommentsFromServer.get(position).getDate().replace("T"," "));
                finalFormattedDate = formatter2.format(formattedDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }


            comment_date .setText(finalFormattedDate.toString());
            comment_date.setTypeface(roboto);

            return commentView;
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void networkDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("No Network. Enable network");
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


    private void SP(String key, String value) {
        SharedPreferences sharedpreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private String getSP(String key) {
        SharedPreferences sharedpreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        return sharedpreferences.getString(key, "");
    }

}