package profilecom.connectgujarat.Services;

import android.content.Context;

import java.util.List;

import profilecom.connectgujarat.DataServices.PostCacheManager;
import profilecom.connectgujarat.Entities.Post.PostDetail;

public class TempCachingService implements PostDownloadListner {
    Context mContext;
    PostService postService = new PostService();
    int page = 0;

    public TempCachingService(Context context) {
        postService.addListener(this);
        mContext = context;
    }

  /*  public void Start() {
        List<PostDetail> postDetailList = postService.getPosts(mContext, "befeoredate", page);
    }

    public void Refresh(){
        page++;
        List<PostDetail> postDetailList = postService.getPosts(mContext, "befeoredate", page);
    }*/

    public void PostDownloaded(List<PostDetail> postDetailList) {
        PostCacheManager cacheManager = new PostCacheManager(mContext);
        cacheManager.SavePost(postDetailList);

        //sendBroadcast()
    }
}
