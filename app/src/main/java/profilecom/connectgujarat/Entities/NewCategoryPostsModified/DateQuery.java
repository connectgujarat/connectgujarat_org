
package profilecom.connectgujarat.Entities.NewCategoryPostsModified;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateQuery {

    @SerializedName("before")
    @Expose
    private String before;

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    @Override
    public String toString() {
        return "{" +
                "before='" + before + '\'' +
                '}';
    }
}
