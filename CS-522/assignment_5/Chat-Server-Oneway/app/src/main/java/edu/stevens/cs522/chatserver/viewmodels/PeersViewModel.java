package edu.stevens.cs522.chatserver.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.stevens.cs522.chatserver.databases.ChatDatabase;
import edu.stevens.cs522.chatserver.entities.Peer;

public class PeersViewModel extends AndroidViewModel {

    public static final String TAG = PeersViewModel.class.getCanonicalName();

    private ChatDatabase chatDatabase;

    private LiveData<List<Peer>> peers;

    public PeersViewModel(Application context) {
        super(context);
        chatDatabase = ChatDatabase.getInstance(context);
        Log.i(TAG, "Creating peers view model....");
    }

    /*
     * TODO finish this
     */
    public LiveData<List<Peer>> fetchAllPeers() {
        if (peers == null){
            peers = loadPeers();
        }
        return peers;
    }

    private LiveData<List<Peer>> loadPeers() {
        return chatDatabase.peerDao().fetchAllPeers();
    }
    // End TODO

    @Override
    public void onCleared() {
        super.onCleared();
        chatDatabase = null;
        Log.i(TAG, "Clearing peers view model....");
    }
}
