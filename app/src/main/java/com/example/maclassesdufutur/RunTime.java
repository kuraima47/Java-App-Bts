package com.example.maclassesdufutur;

import android.content.Context;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RunTime {

    Integer[][] tabTime;
    AccesLocal local;
    boolean already;
    static RunTime current;

    public RunTime(Context context){
        if(current==null) {
            current = this;
            current.local = new AccesLocal(context);
            current.already = false;
            setTabTime();

        }
    }

    public void setTabTime(){
        current.tabTime = current.local.current.recupAllTimeEnCours();
    }

    public Integer getTimeOfTab(Integer i){
        Integer ret = 0;
        for (Integer[] t : current.tabTime){
            if(t[0].equals(i))
                ret = t[1];
        }
        return ret;
    }

    public void UpdateTime(){
        if(!current.already) {
            current.already = true;
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(1000);
                            for (int i = 0; i < current.tabTime.length; i++) {
                                current.tabTime[i][1] += 1;
                            }

                            save();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();
        }
    }

    public void save(){
        current.local.current.Update(tabTime);
    }

}
