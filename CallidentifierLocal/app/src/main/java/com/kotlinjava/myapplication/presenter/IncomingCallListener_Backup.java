package com.kotlinjava.myapplication.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.kotlinjava.myapplication.view.AlertDialogActivity;


public class IncomingCallListener_Backup extends BroadcastReceiver {

    private Context mContext;
    private String mobNo = "NONE";
    private Intent intentMain;

    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"IncomingCallListener..starting service",Toast.LENGTH_LONG).show();
        mContext = context;
        intentMain = intent;

        try {
            TelephonyManager tmgr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            MyPhoneStateListener PhoneListener = new MyPhoneStateListener();
            tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        } catch (Exception e) {
            // show error
            Toast.makeText(context,"IncomingCallListener..TelephonyManager error",Toast.LENGTH_LONG).show();
        }

    }

    //------------------------------------------------------------------------------------------------
    private class MyPhoneStateListener extends PhoneStateListener {

        public void onCallStateChanged(int state, String incomingNumber) {
            mobNo = incomingNumber;
            showDialogOnCall();
        }
    }

    //------------------------------------------------------------------------------------------------
    private void showDialogOnCall() {

        new Handler().postDelayed(() -> {

            Intent i = new Intent(mContext, AlertDialogActivity.class);
            i.putExtras(intentMain);
            i.putExtra("NAME", "Name");
            i.putExtra("MOBILE", mobNo);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        }, 1000);
    }
    //------------------------------------------------------------------------------------------------
}
