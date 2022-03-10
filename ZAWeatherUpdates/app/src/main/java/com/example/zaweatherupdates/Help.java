package com.example.zaweatherupdates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Help extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        drawerLayout = findViewById(R.id.drawer_layout);
    }
    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        //open drawer layout

        drawerLayout.openDrawer(GravityCompat.START);
    }
    public  void ClickLogo(View view){
        //close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            //close drawer if open
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        redirectActivity(this,MainActivity.class);
    }
    public void ClickSettings(View view){
        //redirecy activity to dashboard
        redirectActivity(this,SettingsActivity.class);
    }

    public void ClickInfo(View view)
    {
        redirectActivity(this,Info.class);
    }

    public void ClickPolicy(View view)
    {
        redirectActivity(this,Policy.class);
    }

    public void ClickHelp(View view)
    {
        recreate();
    }

    public void ClickExit(View view){
        //close the app
        Exit(this);
    }

    public static void Exit(Activity activity) {
        //initialize alet dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //set dialogue title
        builder.setTitle("Exit");
        //set Message
        builder.setMessage("Are you sure you want to the Close Application..?");
        //Positive button click
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish activity
                activity.finishAffinity();
                //exit application
                System.exit(0);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity,aClass);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // start the activity

        activity.startActivity(intent);
    }
    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        closeDrawer(drawerLayout);
    }
}