package com.kotlinjava.myapplication.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class StartMyServiceAtBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Toast.makeText(context,"StartMyServiceAtBootReceiver..Restarting service",Toast.LENGTH_LONG).show();
            Intent serviceIntent = new Intent(context, MyBgService.class);
            context.startService(serviceIntent);
        }
    }
}
