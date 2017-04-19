package profilecom.connectgujarat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import profilecom.connectgujarat.DataServices.NewPostCacheManager;
import profilecom.connectgujarat.R;

public class HomePageFragment extends Fragment {

    RelativeLayout home_page_overlay_rl;
    ImageView home_page_overlay_image;
    TextView home_page_overlay_headline;
    TextView home_page_overlay_date;
    TextView home_page_overlay_category;
    TextView home_page_overlay_user;
    ImageView home_page_overlay_down;

    public HomePageFragment() {
    }

   /* public static Fragment newInstance(Context cxt, String title, int position) {
        context = cxt;
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("position", position);
        HomePageFragment fragment = new HomePageFragment();
        fragment.setArguments(args);

        return fragment;
    }*/

    LinearLayout llll;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);


        llll = (LinearLayout) view.findViewById(R.id.llll);
        home_page_overlay_rl = (RelativeLayout) view.findViewById(R.id.home_page_overlay_rl);
        home_page_overlay_image = (ImageView) view.findViewById(R.id.home_page_overlay_image);
        home_page_overlay_headline = (TextView) view.findViewById(R.id.home_page_overlay_headline);
        home_page_overlay_date = (TextView) view.findViewById(R.id.home_page_overlay_date);
        home_page_overlay_category = (TextView) view.findViewById(R.id.home_page_overlay_category);
        home_page_overlay_user = (TextView) view.findViewById(R.id.home_page_overlay_user);
        home_page_overlay_down = (ImageView) view.findViewById(R.id.home_page_overlay_down);




        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Typeface roboto = Typeface.createFromAsset(getActivity().getAssets(),
                "font/Roboto-Regular.ttf");

        NewPostCacheManager cacheManager = new NewPostCacheManager(getActivity());
        NewsListActivity.categoriesIds = cacheManager.GetAllCategoriesIds();
        NewsListActivity.categoriesNames = cacheManager.GetAllCategoriesNames();

        home_page_overlay_rl.setVisibility(View.VISIBLE);

        if(NewsListActivity.categoriesIds.size()>0)
        NewsListActivity.list = cacheManager.
                GetFirstCatPosts(NewsListActivity.categoriesIds.get(0));



        if (NewsListActivity.list != null && NewsListActivity.list.size()>0) {
            final Locality home = NewsListActivity.list .get(0);

            Picasso.with(getActivity())
                    .load(home.getAttUrl())
                    .placeholder(R.drawable.placeholder)

                    .noFade()
                    .into(home_page_overlay_image);

            home_page_overlay_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent =
                            new Intent(getActivity(), NewsDetailsActivity.class);
                    intent.putExtra("title", home.getTitle());
                    intent.putExtra("author", home.getAuthorName());
                    intent.putExtra("date", home.getDate());
                    intent.putExtra("url", home.getAttUrl());
                    intent.putExtra("content", home.getContent());
                    intent.putExtra("cat", home.getCatid());
                    intent.putExtra("posturl", home.getPosturl());
                    intent.putExtra("postId", home.getId());
                    startActivity(intent);


                }
            });

           final Animation animShow = AnimationUtils.loadAnimation( getActivity(), R.anim.view_show);
           final Animation animHide = AnimationUtils.loadAnimation( getActivity(), R.anim.view_hide);

            llll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    home_page_overlay_rl.startAnimation( animHide);

                    home_page_overlay_rl.setVisibility(View.GONE);


                }
            });

            home_page_overlay_headline.setText(Html.fromHtml(home.getTitle()));
            home_page_overlay_headline.setTypeface(roboto);


            Date formattedDate = null;
            String finalFormattedDate = "";

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat formatter2 = new SimpleDateFormat("EEE, dd MMM yyyy");
            try {
                formattedDate = formatter.parse(home.getDate());
                finalFormattedDate = formatter2.format(formattedDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (formattedDate != null) {
                home_page_overlay_date.setText("Date : " + finalFormattedDate);
                home_page_overlay_date.setTypeface(roboto);
            }


            home_page_overlay_user.setText(home.getAuthorName());
            home_page_overlay_user.setTypeface(roboto);

            int pos = 1;
            for (int i = 0; i < NewsListActivity.categoriesIds.size(); i++) {
                if (home.getCatid() == NewsListActivity.categoriesIds.get(i)) {
                    pos = i;
                    break;
                }
            }

            home_page_overlay_category.setText(NewsListActivity.categoriesNames.get(pos));
            home_page_overlay_category.setTypeface(roboto);

            home_page_overlay_down.setVisibility(View.VISIBLE);

           /* home_page_overlay_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    home_page_overlay_rl.setVisibility(View.GONE);
                }
            });*/

        }else{
            Picasso.with(getActivity())
                    .load(R.drawable.placeholder)
                    .noFade()
                    .into(home_page_overlay_image);
        }

    }

    public String getTitle() {
        return getArguments().getString("title");
    }

    public int getPosition() {
        return getArguments().getInt("position");
    }
}
