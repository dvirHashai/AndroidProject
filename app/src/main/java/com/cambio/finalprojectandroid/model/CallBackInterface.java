package com.cambio.finalprojectandroid.model;

import android.graphics.Bitmap;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * Created by Chen on 30/08/2017.
 */

public interface CallBackInterface {


    interface SaveImageListener {
        void complete(String url);

        void fail();
    }


    interface GetImageListener {
        void onSuccess(Bitmap image);

        void onFail();
    }

    interface GetAllEventsAndObserveCallback {
        void onComplete(List<Event> list);

        void onCancel();
    }

    interface GetEventCallback {
        void onComplete(Event event);

        void onCancel();
    }

    interface RegisterEventsUpdatesCallback {
        void onEventUpdate(Event event, DataStateChange StateChange);
    }

    interface GetUserCallback {
        void onComplete(User user);

        void onCancel();
    }

    interface RegisterUserCallBack {
        void onComplete(FirebaseUser user, Task<Void> task);
    }

    interface LoginUserCallBack {
        void onComplete(Task<AuthResult> task);
    }

}
