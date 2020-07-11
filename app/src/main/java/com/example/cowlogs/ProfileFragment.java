package com.example.cowlogs;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class ProfileFragment extends Fragment {


    EditText userName;
    EditText password;
    EditText repeatPassword;
    Button cancelBtn;
    Button saveBtn;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        saveBtn = (Button) view.findViewById(R.id.user_save);
        saveBtn.setOnClickListener(new SaveBtnListener());
        cancelBtn = (Button) view.findViewById(R.id.user_cancel);
        cancelBtn.setOnClickListener(new CancelBtnListener());

        userName = (EditText) view.findViewById(R.id.txt_username);
        password = (EditText) view.findViewById(R.id.txt_password);
        repeatPassword = (EditText) view.findViewById(R.id.txt_repeat_password);
        return view;
    }

    private class SaveBtnListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String username = userName.getText().toString();
            String Password = password.getText().toString();
            String repeat_password = repeatPassword.getText().toString();
            if (username.equals("")) {
                AlertToast.message(getActivity(), "All fields are required.");
                return;
            }
            if (Password.equals("")) {
                AlertToast.message(getActivity(), "All fields are required.");
                return;
            }
            if (repeat_password.equals("")) {
                AlertToast.message(getActivity(), "All fields are required.");
                return;
            }
            if (!Password.equals(repeat_password)) {
                AlertToast.message(getActivity(), "Password and Confirm password not matched.");
                return;
            }

            DBHelper db = new DBHelper(getActivity());

            if (db.createUser(new User(username, Password)) != null) {
                AlertToast.message(getActivity(), "User Saved.");
            }else{
                AlertToast.message(getActivity(), "User not saved.");
            }
            ((MainActivity) getActivity()).showHome();

        }
    }

    private class CancelBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ((MainActivity) getActivity()).showHome();
        }
    }
}
