package com.adgn.testbrocast;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Brocast extends BroadcastReceiver {
    private Context mContext;
    private int mCurrentState = TelephonyManager.CALL_STATE_IDLE ;
    private int mOldState = TelephonyManager.CALL_STATE_IDLE ;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("userbrocas","bbb");
        mContext = context;
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            mOldState = PreferenceHelper.getInt("ggt", mContext);

            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d("userbrocas","CALL_STATE_IDLE");

                    mCurrentState = TelephonyManager.CALL_STATE_IDLE;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d("userbrocas","OFFHOOK");
                    mCurrentState = TelephonyManager.CALL_STATE_OFFHOOK;
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d("userbrocas","RINGING");
                    mCurrentState = TelephonyManager.CALL_STATE_RINGING;
                    break;
            }

            if(mOldState == TelephonyManager.CALL_STATE_IDLE && mCurrentState == TelephonyManager.CALL_STATE_OFFHOOK ) {
                Log.i("userbrocas", "onCallStateChanged: 接通");
                PreferenceHelper.putInt("ggt", mCurrentState, mContext);
            } else if (mOldState == TelephonyManager.CALL_STATE_OFFHOOK && mCurrentState == TelephonyManager.CALL_STATE_IDLE) {
                Log.i("userbrocas", "onCallStateChanged: 結束通話");
                PreferenceHelper.putInt("ggt", mCurrentState, mContext);
            }
        }
    }
}
