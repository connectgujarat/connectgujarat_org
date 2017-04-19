
package profilecom.connectgujarat.Entities.NewCategoryPostsModified;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Query {

    @SerializedName("ignore_sticky_posts")
    @Expose
    private Boolean ignoreStickyPosts;
    @SerializedName("cat")
    @Expose
    private String cat;
    @SerializedName("date_query")
    @Expose
    private DateQuery dateQuery;
    @SerializedName("count")
    @Expose
    private String count;

    public Boolean getIgnoreStickyPosts() {
        return ignoreStickyPosts;
    }

    public void setIgnoreStickyPosts(Boolean ignoreStickyPosts) {
        this.ignoreStickyPosts = ignoreStickyPosts;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public DateQuery getDateQuery() {
        return dateQuery;
    }

    public void setDateQuery(DateQuery dateQuery) {
        this.dateQuery = dateQuery;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "{" +
                "ignoreStickyPosts=" + ignoreStickyPosts +
                ", cat='" + cat + '\'' +
                ", dateQuery=" + dateQuery +
                ", count='" + count + '\'' +
                '}';
    }
}
