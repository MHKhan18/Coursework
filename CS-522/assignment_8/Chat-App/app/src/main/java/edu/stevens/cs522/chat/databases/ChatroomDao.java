package edu.stevens.cs522.chat.databases;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import edu.stevens.cs522.chat.entities.Chatroom;

@Dao
/*
 * Make sure to declare an index on chatroom name, that specifies chat names are unique.
 */
public abstract class ChatroomDao {

    @Query("SELECT id FROM Chatroom WHERE name = :name LIMIT 1")
    protected abstract long getChatRoomId(String name);

    @Query("SELECT * FROM Chatroom")
    public abstract LiveData<List<Chatroom>> fetchAllChatrooms();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insert(Chatroom chatroom);

    @Transaction
    public void upsert(Chatroom chatroom){
        long id = getChatRoomId(chatroom.name);
        if (id == 0){
            insert(chatroom);
        }
    }

}
