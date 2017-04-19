package profilecom.connectgujarat.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import profilecom.connectgujarat.DataServices.NewPostCacheManager;
import profilecom.connectgujarat.Locality;
import profilecom.connectgujarat.NewsListActivity;

public class NewCachingPaginateService extends Service implements NewApiPostDownloadListner, NewApiPostDownloadFailureListener {
    public final static String MY_ACTION = "MY_ACTION";

    static int serviceCallsCounter = 0;
    public static ArrayList<Locality> AllRecordsHolderList = new ArrayList<>();
    static int itsPageCall;

    public NewCachingPaginateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // new service call for a cat
        NewCategoryPostPaginateRF postService = new NewCategoryPostPaginateRF();
        postService.addListener(this);
        int categoryId =23 ;
        int count = 6;
        String beforeDate ="--";
        if (serviceCallsCounter == NewsListActivity.categoriesIds.size()) {

            serviceCallsCounter=0;
        }

        if(intent!=null) {
             categoryId = intent.getExtras().getInt("categoryId", 1);
             count = intent.getExtras().getInt("count", 1);
             beforeDate = intent.getExtras().getString("beforeDate");

            // Log.d("NLA", "NLA=paginate" + beforeDate.replace(" ", "T"));
            //        GetCategoryPost postDetailList =

            itsPageCall = intent.getExtras().getInt("itsPageCall");

        }
        postService.getNewPaginatedPosts(categoryId, beforeDate.replace(" ", "T"), count);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void PostDownloaded(ArrayList<Locality> localityList) {
        Log.d("before saving=>", "before saving=>" + localityList.toString() + "");

       /* if(localityList==null) {

            Toast.makeText(NewCachingPaginateService.this,
                    "No internet connection! Please try again later!", Toast.LENGTH_LONG).show();

        }*/
        synchronized (this) {

            if (itsPageCall != 9999) {


                serviceCallsCounter++;

                // keep adding all records
                if (localityList.size() > 0)
                    AllRecordsHolderList.addAll(localityList);

                Log.d("serviceCounter-service", "serviceCallsCounter"+serviceCallsCounter);

                if (serviceCallsCounter >= NewsListActivity.categoriesIds.size()) {



                    NewPostCacheManager cacheManager = new NewPostCacheManager(this);
                    // saves all records at once
                    cacheManager.SaveNewApiPostPaginate(AllRecordsHolderList);

                    Log.d("serviceCounter", "serviceCallsCounter"+serviceCallsCounter);

                    Intent intent = new Intent();
                    intent.setAction("MY_ACTION");

                    intent.putExtra("newApiPosts", NewsListActivity.categoriesIds.size());
                    sendBroadcast(intent);

                    serviceCallsCounter=0;
                }
            }
            else{

                NewPostCacheManager cacheManager = new NewPostCacheManager(this);
                // saves all records at once
                cacheManager.SaveNewApiPostPaginate(localityList);

                Intent intent = new Intent();
                intent.setAction("MY_ACTION");

                intent.putExtra("newApiPosts", NewsListActivity.OLD_POSTS_DOWNLOADED);
                sendBroadcast(intent);

            }
        }

    }


    @Override
    public void PostDownloadedFailure(ArrayList<Locality> localityList) {

        Intent intent = new Intent();
        intent.setAction(MY_ACTION);

        intent.putExtra("newApiPosts", NewsListActivity.OLD_POSTS_DOWNLOADED);
        sendBroadcast(intent);
    }
}
