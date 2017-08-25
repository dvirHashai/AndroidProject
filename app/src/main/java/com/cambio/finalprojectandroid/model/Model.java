package com.cambio.finalprojectandroid.model;


import com.cambio.finalprojectandroid.MyApplication;

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
        //modelFirebase = new ModelFirebase();
        //synchStudentsDbAndregisterStudentsUpdates();
    }



}
