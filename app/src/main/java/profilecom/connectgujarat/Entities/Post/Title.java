
package profilecom.connectgujarat.Entities.Post;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Title {
    @Override
    public String toString() {
        return "Title{" +
                "rendered='" + rendered + '\'' +
                '}';
    }

    @SerializedName("rendered")
    @Expose
    private String rendered;

    /**
     * 
     * @return
     *     The rendered
     */
    public String getRendered() {
        return rendered;
    }

    /**
     * 
     * @param rendered
     *     The rendered
     */
    public void setRendered(String rendered) {
        this.rendered = rendered;
    }

}
