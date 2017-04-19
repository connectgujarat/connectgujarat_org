package profilecom.connectgujarat;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import profilecom.connectgujarat.Entities.Media.MediaDetails;
import profilecom.connectgujarat.Entities.Post.PostDetail;
import profilecom.connectgujarat.R;
import profilecom.connectgujarat.Services.ImageDownloadListner;
import profilecom.connectgujarat.Services.MediaService;


public class NewsListAdapter
        extends BaseAdapter implements ImageDownloadListner {
    private Context mContext;
    private static LayoutInflater inflater = null;
    private List<PostDetail> mpostDetails;
    // private Integer Id;
    MediaService mediaService;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter2;

    public NewsListAdapter(Context c, List<PostDetail> postDetails) {
        mContext = c;
        mpostDetails = postDetails;

        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mediaService = new MediaService();

       formatter = new SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ss");
        formatter2 = new SimpleDateFormat("EEE, MMM yy");
    }

    @Override
    public int getCount() {
        return mpostDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return mpostDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView textNewsHeadline;
        TextView textDate;
        TextView textBy;
        final ImageView newsImageView;
        TextView category;

        GridView gridView;

        View view = convertView;


        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            view = inflater.inflate(R.layout.activity_news_list_layout, null);
        } else {

            view = convertView;
        }
        textNewsHeadline = (TextView) view.findViewById(R.id.list_news_text);
        textBy = (TextView) view.findViewById(R.id.list_news_user);
        textDate = (TextView) view.findViewById(R.id.list_news_date);
        category = (TextView) view.findViewById(R.id.list_news_category);
        newsImageView = (ImageView) view.findViewById(R.id.list_news_image);

        textNewsHeadline.setText(
                Html.fromHtml(mpostDetails.get(position).getTitle().getRendered())
        );

        //TODO hardcoded for now
        textBy.setText("By : " + /*mpostDetails.get(position).getAuthor().toString()+*/ " Admin");
        category.setText("Uncategorized");

        Date formattedDate = null;
        String finalFormattedDate = "";

        try {
            formattedDate = formatter.parse(mpostDetails.get(position).getDate());
          //  Log.d("datess","act date="+mpostDetails.get(position).getDate()+
          //          "---formattedDate"+formattedDate);
            finalFormattedDate = formatter2.format(formattedDate);
          //  Log.d("datess","finalFormattedDate date="+finalFormattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (formattedDate != null)
            textDate.setText("Date : " + finalFormattedDate);
        //textDate.setText("Date : " + mpostDetails.get(position).getDate());
        //  Id = mpostDetails.get(position).getId();


        final String mediaAPI = mpostDetails.get(position).getFeaturedMedia().toString();
        //Log.d("mediaApi", mediaAPI);

                mediaService.getMedia(mediaAPI, mContext, newsImageView);

        //Log.d("SavePost", "SAvePost-load--imagelink=> " + mpostDetails.get(position).getLink());





        /*Picasso.with(mContext)
                .load(mpostDetails.get(position).getLink())
                .placeholder(R.drawable.placeholder)
                .into(newsImageView);*/


        return view;
    }

    @Override
    public void MediaDownloaded(List<MediaDetails> mediaDetailsList) {

    }


}
