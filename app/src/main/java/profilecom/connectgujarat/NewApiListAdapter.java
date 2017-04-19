package profilecom.connectgujarat;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import profilecom.connectgujarat.Entities.Media.MediaDetails;
import profilecom.connectgujarat.R;
import profilecom.connectgujarat.Services.ImageDownloadListner;

public class NewApiListAdapter
        extends BaseAdapter implements ImageDownloadListner {
    private Context mContext;
    private static LayoutInflater inflater = null;
    private List<Locality> localuoposts;

    // private Integer Id;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter2;

    public NewApiListAdapter(Context c, List<Locality> uiPosts) {
        mContext = c;
        localuoposts = uiPosts;

        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


       formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter2 = new SimpleDateFormat("EEE, dd MMM yyyy");
    }

    @Override
    public int getCount() {
        return localuoposts.size();
    }

    @Override
    public Object getItem(int position) {
        return localuoposts.get(position);
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


        View view = convertView;
        Typeface roboto = Typeface.createFromAsset(mContext.getAssets(),
                "font/Roboto-Regular.ttf");
        //use this.getAssets if you are calling from an Activity
        // txtView.setTypeface(roboto);


        if (view == null) {
            // if it's not recycled, initialize some attributes
            view = inflater.inflate(R.layout.activity_news_list_layout, null);
           }
        textNewsHeadline = (TextView) view.findViewById(R.id.list_news_text);
        textBy = (TextView) view.findViewById(R.id.list_news_user);
        textDate = (TextView) view.findViewById(R.id.list_news_date);
        category = (TextView) view.findViewById(R.id.list_news_category);
        newsImageView = (ImageView) view.findViewById(R.id.list_news_image);

        textNewsHeadline.setText(
                Html.fromHtml(localuoposts.get(position).getTitle())
        );
        textBy.setText("By : " + localuoposts.get(position).getAuthorName());

        textNewsHeadline.setTypeface(roboto);
        textBy.setTypeface(roboto);


        int pos = 1;

        for(int i = 0; i< NewsListActivity.categoriesIds.size(); i++){
            if (localuoposts.get(position).getCatid() == NewsListActivity.categoriesIds.get(i) ) {
                pos = i;
                break;
            }
        }
        category.setText(NewsListActivity.categoriesNames.get(pos) );
        category.setTypeface(roboto);

        Date formattedDate = null;
        String finalFormattedDate = "";

        try {
            formattedDate = formatter.parse(localuoposts.get(position).getDate());

            //Log.d("datess","act date="+localuoposts.get(position).getDate()
               //     +"---formattedDate"+formattedDate);
            finalFormattedDate = formatter2.format(formattedDate);
            //Log.d("datess","finalFormattedDate date="+finalFormattedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (formattedDate != null){
            textDate.setText("Date : " + finalFormattedDate);
             textDate.setTypeface(roboto);
        }



        Display display = ((AppCompatActivity)mContext).getWindowManager().getDefaultDisplay();

        Point size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        { display.getSize(size); int width = size.x; int height = size.y;
            Log.d("wid=",width+", hei="+height);
        }
        //final int width  = mDisplay.getWidth();
        //  final int height = mDisplay.getHeight();
        // int newHeight = (width*4)/3;
        //  newsImageView.setMaxHeight(880);

        Picasso.with(mContext)
                .load(localuoposts.get(position).getAttUrl())
                .placeholder(R.drawable.placeholder)
                //.resize(480, 800)
                //.centerCrop()
                .noFade()
                .into(newsImageView);


        return view;
    }

    @Override
    public void MediaDownloaded(List<MediaDetails> mediaDetailsList) {

    }


}
