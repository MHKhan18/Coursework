package edu.stevens.cs522.chatserver.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import edu.stevens.cs522.base.DateUtils;
import edu.stevens.cs522.base.InetAddressUtils;
import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.entities.Peer;

/**
 * Created by dduggan.
 */

public class ViewPeerActivity extends Activity {

    public static final String PEER_KEY = "peer";

    private TextView userName;
    private TextView timestamp;
    private TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peer);

        Peer peer = getIntent().getParcelableExtra(PEER_KEY);
        if (peer == null) {
            throw new IllegalArgumentException("Expected peer as intent extra");
        }

        // TODO Set the fields of the UI
        userName = findViewById(R.id.view_user_name);
        timestamp = findViewById(R.id.view_timestamp);
        location = findViewById(R.id.view_location);

        String nameField = getResources().getString(R.string.view_user_name);
        String timeField = getResources().getString(R.string.view_timestamp);
        String locationField = getResources().getString(R.string.view_location);

        userName.setText(String.format(nameField, peer.name));
        timestamp.setText(String.format(timeField, formatTimestamp((peer.timestamp))));
        location.setText(String.format(locationField, peer.latitude, peer.longitude));


    }

    private static String formatTimestamp(Date timestamp) {
        LocalDateTime dateTime = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

}
