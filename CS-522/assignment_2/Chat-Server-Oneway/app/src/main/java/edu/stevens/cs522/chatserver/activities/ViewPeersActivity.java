package edu.stevens.cs522.chatserver.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.entities.Peer;


public class ViewPeersActivity extends Activity implements AdapterView.OnItemClickListener {

    public static final String PEERS_KEY = "peers";

    private ArrayAdapter<Peer> peersAdapter;
    private ListView peersList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peers);

        ArrayList<Peer> peers = getIntent().getParcelableArrayListExtra(PEERS_KEY);
        if (peers == null) {
            throw new IllegalArgumentException("Missing list of peers!");
        }

        // TODO display the list of peers, set this activity as onClick listener
        peersList = findViewById(R.id.peer_list);
        peersAdapter = new ArrayAdapter<Peer>(this, android.R.layout.simple_expandable_list_item_1, peers);
        peersList.setAdapter(peersAdapter);

        peersList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*
         * Clicking on a peer brings up details
         */
        Peer peer = peersAdapter.getItem(position);
        Intent intent = new Intent(this, ViewPeerActivity.class);
        intent.putExtra(ViewPeerActivity.PEER_KEY, peer);
        startActivity(intent);
    }
}
