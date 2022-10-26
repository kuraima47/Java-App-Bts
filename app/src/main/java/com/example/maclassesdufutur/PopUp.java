package com.example.maclassesdufutur;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class PopUp extends Dialog {


    private TextView question;
    private Button yesButton;
    private Button noButton;


    public PopUp(Activity activity) {
        super(activity, R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.popup);
        this.question = findViewById(R.id.textView3);
        this.yesButton = findViewById(R.id.button6);
        this.noButton = findViewById(R.id.button7);
    }

    public void setQuestion(String activiteName) { this.question.setText("Veux tu démarrer l'activite " + activiteName + " ?");}

    public Button getYesButton(){return yesButton;}

    public Button getNoButton(){return noButton;}

    public void build(){
        show();
    }

}
