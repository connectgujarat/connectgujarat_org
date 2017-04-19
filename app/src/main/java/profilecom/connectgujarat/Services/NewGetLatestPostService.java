package profilecom.connectgujarat.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import profilecom.connectgujarat.DataServices.NewPostCacheManager;
import profilecom.connectgujarat.Locality;
import profilecom.connectgujarat.NewsListActivity;

public class NewGetLatestPostService extends Service implements NewApiPostDownloadListner, NewApiPostDownloadFailureListener {
    public final static String MY_ACTION = "MY_ACTION";

    static int serviceCallsCounter = 0;
    public static Locality AllRecordsHolderList = new Locality();
    static int itsPageCall;

    public NewGetLatestPostService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // new service call for a cat
        NewGetLatestPostServiceRF postService = new NewGetLatestPostServiceRF();
        postService.addListener(this);
        int count = 1;

        if (intent != null) {
            count = intent.getExtras().getInt("count", 1);

            // Log.d("NLA", "NLA=paginate" + beforeDate.replace(" ", "T"));
            //        GetCategoryPost postDetailList =

            // itsPageCall = intent.getExtras().getInt("itsPageCall");

        }
        postService.getLatestPost();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void PostDownloaded(ArrayList<Locality> localityList) {

        Log.d("NewGetLatestPostService", "yes");

        Intent intent = new Intent();
        intent.setAction("MY_ACTION");

        if(localityList!=null && localityList.size()>0)
            new NewPostCacheManager(this).SaveHPPost(localityList.get(0));

      //  NewsListActivity.localityList = localityList;
       // Log.d("hh", NewsListActivity.localityList.toString());

        intent.putExtra("newApiPosts", NewsListActivity.SLIDE_POST);
        sendBroadcast(intent);


    }


    @Override
    public void PostDownloadedFailure(ArrayList<Locality> localityList) {
        Log.d("NewGetLatestPostService", "no");

        Intent intent = new Intent();
        intent.setAction(MY_ACTION);

        intent.putExtra("newApiPosts", NewsListActivity.SLIDE_POST);
        sendBroadcast(intent);
    }
}
