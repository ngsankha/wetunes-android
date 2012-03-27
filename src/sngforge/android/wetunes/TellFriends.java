package sngforge.android.wetunes;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TellFriends extends Activity implements OnClickListener {
	
	TextView sendInfo;
	EditText phNo,msg;
	Button sendBtn;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tellfriends);
		sendInfo=(TextView)findViewById(R.id.sendInfo);
		sendInfo.setText("Tell your friends about WeTunes and share and socialise with them too to get a better user experience!\n\nJust key in your friends's number and send him/her a message about WeTunes!\n\nWARNING: Standard SMS charges apply.");
		msg=(EditText)findViewById(R.id.msg);
		phNo=(EditText)findViewById(R.id.phNo);
		sendBtn=(Button)findViewById(R.id.sendBtn);
		sendBtn.setOnClickListener(this);
		msg.setText("Hey!Try out the WeTunes app from the Android App Market.It will let us socialise and share music.\n\nAfter installing,send me a friend request at this pin "+Globals.settings.getString("pin", "")+"!");
	}
	
	public void onClick(View v) {
		if(v==sendBtn)
			sendMsg();
	}
	
	public void sendMsg(){
		String phoneNo = phNo.getText().toString();
        String message = msg.getText().toString();
        if (phoneNo.length()>0 && message.length()>0 && message.length()<161)
            sendSMS(phoneNo, message);                
        else if(phoneNo.length()>0 && message.length()>0)
        	Toast.makeText(this,"Message length should be less than 160 characters!",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Please enter both phone number and message.", Toast.LENGTH_SHORT).show();
	}
	
	private void sendSMS(String phoneNumber, String message)
    {        
		String SENT = "SMS_SENT";
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,new Intent(SENT), 0);
		registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "Message sent successfully!", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Error - Generic failure", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "Error - No service", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Error - Null PDU", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Error - Radio off", 
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));
		
		PendingIntent pi = PendingIntent.getActivity(this, 0,new Intent(this, TellFriends.class), 0); 
		
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, pi);  
    }
}
