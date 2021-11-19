package com.kotlinjava.myapplication.presenter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kotlinjava.myapplication.R;
import com.kotlinjava.myapplication.view.AlertDialogActivity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.WINDOW_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;


public class IncomingCallListener extends BroadcastReceiver {

    private Context mContext;
    private String mainIncomingNo = "NONE",name="NONE";
    private Intent intentMain;
    private WindowManager wm;
    private static LinearLayout ly1;
    private WindowManager.LayoutParams params1;

    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "IncomingCallListener............", Toast.LENGTH_LONG).show();
        mContext = context;
        intentMain = intent;
        invokeTelephoneManager(context, intent);
    }

    private void invokeTelephoneManager(Context context, Intent intent) {
        try {
            TelephonyManager tmgr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            MyPhoneStateListener PhoneListener = new MyPhoneStateListener();
            tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        } catch (Exception e) {
            // show error
            Toast.makeText(context, "IncomingCallListener..TelephonyManager error", Toast.LENGTH_LONG).show();
        }
    }

    //------------------------------------------------------------------------------------------------
    private class MyPhoneStateListener extends PhoneStateListener {

        public void onCallStateChanged(int state, String incomingNumber) {

            mainIncomingNo = incomingNumber;
            Log.v("Call", "...........mainIncomingNo:" + mainIncomingNo);
            SharedPreferences sharedpreferences;
            sharedpreferences = mContext.getSharedPreferences("MyPref", Activity.MODE_PRIVATE);
            String mobileNoPreference = sharedpreferences.getString(mainIncomingNo, null);
            Log.v("Call", "..........Before split shared pref mobileNoPreference:" + mobileNoPreference);
            if( mobileNoPreference != null && mobileNoPreference.contains("-")){
                String[] parts = mobileNoPreference.split("-");
                mobileNoPreference = parts[0]; // 004
                name = parts[1]; // 034556
            }
            Log.v("Call", "...........after split shared pref mobileNoPreference:" + mobileNoPreference);
            Log.v("Call", "...........after split shared pref...............name:" + name);
            if (mobileNoPreference != null && mobileNoPreference.equalsIgnoreCase(mainIncomingNo)) {
                //Toast.makeText(mContext,"same no.....mobileNo:"+mobileNo,Toast.LENGTH_SHORT).show();
                Log.v("Call", "If..mobileNoPreference.equalsIgnoreCase(mainIncomingNo)...............");
                showCustomPopupMenu_Type2(mContext, intentMain);
            } else if (mainIncomingNo.isEmpty()) {
                Log.v("Call", "else if....mainIncomingNo.isEmpty()............................");
                showCustomPopupMenu_Type2(mContext, intentMain);
            } else {
                Log.v("Call", "else.......NOT same no............................");
                // Toast.makeText(mContext, "NOT same no.....mobileNo:" + mobileNo, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //------------------------------------------------------------------------------------------------
    private void showCustomPopupMenu_Type2(Context context, Intent intent) {

        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //LayoutInflater layoutInflater = (LayoutInflater)getSystemService(mContext,null);
        //View valetModeWindow = layoutInflater.inflate(R.layout.contactlist_row_backup, null);
        //ViewGroup viewgroup = (ViewGroup) View.inflate(mContext, R.layout.contactlist_row_backup, null);
        ViewGroup viewgroup = (ViewGroup) View.inflate(mContext, R.layout.custom_card, null);
        TextView title = (TextView) viewgroup.findViewById(R.id.name);
        TextView phone = (TextView) viewgroup.findViewById(R.id.no);
        title.setText(""+name);
        phone.setText("" + mainIncomingNo);
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                300,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER | Gravity.CENTER;
        params.x = 0;
        params.y = 0;
        wm.addView(viewgroup, params);

        // To remove the view once the dialer app is closed.
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            String state1 = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state1.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                //wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
                if (viewgroup != null) {
                    wm.removeView(viewgroup);
                    viewgroup = null;
                    Log.v("Call", " wm.removeView(viewgroup);.................");
                }
                //((ViewGroup) viewgroup.getParent()).removeView(viewgroup);
                if(viewgroup!=null)
                viewgroup.removeAllViewsInLayout();
                //wm.removeView(viewgroup);
            }
        }

    }

    //------------------------------------------------------------------------------------------------
    private void showDialogOnCall() {

        new Handler().postDelayed(() -> {

            Intent i = new Intent(mContext, AlertDialogActivity.class);
            i.putExtras(intentMain);
            i.putExtra("NAME", name);
            i.putExtra("MOBILE", mainIncomingNo);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        }, 1000);
    }
    //------------------------------------------------------------------------------------------------
}
