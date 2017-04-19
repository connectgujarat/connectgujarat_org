package profilecom.connectgujarat.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;

import profilecom.connectgujarat.NewsDetailsActivity;

public class CommentAddService extends Service implements CommentListener {
    CommentAddRF commmentAddService = new CommentAddRF();

    public CommentAddService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        commmentAddService.addListener(this);

        int postId = intent.getExtras().getInt("postId", 1);
        String author_name = intent.getExtras().getString("author_name", "--");
        String author_email = intent.getExtras().getString("author_email", "--");
        String content = intent.getExtras().getString("content", "--");

        CommentBody commentBody = new CommentBody();
        commentBody.setAuthor_email(author_email);
        commentBody.setAuthor_name(author_name);
        commentBody.setContent(content);

        commmentAddService.AddAComment(postId, commentBody);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void CommentSuccess(ArrayList<CommentDetails> commentDetails) {

        Intent intent = new Intent();
        intent.setAction(NewsDetailsActivity.MY_LOCAL_ACTION);

        intent.putExtra("comments", 200);

        sendBroadcast(intent);

        stopSelf();

    }


    @Override
    public void CommentFailure(ArrayList<CommentDetails> commentDetails) {

        Intent intent = new Intent();
        intent.setAction(NewsDetailsActivity.MY_LOCAL_ACTION);

        intent.putExtra("comments", 404);

        sendBroadcast(intent);

        stopSelf();

    }


}
