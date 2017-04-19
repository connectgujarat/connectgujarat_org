package profilecom.connectgujarat.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.List;

import profilecom.connectgujarat.DataServices.PostCacheManager;
import profilecom.connectgujarat.Entities.Post.PostDetail;

public class BlogCachingService extends Service implements PostDownloadListner {
    PostService postService = new PostService();
    public final static String MY_ACTION = "MY_ACTION";

    public BlogCachingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        postService.addListener(this);

        //Log.d("onStartCommand","onStartCommand called");
        int categoryId =intent.getExtras().getInt("catId", 1);
        int perPage =intent.getExtras().getInt("postPerPage", 1);
        int pageNumber =intent.getExtras().getInt("pageNumber", 1);
        String after =intent.getExtras().getString("afterDate");

       // Log.d("onStartCommand", "BlogCachingService- catid=" + categoryId + ", postperpage= " + pageNumber);

        List<PostDetail> postDetailList = postService.getPosts(this, categoryId, perPage, pageNumber, after);
       //if(postDetailList!=null)
       // Log.d("myapp.FromService=>",postDetailList.toString());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void PostDownloaded(List<PostDetail> postDetailList) {
       // Log.d("before saving=>",postDetailList.toString()+"");

        PostCacheManager cacheManager = new PostCacheManager(this);
        cacheManager.SavePost(postDetailList);



        Intent intent = new Intent();
        intent.setAction(MY_ACTION);

        intent.putExtra("oldPosts", 0);

        sendBroadcast(intent);
    }
}
