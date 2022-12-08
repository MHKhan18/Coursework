package edu.stevens.cs522.chatserver.activities;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.entities.Peer;
import edu.stevens.cs522.chatserver.ui.TextAdapter;
import edu.stevens.cs522.chatserver.viewmodels.PeersViewModel;


public class ViewPeersActivity extends FragmentActivity implements TextAdapter.OnItemClickListener {

    /*
     * TODO See ChatServer for example of what to do, query peers database instead of messages database.
     */
    final static public String TAG = ViewPeersActivity.class.getCanonicalName();

    private TextAdapter<Peer> peerAdapter;
    private PeersViewModel peersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "(Re)starting ViewPeersActivity activity....");
        setContentView(R.layout.view_peers);

        // Initialize the recyclerview and adapter for peers
        RecyclerView peersList = findViewById(R.id.peer_list);
        peersList.setLayoutManager(new LinearLayoutManager(this));

        peerAdapter = new TextAdapter<>(peersList, this);
        peersList.setAdapter(peerAdapter);

        // TODO create the view model and query for a list of all peers
        peersViewModel = new ViewModelProvider(this).get(PeersViewModel.class);


        // TODO observer for list of peers updates the peer adapter
        peersViewModel.fetchAllPeers().observe(
                this,
                new Observer<List<Peer>>() {
                    @Override
                    public void onChanged(List<Peer> peers) {
                        peerAdapter.setDataset(peers);
                        peersList.setAdapter(peerAdapter);
                    }
                }
        );
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Leaving ViewPeersActivity activity....");
        super.onDestroy();
    }

    /*
     * Callback interface defined in TextAdapter, for responding to clicks on rows.
     */
    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        /*
         * Clicking on a peer brings up details
         */
        Peer peer = peerAdapter.getItem(position);

        Intent intent = new Intent(this, ViewPeerActivity.class);
        intent.putExtra(ViewPeerActivity.PEER_KEY, peer);
        startActivity(intent);

    }
}
