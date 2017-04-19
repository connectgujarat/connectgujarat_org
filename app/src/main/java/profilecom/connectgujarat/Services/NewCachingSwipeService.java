package profilecom.connectgujarat.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;

import profilecom.connectgujarat.DataServices.NewPostCacheManager;
import profilecom.connectgujarat.Locality;
import profilecom.connectgujarat.NewsListActivity;

public class NewCachingSwipeService extends Service implements NewApiPostDownloadListner, NewApiPostDownloadFailureListener
        {
    NewCategoryPostsRF postService = new NewCategoryPostsRF();
    public final static String MY_ACTION = "MY_ACTION";

    public NewCachingSwipeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NewCategorySwipeRF postService = new NewCategorySwipeRF();
        postService.addListener(this);

       // Log.d("onStartCommand", "onStartCommand called");

        int categoryId =intent.getExtras().getInt("categoryId", 1);
        int count =intent.getExtras().getInt("count", 1);
        String afterDate =intent.getExtras().getString("afterDate");

        try {
            if (afterDate.length() > 4)
                postService.
                        getSwipePosts(categoryId, afterDate.replace(" ", "T"), count);
        }catch (Exception e){

        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void PostDownloaded(ArrayList<Locality> localityList) {
        //Log.d("before saving=>","before saving=>"+ localityList.toString() + "");

        NewPostCacheManager cacheManager = new NewPostCacheManager(this);
        cacheManager.SaveNewApiPostPaginate(localityList);


        Intent intent = new Intent();
        intent.setAction(MY_ACTION);

        intent.putExtra("newApiPosts", NewsListActivity.NEW_POSTS_DOWNLOADED);

        sendBroadcast(intent);

        stopSelf();
    }

            @Override
            public void PostDownloadedFailure(ArrayList<Locality> localityList) {

                Intent intent = new Intent();
                intent.setAction(MY_ACTION);

                intent.putExtra("newApiPosts", NewsListActivity.NEW_POSTS_DOWNLOADED);

                sendBroadcast(intent);

                stopSelf();
            }
        }
