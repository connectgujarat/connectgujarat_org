package profilecom.connectgujarat.Services;

import java.util.List;

import profilecom.connectgujarat.Entities.Media.MediaDetails;


public interface ImageDownloadListner {
    void MediaDownloaded(List<MediaDetails> mediaDetailsList);
}
