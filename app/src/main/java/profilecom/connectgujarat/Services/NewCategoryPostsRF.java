package profilecom.connectgujarat.Services;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import profilecom.connectgujarat.Entities.NewCategoryPosts.GetCategoryPost;
import profilecom.connectgujarat.Entities.NewCategoryPostsModified.ExamplePosts;


public class NewCategoryPostsRF {
    private ExamplePosts mMediaContent;
    private boolean bExecuted = false;
    ICategoryPaginatePosts service = null;

    private List<GetCategoryPost> mGetCategoryPostList;
    private List<NewApiPostDownloadListner> listeners = new ArrayList<NewApiPostDownloadListner>();

    public void addListener(NewApiPostDownloadListner toAdd) {
        listeners.add(toAdd);
    }


    public NewCategoryPostsRF(){
        String url = "http://connectgujarat.com/"; //Resources.getSystem().getString(R.string.main_url);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

         service = retrofit.create(ICategoryPaginatePosts.class);

        //Log.d("url","url=>"+service.toString());


    }


    // not using
    public ExamplePosts getNewCategoryPosts(int id, String date, int count) {

        Call<ExamplePosts> repoMedia = service.GetCategoryPaginatePosts(id, date, count);
      //  Log.d("url", "url=>"+ service.GetCategoryPosts(id,date, count).request().url().toString());
        bExecuted = false;

        repoMedia.enqueue(new Callback<ExamplePosts>() {

            @Override
            public void onResponse(Call<ExamplePosts> call,
                                   Response<ExamplePosts> response) {

        //        Log.d("GetCategoryPost=>","GetCategoryPost"+ response.body().toString() + "");
                if (response.isSuccessful()) {


                   /* try {
                        JSONArray emp = new JSONObject(response.body().toString()).getJSONArray("posts");
                        JSONObject jobj = emp.getJSONObject(0);
                        String content = jobj.getString("content");

                        Log.d("content-manual","content=> " + content);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/


                    //             Log.d("GetCategoryPost=>", "GetCategoryPost"+ response.body().toString());
                    mMediaContent = response.body();

                    Gson gson = new Gson();
                    GetCategoryPost gci =
                            gson.fromJson(mMediaContent.toString(), GetCategoryPost.class);
                  //  Log.d("NCPRF=> ","NCPRF(fcheck)=> "  + gci.toString());

                 //   for (NewApiPostDownloadListner hl : listeners){
                        //listeners.get(0).PostDownloaded(gci);
                  //  }


                    bExecuted = true;

                } else {
                    bExecuted = true;
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(Call<ExamplePosts> call, Throwable t) {
                bExecuted = true;
                // Log.d("mediaresp=>", "error");
               // Log.d("mediarespc=>", t. + "");
                // something went completely south (like no internet connection)
//                Log.d("Error", t.getMessage());
            }
        });
        return mMediaContent;
    }
}
