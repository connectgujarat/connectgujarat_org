package profilecom.connectgujarat.Services;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import profilecom.connectgujarat.Entities.NewCategoryPosts.GetCategoryPost;
import profilecom.connectgujarat.Entities.NewCategoryPostsModified.ExamplePosts;
import profilecom.connectgujarat.Entities.NewCategoryPostsModified.Post;
import profilecom.connectgujarat.Locality;


public class NewCategoryPostPaginateRF {
    private boolean bExecuted = false;
    ICategoryPaginatePosts service = null;

    private List<GetCategoryPost> mGetCategoryPostList;
    private List<NewApiPostDownloadListner> listeners
            =
            new ArrayList<NewApiPostDownloadListner>(0);

    private static int val=0;

    public void addListener(NewApiPostDownloadListner toAdd) {
        listeners.add(toAdd);
    }


    public NewCategoryPostPaginateRF() {

        final okhttp3.OkHttpClient okHttpClient = new okhttp3.OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.MINUTES)
                .connectTimeout(5, TimeUnit.MINUTES)
                .build();
        String url = //"http://connectgujarat.com/"; //
                    // Resources.getSystem().getString(R.string.main_url);
                    "http://139.59.48.164/";

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        service = retrofit.create(ICategoryPaginatePosts.class);

        //Log.d("url","url=>"+service.toString());

    }


    public void getNewPaginatedPosts(final int catid, String date, int count) {

        Call<ExamplePosts> repoMedia = service.GetCategoryPaginatePosts(catid, date, count);
        Log.d("url", "url(getNewPaginatedPosts)=>" +service.GetCategoryPaginatePosts(catid, date, count).request().url().toString());
        bExecuted = false;

        repoMedia.enqueue(new Callback<ExamplePosts>() {

            @Override
            public void onResponse(Call<ExamplePosts> call,
                                   Response<ExamplePosts> response) {

                ArrayList<Locality> localityList = new ArrayList<Locality>();
                JSONArray posts = null;

                Log.d("response.isSuccessful()", response.isSuccessful() + "");
                // Log.d("response",response.body()+"");
                if (response.errorBody() != null)
                    Log.d("response-er", response.code() + "");
                // if (response.isSuccessful()) {

                if (response.isSuccessful()) {
                    Gson gson = new Gson();

                    ExamplePosts gci =
                            gson.fromJson(response.body().toString(), ExamplePosts.class);

                    //   Log.d("gci", gci.toString());
                /*
                    try {
                        posts = new JSONObject(response.body().toString()).getJSONArray("posts");
                              *//*  .replaceAll("[a-z]{1}'[a-z]{1}", "")
                        .replaceAll("[a-z]{1}\"[a-z]{1}"," ") )
                                .getJSONArray("posts") ;*//*

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("posts",posts.length()+"");
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

                                Log.d("title-manual", "title=> " + i + "==" + title);
                                //Log.d("content-manual", "content=> " + i + "==" +
                                // Html.fromHtml(content));

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

                                //Log.d("exception for post", "post" + i);
                                e.printStackTrace();

                            }
                        }
                    }*/

                    // 1 receiver 1 sender 1 cat

                    //Log.d("serviceCallsCounter-val", "serviceCallsCounter"+val++);

                    for (int i = 0; i < gci.getPosts().size(); i++) {
                        // try {

                        Locality localRecord = new Locality();
                        localRecord.setCatid(catid);

                        Post p = gci.getPosts().get(i);

                        localRecord.setId(p.getId());
                        localRecord.setContent(p.getContent());
                        localRecord.setTitle(p.getTitle());
                        localRecord.setDate(p.getDate());
                        localRecord.setType(p.getType());
                        localRecord.setPosturl(p.getUrl());

                        // Log.d("title-manual", "title=> " + i + "==" + title);
                        //Log.d("content-manual", "content=> " + i + "==" +
                        // Html.fromHtml(content));

                        //  JSONObject author = eachPost.getJSONObject("author");
                        if (p.getAuthor() != null) {
                            // String name = author.getString("name");
                            localRecord.setAuthorName(p.getAuthor().getName());
                        }

                        // JSONArray attachments = eachPost.getJSONArray("attachments");
                        if (p.getAttachments().size() != 0) {
                            // JSONObject firstAttachment = attachments.getJSONObject(0);
                            if (p.getAttachments().get(0) != null) {
                                // String urlImage = firstAttachment.getString("url");
                                localRecord.setAttUrl(p.getAttachments().get(0).getUrl());
                            }
                        }
                        // each post

                        localityList.add(localRecord);

                    }



               /* } else {
                    bExecuted = true;
                    // error response, no access to resource?
                }*/

                }
                listeners.get(0).PostDownloaded(localityList);
                bExecuted = true;
            }


            @Override
            public void onFailure(Call<ExamplePosts> call, Throwable t) {
                bExecuted = true;
                // Log.d("mediaresp=>", "error");
                // Log.d("mediarespc=>", t. + "");
                // something went completely south (like no internet connection)

                Log.d("Error", t.getMessage());

              //  listeners.get(0).PostDownloaded(null);
            }
        });
    }

}

