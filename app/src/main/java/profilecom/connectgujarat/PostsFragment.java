package profilecom.connectgujarat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;
import profilecom.connectgujarat.DataServices.NewPostCacheManager;
import profilecom.connectgujarat.Services.NewCachingPaginateService;
import profilecom.connectgujarat.Services.NewCachingSwipeService;


public class PostsFragment extends Fragment {

    private static ListView mNewsListView;
    private static TextView nopoststv;

    private static int preLast;
    int count = 0;
    private static NewApiListAdapter newApiListAdapter;
    static String beforeDate;
    static int idofLast, catId;
    RelativeLayout hidableRL;
    static boolean UpdateCallFlag;
    private static SwipeRefreshLayout swipe = null;
    static String afterDate;
    static int idofFirst, catFirstPostId;
    private static ProgressBar progressBarPaginate;
    private boolean refresh = false;


    public PostsFragment() {

    }

  /*  public static Fragment newInstance(Context cxt, String title, int position) {

         context = cxt;
           Bundle args = new Bundle();
            args.putString("title", title);
            args.putInt("position", position);
         PostsFragment fragment = new PostsFragment();
            fragment.setArguments(args);

        return fragment;
    }*/

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)
                getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    NewPostCacheManager cacheManager = new NewPostCacheManager(getActivity());

    public void LoadSwipedPosts() {

        List<Locality> swipedPosts = cacheManager.
                GetSwipedPostsOnCat(catFirstPostId);
        //  Log.d("swipedPosts", "swipedPosts" + swipedPosts.toString());

        NewsListActivity.list.clear();
        NewsListActivity.list.addAll(swipedPosts);
        newApiListAdapter.notifyDataSetChanged();

        swipe.setRefreshing(false);
    }

    public  void LoadFirstCategoryPosts() {

        NewsListActivity.list.clear();

        if(NewsListActivity.categoriesIds.size()>0 ) {

            Log.d("firstcatposts", "firstcatposts" + NewsListActivity.list.size());
            NewsListActivity.list = cacheManager.GetFirstCatPosts(NewsListActivity.categoriesIds.get(0));

            if (NewsListActivity.list != null) {
                newApiListAdapter = new NewApiListAdapter(getActivity(),
                        NewsListActivity.list);
                mNewsListView.setAdapter(newApiListAdapter);

                mNewsListView.setVisibility(View.VISIBLE);
                nopoststv.setVisibility(View.INVISIBLE);

            }

        }
        else{
            mNewsListView.setVisibility(View.INVISIBLE);
            nopoststv.setVisibility(View.VISIBLE);

        }

    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_newslist, container, false);

        progressBarPaginate = (ProgressBar) view.findViewById(R.id.progressBarPaginate);


        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        mNewsListView = (ListView) view.findViewById(R.id.news_listview);
        nopoststv = (TextView) view.findViewById(R.id.nopoststv);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

     /*   progressBarPaginate.setVisibility(View.INVISIBLE);
        progressBarPaginate.setIndeterminate(false);
 */
       // mNewsListView.getParent().requestDisallowInterceptTouchEvent(true);

        getView().getParent().requestDisallowInterceptTouchEvent(true);
//
        LoadFirstCategoryPosts();

        scrollingPage();


     /*   swipe.setColorScheme
                (getResources().getColor(android.R.color.holo_blue_dark), android.R.color.holo_blue_light,
                        android.R.color.holo_green_light, android.R.color.holo_green_light);*/

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshViews();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        if (refresh) {
            refreshViews();
            refresh = false;
        }
    }

    public void refreshViews() {
        if (isNetworkAvailable()) {
            Intent intent = new Intent(getActivity(),
                    NewCachingSwipeService.class);

            intent.putExtra("categoryId", catFirstPostId);
            intent.putExtra("afterDate", afterDate);
            intent.putExtra("count", 10);

            getActivity().startService(intent);


        } else {
            swipe.setRefreshing(false);

        }
    }

    public String getTitle() {
        return getArguments().getString("title");
    }

    public int getPosition() {
        return getArguments().getInt("position");
    }

    private void scrollingPage() {

        mNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {

                // Log.d("content-NLA", "content" + list.get(position).getContent());
                //SP("movetoDetails", "true");

                Intent intent =
                        new Intent(getActivity(), NewsDetailsActivity.class);
                intent.putExtra("title", NewsListActivity.list.get(position).getTitle());
                intent.putExtra("author", NewsListActivity.list.get(position).getAuthorName());
                intent.putExtra("date", NewsListActivity.list.get(position).getDate());
                intent.putExtra("url", NewsListActivity.list.get(position).getAttUrl());
                intent.putExtra("content", NewsListActivity.list.get(position).getContent());
                intent.putExtra("cat", NewsListActivity.list.get(position).getCatid());
                intent.putExtra("posturl", NewsListActivity.list.get(position).getPosturl());
                intent.putExtra("postId", NewsListActivity.list.get(position).getId());
                getActivity().startActivity(intent);
            }
        });


        mNewsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

              //  if (scrollState != SCROLL_STATE_IDLE)
                 //   view.getParent().requestDisallowInterceptTouchEvent(true);
               /* else
                    view.getParent().requestDisallowInterceptTouchEvent(false);*/


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                               int visibleItemCount, int totalItemCount) {

              if (firstVisibleItem == 0 && totalItemCount >= 1) {
                  swipe.setEnabled(true);

                  afterDate = NewsListActivity.list.get(firstVisibleItem).getDate();
                  //Log.d("NLA", "afterDate=" + afterDate.replace(" ", "T"));

                  idofFirst = NewsListActivity.list.get(firstVisibleItem).getId();
                  catFirstPostId = NewsListActivity.list.get(firstVisibleItem).getCatid();
              }

              int lastItem = firstVisibleItem + visibleItemCount;
              //8 +2 = 10..

              if (lastItem == totalItemCount) {
                  // 10 = 10

                  if (preLast != lastItem) {
                      //to avoid multiple calls for last item
                      //0!=10..
                      //tcs.Refresh();

                      preLast = lastItem;

                      if (!UpdateCallFlag) {
                          UpdateCallFlag = true;

                          if (totalItemCount > 0) {
                              beforeDate = NewsListActivity.list.get(totalItemCount - 1).getDate();
                              Log.d("NLA", "beforeDate=" + beforeDate.replace(" ", "T"));

                              idofLast = NewsListActivity.list.get(totalItemCount - 1).getId();
                              catId = NewsListActivity.list.get(totalItemCount - 1).getCatid();

                              if (isNetworkAvailable() == true) {
                                  progressBarPaginate.setVisibility(View.VISIBLE);

                                  Intent intent = new Intent(getActivity(),
                                          NewCachingPaginateService.class);

                                  intent.putExtra("itsPageCall", 9999);
                                  intent.putExtra("categoryId", catId);
                                  intent.putExtra("beforeDate", beforeDate);
                                  intent.putExtra("count", 6);

                                  getActivity().startService(intent);
                              } else {
                                  progressBarPaginate.setVisibility(View.INVISIBLE);
                                  UpdateCallFlag = false;
                              }

                          }
                      }

                  }

              }
            }
            }

        );
        mNewsListView.setEnabled(true);


    }


    public  void updateUI(int position, Context c) {

        NewsListActivity.list = cacheManager.
                GetFirstCatPosts(NewsListActivity.categoriesIds.get(position));

               //Log.d("clickedposts", "clickedposts" + list.size());
        //Log.d("total","total"+cacheManager.GetNewApiAllPostListToUI("").toString());

        if (NewsListActivity.list != null) {
            newApiListAdapter = new NewApiListAdapter((AppCompatActivity)c, NewsListActivity.list);
            mNewsListView.setAdapter(newApiListAdapter);
            mNewsListView.setVisibility(View.VISIBLE);
            nopoststv.setVisibility(View.INVISIBLE);

            if(NewsListActivity.list.size()>0) {

                afterDate = NewsListActivity.list.get(0).getDate();
                idofFirst = NewsListActivity.list.get(0).getId();
                catFirstPostId = NewsListActivity.list.get(0).getCatid();

            }else{

                afterDate = "-sorry";
                idofFirst = -9999;
                catFirstPostId = -9999;

            }

        } else {
            mNewsListView.setVisibility(View.INVISIBLE);
            nopoststv.setVisibility(View.VISIBLE);
            afterDate = "--sorry--";
            beforeDate = "--sorry--";
            idofLast = -9999;
            idofFirst = -9999;
            catId = -9999;
            catFirstPostId = -9999;
        }
    }


    public void LoadPaginatedPosts() {

        List<Locality> paginatedPosts =
                cacheManager.GetPaginatedListOnLastID(idofLast, catId);

        progressBarPaginate.setVisibility(View.INVISIBLE);

        NewsListActivity.list.addAll(paginatedPosts);
        newApiListAdapter.notifyDataSetChanged();
        UpdateCallFlag = false;


    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d("movetoDetails", getSP("movetoDetails"));

        if (isNetworkAvailable() && getSP("movetoDetails").equals("")) {
            Log.d("onStop", "onStop....");

            Intent intent = new Intent(getActivity(),
                    NewCachingSwipeService.class);

            intent.putExtra("categoryId", catFirstPostId);
            intent.putExtra("afterDate", afterDate);
            intent.putExtra("count", 6);

            getActivity().startService(intent);

        }
    }

    public void SP(String key, String value) {
        SharedPreferences sharedpreferences =
                getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getSP(String key) {
        SharedPreferences sharedpreferences =
                getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        return sharedpreferences.getString(key, "");
    }

    public void setRefresh() {
        refresh = true;
    }
}