package profilecom.connectgujarat.Adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;




import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import profilecom.connectgujarat.Entities.Media.MediaContent;
import profilecom.connectgujarat.Entities.Post.PostDetail;
import profilecom.connectgujarat.R;
import profilecom.connectgujarat.Services.MediaService;


public class NewsDetailsListAdapter extends BaseAdapter {
    private Context mContext;
    private static LayoutInflater inflater = null;
    private List<profilecom.connectgujarat.Entities.Post.PostDetail> mpostDetails;
    PostDetail pd;
    // private Integer Id;

    public NewsDetailsListAdapter(Context c, List<PostDetail> postDetails) {
        mContext = c;
        mpostDetails = postDetails;

        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        pd = mpostDetails.get(0);
    }

    @Override
    public int getCount() {
        return 1;
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
        TextView category;
        ImageView newsImageView;
        TextView list_news_user_2_content;

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
        newsImageView = (ImageView) view.findViewById(R.id.list_news_image);
        category = (TextView) view.findViewById(R.id.list_news_category);

        list_news_user_2_content
                = (TextView) view.findViewById(R.id.list_news_user_2_content);
        list_news_user_2_content.setVisibility(View.VISIBLE);


        /*Gson gson = new Gson();
        String content = gson.toJson(pd.getContent().getRendered());*/

        String dd = Html.fromHtml(pd.getContent().getRendered()).toString();
        list_news_user_2_content.setText(dd);


        textNewsHeadline.setText(pd.getTitle().getRendered());

        // TODO hardcoded
        textBy.setText("By : " +/* pd.getAuthor().toString()*/ " Admin");
        category.setText("Uncategorized");//+pd.getCategories());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ss");
        Date formattedDate = null;
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
        String done = "";

        try {
            formattedDate = formatter.parse(pd.getDate());
            done = formatter2.format(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (formattedDate != null)
            textDate.setText("Date : " + done);


        //  Id = mpostDetails.get(position).getId();


        /* some issue from API end- failure*/
        String mediaAPI = mpostDetails.get(position).getFeaturedMedia().toString();
        //Log.d("mediaApi", mediaAPI);
            MediaService mediaService = new MediaService();
            MediaContent mediaContentscontents =
                    mediaService.getMedia(mediaAPI, mContext, newsImageView);

       // Log.d("mediaContentscontents",mediaContentscontents.toString());

       /* Picasso.with(mContext)
                .load("http://i.img")
                .placeholder(R.drawable.placeholder)
                .into(newsImageView);*/


        return view;
    }


}
