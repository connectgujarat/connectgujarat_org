package profilecom.connectgujarat.Services;

import android.text.Html;
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
import profilecom.connectgujarat.Entities.Comments.Comment;


public class CommentRetrieveRF {
    private boolean bExecuted = false;
    ICommentRetreiveComments service = null;

    private List<CommentListener> listeners
            = new ArrayList<CommentListener>(0);

   // private static int val=0;

    public void addListener(CommentListener toAdd) {
        listeners.add(toAdd);
    }


    public CommentRetrieveRF() {
        String url = "http://connectgujarat.com/"; //Resources.getSystem().getString(R.string.main_url);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ICommentRetreiveComments.class);

        //Log.d("url","url=>"+service.toString());

    }


    public void getRetreivedComments(final int catid, String date, int count) {

        Call<List<Comment>> repoRetComments = service.GetAllComments(catid, date, count);
        Log.d("url", "url(comments)=>" +
                service.GetAllComments(catid, date, count).request().url().toString());
        bExecuted = false;

        repoRetComments.enqueue(new Callback<List<Comment>>() {

            @Override
            public void onResponse(Call<List<Comment>> call,
                                   Response<List<Comment>>response) {

                ArrayList<CommentDetails> allCommentsList = new ArrayList<CommentDetails>();
                JSONArray comments = null;

                if (response.isSuccessful()) {
                    try {
                        comments = new JSONArray(response.body().toString());
                        Log.d("comments", "comments"+comments.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (comments != null) {
                        for (int i = 0; i < comments.length(); i++) {
                            try {
                                JSONObject eachComment = comments.getJSONObject(i);

                                CommentDetails commentDetails = new CommentDetails();

                                int id = eachComment.getInt("id");
                                commentDetails.setCommentId(id);

                                int postId = eachComment.getInt("post");
                                commentDetails.setPostId(postId);

                                String author_name = eachComment.getString("author_name");
                                commentDetails.setAuthorname(author_name);

                                String date = eachComment.getString("date");
                                commentDetails.setDate(date);

                                String status = eachComment.getString("status");
                                commentDetails.setStatus(status);

                                JSONObject content = eachComment.getJSONObject("content");
                                String rendered = content.getString("rendered");
                                commentDetails.setCommentContent(Html.fromHtml(rendered).toString() );

                                // each comment
                                allCommentsList.add(commentDetails);

                            } catch (JSONException e) {

                                Log.d("exception for comment", "comment" + i);
                                e.printStackTrace();

                            }
                        }
                    }

                    // 1 receiver 1 sender 1 cat

                    Log.d("allCommentsList-val", "allCommentsList" + allCommentsList.size());

                    listeners.get(0).CommentSuccess(allCommentsList);
                    bExecuted = true;

                } else {
                    bExecuted = true;
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                bExecuted = true;
                // Log.d("mediaresp=>", "error");
                // Log.d("mediarespc=>", t. + "");
                // something went completely south (like no internet connection)
                Log.d("allCommentsList-val", "allCommentsList" +"failure");

                listeners.get(0).CommentFailure(null);

                Log.d("Error", t.getMessage());

              //  listeners.get(0).PostDownloaded(null);
            }
        });
    }

}

