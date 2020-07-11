package com.example.cowlogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class BreedList extends ListFragment{

    private int breedIndex;
    private DBHelper db;
    ArrayList<BreedLogs> breedLogsArrayList;


    public BreedList() {
    }

    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_breed_list, container, false);
        breedIndex = getArguments().getInt("breed");
        db = new DBHelper(getActivity());
        if (MainActivity.filteredList(breedIndex).isEmpty()) {
            breedLogsArrayList = db.getBreedLogsByBreedType(breedIndex);
            db.close();
        } else {
            breedLogsArrayList =  MainActivity.filteredList(breedIndex);
        }
        String[] breedArray = new String[breedLogsArrayList.size()];

        for (int i = 0; i < breedLogsArrayList.size(); i++) {
            breedArray[i] = breedLogsArrayList.get(i).format();
        }
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, breedArray));
        ((Button) view.findViewById(R.id.btnReturn)).setText("Go to Previous Page");

        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

