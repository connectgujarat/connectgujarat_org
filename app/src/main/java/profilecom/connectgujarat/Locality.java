package profilecom.connectgujarat;



import android.os.Parcel;
import android.os.Parcelable;


public class Locality implements Parcelable {

 /*   private String mDescription;
    private double mLatitude;
    private double mLongitude;*/

    int id;
    String type;
    String title;
    String status;
    String content;
    String date;
    String modified;
    String authorName;
    String attUrl;
    int catid;
    String posturl;

    public String getPosturl() {
        return posturl;
    }

    public void setPosturl(String posturl) {
        this.posturl = posturl;
    }

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    public Locality(int id, String type, String title,
                    String status, String content, String date,
                    String modified, String authorName, String attUrl,
                    int catid, String posturl) {
        super();
        this.id = id;
        this.type = type;
        this.title = title;
        this.status = status;
        this.content = content;
        this.date = date;
        this.modified = modified;
        this.authorName = authorName;
        this.attUrl = attUrl;
        this.catid = catid;
        this.posturl = posturl;

    }

    public Locality() {
        super();
    }


    @SuppressWarnings("unused")
    public Locality(Parcel in) {
        this();
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        this.id = in.readInt();
        this.type = in.readString();
        this.title = in.readString();
        this.status = in.readString();
        this.content = in.readString();
        this.date = in.readString();
        this.modified = in.readString();
        this.authorName = in.readString();
        this.attUrl = in.readString();
        this.catid = in.readInt();
        this.posturl = in.readString();

    }

    public int describeContents() {
        return 0;
    }

    public final Creator<Locality> CREATOR =
            new Creator<Locality>() {

                public Locality createFromParcel(Parcel in) {
                    return new Locality(in);
                }

                public Locality[] newArray(int size) {
                    return new Locality[size];
                }
            };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAttUrl() {
        return attUrl;
    }

    public void setAttUrl(String attUrl) {
        this.attUrl = attUrl;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeString(title);
        dest.writeString(status);
        dest.writeString(content);
        dest.writeString(date);
        dest.writeString(modified);
        dest.writeString(authorName);
        dest.writeString(attUrl);
        dest.writeInt(catid);
        dest.writeString(posturl);
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", catid=" + catid +
                '}';
    }
}