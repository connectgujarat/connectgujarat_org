package profilecom.connectgujarat.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.List;

import profilecom.connectgujarat.DataServices.NewPostCacheManager;
import profilecom.connectgujarat.Entities.NewCategoryModified.Example;
import profilecom.connectgujarat.NewsListActivity;

public class NewCategoryIndexService extends Service implements NewApiMoidifiedCategoryListner {
    //NewCategoryIndexRF postService = new NewCategoryIndexRF();
    NewCategoryIndexModifiedRF postService = new NewCategoryIndexModifiedRF();
    public final static String MY_ACTION = "MY_ACTION";

    public NewCategoryIndexService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        postService.addListener(this);

        //   Log.d("onStartCommand","onStartCommand called");

        postService.getCategoryIndexModified();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void PostDownloaded(List<Example> getCategoryIndex) {
        //Log.d("before saving=>",getCategoryIndex.toString()+"");

         NewPostCacheManager cacheManager = new NewPostCacheManager(this);
         cacheManager.SaveAllCategories(getCategoryIndex);

        /*
        Collections.reverse(NewsListActivity.categoriesIds);
        //Log.d("categoriesIds", "categoriesIds" +NewsListActivity.categoriesIds.toString());

        Collections.reverse(NewsListActivity.categoriesNames);
        //Log.d("categoriesIds","categoriesNames" +NewsListActivity.categoriesNames.toString());*/

        Intent intent = new Intent();
        intent.setAction(MY_ACTION);

        intent.putExtra("newApiPosts",
                NewsListActivity.CATEGORIES_LIST_DOWNLOADED);

        sendBroadcast(intent);

        stopSelf();
    }

    @Override
    public void PostDownloadedFailure() {

        Intent intent = new Intent();
        intent.setAction(MY_ACTION);

        intent.putExtra("newApiPosts",
                NewsListActivity.CATEGORIES_LIST_DOWNLOADED);

        sendBroadcast(intent);
    }
}
