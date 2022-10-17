package edu.stevens.cs522.chatserver.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import edu.stevens.cs522.chatserver.entities.Chatroom;

public class SharedViewModel extends AndroidViewModel {

    public SharedViewModel(Application context) {
        super(context);
        selected = new MutableLiveData<>();
    }

    private MutableLiveData<Chatroom> selected;

    public void select(Chatroom chatroom) {
        selected.setValue(chatroom);
    }

    public Chatroom getSelected() {
        return selected.getValue();
    }

    public void observe(LifecycleOwner ui, Observer<Chatroom> observer) {
        selected.observe(ui, observer);
    }

}
