package ewbcalpoly.impact.ewbimpact;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AccountInfo extends Activity {
	private String pname;
	private String pemail;
	private String ppassword;
	private String pphone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_info);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		getValues();
		Button submit = (Button) findViewById(R.id.button1);
		submit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				setInfo();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_account_info, menu);
		return true;
	}
	public void setInfo(){
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("vid", Integer
				.toString(MainActivity.vid)));
		EditText name = (EditText) findViewById(R.id.editText1);
		EditText email = (EditText) findViewById(R.id.editText2);
		EditText phone = (EditText) findViewById(R.id.editText3);
		EditText password = (EditText) findViewById(R.id.editText4);

		postParameters.add(new BasicNameValuePair("pname", name.getText().toString()));
		postParameters.add(new BasicNameValuePair("pemail", email.getText().toString()));
		postParameters.add(new BasicNameValuePair("pphone", phone.getText().toString()));
		postParameters.add(new BasicNameValuePair("ppassword", password.getText().toString()));

		String response;

		try {
			response = CustomHttpClient.executeHttpPost(
					"http://ewb-calpoly.org/androidAccountUpdate.php",
					postParameters);

			String result = response.toString();

			try {
				JSONArray jArray = new JSONArray(result);
				for (int i = 0; i < jArray.length(); i++) {
					System.out.println(result);
					JSONObject json_data = jArray.getJSONObject(i);
					

				}
			} catch (JSONException e) {
			}

			try {
				Intent home = new Intent(AccountInfo.this, MainActivity.class);
				AccountInfo.this.startActivity(home);
			} catch (Exception e) {
				Log.e("log_tag", "Error in Display!" + e.toString());
				Toast.makeText(
						getApplicationContext(),
						"Error in Display. Call (408) 636-6662 \n"
								+ e.toString(), Toast.LENGTH_LONG).show();

			}
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection!!" + e.toString());
			Toast.makeText(
					getApplicationContext(),
					"Error in Connection. Call (408) 636-6662 \n"
							+ e.toString(), Toast.LENGTH_LONG).show();

		}
	}
	public void getValues(){
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("vid", Integer
				.toString(MainActivity.vid)));
		String response;

		try {
			response = CustomHttpClient.executeHttpPost(
					"http://ewb-calpoly.org/androidAccount.php",
					postParameters);

			String result = response.toString();

			try {
				JSONArray jArray = new JSONArray(result);
				for (int i = 0; i < jArray.length(); i++) {
					System.out.println(result);
					JSONObject json_data = jArray.getJSONObject(i);
					pname = json_data.getString("pname");
					pphone = json_data.getString("pphone");
					ppassword = json_data.getString("ppassword");
					pemail = json_data.getString("pemail");

				}
			} catch (JSONException e) {
			}

			try {
				TextView name = (TextView) findViewById(R.id.editText1);
				name.setText(pname);
				TextView email = (TextView) findViewById(R.id.editText2);
				email.setText(pemail);
				TextView phone = (TextView) findViewById(R.id.editText3);
				phone.setText(pphone);
				TextView password = (TextView) findViewById(R.id.editText4);
				password.setText(ppassword);
			} catch (Exception e) {
				Log.e("log_tag", "Error in Display!" + e.toString());
				Toast.makeText(
						getApplicationContext(),
						"Error in Display. Call (408) 636-6662 \n"
								+ e.toString(), Toast.LENGTH_LONG).show();

			}
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection!!" + e.toString());
			Toast.makeText(
					getApplicationContext(),
					"Error in Connection. Call (408) 636-6662 \n"
							+ e.toString(), Toast.LENGTH_LONG).show();

		}
	}
}
