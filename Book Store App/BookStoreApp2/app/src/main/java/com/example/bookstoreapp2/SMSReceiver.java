package com.example.bookstoreapp2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
    // This method onReceiver will listen for SMS broadcasts and be invoked each time it catches one.
    @Override
    public void onReceive(Context context, Intent intent) {
        // Create an array of messages and then use Telephony to get the message from intent.
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        // For loop to loop through each character of the sms and get the message body.
        for (int i = 0; i < messages.length; i++) {
            SmsMessage currentMessage = messages[i];
            String message = currentMessage.getDisplayMessageBody();

            // For every new SMS message, send a Broadcast to the MainActivity with the message in order to
            // tokenize the contents.
            Intent msgIntent = new Intent();
            msgIntent.setAction("SMS_FILTER");
            msgIntent.putExtra("SMS_MSG_KEY", message);
            context.sendBroadcast(msgIntent);
        }
    }
}