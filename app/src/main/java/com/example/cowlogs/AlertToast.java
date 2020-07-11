package com.example.cowlogs;

import android.content.Context;
import android.widget.Toast;

public class AlertToast {
    public static void message(Context context, String message) {
        Toast.makeText(context, message, Toast
                .LENGTH_SHORT).show();
    }
}
