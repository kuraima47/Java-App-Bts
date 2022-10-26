package com.example.maclassesdufutur;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Dictionary;


public class AccesLocal {

    private String nomBase = "paulCornu.sqlite";
    private Integer versionBase = 1;
    private SqlLite accesBDD;
    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;
    static public AccesLocal current;

    public AccesLocal(Context context){
        if(current == null) {
            accesBDD = new SqlLite(context, nomBase, null, versionBase);
            dbRead = accesBDD.getReadableDatabase();
            dbWrite = accesBDD.getWritableDatabase();
            current = this;
        }
    }

    public void ajout(String activite, String tache, Integer time, String status){
        String request = "insert into taches (nameActivite,nameTache,status,time) values";
        request += "(\""+activite+"\",\""+tache+"\",\""+status+"\","+time+")";
        current.dbWrite.execSQL(request);
    }

    public void delete(String activite, String tache){
        String request = "delete from taches where nameActivite=\""+activite+"\"and nameTache=\""+tache+"\"";
        current.dbWrite.execSQL(request);
    }

    @SuppressLint("Range")
    public String[][] StopAllActivite(String activite){
        String req = "select nameTache, time from taches where nameActivite=\""+activite+"\"";
        Cursor cursor = current.dbRead.rawQuery(req, null);
        String[][] times = new String[cursor.getCount()][2];
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String[] columnNames = cursor.getColumnNames();
            for (int i = 0; i < columnNames.length; i++) {
                times[cursor.getPosition()][i] = cursor.getString(cursor.getColumnIndex(columnNames[i]));
            }
        }
        cursor.close();
        return times;
    }

    @SuppressLint("Range")
    public String getStatus(String activite, String tache){
        String rep = "";
        String req = "select status from taches where nameActivite=\""+activite+"\" and nameTache=\""+tache+"\"";
        Cursor cursor = current.dbRead.rawQuery(req, null);
        if(cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                String[] columnNames = cursor.getColumnNames();
                for (int i = 0; i < columnNames.length; i++) {
                    rep = cursor.getString(cursor.getColumnIndex(columnNames[i]));
                }
            }
        }
        cursor.close();
        return rep;
    }

    public void changeStatus(String activite, String tache, String status){
        String request = "UPDATE taches SET status=\"" + status + "\" WHERE nameActivite=\"" + activite + "\" and nameTache=\"" + tache + "\"";
        current.dbWrite.execSQL(request);
    }


    public void Update(Integer[][] tab){
        if(tab != null) {
            for (Integer[] integers : tab) {
                String request = "UPDATE taches SET time = " + integers[1] + " WHERE id=" + integers[0];
                current.dbWrite.execSQL(request);
            }
        }
    }

    @SuppressLint("Range")
    public Integer recupereTime(String activite, String tache){
        int time = 0;
        String req = "select time from taches where nameActivite=\""+activite+"\" and nameTache=\""+tache+"\"";
        Cursor cursor = current.dbRead.rawQuery(req, null);
        if(cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                String[] columnNames = cursor.getColumnNames();
                for (int i = 0; i < columnNames.length; i++) {
                    time = cursor.getInt(cursor.getColumnIndex(columnNames[i]));
                    System.out.println(time);
                }
            }
        }
        cursor.close();
        return time;
    }

    @SuppressLint("Range")
    public Integer[][] recupAllTimeEnCours(){
        String req = "select id, time from taches where status=\"enCours\"";
        Cursor cursor = current.dbRead.rawQuery(req, null);
        Integer[][] times = new Integer[cursor.getCount()][2];
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String[] columnNames = cursor.getColumnNames();
            for (int i = 0; i < columnNames.length; i++) {
                times[cursor.getPosition()][i] = cursor.getInt(cursor.getColumnIndex(columnNames[i]));
            }
        }
        cursor.close();
        return times;
    }

    @SuppressLint("Range")
    public Integer getID(String activite, String tache){
        int id = 0;
        String req = "select id from taches where nameActivite=\""+activite+"\" and nameTache=\""+tache+"\"";
        Cursor cursor = current.dbRead.rawQuery(req, null);
        if(cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                String[] columnNames = cursor.getColumnNames();
                for (int i = 0; i < columnNames.length; i++) {
                    id = cursor.getInt(cursor.getColumnIndex(columnNames[i]));
                }
            }
        }
        cursor.close();
        return id;
    }
}
