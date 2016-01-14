package com.example.kuba.myapplication2.Services;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastCreator {
    public Toast createToast(Context context,  String value){
        Toast toast = Toast.makeText(context, value, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
        return toast;
    }


}