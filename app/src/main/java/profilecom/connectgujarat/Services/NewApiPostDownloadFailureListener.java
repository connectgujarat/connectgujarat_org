package profilecom.connectgujarat.Services;

import java.util.ArrayList;

import profilecom.connectgujarat.Locality;


public interface NewApiPostDownloadFailureListener {
    void PostDownloadedFailure(ArrayList<Locality> localityList);
}
