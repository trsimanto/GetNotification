package com.example.getnotification;

import android.content.Context;
import android.content.SharedPreferences;
//SharedPreferences er jonno toere kora hoese


public class SharedPref {
    SharedPreferences mySharedPref;
    public SharedPref(Context context) {
        mySharedPref =context.getSharedPreferences("filename",Context.MODE_PRIVATE);
    }



    public void setNotifyCount(int count) {
        SharedPreferences.Editor editor=mySharedPref.edit();
        editor.putInt("count",count);
        editor.commit();
    }
    public int getNotifycount() {
        int  count=mySharedPref.getInt("count",0);
        return count;
    }





    public void setMassagee(String  massagee) {
        SharedPreferences.Editor editor=mySharedPref.edit();
        editor.putString("massagee",massagee);
        editor.commit();
    }
    public String  getMassagee() {
        String   massagee=mySharedPref.getString("massagee","");
        return massagee;
    }


    public void setUnMassagee(String  massagee) {
        SharedPreferences.Editor editor=mySharedPref.edit();
        editor.putString("unmassagee",massagee);
        editor.commit();
    }
    public String  getUnMassagee() {
        String   massagee=mySharedPref.getString("unmassagee","");
        return massagee;
    }






}
