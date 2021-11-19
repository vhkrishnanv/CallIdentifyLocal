package com.kotlinjava.myapplication.view;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AlertDialogActivity extends AppCompatActivity {

    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        Toast.makeText(AlertDialogActivity.this, "Showing Call Information Dialog", Toast.LENGTH_LONG).show();

        String name = getIntent().getStringExtra("NAME");
        String mobileNo = getIntent().getStringExtra("MOBILE");
        Toast.makeText(AlertDialogActivity.this, "MobileNo:"+mobileNo, Toast.LENGTH_LONG).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(AlertDialogActivity.this);
        builder
                .setTitle("" + name)
                .setMessage("" + mobileNo)
                .setCancelable(false);
        alert = builder.create();
        alert.show();
    }

    public void onDestroy() {
        super.onDestroy();
        alert.dismiss();
        Log.v("Call", "onDestroy()...................");
    }
}
