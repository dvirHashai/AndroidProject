package com.cambio.finalprojectandroid.model;


import android.graphics.Bitmap;
import android.webkit.URLUtil;

import com.cambio.finalprojectandroid.MyApplication;

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
}
