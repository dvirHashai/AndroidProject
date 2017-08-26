package com.cambio.finalprojectandroid.model;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.URLUtil;

import com.cambio.finalprojectandroid.MyApplication;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.cambio.finalprojectandroid.model.ModelFiles.saveImageToFile;

/**
 * Created by dvirh on 8/22/2017.
 */

public class Model {
    public final static Model instace = new Model();

    private ModelMem modelMem;
    private ModelSql modelSql;
    private ModelFirebase modelFirebase;

    private Model() {
        modelMem = new ModelMem();
       // modelSql = new ModelSql(MyApplication.getMyContext());
        modelFirebase = new ModelFirebase();
        //synchStudentsDbAndregisterStudentsUpdates();
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

    public void addEvent(Event event){
        modelFirebase.addEvent(event);
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
                saveImageToFile(imageBmp,fileName);
                listener.complete(url);
            }

            @Override
            public void fail() {
                listener.fail();
            }
        });


    }

  /*  public interface GetAllStudentsAndObserveCallback {
        void onComplete(List<Event> list);
        void onCancel();
    }

    private void synchStudentsDbAndregisterStudentsUpdates() {
        //1. get local lastUpdateTade
        SharedPreferences pref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        final double lastUpdateDate = pref.getFloat("StudnetsLastUpdateDate",0);
        Log.d("TAG","lastUpdateDate: " + lastUpdateDate);

        modelFirebase.RegisterEventsUpdates(lastUpdateDate,new ModelFirebase.RegisterEventsUpdatesCallback() {
            @Override
            public void onStudentUpdate(Event event) {
                //3. update the local db
                EventSql.addStudent(modelSql.getWritableDatabase(),event);
                //4. update the lastUpdateTade
                SharedPreferences pref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
                final double lastUpdateDate = pref.getFloat("StudnetsLastUpdateDate",0);
                if (lastUpdateDate < event.lastUpdateDate){
                    SharedPreferences.Editor prefEd = MyApplication.getMyContext().getSharedPreferences("TAG",
                            Context.MODE_PRIVATE).edit();
                    prefEd.putFloat("StudnetsLastUpdateDate", (float) event.lastUpdateDate);
                    prefEd.commit();
                    Log.d("TAG","StudnetsLastUpdateDate: " + event.lastUpdateDate);
                }

                EventBus.getDefault().post(new UpdateStudentEvent(student));
            }
        });
    }

    public void getAllStudents(final GetAllStudentsAndObserveCallback callback){

        //5. read from local db
        List<Student> data = StudentSql.getAllStudents(modelSql.getReadableDatabase());

        //6. return list of students
        callback.onComplete(data);

    }

    public class UpdateStudentEvent {
        public final Student student;
        public UpdateStudentEvent(Student student) {
            this.student = student;
        }
    }
*/
}
