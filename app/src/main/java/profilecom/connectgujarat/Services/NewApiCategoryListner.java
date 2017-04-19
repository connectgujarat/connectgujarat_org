package profilecom.connectgujarat.Services;

import profilecom.connectgujarat.Entities.NewCategoryIndex.GetCategoryIndex;


public interface NewApiCategoryListner {
    void PostDownloaded(GetCategoryIndex getCategoryIndex);
    void PostDownloadedFailure();

}
