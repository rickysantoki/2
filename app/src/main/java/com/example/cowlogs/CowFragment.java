package com.example.cowlogs;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.locks.Condition;


public class CowFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    private String[] BreedList = {"Angus", "Hereford", "Brahman", "Shorthorn", "Brangus"};
    Integer breedIndex;
    EditText EditText1;
    EditText EditText2;
    EditText EditText3;
    Spinner Spinner1;
    Button saveBtn;
    Button showBtn;
    TextView breedLabel;
    TextView StartView;
    DBHelper db;

    private static final int REQUEST_CODE_PERMISSION = 1;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    TrackGPS gps;

    public CowFragment() {

    }

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cow_fragment, container, false);
        breedIndex = getArguments().getInt("breed");

        breedLabel = (TextView) view.findViewById(R.id.breed_name);
        breedLabel.setText(BreedList[breedIndex]);

        EditText1 = (EditText) view.findViewById(R.id.EditText1);
        EditText2 = (EditText) view.findViewById(R.id.EditText2);
        EditText3 = (EditText) view.findViewById(R.id.EditText3);
        Spinner1 = (Spinner) view.findViewById(R.id.Spinner1);

        saveBtn = (Button) view.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new SaveBtnListener());
        showBtn = (Button) view.findViewById(R.id.showBtn);
        showBtn.setOnClickListener(new showLogsBtnListener());

        StartView = (TextView) view.findViewById(R.id.StartView);

        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), mPermission)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner1.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) new SaveBtnListener());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void clearForm() {
        EditText1.setText("");
        EditText2.setText("");
        EditText3.setText("");

    }


    public class SaveBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String id;
            String w;
            String a;
            String Condition;
            String Start;
            gps = new TrackGPS(getActivity());

            if (gps.canGetLocation()) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                StartView.setText("Details:" + obtainTime() + " Latitude: " + latitude + " Longitude: " + longitude);
            }
                try {
                    id = EditText1.getText().toString();
                   /* ID = Integer.parseInt(id);*/
                } catch (NumberFormatException e) {
                    AlertToast.message(getActivity(), "Please enter correct ID");
                    return;
                }
                try {
                    w = EditText2.getText().toString();
                   /* weight = Integer.parseInt(w);*/
                } catch (NumberFormatException e) {
                    AlertToast.message(getActivity(), "Please enter correct Weight in kgs");
                    return;
                }

                try {
                    a = EditText3.getText().toString();
                   /* Age = Integer.parseInt(a);*/
                } catch (NumberFormatException e) {
                    AlertToast.message(getActivity(), "Please enter correct Age in months");
                    return;
                }

                try {
                    /* Condition = Spinner1.getSelectedItem().toString();*/
                    Condition = String.valueOf(Spinner1.getSelectedItem());
                } catch (NumberFormatException e) {
                    AlertToast.message(getActivity(), "Please fill in the data correctly");
                    return;
                }
            try {
                Start = StartView.getText().toString();
            } catch (NumberFormatException e) {
                return;
            }



                try {
                    BreedLogs breedLogs = new BreedLogs(id, w, a, Condition, Start);
                    breedLogs.setBreedType(breedIndex);
                    MainActivity.BREED_LIST.add(breedLogs);
                    AlertToast.message(getActivity(), "Success");
                    clearForm();
                } catch (Exception e) {
                    AlertToast.message(getActivity(), "Error");
                }

            }
        }

        private class showLogsBtnListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                Fragment frag = new BreedList();
                Bundle args = new Bundle();
                args.putInt("breed", breedIndex);
                frag.setArguments(args);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.cowPlace, frag);
                obtainTime();
                ft.commit();

            }
        }

        private String obtainTime() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("dd / MM / yyyy  hh:mm");
            String strDate = mdformat.format(calendar.getTime());
            return strDate;
        }

        public void onDestroy() {
            super.onDestroy();
            gps = new TrackGPS(getActivity());
            gps.stopUsingGPS();
        }

    }





