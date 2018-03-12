package com.softalanta.wapi.registration.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by chris on 12/21/17.
 */

public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    public interface SmsListener {
         void messageReceived(String messageText);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle data  = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            System.out.println("*****************Received SMS*************");
            System.out.println(smsMessage);
            String sender = smsMessage.getDisplayOriginatingAddress();
            System.out.println(sender);
            //Check the sender to filter messages which we require to read

            if (sender.equals("wapi"))
            {

                String messageBody = smsMessage.getMessageBody();
                //Pass the message text to interface
                mListener.messageReceived(messageBody);

            }
        }

    }

    public static  void  bindListener(SmsListener listener){
       mListener = listener;
    }


}
