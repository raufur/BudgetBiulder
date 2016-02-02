package com.epsilon.coders.budgetbiulder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.epsilon.coders.budgetbiulder.DBHandler.DatabaseHandler;
import com.epsilon.coders.budgetbiulder.Entity.Account;
import com.epsilon.coders.budgetbiulder.Entity.Category;


public class SplashScreenActivity extends ActionBarActivity {
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = DatabaseHandler.getInstance(this);

        //Store in Shared Preferences if this is the first time the app is launched
        SharedPreferences settings = getSharedPreferences(MainActivity.PREF, 0);
        SharedPreferences.Editor editor = settings.edit();
        if(settings.getBoolean("firstLaunch", true)) {
            //Initiate the DB
            initDatabase();
            editor.putBoolean("firstLaunch", false);
            editor.commit();
        }

        //Launch the Main activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void initDatabase(){
        String pathDebut = "android.resource://" + getPackageName() + "/";

        db.addCategory(new Category("Uncategorized", Uri.parse(pathDebut + R.drawable.uncategorized).toString()));
        db.addCategory(new Category("Courses", Uri.parse(pathDebut + R.drawable.courses).toString()));
        db.addCategory(new Category("Alimentation", Uri.parse(pathDebut + R.drawable.alimentation).toString()));
        db.addCategory(new Category("Transport", Uri.parse(pathDebut + R.drawable.transport).toString()));
        db.addCategory(new Category("Logement", Uri.parse(pathDebut + R.drawable.logement).toString()));
        db.addCategory(new Category("Mobilier", Uri.parse(pathDebut + R.drawable.electromenager).toString()));
        db.addCategory(new Category("Revenu", Uri.parse(pathDebut + R.drawable.salaire).toString()));
        db.addCategory(new Category("Gadget", Uri.parse(pathDebut + R.drawable.gadget).toString()));
        db.addCategory(new Category("Shopping", Uri.parse(pathDebut + R.drawable.shopping).toString()));
        db.addCategory(new Category("Frais annexe", Uri.parse(pathDebut + R.drawable.banque).toString()));
        db.addCategory(new Category("Média", Uri.parse(pathDebut + R.drawable.media).toString()));
        db.addCategory(new Category("Administration", Uri.parse(pathDebut + R.drawable.administration).toString()));
        db.addCategory(new Category("Santé", Uri.parse(pathDebut + R.drawable.sante).toString()));
        db.addCategory(new Category("Animaux", Uri.parse(pathDebut + R.drawable.animaux).toString()));
        db.addCategory(new Category("Cadeau", Uri.parse(pathDebut + R.drawable.cadeau).toString()));

        db.addAccount(new Account("Compte courant", "Banque", Uri.parse(pathDebut + R.drawable.no_image).toString())); // Default account
    }
}
