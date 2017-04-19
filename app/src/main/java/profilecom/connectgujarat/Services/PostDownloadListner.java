package profilecom.connectgujarat.Services;

import java.util.List;

import profilecom.connectgujarat.Entities.Post.PostDetail;


public interface PostDownloadListner {
    void PostDownloaded(List<PostDetail> postDetailList);
}
