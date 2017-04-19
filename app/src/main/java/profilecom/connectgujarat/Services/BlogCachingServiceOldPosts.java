package profilecom.connectgujarat.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.List;

import profilecom.connectgujarat.DataServices.PostCacheManager;
import profilecom.connectgujarat.Entities.Post.PostDetail;

public class BlogCachingServiceOldPosts extends Service implements PostDownloadListner {
    PostServiceOldPosts postServiceOldPosts = new PostServiceOldPosts();

    public final static String MY_ACTION = "MY_ACTION";

    public BlogCachingServiceOldPosts() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // Log.d("onStartCommand","onStartCommand called");
        int catId =intent.getExtras().getInt("catId", 1);
        int postPerPage =intent.getExtras().getInt("postPerPage", 1);
        int pageNumber =intent.getExtras().getInt("pageNumber", 1);
        String beforeDate =intent.getExtras().getString("beforeDate");
        //Log.d("onStartCommand","BlogCachingServiceOldPosts- catid= "+ catId + ", postperpage= "+postPerPage);

        postServiceOldPosts.addListener(this);
        //Log.d("beforeDate-service",beforeDate);
        List<PostDetail> postDetailList = postServiceOldPosts.getPostsOld
                (BlogCachingServiceOldPosts.this, catId, postPerPage, pageNumber, beforeDate);


        //if(postDetailList!=null)
       // Log.d("myapp.FromService=>",postDetailList.toString());
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void PostDownloaded(List<PostDetail> postDetailList) {
        //Log.d("before saving=>",postDetailList.toString()+"");

        PostCacheManager cacheManager = new PostCacheManager(this);
        cacheManager.SaveOldPost(postDetailList);

        Intent intent = new Intent();
        intent.setAction(MY_ACTION);

        intent.putExtra("oldPosts", 25);

        sendBroadcast(intent);
        stopSelf();
    }
}
