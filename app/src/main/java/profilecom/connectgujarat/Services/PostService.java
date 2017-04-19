package profilecom.connectgujarat.Services;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import profilecom.connectgujarat.Entities.Post.PostDetail;

public class PostService {
    private List<PostDetail> mPostDetailList;
    private List<PostDownloadListner> listeners = new ArrayList<PostDownloadListner>();

    public void addListener(PostDownloadListner toAdd) {
        listeners.add(toAdd);
    }

    public List<PostDetail> getPosts(final Context c, final int categoryId,
                                     final int perPage, final int pageNumber, final String after) {
        String url = "http://connectgujarat.com/"; //Resources.getSystem().getString(R.string.main_url);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IPost service = retrofit.create(IPost.class);
        Call<List<PostDetail>> repos = service.GetPosts(categoryId, perPage, pageNumber, after);

//        Log.d("PostService","PostService-url"+service.GetPosts( categoryId, perPage, pageNumber, after).request().url().toString());


        repos.enqueue(new Callback<List<PostDetail>>() {
            @Override
            public void onResponse(Call<List<PostDetail>> call,
                                   Response<List<PostDetail>> response) {

               // Log.d("resp=>", response.body().toString());
                if (response.isSuccessful()) {
                    //Log.d("sucess", String.valueOf(response.body().size()));
                    mPostDetailList = response.body();

                    // Notify everybody that may be interested.
                    for (PostDownloadListner hl : listeners)
                        hl.PostDownloaded(mPostDetailList);

                    //Log.d("myapp.PostService=>",mPostDetailList.toString());

                    //NewsListAdapter NewsListAdapter = new NewsListAdapter(c, mPostDetailList);
                    //listView.setAdapter(NewsListAdapter);
                } else {
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(Call<List<PostDetail>> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.d("Error", t.getMessage());
            }
        });

        return mPostDetailList;
    }
}
