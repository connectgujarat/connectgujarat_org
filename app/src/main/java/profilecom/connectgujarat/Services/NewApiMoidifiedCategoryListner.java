package profilecom.connectgujarat.Services;

import java.util.List;

import profilecom.connectgujarat.Entities.NewCategoryModified.Example;


public interface NewApiMoidifiedCategoryListner {
    void PostDownloaded(List<Example> getCategoryIndex);
    void PostDownloadedFailure();

}
