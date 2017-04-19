package profilecom.connectgujarat.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import profilecom.connectgujarat.Entities.NewCategoryPostsModified.ExamplePosts;

public class NewCachingServiceOCC extends Service// implements NewApiPostDownloadListner
 {
    NewCategoryPostsRF postService = new NewCategoryPostsRF();
    public final static String MY_ACTION = "MY_ACTION";

    public NewCachingServiceOCC() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // postService.addListener(this);

     //   Log.d("onStartCommand","onStartCommand called");

        int categoryId =intent.getExtras().getInt("categoryId", 1);
        int count =intent.getExtras().getInt("count", 1);
        String afterDate =intent.getExtras().getString("afterDate");

        //Log.d("NCSOCC","NCSOCC"+categoryId);

        // not used
        ExamplePosts postDetailList = postService.
                getNewCategoryPosts(categoryId, afterDate.replace(" ","T"), count);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

   /* @Override
    public void PostDownloaded(GetCategoryPost getCategoryPostList) {
        Log.d("before saving=>", getCategoryPostList.toString() + "");

        //if default
       // NewPostCacheManager cacheManager = new NewPostCacheManager(this);
        //cacheManager.SaveNewApiPost(getCategoryPostList);

        //else

        ArrayList<Locality> localList = new ArrayList<Locality>();

        for(int i=0; i<getCategoryPostList.getPosts().size();i++)
        {

           Posts p= getCategoryPostList.getPosts().get(i);

            Locality up = new Locality();
            up.setId(p.getId());
            up.setTitle(p.getTitle());
            up.setType(p.getType());
            up.setStatus(p.getStatus());
            up.setContent(p.getContent());
            up.setDate(p.getDate());
            up.setModified(p.getModified());
            up.setAttUrl(p.getUrl());
            up.setAuthorName(p.getAuthor().getName());

            localList.add(up);


        }

        Intent intent = new Intent();
        intent.setAction(MY_ACTION);

        intent.putExtra("newApiPosts", 1234);
        intent.putParcelableArrayListExtra("localList", localList);

        sendBroadcast(intent);

        stopSelf();
    }*/
}
