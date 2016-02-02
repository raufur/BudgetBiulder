package com.epsilon.coders.budgetbiulder.Settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.epsilon.coders.budgetbiulder.MainActivity;
import com.epsilon.coders.budgetbiulder.R;
import com.epsilon.coders.budgetbiulder.Utility.JsonUtility;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference myPrefExport = findPreference("export_data");
        myPrefExport.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                exportData();
                return true;
            }
        });

        Preference myPrefImport = findPreference("import_data");
        myPrefImport.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                importData();
                return true;
            }
        });
    }

    private void exportData(){
        AlertDialog.Builder newDialog = new AlertDialog.Builder(getActivity());
        newDialog.setTitle("Export data");
        newDialog.setMessage("Exporting data will erase all previously exported data. Proceed?");

        newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                OutputStream fOut = null;
                String path = Environment.getExternalStorageDirectory() + "/WonderBudget";
                File directory = new File(path);
                if(!directory.exists() && !directory.isDirectory()){
                    directory.mkdirs();
                }

                String fileName = "database.json";
                File file = new File(path, fileName);
                try {
                    fOut = new FileOutputStream(file, false);
                    JsonUtility.writeJsonStream(getActivity(), fOut);
                    fOut.flush();
                    fOut.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getActivity(), SettingsFragment.this.getResources().getString(R.string.data_exported), Toast.LENGTH_SHORT).show();
            }

        });

        newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        newDialog.show();
    }

    private void importData(){
        AlertDialog.Builder newDialog = new AlertDialog.Builder(getActivity());
        newDialog.setTitle("Import data");
        newDialog.setMessage("Importing data will erase your current data. Proceed?");

        newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
            File exported = new File(Environment.getExternalStorageDirectory() + "/WonderBudget/database.json");
            if(exported.exists()){
                try {
                    InputStream in = new FileInputStream(exported);
                    JsonUtility.readJsonToDatabase(getActivity(), in);

                    // Delete the "month" and "year" in preferences, so the add recurring transactions operation would be launched again
                    SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.PREF, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.remove("month");
                    editor.remove("year");
                    editor.commit();

                    Toast.makeText(getActivity(), SettingsFragment.this.getResources().getString(R.string.data_imported), Toast.LENGTH_SHORT).show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(getActivity(), "No data has been exported yet", Toast.LENGTH_SHORT).show();
            }
            }

        });

        newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        newDialog.show();
    }
}
