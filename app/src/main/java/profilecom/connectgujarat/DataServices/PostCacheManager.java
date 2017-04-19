package profilecom.connectgujarat.DataServices;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import profilecom.connectgujarat.Entities.Post.PostDetail;
import profilecom.connectgujarat.Services.MediaServiceBeforeDBSave;


public class PostCacheManager extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "postsmanager";

    // Contacts table name
    private static final String TABLE_POSTS = "posts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "guid";
    private static final String KEY_POST = "poststring";
    private static final String KEY_CATEGORY = "postcategory";
    private static final String KEY_MEDIA = "mediastring";
    private static final String KEY_DATE = "postdate";
    private static final String KEY_LINK = "imagelink";



    public PostCacheManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public boolean IsRecordexist(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        String selectQuery = "SELECT  * FROM " + TABLE_POSTS +
                " where " + KEY_ID + " = " + id;

        Cursor cursor = db.rawQuery(selectQuery, null);
        Gson gson = new Gson();

        boolean doesExist = false;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            String postString = cursor.getString(2);
            PostDetail post = gson.fromJson(postString, PostDetail.class);

            if (post.getId() == id)
                doesExist = true;

        }
        return doesExist;
    }

    public void SavePost(int id, String guid, String post, String date, String category, String imagelink) {

        SQLiteDatabase db = this.getWritableDatabase();

        if (!IsRecordexist(id)) {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, id);
            values.put(KEY_NAME, guid);
            values.put(KEY_POST, post);
            values.put(KEY_DATE, date);
            values.put(KEY_CATEGORY, category);
            values.put(KEY_LINK, imagelink);

            try {
                // Inserting Row

                db.insert(TABLE_POSTS, null, values);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        db.close(); // Closing database connection

    }

    public String GetLastPostID() {
        return "";
    }

    public List<PostDetail> GetPostList(String tillDate) {
        List<PostDetail> posts = new ArrayList<PostDetail>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_POSTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Gson gson = new Gson();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String postString = cursor.getString(2);
                PostDetail post = gson.fromJson(postString, PostDetail.class);
                post.setLink(cursor.getString(6));
               // Log.d("post","set-post"+post.getLink());
               // Log.d("getPostList=> ","getPostList "+post.getId());
                posts.add(post);
            } while (cursor.moveToNext());
        }

        Collections.reverse(posts);

        // return contact list
        return posts;
    }

    public List<PostDetail> GetOldPostListOnLastID(int idofLast) {
        List<PostDetail> posts = new ArrayList<PostDetail>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_POSTS +
                " WHERE " + KEY_ID + " < " + idofLast;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Gson gson = new Gson();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String postString = cursor.getString(2);
                PostDetail post = gson.fromJson(postString, PostDetail.class);

                posts.add(post);
            } while (cursor.moveToNext());
        }

        // return contact list
        return posts;
    }


    public List<PostDetail> GetPostListById(Integer id) {
        List<PostDetail> posts = new ArrayList<PostDetail>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_POSTS
                + " WHERE " + KEY_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Gson gson = new Gson();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String postString = cursor.getString(2);
                PostDetail post = gson.fromJson(postString, PostDetail.class);

                posts.add(post);
            } while (cursor.moveToNext());
        }

        // return contact list
        return posts;
    }

    public List<PostDetail> GetNextFiveList(String ID) {
        return null;
    }

    public void SavePost(List<PostDetail> postDetailList) {
        // if >10 deleted
        if (postDetailList.size() >= 10)
            deleteAllPosts();
        // any <10 posts added
        for(int i=0;i< postDetailList.size();i++) {
            Gson gson = new Gson();
            PostDetail pd = postDetailList.get(i);
            String post = gson.toJson(pd);



            MediaServiceBeforeDBSave mediaService = new MediaServiceBeforeDBSave();
            String imagelink =".";
                    //mediaService.getMedia( pd.getFeaturedMedia().toString() );
            SavePost(pd.getId().intValue(), pd.getGuid().toString(),
                    post, pd.getDate(), pd.getCategories().toString(), imagelink);

         //   Log.d("SavePost", "SAvePost-imagelink=> " +imagelink);
        }
    }

    public void SaveOldPost(List<PostDetail> postDetailList) {

        List<PostDetail> pdb = GetPostList("");


       // Log.d("before", "before=>" + pdb.size() + "....extra old posts" + postDetailList.size());

        for (PostDetail pd : postDetailList) {
            Gson gson = new Gson();
            String post = gson.toJson(pd);

            MediaServiceBeforeDBSave mediaService = new MediaServiceBeforeDBSave();
            String imagelink = "..";
                  //  mediaService.getMedia(pd.getFeaturedMedia().toString());
            SavePost(pd.getId().intValue(), pd.getGuid().toString(),
                    post, pd.getDate(), pd.getCategories().toString(), imagelink);

        }

        List<PostDetail> pda = GetPostList("");
        //Log.d("after", "after=>" + pda.size() + "");

    }

    private void deleteAllPosts() {

        // Select All Query
        String deleteQuery = "DELETE * FROM " + TABLE_POSTS;

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Inserting Row
            db.execSQL(deleteQuery);//TABLE_POSTS, null, null);

            List<PostDetail> pd = GetPostList("");
            if (pd.size() == 0)
                Log.d("00xx00", "00");
        } catch (Exception e) {
            //e.printStackTrace();
        }
        db.close(); // Closing database connection


    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_POSTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_POST + " TEXT,"
                + KEY_CATEGORY + " TEXT,"
                + KEY_MEDIA + " TEXT,"
                + KEY_DATE + " DATETIME, "
                + KEY_LINK + " TEXT" + ")";
        db.execSQL(CREATE_POSTS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);

        // Create tables again
        onCreate(db);
    }

}
