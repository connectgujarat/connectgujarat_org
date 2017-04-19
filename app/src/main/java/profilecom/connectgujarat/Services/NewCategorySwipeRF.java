package profilecom.connectgujarat.Services;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import profilecom.connectgujarat.Entities.NewCategoryPosts.GetCategoryPost;
import profilecom.connectgujarat.Locality;


public class NewCategorySwipeRF {
    private boolean bExecuted = false;
    ICategorySwipePosts service = null;

    private List<GetCategoryPost> mGetCategoryPostList;
    private List<NewApiPostDownloadListner> listeners
            =
            new ArrayList<NewApiPostDownloadListner>(0);

    private static int val=0;

    public void addListener(NewApiPostDownloadListner toAdd) {
        listeners.add(toAdd);
    }


    public NewCategorySwipeRF() {

        String url = // "http://connectgujarat.com/";
                    // Resources.getSystem().getString(R.string.main_url);
                    "http://139.59.48.164/";

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ICategorySwipePosts.class);

        //Log.d("url","url=>"+service.toString());

    }


    public void getSwipePosts(final int catid, String date, int count) {

        Call<GetCategoryPost> repoMedia = service.GetSwipedPosts(catid, date, count);
        Log.d("url", "url(getNewPaginatedPosts)=>" +
                service.GetSwipedPosts(catid, date, count).request().url().toString());
        bExecuted = false;

        repoMedia.enqueue(new Callback<GetCategoryPost>() {

            @Override
            public void onResponse(Call<GetCategoryPost> call,
                                   Response<GetCategoryPost> response) {

                ArrayList<Locality> localityList = new ArrayList<Locality>();
                JSONArray posts = null;

                if (response.isSuccessful()) {

                    try {
                        posts = new JSONObject(response.body().toString()
                                .replaceAll("[a-z]{1}'[a-z]{1}", "")
                        .replaceAll("[a-z]{1}\"[a-z]{1}"," ") )
                                .getJSONArray("posts") ;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (posts != null) {

                        for (int i = 0; i < posts.length(); i++) {
                            try {
                                JSONObject eachPost = posts.getJSONObject(i);

                                Locality localRecord = new Locality();
                                localRecord.setCatid(catid);

                                int id = eachPost.getInt("id");
                                String content = eachPost.getString("content");
                                String title = eachPost.getString("title");
                                String date = eachPost.getString("date");
                                String type = eachPost.getString("type");
                                String posturl = eachPost.getString("url");
                                localRecord.setId(id);
                                localRecord.setContent(content);
                                localRecord.setTitle(title);
                                localRecord.setDate(date);
                                localRecord.setType(type);
                                localRecord.setPosturl(posturl);

                                //Log.d("content-manual", "content=> " + i + "==" + content);
                                //Log.d("content-manual", "content=> " + i + "==" + Html.fromHtml(content));

                                JSONObject author = eachPost.getJSONObject("author");
                                if (author != null) {
                                    String name = author.getString("name");
                                    localRecord.setAuthorName(name);
                                }

                                JSONArray attachments = eachPost.getJSONArray("attachments");
                                if (attachments.length() != 0) {
                                    JSONObject firstAttachment = attachments.getJSONObject(0);
                                    if (firstAttachment != null) {
                                        String urlImage = firstAttachment.getString("url");
                                        localRecord.setAttUrl(urlImage);
                                    }
                                }

                                // each post
                                localityList.add(localRecord);

                            } catch (JSONException e) {

                                Log.d("exception for post", "post" + i);
                                e.printStackTrace();

                            }
                        }
                    }

                    // 1 receiver 1 sender 1 cat

                    Log.d("serviceCallsCounter-val", "serviceCallsCounter"+val++);

                    listeners.get(0).PostDownloaded(localityList);
                    bExecuted = true;

                } else {
                    bExecuted = true;
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(Call<GetCategoryPost> call, Throwable t) {
                bExecuted = true;
                // Log.d("mediaresp=>", "error");
                // Log.d("mediarespc=>", t. + "");
                // something went completely south (like no internet connection)

                //Log.d("Error", t.getMessage());

              //  listeners.get(0).PostDownloaded(null);
            }
        });
    }

}

