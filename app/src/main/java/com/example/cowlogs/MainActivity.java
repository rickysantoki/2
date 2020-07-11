package com.example.cowlogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ArrayAdapter<CharSequence> adapter;
    private int currentPage = 5;
    public static List<BreedLogs> BREED_LIST = new ArrayList<>();
    ArrayList<String> data = new ArrayList<>();
    ConfirmDialogFragment dialogFragment;
    DBHelper db;
    Spinner spinner1;
    String body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHelper(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        dialogFragment = ConfirmDialogFragment.newInstance(
                "Are you sure? This will delete all the entries.");
        setSupportActionBar(toolbar);
        showCurrentPage();

        addListenerOnButton();
        addListenerOnSpinnerItemSelection();
        loadMailData();
    }

    private void addListenerOnButton() {
        spinner1 = (Spinner) findViewById(R.id.Spinner1);
    }

    private void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.Spinner1);

    }


    public void showCowPage(View view) {
        int intID = view.getId();
        Button button = (Button) findViewById(intID);
        currentPage = Integer.valueOf((String) button.getTag());
        CowFragment frag = new CowFragment();
        Bundle args = new Bundle();
        args.putInt("breed", currentPage);
        frag.setArguments(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.cowPlace, frag).commit();
    }

    public void showCurrentPage() {

        Fragment frag;
        frag = new Fragment();
        if (currentPage == 5) {
            frag = new HomeFragment();
        }
        if (currentPage >= 0 && currentPage <= 4) {
            frag = new CowFragment();
            Bundle args = new Bundle();
            args.putInt("breed", currentPage);
            frag.setArguments(args);
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.cowPlace, frag);
        ft.commit();
    }

    public void onClick(View view) {
        //Retrieve index from view's tag
        currentPage = Integer.valueOf((String) view.getTag());
        showCurrentPage();
    }

    public void showNextPage(View view) {
        if (currentPage == 4) {
            showHomePage(view);
        } else {
            currentPage++;
        }
        showCurrentPage();
    }

    public void showPreviousPage(View view) {
        if (currentPage == 0) {
            currentPage = 5;
        } else {
            currentPage--;
        }
        showCurrentPage();
    }

    public void showHomePage(View view) {
        showHome();
    }

    public void showHome() {
        currentPage = 5;
        showCurrentPage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                ProfileFragment frag = new ProfileFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.cowPlace, frag);
                ft.commit();
                return true;
            case R.id.action_save:
                //save to database
                if (saveDataToDB()) {
                    AlertToast.message(this, "data saved successfully");
                } else {
                    AlertToast.message(this, "data not saved");
                }
                return true;
            case R.id.action_send:
                final Context ctx = this;

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Are you sure? This will delete all entries and send an Email");
                alertDialogBuilder.setMessage("Save entries first?");
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                body = data.toString();
                                SQLiteDatabase db = null;
                                try {
                                    finish();
                                    startActivity(getIntent());
                                    Toast.makeText(MainActivity.this, "All entries deleted.", Toast.LENGTH_LONG).show();
                                } catch (Exception ee) {
                                    Toast.makeText(MainActivity.this, "Some error in deleting entries", Toast.LENGTH_LONG).show();
                                } finally {
                                    try {
                                        db.close();
                                    } catch (Exception ee) {
                                    }
                                }
                                if (body.equalsIgnoreCase("Date/Time:")) {
                                    body = body.replace("Date/Time:", "\nDate/Time:");
                                    //body = body.replace(",","\n\n");
                                }
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("message/rfc822");
                                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"abhiftw22@gmail.com"});
                                i.putExtra(Intent.EXTRA_SUBJECT, "New Logger Data");
                                i.putExtra(Intent.EXTRA_TEXT, "Data\n" + data.toString()+ "\n");
                                try {
                                    startActivity(Intent.createChooser(i, "Send mail..."));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(getIntent());
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure you want to exit?")
                .setMessage("Save entry to database first?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        saveDataToDB();
                        setResult(RESULT_OK, new Intent().putExtra("EXIT", true));
                        finish();
                    }
                }).create().show();

    }


    public boolean saveDataToDB() {
        try {
            for (BreedLogs b : BREED_LIST) {
                db.createBreedLog(b);
            }
            BREED_LIST.clear();
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public void returnToCowPage(View view) {
        Fragment frag = new CowFragment();
        Bundle args = new Bundle();
        args.putInt("breed", currentPage);
        frag.setArguments(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.cowPlace, frag);
        ft.commit();
    }

    public static ArrayList<BreedLogs> filteredList(int breedIndex) {
        ArrayList<BreedLogs> result = new ArrayList<>();
        for (BreedLogs breedLogs : BREED_LIST) {
            if (breedLogs.getBreedType() == breedIndex) {
                result.add(breedLogs);
            }
        }
        return result;
    }

    Cursor c;

    private void loadMailData() {
        SQLiteDatabase db = openOrCreateDatabase("BreedLogsDB", MODE_PRIVATE, null);
        try {
            String q = " SELECT * FROM " + BreedLogs.TABLE_NAME;
            c = db.rawQuery(q, null);

            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndex(BreedLogs.COLUMN_BREED_ID));
                String age = c.getString(c.getColumnIndex(BreedLogs.COLUMN_AGE));
                String weight = c.getString(c.getColumnIndex(BreedLogs.COLUMN_WEIGHT));
                String condition = c.getString(c.getColumnIndex(BreedLogs.COLUMN_CONDITION));
                String start = c.getString(c.getColumnIndex(BreedLogs.COLUMN_START));
                String mix = id + " " + age + " " + weight + " " + condition + " " + start + "\n\n";
                data.add(mix);
            }
        }catch(Exception e){
            Log.e("SQL PROBLEM", "Cannot select");
        }
        finally {
            try {
                db.close();
            }catch (Exception ee){
            }
        }
    }
}
