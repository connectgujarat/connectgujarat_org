package profilecom.connectgujarat.DataServices;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import profilecom.connectgujarat.Entities.NewCategoryIndex.GetCategoryIndex;
import profilecom.connectgujarat.Entities.NewCategoryModified.Example;
import profilecom.connectgujarat.Locality;


public class NewPostCacheManager extends SQLiteOpenHelper {
    // All Static variables
    // Database Version

    private static final int DATABASE_VERSION = 2
            ;
    // Database Name
    private static final String DATABASE_NAME = "pcmanager.db";

    // Contacts table name
    private static final String TABLE_ALL_POSTS = "allposts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_category_id = "categor";
    private static final String KEY_type = "type";
    private static final String KEY_title = "title";
    private static final String KEY_status = "status";
    private static final String KEY_cotent = "content";
    private static final String KEY_date = "date";
    private static final String KEY_modified = "modified";
    private static final String KEY_author_name = "authorname";
    private static final String KEY_attachment_url = "atturl";
    private static final String KEY_post_url = "posturl";



    // Contacts table name
    private static final String TABLE_CATEGORIES = "categories";
    // categories Table Columns names
    private static final String CATEGORY_ID = "catid";
    private static final String CATEGORY_NAME = "catname";


    // Contacts table name
    private static final String TABLE_HP = "homepage";
    // categories Table Columns names
    private static final String HP_ID = "id";
    private static final String HP_TYPE = "type";
    private static final String HP_TITLE = "title";
    private static final String HP_STATUS = "status";
    private static final String HP_CONTENT = "content";
    private static final String HP_DATE = "date";
    private static final String HP_MODI = "modified";
    private static final String HP_AUTNAME = "authorname";
    private static final String HP_ATTURL = "atturl";
    private static final String HP_CATID = "catid";
    private static final String HP_POSTURL = "posturl";

    private static SQLiteDatabase db = null;
    public NewPostCacheManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public boolean IsRecordexist(int id, int catId) {


        String selectQuery = "SELECT  * FROM " + TABLE_ALL_POSTS +
                " where " + KEY_ID + " = " + id +
                " and " + catId + " = " + catId;

        Cursor cursor = db.rawQuery(selectQuery, null);

        boolean doesExist = false;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {


            if (cursor.getColumnIndex(KEY_ID) == id)
                doesExist = true;

        }
        return doesExist;
    }

    public void SavePost(
            int id, int catid,
            String type, String title, String status,
            String content, String date,
            String modi, String authorname,
            String atturl, String posturl
    ) {

        try {

            if (!IsRecordexist(id, catid)) {
                ContentValues values = new ContentValues();
                values.put(KEY_ID, id);
                values.put(KEY_category_id, catid);
                values.put(KEY_type, type);
                values.put(KEY_title, title);
                values.put(KEY_status, status);
                values.put(KEY_cotent, content);
                values.put(KEY_date, date);
                values.put(KEY_modified, modi);
                values.put(KEY_author_name, authorname);
                values.put(KEY_attachment_url, atturl);
                values.put(KEY_post_url, posturl);

                try {
                    db.insert(TABLE_ALL_POSTS, null, values);
                } catch (SQLiteConstraintException e) {
                    e.printStackTrace();

                } catch (SQLException e) {
                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        } catch (SQLiteConstraintException e) {

        } catch (SQLException e) {

        } catch (Exception e) {

        }

    }

  /*  public String GetLastPostID() {
        return "";
    }*/

    public List<Locality> GetNewApiAllPostListToUI(String tillDate) {
        List<Locality> posts = new ArrayList<Locality>();


        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ALL_POSTS;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Locality p = new Locality();
                p.setId(cursor.getInt(0));
                p.setCatid(cursor.getInt(1));
                p.setType(cursor.getString(2));
                p.setTitle(cursor.getString(3));
                p.setStatus(cursor.getString(4));
                p.setContent(cursor.getString(5));
                p.setDate(cursor.getString(6));
                p.setModified(cursor.getString(7));
                p.setAuthorName(cursor.getString(8));
                p.setAttUrl(cursor.getString(9));
                p.setPosturl(cursor.getString(10));
                posts.add(p);

            } while (cursor.moveToNext());
        }

        //Collections.reverse(posts);
        // return contact list
        return posts;
    }


    public List<Locality> GetPaginatedListOnLastID(int lastId, int catId) {
        List<Locality> posts = new ArrayList<Locality>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT * FROM " + TABLE_ALL_POSTS +
                " WHERE (" +KEY_category_id + " = " + catId + " and "
                + lastId + " > "+ KEY_ID + ") order by " + KEY_ID + " desc ";
        //Log.d("selectQuery", "selectQuery"+selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Locality p = new Locality();
                p.setId(cursor.getInt(0));
                p.setCatid(cursor.getInt(1));
                p.setType(cursor.getString(2));
                p.setTitle(cursor.getString(3));
                p.setStatus(cursor.getString(4));
                p.setContent(cursor.getString(5));
                p.setDate(cursor.getString(6));
                p.setModified(cursor.getString(7));
                p.setAuthorName(cursor.getString(8));
                p.setAttUrl(cursor.getString(9));
                p.setPosturl(cursor.getString(10));

                posts.add(p);

            } while (cursor.moveToNext());
        }

       // Collections.reverse(posts);
        // return contact list
        return posts;
    }
    public List<Locality> GetSwipedPostsOnCat( int catId) {
        List<Locality> posts = new ArrayList<Locality>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT * FROM " + TABLE_ALL_POSTS +
                " WHERE ( " +KEY_category_id + " = " + catId
                + " ) order by " + KEY_ID + " desc ";
       // Log.d("selectQuery", "selectQuery"+selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Locality p = new Locality();
                p.setId(cursor.getInt(0));
                p.setCatid(cursor.getInt(1));
                p.setType(cursor.getString(2));
                p.setTitle(cursor.getString(3));
                p.setStatus(cursor.getString(4));
                p.setContent(cursor.getString(5));
                p.setDate(cursor.getString(6));
                p.setModified(cursor.getString(7));
                p.setAuthorName(cursor.getString(8));
                p.setAttUrl(cursor.getString(9));
                p.setPosturl(cursor.getString(10));

                posts.add(p);

            } while (cursor.moveToNext());
        }

        // Collections.reverse(posts);
        // return contact list
        return posts;
    }

    public ArrayList<Locality> GetFirstCatPosts(int catid) {
        ArrayList<Locality> posts = new ArrayList<Locality>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT * FROM " + TABLE_ALL_POSTS +
                " WHERE " + KEY_category_id + " = " + catid
                + " order by " + KEY_ID + " desc ";

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Locality p = new Locality();
                p.setId(cursor.getInt(0));
                p.setCatid(cursor.getInt(1));
                p.setType(cursor.getString(2));
                p.setTitle(cursor.getString(3));
                p.setStatus(cursor.getString(4));
                p.setContent(cursor.getString(5));
                p.setDate(cursor.getString(6));
                p.setModified(cursor.getString(7));
                p.setAuthorName(cursor.getString(8));
                p.setAttUrl(cursor.getString(9));
                p.setPosturl(cursor.getString(10));

                posts.add(p);

            } while (cursor.moveToNext());
        }

       // Collections.reverse(posts);
        // return contact list
        return posts;
    }


    public void SaveNewApiPostPaginate(ArrayList<Locality> localityList) {

        db = this.getWritableDatabase();

        for (int i = 0; i < localityList.size(); i++) {

            Locality local = localityList.get(i);

           // if (!IsRecordexist(local.getId(), local.getCatid())) {

                SavePost(local.getId(), local.getCatid(), local.getType(), local.getTitle(),
                        local.getStatus(), local.getContent(), local.getDate(),
                        local.getModified(), local.getAuthorName(), local.getAttUrl(),
                        local.getPosturl()
                );
            //}
        }
    }

 /*   public void SaveNewApiPost(GetCategoryPost getCategoryPost) {
        // if >10 deleted
       // if (getCategoryPost.getPosts().size() >= 10)
           // deleteAllPosts();
        // any <10 posts added
        for (int i = 0; i < getCategoryPost.getPosts().size(); i++) {
            SavePost(getCategoryPost.getPosts().get(i).getId().intValue(),
                    getCategoryPost.getPosts().get(i).getType(),
                    getCategoryPost.getPosts().get(i).getTitle(),
                    getCategoryPost.getPosts().get(i).getStatus(),
                    getCategoryPost.getPosts().get(i).getContent(),
                    getCategoryPost.getPosts().get(i).getDate(),
                    getCategoryPost.getPosts().get(i).getModified(),
                    getCategoryPost.getPosts().get(i).getAuthor().getName(),
                    getCategoryPost.getPosts().get(i).getAttachments().get(0).getUrl());

         //   Log.d("SavePost", "SAvePost-imagelink=> " +);
        }

     List<Locality> lp =   GetNewApiAllPostListToUI("");
        Log.d("lp","lp"+lp.size());

    }*/


    private void deleteAllPosts() {

        // Select All Query
        String deleteQuery = "DELETE * FROM " + TABLE_ALL_POSTS;


        try {
            db.execSQL(deleteQuery);//TABLE_POSTS, null, null);

            List<Locality> pd = GetNewApiAllPostListToUI("");
            if (pd.size() == 0)
                Log.d("00xx00", "00");
        } catch (SQLiteException e) {
            //e.printStackTrace();
        }
    }

    public ArrayList<String> getFirstImageForCats(ArrayList<Integer> allCats) {
        ArrayList<String> allUrls = new ArrayList<String>();


        for(int i=0; i < allCats.size(); i++) {
            // Select All Query
            String selectQuery = "select id,atturl from allposts where categor = "
                    + allCats.get(i) + " order by id desc";

          //  Log.d("catsetup","catsetup"+  selectQuery );

            //        select url from table where catid = i order by id desc

            if(db==null) db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);



            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                //do{
                //Log.d("catsetup","catsetup"+  cursor.getString(1) );

                String url = cursor.getString(1);

                if (url != null)
                    allUrls.add(url);
                else
                    allUrls.add("http://i.img");
                // } while (cursor.moveToNext());
            }
            else
                allUrls.add("http://i.img");
        }

        //Log.d("tally", "-"+allCats.size()+":"+allUrls.size()+"=>"+allUrls.toString());

        // Collections.reverse(posts);
        // return contact list
        return allUrls;
    }



    public void SaveAllCategories(GetCategoryIndex getCategoryIndex) {

        if(db == null)
        db = this.getWritableDatabase();

        for (int i =  getCategoryIndex.getCategories().size()-1 ; i >=0 ; i--) {

            SaveCategories(getCategoryIndex.getCategories().get(i).getId(),
                    getCategoryIndex.getCategories().get(i).getTitle());
        }

    }

    public void SaveAllCategories(List<Example> getCategoryIndex) {

        if(db == null)
            db = this.getWritableDatabase();

        for (int i =  getCategoryIndex.size()-1 ; i >=0 ; i--) {

            SaveCategories(getCategoryIndex.get(i).getId(),
                    getCategoryIndex.get(i).getName());
        }

    }
    public ArrayList<Integer> GetAllCategoriesIds() {

        ArrayList<Integer> catIds = new ArrayList<>();

        if(db == null)
            db = this.getWritableDatabase();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES;
        //Log.d("selectQuery", "selectQuery"+selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                catIds.add(cursor.getInt(0));

            } while (cursor.moveToNext());
        }
        Collections.reverse(catIds);

        return catIds;

    }

    public ArrayList<String> GetAllCategoriesNames() {

        ArrayList<String> catNames = new ArrayList<>();

        if(db == null)
            db = this.getWritableDatabase();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES;
        //Log.d("selectQuery", "selectQuery"+selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                catNames.add(cursor.getString(1));

            } while (cursor.moveToNext());
        }

        Log.d(
                "catnames",catNames.toString()
        );
        Collections.reverse(catNames);
        return catNames;

    }


    public void SaveCategories(int catid, String catname) {

        try {

            ContentValues values = new ContentValues();
            values.put(CATEGORY_ID, catid);
            values.put(CATEGORY_NAME, catname);


            try {
                db.insert(TABLE_CATEGORIES, null, values);
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();

            } catch (SQLException e) {
                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();

            }
        } catch (SQLiteConstraintException e) {

        } catch (SQLException e) {

        } catch (Exception e) {

        }

    }


    /*public List<Locality> GetNewApiAllPostListToUI(int catid) {
        List<Locality> posts = new ArrayList<Locality>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ALL_POSTS +
                " WHERE " + KEY_category_id + " = " + catid;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Locality p = new Locality();
                p.setId(cursor.getInt(0));
                p.setCatid(cursor.getInt(1));
                p.setType(cursor.getString(2));
                p.setTitle(cursor.getString(3));
                p.setStatus(cursor.getString(4));
                p.setContent(cursor.getString(5));
                p.setDate(cursor.getString(6));
                p.setModified(cursor.getString(7));
                p.setAuthorName(cursor.getString(8));
                p.setAttUrl(cursor.getString(9));
                posts.add(p);

            } while (cursor.moveToNext());
        }

        Collections.reverse(posts);
        // return contact list
        return posts;
    }*/

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALL_POSTS_TABLE = "CREATE TABLE " + TABLE_ALL_POSTS + "( "
                + KEY_ID + " INTEGER, "
                + KEY_category_id + " INTEGER, "
                + KEY_type + " TEXT, "
                + KEY_title + " TEXT, "
                + KEY_status + " TEXT, "
                + KEY_cotent + " TEXT, "
                + KEY_date + " TEXT, "
                + KEY_modified + " TEXT, "
                + KEY_author_name + " TEXT, "
                + KEY_attachment_url + " TEXT, "
                + KEY_post_url + " TEXT "
                + ")";

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + " ("
                + CATEGORY_ID + " INTEGER, "
                + CATEGORY_NAME + " TEXT "
                + ")";

        String CREATE_HP_TABLE = "CREATE TABLE " + TABLE_HP + " ("
                + HP_ID + " INTEGER, "
                + HP_TYPE + " TEXT, "
                + HP_TITLE + " TEXT, "
                + HP_STATUS + " TEXT, "
                + HP_CONTENT + " TEXT, "
                + HP_DATE + " TEXT, "
                + HP_MODI + " TEXT, "
                + HP_AUTNAME + " TEXT, "
                + HP_ATTURL + " TEXT, "
                + HP_CATID + " INTEGER, "
                + HP_POSTURL + " TEXT "
                + ")";

        db.execSQL(CREATE_HP_TABLE);
        db.execSQL(CREATE_ALL_POSTS_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);



    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALL_POSTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HP);
        // Create tables again
        onCreate(db);
    }



        /**
         * Directory that files are to be read from and written to
         **/
        protected final File DATABASE_DIRECTORY =
                new File(Environment.getExternalStorageDirectory(), "MyDirectory");

        /** File path of Db to be imported **/
        //  protected  final File IMPORT_FILE = new File(DATABASE_DIRECTORY,"MyDb.db");

        // public static final String PACKAGE_NAME = "com.example.app";
        // public static final String DATABASE_NAME = "example.db";
        // public static final String DATABASE_TABLE = "entryTable";

        /**
         * Contains: /data/data/com.example.app/databases/example.db
         **/
        private final File DATA_DIRECTORY_DATABASE =
                new File(Environment.getDataDirectory() +
                        "/data/" + "profilecom.connectgujarat" +
                        "/databases/" + DATABASE_NAME);

        /**
         * Saves the application database to the
         * export directory under MyDb.db
         **/
        public boolean exportDb() {
            Log.d("export","entered");

            if (!SdIsPresent()) return false;

            File dbFile = DATA_DIRECTORY_DATABASE;
            String filename = "MyDb.db";

            File exportDir = DATABASE_DIRECTORY;
            File file = new File(exportDir, filename);

            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            Log.d("export","dir done");


            try {
                file.createNewFile();
                copyFile(dbFile, file);

                Log.d("export","done");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        private void copyFile(File src, File dst) throws IOException {
            FileChannel inChannel = new FileInputStream(src).getChannel();
            FileChannel outChannel = new FileOutputStream(dst).getChannel();
            try {
                inChannel.transferTo(0, inChannel.size(), outChannel);
            } finally {
                if (inChannel != null)
                    inChannel.close();
                if (outChannel != null)
                    outChannel.close();
            }
        }

        /**
         * Returns whether an SD card is present and writable
         **/
        public boolean SdIsPresent() {
            return Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
        }
    public void SaveHPPost(Locality home) {

        if(db == null)
            db = this.getWritableDatabase();

        deleteAllHP();


        try {
            ContentValues values = new ContentValues();
            values.put(HP_ID, home.getId());
            values.put(HP_TITLE, home.getTitle());
            values.put(HP_AUTNAME, home.getAuthorName());
            values.put(HP_DATE, home.getDate());
            values.put(HP_ATTURL, home.getAttUrl());
            values.put(HP_CONTENT, home.getContent());
            values.put(HP_CATID, home.getCatid());
            values.put(HP_POSTURL, home.getPosturl());

            values.put(HP_TITLE, home.getTitle());
            values.put(HP_POSTURL, home.getPosturl());

            try {
                db.insert(TABLE_HP, null, values);
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();

            } catch (SQLException e) {
                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();

            }
        } catch (SQLiteConstraintException e) {

        } catch (SQLException e) {

        } catch (Exception e) {

        }

    }

    public Locality GetHPPost() {


        Locality home = null;//new Locality();

        if(db == null)
            db = this.getWritableDatabase();

        // Select All Query
        String selectQuery3 = "SELECT * FROM " + TABLE_CATEGORIES;
        Log.d("selectQuery", "selectQuery"+selectQuery3);
        Cursor cursor3 = db.rawQuery(selectQuery3, null);

        String selectQuery2 = "SELECT * FROM " + TABLE_ALL_POSTS;
        Log.d("selectQuery", "selectQuery"+selectQuery2);
        Cursor cursor2 = db.rawQuery(selectQuery2, null);

        String selectQuery = "SELECT * FROM " + TABLE_HP;
        Log.d("selectQuery", "selectQuery"+selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            home = new Locality();

            do {

                home.setId(cursor.getInt(0));
                home.setType(cursor.getString(1));
                home.setTitle(cursor.getString(2));
                home.setStatus(cursor.getString(3));
                home.setContent(cursor.getString(4));
                home.setDate(cursor.getString(5));
                home.setModified(cursor.getString(6));
                home.setAuthorName(cursor.getString(7));
                home.setAttUrl(cursor.getString(8));
                home.setCatid(cursor.getInt(9));
                home.setPosturl(cursor.getString(10));

            } while (cursor.moveToNext());
        }

      //  Log.d("home",home.toString());
        return home;

    }

    private void deleteAllHP() {

        // Select All Query
        String deleteQuery = "DELETE * FROM " + TABLE_HP;


        try {
            db.execSQL(deleteQuery);//TABLE_POSTS, null, null);

        } catch (SQLiteException e) {
            //e.printStackTrace();
        }
    }
}
