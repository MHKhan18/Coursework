package edu.stevens.cs522.chat.rest.client;

import java.io.OutputStream;

import edu.stevens.cs522.chat.entities.Message;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/*
 * The API for the chat server.
 *
 * TODO annotate the methods with HTTP operations and context paths
 */
public interface ServerApi {

    public final static String CHAT_NAME = "chat-name";

    public final static String LAST_SEQ_NUM = "last-seq-num";

    public Call<Void> register(String chatName);

    public Call<Void> postMessage(String chatName, Message chatMessage);

    public Call<ResponseBody> syncMessages(String chatName,
                                           long lastSeqNum,
                                           RequestBody requestBody);

}
