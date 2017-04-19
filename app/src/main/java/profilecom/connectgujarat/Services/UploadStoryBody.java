package profilecom.connectgujarat.Services;


public class UploadStoryBody {

    String sendid;
    String subjectine;
    String body;
    String toid;
    String image;
    String video;
    String audio;

    @Override
    public String toString() {
        return "{" +
                "sendid='" + sendid + '\'' +
                ", subjectine='" + subjectine + '\'' +
                ", body='" + body + '\'' +
                ", toid='" + toid + '\'' +
                ", image='" + image + '\'' +
                ", video='" + video + '\'' +
                ", audio='" + audio + '\'' +
                '}';
    }

    public String getSendid() {
        return sendid;
    }

    public void setSendid(String sendid) {
        this.sendid = sendid;
    }

    public String getSubjectine() {
        return subjectine;
    }

    public void setSubjectine(String subjectine) {
        this.subjectine = subjectine;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getToid() {
        return toid;
    }

    public void setToid(String toid) {
        this.toid = toid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}
