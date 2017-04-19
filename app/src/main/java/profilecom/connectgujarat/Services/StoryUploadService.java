package profilecom.connectgujarat.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import profilecom.connectgujarat.NewsListActivity;

public class StoryUploadService extends Service implements UploadStoryListener {
    StoryUploadRF uploadService = new StoryUploadRF();

    public StoryUploadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        uploadService.addListener(this);

        String body = intent.getExtras().getString("body", "--");
        /*String image = intent.getExtras().getString("image", "--");
        String video = intent.getExtras().getString("video", "--");
        String audio = intent.getExtras().getString("audio", "--");*/

        String name = intent.getExtras().getString("name", "--");
        String email = intent.getExtras().getString("email", "--");
        String subject = intent.getExtras().getString("subject", " ");
        String story = intent.getExtras().getString("story", " ");


        UploadStoryBody uploadStoryBody = new UploadStoryBody();
        uploadStoryBody.setToid("nelson@squareconsulting.net");
        uploadStoryBody.setSendid(email);
        uploadStoryBody.setSubjectine(subject);

        uploadStoryBody.setBody(story);
        uploadStoryBody.setImage(NewsListActivity.decodedImg);
        uploadStoryBody.setVideo(NewsListActivity.decodedVid);
        uploadStoryBody.setAudio(NewsListActivity.decodedAud);
        uploadStoryBody.setBody(NewsListActivity.decodedTxt);

        //Log.d("uploadStoryBody",uploadStoryBody.toString());

        uploadService.UploadANews( uploadStoryBody);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void UploadStorySuccess(int status) {



       /* Toast.makeText(StoryUploadService.this,
                "Thank You! Your story/video/audio/image has been successfully submitted ", Toast.LENGTH_LONG).show();*/

        NewsListActivity.decodedImg = "";
        NewsListActivity.decodedTxt = "";
        NewsListActivity.decodedVid="";
        NewsListActivity.decodedAud ="--";


        Intent intent = new Intent();
        intent.setAction("MY_ACTION");
        intent.putExtra("newApiPosts", 2216);

        sendBroadcast(intent);
        stopSelf();

    }


    @Override
    public void UploadStoryFailure(int status) {

       /* Toast.makeText(StoryUploadService.this,
                "Upload failed! ", Toast.LENGTH_LONG).show();*/
        /*Toast.makeText(StoryUploadService.this,
                "Thank You! Your story/video/audio/image has been successfully submitted ", Toast.LENGTH_LONG).show();
*/
        NewsListActivity.decodedImg = "";
        NewsListActivity.decodedTxt = "";
        NewsListActivity.decodedVid="";
        NewsListActivity.decodedAud ="--";

        Intent intent = new Intent();
        intent.setAction("MY_ACTION");
        intent.putExtra("newApiPosts", 2216);

        sendBroadcast(intent);
        stopSelf();

    }


}
