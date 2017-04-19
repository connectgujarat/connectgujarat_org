package profilecom.connectgujarat.chat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.sendbird.android.AdminMessage;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.FileMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.android.UserListQuery;
import com.sendbird.android.UserMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import profilecom.connectgujarat.NewsListActivity;
import profilecom.connectgujarat.R;

public class SendBirdGroupChannelListActivity extends AppCompatActivity {
    private SendBirdGroupChannelListFragment mSendBirdGroupChannelListFragment;

    private View mTopBarContainer;
    private View mSettingsContainer;

    private TabLayout tabLayout;
    public static ViewPager viewPager;

    public ListView mListView;
    private UserListQuery mUserListQuery;
    private SendBirdUserAdapter mAdapter;
   // public static HashSet<String> mSelectedUserIds;
    ActionBar ab;
    static Typeface roboto;


    private void chatKill(){
        SendBird.disconnect(null);
        finish();

        Intent intent = new Intent(SendBirdGroupChannelListActivity.this, NewsListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {

        chatKill();
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

    static String userid ="";

    private void ReconnectOnRefresh() {
        SendBird.connect(getSP("user_id"), new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {


                String nickname = "";

                SendBird.updateCurrentUserInfo(getSP("user_id"),
                        getSP("profpic"),
                        new SendBird.UserInfoUpdateHandler() {
                            @Override
                            public void onUpdated(SendBirdException e) {

                                // SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
                                SP("user_id", getSP("user_id"));
                                //editor.commit();
                                Log.d("ReconnectOnRefresh","ReconnectOnRefresh");

                                setupViewPager((ViewPager) findViewById(R.id.viewpager));
                               /* Intent intent = new Intent(SendBirdGroupChannelListActivity.this, SendBirdGroupChannelListActivity.class);
                                startActivity(intent);*/
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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  overridePendingTransition(R.anim.sendbird_slide_in_from_bottom, R.anim.sendbird_slide_out_to_top);
        setContentView(R.layout.activity_sendbird_group_channel_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

       // mUserListQuery = null;
        ((LinearLayout)findViewById(R.id.logoutchat_ll)).setVisibility(View.INVISIBLE);

        userid = getSP("user_id");

        roboto = Typeface.createFromAsset(getAssets(),
                "font/Roboto-Regular.ttf");

        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.toolbar);
        myChildToolbar.setClickable(true);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ab = getSupportActionBar();
        //ab.setDisplayHomeAsUpEnabled(true);

        ab.setCustomView(R.layout.custom_toolbar_chat);
        ab.setDisplayShowCustomEnabled(true);

        myChildToolbar.findViewById(R.id.chat_text2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout)findViewById(R.id.logoutchat_ll)).setVisibility(View.VISIBLE);

            }
        });

        myChildToolbar.findViewById(R.id.chat_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendBird.disconnect(null);
                ((LinearLayout)findViewById(R.id.logoutchat_ll)).setVisibility(View.INVISIBLE);

                ReconnectOnRefresh();
                ((LinearLayout)findViewById(R.id.logoutchat_ll)).setVisibility(View.INVISIBLE);



            }
        });

        myChildToolbar.findViewById(R.id.chat_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((LinearLayout)findViewById(R.id.logoutchat_ll)).setVisibility(View.INVISIBLE);

            }
        });

        findViewById(R.id.tabs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LinearLayout)findViewById(R.id.logoutchat_ll)).setVisibility(View.INVISIBLE);

            }
        });



        ((TextView)findViewById(R.id.logoutchat)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendBird.disconnect(null);
                LoginManager.getInstance().logOut();

                if(ChatActivity.mGoogleApiClient!=null)
                    ChatActivity.mGoogleApiClient =null;
                   // if(ChatActivity.mGoogleApiClient.isConnected())
               /* Auth.GoogleSignInApi.signOut(ChatActivity.mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                              //  updateUI(false);
                                ChatActivity.mGoogleApiClient.disconnect();
                                ChatActivity.mGoogleApiClient =null;
                            }
                        });*/

                SP("user_id","");
                ((LinearLayout)findViewById(R.id.logoutchat_ll)).setVisibility(View.INVISIBLE);

                finish();

                Intent intent = new Intent(SendBirdGroupChannelListActivity.this, NewsListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
      //  tabLayout.setTabTextColors(R.color.normal_color,R.color.selected_color);

        myChildToolbar.findViewById(R.id.chat_back).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        chatKill();

                      /*  overridePendingTransition
                                (R.anim.sendbird_slide_in_from_top,
                                        R.anim.sendbird_slide_out_to_bottom);*/
                    }
                });


        // initFragment();
        //initUIComponents();

        // Toast.makeText(this, "Long press the channel to hide or leave it.", Toast.LENGTH_LONG).show();
    }

    private static SendBirdGroupChannelListFragment sendBirdGroupChannelListFragment ;

    private static SendBirdUserListActivity sendBirdUserListActivity ;

    private void setupViewPager(final ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        sendBirdGroupChannelListFragment = new SendBirdGroupChannelListFragment();

        sendBirdUserListActivity = new SendBirdUserListActivity() ;

        adapter.addFrag(sendBirdUserListActivity, "Users");
        adapter.addFrag(sendBirdGroupChannelListFragment, "Conversations");

        /*adapter.addFrag(new TwoFragment(), "TWO");
        adapter.addFrag(new ThreeFragment(), "THREE");
        adapter.addFrag(new FourFragment(), "FOUR");
        adapter.addFrag(new FiveFragment(), "FIVE");
        adapter.addFrag(new SixFragment(), "SIX");
        adapter.addFrag(new SevenFragment(), "SEVEN");
        adapter.addFrag(new EightFragment(), "EIGHT");
        adapter.addFrag(new NineFragment(), "NINE");
        adapter.addFrag(new TenFragment(), "TEN");*/

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position==1){

                  //  SendBird.init("AE15064E-A510-4EE4-9AED-4E4230871392", SendBirdGroupChannelListActivity.this);
                   // mUserListQuery = null;

                  //  mAdapter = new SendBirdUserAdapter(SendBirdGroupChannelListActivity.this);
                    //mListView.setAdapter(mAdapter);

                    //mUserListQuery =  SendBird.createUserListQuery();
                     //sendBirdUserListActivity.loadMoreUsers();

                    ((LinearLayout)findViewById(R.id.logoutchat_ll)).setVisibility(View.INVISIBLE);

                  //
                }
            }

            @Override
            public void onPageSelected(int position) {
                ((LinearLayout)findViewById(R.id.logoutchat_ll)).setVisibility(View.INVISIBLE);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

                ((LinearLayout)findViewById(R.id.logoutchat_ll)).setVisibility(View.INVISIBLE);

            }
        });
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


    @Override
    protected void onResume() {
        super.onResume();
        ((LinearLayout)findViewById(R.id.logoutchat_ll)).setVisibility(View.INVISIBLE);

        /**
         * If the minimum SDK version you support is under Android 4.0,
         * you MUST uncomment the below code to receive push notifications.
         */
//        SendBird.notifyActivityResumedForOldAndroids();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((LinearLayout)findViewById(R.id.logoutchat_ll)).setVisibility(View.INVISIBLE);

        /**
         * If the minimum SDK version you support is under Android 4.0,
         * you MUST uncomment the below code to receive push notifications.
         */
//        SendBird.notifyActivityPausedForOldAndroids();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        resizeMenubar();
    }

    private void resizeMenubar() {
        ViewGroup.LayoutParams lp = mTopBarContainer.getLayoutParams();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.height = (int) (28 * getResources().getDisplayMetrics().density);
        } else {
            lp.height = (int) (48 * getResources().getDisplayMetrics().density);
        }
        mTopBarContainer.setLayoutParams(lp);
    }

    @Override
    public void finish() {
        super.finish();
       // overridePendingTransition(R.anim.sendbird_slide_in_from_top, R.anim.sendbird_slide_out_to_bottom);
    }

    private void initFragment() {
        mSendBirdGroupChannelListFragment = new SendBirdGroupChannelListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mSendBirdGroupChannelListFragment)
                .commit();
    }

    private void initUIComponents() {
        mTopBarContainer = findViewById(R.id.top_bar_container);

        mSettingsContainer = findViewById(R.id.settings_container);
        mSettingsContainer.setVisibility(View.GONE);

        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btn_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSettingsContainer.getVisibility() != View.VISIBLE) {
                    mSettingsContainer.setVisibility(View.VISIBLE);
                } else {
                    mSettingsContainer.setVisibility(View.GONE);
                }
            }
        });
/*

        findViewById(R.id.add_comment_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendBirdGroupChannelListActivity.this, SendBirdUserListActivity.class);
                mSendBirdGroupChannelListFragment.startActivityForResult(
                        intent, SendBirdGroupChannelListFragment.REQUEST_INVITE_USERS);
                mSettingsContainer.setVisibility(View.GONE);
            }
        });
*/

        findViewById(R.id.btn_version).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SendBirdGroupChannelListActivity.this)
                        .setTitle("SendBird")
                        .setMessage("SendBird SDK " + SendBird.getSDKVersion())
                        .setPositiveButton("OK", null).create().show();

                mSettingsContainer.setVisibility(View.GONE);
            }
        });

        resizeMenubar();
    }

    public static SendBirdGroupChannelAdapter mSendBirdGroupChannelAdapter;

    public static class SendBirdGroupChannelListFragment extends Fragment {
        private static final String identifier = "SendBirdGroupChannelList";
        private static final int REQUEST_INVITE_USERS = 100;
        private ListView mListView;
        private GroupChannelListQuery mQuery;

        public SendBirdGroupChannelListFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.sendbird_fragment_group_channel_list, container, false);
            initUIComponents(rootView);
            return rootView;
        }

        private void initUIComponents(View rootView) {
            mListView = (ListView) rootView.findViewById(R.id.list);
            mListView.setAdapter(mSendBirdGroupChannelAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    GroupChannel channel = mSendBirdGroupChannelAdapter.getItem(position);
                    Intent intent = new Intent(getActivity(), SendBirdGroupChatActivity.class);
                    intent.putExtra("channel_url", channel.getUrl());
                    startActivity(intent);
                }
            });
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (firstVisibleItem + visibleItemCount >= (int) (totalItemCount * 0.8f)) {
                        loadNextChannels();
                    }
                }
            });
            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    final GroupChannel channel = mSendBirdGroupChannelAdapter.getItem(position);
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Leave")
                            .setMessage("Do you want to leave or hide this channel?")
                            .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    channel.leave(new GroupChannel.GroupChannelLeaveHandler() {
                                        @Override
                                        public void onResult(SendBirdException e) {
                                            if (e != null) {
                                            //    Toast.makeText(getActivity(), "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                          //  Toast.makeText(getActivity(), "Channel left.", Toast.LENGTH_SHORT).show();
                                            mSendBirdGroupChannelAdapter.remove(position);
                                            mSendBirdGroupChannelAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            })
                            .setNeutralButton("Hide", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    channel.hide(new GroupChannel.GroupChannelHideHandler() {
                                        @Override
                                        public void onResult(SendBirdException e) {
                                            if (e != null) {
                                              //  Toast.makeText(getActivity(), "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                         //   Toast.makeText(getActivity(), "Channel hidden.", Toast.LENGTH_SHORT).show();

                                            mSendBirdGroupChannelAdapter.remove(position);
                                            mSendBirdGroupChannelAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Cancel", null).create().show();
                    return true;
                }
            });

            mSendBirdGroupChannelAdapter = new SendBirdGroupChannelAdapter(getActivity());
            mListView.setAdapter(mSendBirdGroupChannelAdapter);
        }

        private void loadNextChannels() {
            if (mQuery == null || mQuery.isLoading()) {
                return;
            }

            if (!mQuery.hasNext()) {
                return;
            }

            mQuery.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler() {
                @Override
                public void onResult(List<GroupChannel> list, SendBirdException e) {
                    if (e != null) {
                       // Toast.makeText(getActivity(), "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mSendBirdGroupChannelAdapter.addAll(list);
                    mSendBirdGroupChannelAdapter.notifyDataSetChanged();

                    if (mSendBirdGroupChannelAdapter.getCount() == 0) {
                        //Toast.makeText(getActivity(), "No channels found.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        private void create(final String[] userIds) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.sendbird_view_group_create_channel, null);
            final EditText chName = (EditText) view.findViewById(R.id.etxt_chname);
            //final CheckBox distinct = (CheckBox) view.findViewById(R.id.chk_distinct);


            GroupChannel.createChannelWithUserIds(Arrays.asList(userIds),
                    true, chName.getText().toString(), null, null,
                    new GroupChannel.GroupChannelCreateHandler() {
                        @Override
                        public void onResult(GroupChannel groupChannel, SendBirdException e) {
                            if (e != null) {
                               // Toast.makeText(getActivity(), "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            mSendBirdGroupChannelAdapter.replace(groupChannel);
                        }

                    });
            /*
            new AlertDialog.Builder(getActivity())
                    .setView(view)
                    .setTitle("Create Group Channel")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton("Cancel", null).create().show();*/
        }

       /* @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_INVITE_USERS) {
                    String[] userIds = data.getStringArrayExtra("user_ids");
                    create(userIds);
                }
            }
        }*/

        @Override
        public void onPause() {
            super.onPause();
            SendBird.removeChannelHandler(identifier);
        }

        @Override
        public void onResume() {
            super.onResume();

            SendBird.addChannelHandler(identifier, new SendBird.ChannelHandler() {
                @Override
                public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
                    if (baseChannel instanceof GroupChannel) {
                        GroupChannel groupChannel = (GroupChannel) baseChannel;
                        mSendBirdGroupChannelAdapter.replace(groupChannel);
                    }
                }

                @Override
                public void onUserJoined(GroupChannel groupChannel, User user) {
                    // Member changed. Refresh group channel item.
                    mSendBirdGroupChannelAdapter.notifyDataSetChanged();
                }

                @Override
                public void onUserLeft(GroupChannel groupChannel, User user) {
                    // Member changed. Refresh group channel item.
                    mSendBirdGroupChannelAdapter.notifyDataSetChanged();
                }
            });

            mSendBirdGroupChannelAdapter.clear();
            mSendBirdGroupChannelAdapter.notifyDataSetChanged();

            mQuery = GroupChannel.createMyGroupChannelListQuery();
            if(mQuery!=null)
            mQuery.setIncludeEmpty(true);
            loadNextChannels();
        }

    }

    public static ArrayList<GroupChannel> mItemListChannel;

    public static class SendBirdGroupChannelAdapter extends BaseAdapter {
        private final Context mContext;
        private final LayoutInflater mInflater;

        public SendBirdGroupChannelAdapter(Context context) {
            mContext = context;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mItemListChannel = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return mItemListChannel.size();
        }

        @Override
        public GroupChannel getItem(int position) {
            return mItemListChannel.get(position);
        }

        public void clear() {
            mItemListChannel.clear();
        }

        public GroupChannel remove(int index) {
            return mItemListChannel.remove(index);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addAll(List<GroupChannel> channels) {
            mItemListChannel.addAll(channels);
        }

        public void replace(GroupChannel newChannel) {
            for (GroupChannel oldChannel : mItemListChannel) {
                if (oldChannel.getUrl().equals(newChannel.getUrl())) {
                    mItemListChannel.remove(oldChannel);
                    break;
                }
            }

            mItemListChannel.add(0, newChannel);
            notifyDataSetChanged();


            Intent intent = new Intent(mContext, SendBirdGroupChatActivity.class);
            intent.putExtra("channel_url", newChannel.getUrl());
            mContext.startActivity(intent);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.sendbird_view_group_channel, parent, false);
                viewHolder.setView("img_thumbnail", convertView.findViewById(R.id.img_thumbnail));
                viewHolder.setView("txt_topic", convertView.findViewById(R.id.txt_topic));
                viewHolder.setView("txt_member_count", convertView.findViewById(R.id.txt_member_count));
                viewHolder.setView("txt_unread_count", convertView.findViewById(R.id.txt_unread_count));
                viewHolder.setView("txt_date", convertView.findViewById(R.id.txt_date));
                viewHolder.setView("txt_desc", convertView.findViewById(R.id.txt_desc));

                convertView.setTag(viewHolder);
            }

            GroupChannel item = getItem(position);
            viewHolder = (ViewHolder) convertView.getTag();

            if(item!=null){
                if(item.getMembers()!= null || item.getMembers().size()!=0){
                    Helper.displayUrlImage(viewHolder.getView("img_thumbnail", ImageView.class),
                        Helper.getDisplayCoverImageUrl(item.getMembers()),true);
                }
            }
            viewHolder.getView("txt_topic", TextView.class).
                    setText(Helper.getDisplayMemberNames(item.getMembers(), false));
            viewHolder.getView("txt_topic", TextView.class).
                    setTypeface(roboto);


            if (item.getUnreadMessageCount() > 0) {
                viewHolder.getView("txt_unread_count", TextView.class).setVisibility(View.VISIBLE);
                viewHolder.getView("txt_unread_count", TextView.class).setText("" + item.getUnreadMessageCount());
                viewHolder.getView("txt_unread_count", TextView.class).setTypeface(roboto);
            } else {
                viewHolder.getView("txt_unread_count", TextView.class).setVisibility(View.INVISIBLE);
            }

            viewHolder.getView("txt_member_count", TextView.class).setVisibility(View.INVISIBLE);
            viewHolder.getView("txt_member_count", TextView.class).setText("" + item.getMemberCount());
            viewHolder.getView("txt_member_count", TextView.class). setTypeface(roboto);
            BaseMessage message = item.getLastMessage();
            if (message == null) {
                viewHolder.getView("txt_date", TextView.class).setText("");
                viewHolder.getView("txt_desc", TextView.class).setText("");
            } else if (message instanceof UserMessage) {
                viewHolder.getView("txt_date", TextView.class).setText(Helper.getDisplayTimeOrDate(mContext, message.getCreatedAt()));
                viewHolder.getView("txt_desc", TextView.class).setText(((UserMessage) message).getMessage());
                viewHolder.getView("txt_desc", TextView.class). setTypeface(roboto);
            } else if (message instanceof AdminMessage) {
                viewHolder.getView("txt_date", TextView.class).setText(Helper.getDisplayTimeOrDate(mContext, message.getCreatedAt()));
                viewHolder.getView("txt_desc", TextView.class).setText(((AdminMessage) message).getMessage());
                viewHolder.getView("txt_desc", TextView.class).setTypeface(roboto);
            } else if (message instanceof FileMessage) {
                viewHolder.getView("txt_date", TextView.class).setText(Helper.getDisplayTimeOrDate(mContext, message.getCreatedAt()));
                viewHolder.getView("txt_desc", TextView.class).setText("(FILE)");
                viewHolder.getView("txt_desc", TextView.class).setTypeface(roboto);
            }

            return convertView;
        }

        private static class ViewHolder {
            private Hashtable<String, View> holder = new Hashtable<>();

            public void setView(String k, View v) {
                holder.put(k, v);
            }

            public View getView(String k) {
                return holder.get(k);
            }

            public <T> T getView(String k, Class<T> type) {
                return type.cast(getView(k));
            }
        }
    }

    //-----------------------------

    public static class SendBirdUserListActivity extends Fragment {
        //  private SendBirdUserListFragment mSendBirdUserListFragment;
        private View mTopBarContainer;

        public ListView mListView;
        private UserListQuery mUserListQuery;
        private SendBirdUserAdapter mAdapter;
        public static HashSet<String> mSelectedUserIds;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_sendbird_user_list, container, false);

            mSelectedUserIds = new HashSet<>();
            mUserListQuery = SendBird.createUserListQuery();


             loadMoreUsers();

            mListView = (ListView) rootView.findViewById(R.id.list);
            mAdapter = new SendBirdUserAdapter(getActivity());
            mListView.setAdapter(mAdapter);

           /* mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String[] userIds = mSelectedUserIds.toArray(new String[0]);
                    //String[] userIds = data.getStringArrayExtra("user_ids");
                    create(userIds);
                   *//* if (userIds.length > 0) {
                        Intent data = new Intent();
                        data.putExtra("user_ids", userIds);
                        setResult(RESULT_OK, data);
                        finish();
                    }*//*

                }
            });*/

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= (int) (totalItemCount * 0.8f)) {

                    loadMoreUsers();

                }
            }
        });

            // initFragment();
            //initUIComponents();

            return rootView;
        }

        //TODO load instant online users

        public ArrayList<User> loadMoreUsers() {


            Log.d("loadMoreUsers","entered");

            if (mUserListQuery != null && mUserListQuery.hasNext() && !mUserListQuery.isLoading()) {

                mUserListQuery.next(new UserListQuery.UserListQueryResultHandler() {

                    @Override
                    public void onResult(List<User> list, SendBirdException e) {

                        if (e != null) {
                           /* Toast.makeText(SendBirdUserListActivity.this, ""
                                    + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();*/
                            return;
                        }

                        // TODO filter users online / offline
                        Log.d("loadMoreUsers","filtered");
                        List<User> listOnline = new ArrayList<User>();
                        List<User> listOffline = new ArrayList<User>();

                        for(User u:list) {
                            boolean st = u.getConnectionStatus()== User.ConnectionStatus.ONLINE;
                            Log.d("initial users=>", u.getUserId() + st  );

                            if (u.getConnectionStatus() == User.ConnectionStatus.ONLINE ){
                                listOnline.add(u);
                            }
                            else {

                                listOffline.add(u);
                            }

                            // remove myself
                            if(u.getUserId().equals(userid.replace(" ","+"))){
                                listOnline.remove(u);

                            }
                        }

                        List<User> allUsers = new ArrayList<User>();
                        allUsers.addAll(listOnline);
                        allUsers.addAll(listOffline);

                        mAdapter.addAll(allUsers);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            return mItemListInitial;
        }

        ArrayList<User> mItemListInitial = new ArrayList<>();

       /* public void loadMoreUsers() {
            if (mUserListQuery != null && mUserListQuery.hasNext() && !mUserListQuery.isLoading()) {
                mUserListQuery.next(new UserListQuery.UserListQueryResultHandler() {
                    @Override
                    public void onResult(List<User> list, SendBirdException e) {
                        if (e != null) {
                         *//*   Toast.makeText(SendBirdUserListActivity.this, ""
                                    + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;*//*
                        }

                        List<User> arrlist = new ArrayList<User>();
                        for (User u: list)
                            Log.d("on scroll-users=>", u.getUserId());

                        for(User u:list) {
                            if (u.getConnectionStatus() == User.ConnectionStatus.ONLINE )
                                arrlist.add(u);

                            if(u.getUserId().equals(MainActivity.sUserId))
                                arrlist.remove(u);
                        }

                        mAdapter.addAll(arrlist);
                        mAdapter.notifyDataSetChanged();

                    }
                });
            }
        }
*/
        @Override
        public void onResume() {
            super.onResume();
            /**
             * If the minimum SDK version you support is under Android 4.0,
             * you MUST uncomment the below code to receive push notifications.
             */
//        SendBird.notifyActivityResumedForOldAndroids();
        }

        @Override
        public void onPause() {
            super.onPause();
            /**
             * If the minimum SDK version you support is under Android 4.0,
             * you MUST uncomment the below code to receive push notifications.
             */
//        SendBird.notifyActivityPausedForOldAndroids();
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            resizeMenubar();
        }

        private void resizeMenubar() {
            ViewGroup.LayoutParams lp = mTopBarContainer.getLayoutParams();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                lp.height = (int) (28 * getResources().getDisplayMetrics().density);
            } else {
                lp.height = (int) (48 * getResources().getDisplayMetrics().density);
            }
            mTopBarContainer.setLayoutParams(lp);
        }

       /* @Override
        public void finish() {
            super.finish();
            overridePendingTransition(R.anim.sendbird_slide_in_from_top, R.anim.sendbird_slide_out_to_bottom);
        }*/

  /*  private void initFragment() {
        mSendBirdUserListFragment = new SendBirdUserListFragment();
        mSendBirdUserListFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mSendBirdUserListFragment)
                .commit();
    }*/

    /*private void initUIComponents() {
        mTopBarContainer = findViewById(R.id.top_bar_container);

        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* String[] userIds = mSendBirdUserListFragment.mSelectedUserIds.toArray(new String[0]);
                if (userIds.length > 0) {
                    Intent data = new Intent();
                    data.putExtra("user_ids", userIds);
                    setResult(RESULT_OK, data);
                    finish();
                }*//*
            }
        });

        resizeMenubar();



    */}

    public static class SendBirdUserAdapter extends BaseAdapter {
            private final Context mContext;
            private final LayoutInflater mInflater;
            private ArrayList<User> mItemList;

            public SendBirdUserAdapter(Context context) {
                mContext = context;
                mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                mItemList = new ArrayList<>();
            }

            @Override
            public int getCount() {
                return mItemList.size();
            }

            @Override
            public User getItem(int position) {
                return mItemList.get(position);
            }

            public void clear() {
                mItemList.clear();
            }

            public User remove(int index) {
                return mItemList.remove(index);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            public void addAll(Collection<User> users) {
                mItemList.addAll(users);
                notifyDataSetChanged();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder;

                if (convertView == null) {
                    viewHolder = new ViewHolder();

                    convertView = mInflater.inflate(R.layout.sendbird_view_user, parent, false);
                    viewHolder.setView("root_view", convertView);
                    viewHolder.setView("img_thumbnail", convertView.findViewById(R.id.img_thumbnail));
                    viewHolder.setView("txt_name", convertView.findViewById(R.id.txt_name));
                    viewHolder.setView("chk_select", convertView.findViewById(R.id.chk_select));
                    viewHolder.setView("txt_status", convertView.findViewById(R.id.txt_status));

                    convertView.setTag(viewHolder);
                }

                final User item = getItem(position);
                viewHolder = (ViewHolder) convertView.getTag();

                Helper.displayUrlImage(viewHolder.getView("img_thumbnail", ImageView.class), item.getProfileUrl(), true);
                viewHolder.getView("txt_name", TextView.class).setText(item.getUserId().replace("+"," ") );
                viewHolder.getView("txt_name", TextView.class).setTypeface(roboto);

                viewHolder.getView("txt_name", TextView.class).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SendBirdUserListActivity.mSelectedUserIds.clear();
                        SendBirdUserListActivity.mSelectedUserIds.add(item.getUserId());

                        String[] userIds = SendBirdUserListActivity.mSelectedUserIds.toArray(new String[0]);
                        //String[] userIds = data.getStringArrayExtra("user_ids");
                        create(userIds);
                       /* String[] userIds = mSelectedUserIds.toArray(new String[0]);
                        if (userIds.length > 0) {

                            Intent data = new Intent();
                            data.putExtra("user_ids", userIds);
                            setResult(RESULT_OK, data);
                            finish();
                        }*/
                    }
                });


                viewHolder.getView("chk_select", CheckBox.class).
                        setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    SendBirdUserListActivity.mSelectedUserIds.add(item.getUserId());


                                } else {
                                    SendBirdUserListActivity.mSelectedUserIds.remove(item.getUserId());
                                }


                            }
                        });
                viewHolder.getView("chk_select", CheckBox.class).
                        setChecked(SendBirdUserListActivity.mSelectedUserIds.contains(item.getUserId()));


                if (item.getConnectionStatus() != User.ConnectionStatus.ONLINE) {
                    viewHolder.getView("txt_status", TextView.class).setVisibility(View.VISIBLE);
                    viewHolder.getView("txt_status", TextView.class).setBackgroundResource(R.drawable.label_round_offline);

                    //viewHolder.getView("txt_status", TextView.class).setTypeface(roboto);

                } else {
                    viewHolder.getView("txt_status", TextView.class).setVisibility(View.VISIBLE);
                    viewHolder.getView("txt_status", TextView.class).setBackgroundResource(R.drawable.label_round);

                }

                return convertView;
            }

            private class ViewHolder {
                private Hashtable<String, View> holder = new Hashtable<>();

                public void setView(String k, View v) {
                    holder.put(k, v);
                }

                public View getView(String k) {
                    return holder.get(k);
                }

                public <T> T getView(String k, Class<T> type) {
                    return type.cast(getView(k));
                }
            }


    private void create(final String[] userIds) {

       // viewPager.setCurrentItem(0);


        LayoutInflater li  = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.sendbird_view_group_create_channel, null);
        final EditText chName = (EditText) view.findViewById(R.id.etxt_chname);
        //final CheckBox distinct = (CheckBox) view.findViewById(R.id.chk_distinct);




        GroupChannel.createChannelWithUserIds(Arrays.asList(userIds),
                true, chName.getText().toString(), null, null,
                new GroupChannel.GroupChannelCreateHandler() {
                    @Override
                    public void onResult(GroupChannel groupChannel, SendBirdException e) {
                        if (e != null) {
                          //  Toast.makeText(mContext, "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        mSendBirdGroupChannelAdapter.replace(groupChannel);
                    }

                });


            /*
            new AlertDialog.Builder(getActivity())
                    .setView(view)
                    .setTitle("Create Group Channel")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton("Cancel", null).create().show();*/
    }

   /* public void replace(GroupChannel newChannel) {
        for (GroupChannel oldChannel : mItemListChannel) {
            if (oldChannel.getUrl().equals(newChannel.getUrl())) {
                mItemListChannel.remove(oldChannel);
                break;
            }
        }

        mItemListChannel.add(0, newChannel);
        mSendBirdGroupChannelAdapter.notifyDataSetChanged();

       // GroupChannel channel = mItemListChannel.get(mItemListChannel.size()-1);
                //mSendBirdGroupChannelAdapter.getItem(mSendBirdGroupChannelAdapter.getCount()-1);
        Intent intent = new Intent(mContext, SendBirdGroupChatActivity.class);
        intent.putExtra("channel_url", newChannel.getUrl());
        mContext.startActivity(intent);
    }*/

    }

    protected void setMenuBackground(){
        // Log.d(TAG, "Enterting setMenuBackGround");
        getLayoutInflater().setFactory( new LayoutInflater.Factory() {

            @Override
            public View onCreateView(String name, Context context,
                                     AttributeSet attrs) {
                if ( name.equalsIgnoreCase( "com.android.internal.view.menu.IconMenuItemView" ) ) {
                    try { // Ask our inflater to create the view
                        LayoutInflater f = getLayoutInflater();
                        final View view = f.createView( name, null, attrs );
                    /* The background gets refreshed each time a new item is added the options menu.
                    * So each time Android applies the default background we need to set our own
                    * background. This is done using a thread giving the background change as runnable
                    * object */
                        new Handler().post(new Runnable() {
                            public void run () {
                                // sets the background color
                                view.setBackgroundResource( R.color.colorAccent);
                                // sets the text color
                                ((TextView) view).setTextColor(Color.WHITE);
                                // sets the text size
                                ((TextView) view).setTextSize(18);
                            }
                        } );
                        return view;
                    }
                    catch ( InflateException e ) {}
                    catch ( ClassNotFoundException e ) {}
                }
                return null;
            }});
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//       // setMenuBackground();
//        return true;
//    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.logout_menu:
              //  Toast.makeText(this, "You have selected Bookmark Menu", Toast.LENGTH_SHORT).show();

                SendBird.disconnect(null);
                LoginManager.getInstance().logOut();
                SP("user_id","");
                ((LinearLayout)findViewById(R.id.logoutchat_ll)).setVisibility(View.INVISIBLE);

                finish();

                Intent intent = new Intent(SendBirdGroupChannelListActivity.this, NewsListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
        }
        return true;

    }*/
}


/*
class SendBirdUserListFragment extends Fragment {
    public ListView mListView;
    private UserListQuery mUserListQuery;
    private SendBirdUserAdapter mAdapter;
    public HashSet<String> mSelectedUserIds;

    public SendBirdUserListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sendbird_fragment_user_list, container, false);
        initUIComponents(rootView);

        mUserListQuery = SendBird.createUserListQuery();
        loadMoreUsers();

        return rootView;

    }

    private void initUIComponents(View rootView) {


    }

    }
}
*/
