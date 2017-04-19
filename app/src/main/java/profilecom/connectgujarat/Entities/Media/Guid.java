
package profilecom.connectgujarat.Entities.Media;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Guid {

    @Override
    public String toString() {
        return "Guid{" +
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
