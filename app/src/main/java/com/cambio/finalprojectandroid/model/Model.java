package com.cambio.finalprojectandroid.model;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.URLUtil;

import com.cambio.finalprojectandroid.MyApplication;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.cambio.finalprojectandroid.model.DataStateChange.ADDED;
import static com.cambio.finalprojectandroid.model.ModelFiles.saveImageToFile;

/**
 * Created by dvirh on 8/22/2017.
 * EventUpdateEvent method to notify event from firebase
 */

public class Model {
    public static Model instace;

    private ModelMem modelMem;
    private ModelSql modelSql;
    private ModelFirebase modelFirebase;


    private Model() {
        modelMem = new ModelMem();
        modelSql = new ModelSql(MyApplication.getMyContext());
        modelFirebase = new ModelFirebase();
        synchAndRegisterEventData();

        // modelSql.onUpgrade(modelSql.getWritableDatabase(),11,12);

    }

    public static void getInstance() {
        if (instace == null) {
            instace = new Model();
        }
    }

    public ModelMem getModelMem() {
        return modelMem;
    }

    public void setModelMem(ModelMem modelMem) {
        this.modelMem = modelMem;
    }

    public ModelSql getModelSql() {
        return modelSql;
    }

    public void setModelSql(ModelSql modelSql) {
        this.modelSql = modelSql;
    }

    public ModelFirebase getModelFirebase() {
        return modelFirebase;
    }

    public void setModelFirebase(ModelFirebase modelFirebase) {
        this.modelFirebase = modelFirebase;
    }

    public void addEvent(Event event) {
        modelFirebase.addEvent(event);
        //EventBus.getDefault().post(new EventUpdateEvent(event));
    }

    public interface GetEventCallback {
        void onComplete(Event event);

        void onCancel();
    }

    public void getEvent(String stId, final GetEventCallback callback) {

        modelFirebase.getEvent(stId, new ModelFirebase.GetEventCallback() {
            @Override
            public void onComplete(Event event) {
                callback.onComplete(event);
            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }
        });

    }

    public interface SaveImageListener {
        void complete(String url);

        void fail();
    }

    public void saveImage(final Bitmap imageBmp, final String name, final SaveImageListener listener) {
        modelFirebase.saveImage(imageBmp, name, new SaveImageListener() {
            @Override
            public void complete(String url) {
                String fileName = URLUtil.guessFileName(url, null, null);
                saveImageToFile(imageBmp, fileName);
                listener.complete(url);
            }

            @Override
            public void fail() {
                listener.fail();
            }
        });


    }


    public interface GetImageListener {
        void onSuccess(Bitmap image);

        void onFail();
    }

    public void getImage(final String url, final GetImageListener listener) {
        //check if image exsist localy
        final String fileName = URLUtil.guessFileName(url, null, null);
        ModelFiles.loadImageFromFileAsynch(fileName, new ModelFiles.LoadImageFromFileAsynch() {
            @Override
            public void onComplete(Bitmap bitmap) {
                if (bitmap != null) {
                    Log.d("TAG", "getImage from local success " + fileName);
                    listener.onSuccess(bitmap);
                } else {
                    modelFirebase.getImage(url, new GetImageListener() {
                        @Override
                        public void onSuccess(Bitmap image) {
                            String fileName = URLUtil.guessFileName(url, null, null);
                            Log.d("TAG", "getImage from FB success " + fileName);
                            saveImageToFile(image, fileName);
                            listener.onSuccess(image);
                        }

                        @Override
                        public void onFail() {
                            Log.d("TAG", "getImage from FB fail ");
                            listener.onFail();
                        }
                    });

                }
            }
        });
    }


    public interface GetAllEventsAndObserveCallback {
        void onComplete(List<Event> list);

        void onCancel();
    }

    private void synchAndRegisterEventData() {
        //1. get local lastUpdateTade
        if (MyApplication.getMyContext() != null) {
            SharedPreferences pref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
            final double lastUpdateDate = pref.getFloat("EventsLastUpdateDate", 0);
            Log.d("TAG", "lastUpdateDate: " + lastUpdateDate);

            modelFirebase.synchAndRegisterEventData(lastUpdateDate, new ModelFirebase.RegisterEventsUpdatesCallback() {
                @Override
                public void onEventUpdate(Event event, DataStateChange stateChange) {
                    switch (stateChange) {
                        case ADDED:
                            //3. update the local db
                            EventSql.addEvent(modelSql.getWritableDatabase(), event);
                            break;
                        case REMOVED:
                            EventSql.deleteEventItem(modelSql.getWritableDatabase(), event.getId());
                            break;
                        case CHANGED:
                            //TODO EventSql updatechangeQuery
                            EventSql.onUpDateEventItem(modelSql.getWritableDatabase(), event);

                            break;
                    }
                    //4. update the lastUpdateTade
                    SharedPreferences pref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
                    final double lastUpdateDate = pref.getFloat("EventsLastUpdateDate", 0);
                    if (lastUpdateDate < event.getLastUpDateTime()) {
                        SharedPreferences.Editor prefEd = MyApplication.getMyContext().getSharedPreferences("TAG",
                                Context.MODE_PRIVATE).edit();
                        prefEd.putFloat("EventsLastUpdateDate", (float) event.getLastUpDateTime());
                        prefEd.commit();
                        Log.d("TAG", "EventsLastUpdateDate: " + event.getLastUpDateTime());
                    }
                    EventBus.getDefault().post(new EventUpdateEvent(event, stateChange));


                }
            });
        }
    }

    public void getAllEvents(final GetAllEventsAndObserveCallback callback) {

        //5. read from local db
        List<Event> data = EventSql.getAllEvents(modelSql.getReadableDatabase());

        //6. return list of Events
        callback.onComplete(data);

    }

    public class EventUpdateEvent {
        public final Event event;
        public final DataStateChange stateChange;

        public EventUpdateEvent(Event event, DataStateChange stateChange) {
            this.event = event;
            this.stateChange = stateChange;
        }
    }

    public void deleteEventItem(String eventId) {

        modelFirebase.deleteEventItem(eventId);
    }

}
