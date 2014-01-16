package ewbcalpoly.impact.ewbimpact;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.MotionEvent;

public class Register extends Activity {

	private Spinner spinner1;
	private static List<String> list = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

	}

	@SuppressLint("NewApi")
	protected void onResume() {
		super.onResume();
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		setContentView(R.layout.activity_register);
		addItemsOnSpinner2("None");
		addItemsOnSpinner2("Other");
		addOrg();
		addItemsOnSpinner2("Other");

		if (MainActivity.vid != 0) {
			Intent myIntent = new Intent(Register.this, MainActivity.class);
			Register.this.startActivity(myIntent);
			Toast.makeText(getApplicationContext(), "You're Already Logged In",
					Toast.LENGTH_LONG).show();
		}
		Button submit = (Button) findViewById(R.id.button1);
		submit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				EditText name = (EditText) findViewById(R.id.editText1);
				String names = name.getText().toString();
				EditText email = (EditText) findViewById(R.id.editText2);
				String emails = email.getText().toString();
				EditText phone = (EditText) findViewById(R.id.editText3);
				String phones = phone.getText().toString();
				EditText password = (EditText) findViewById(R.id.editText4);
				String passwords = password.getText().toString();
				String organizations = String.valueOf(spinner1
						.getSelectedItem());
				if (names.isEmpty()) {
					Toast.makeText(getApplicationContext(),
							"Please Set a Name", Toast.LENGTH_LONG).show();
				} else if (emails.isEmpty()) {
					Toast.makeText(getApplicationContext(),
							"Please Set an Email", Toast.LENGTH_LONG).show();
				} else if (passwords.isEmpty()) {
					Toast.makeText(getApplicationContext(),
							"Please Set a Password", Toast.LENGTH_LONG).show();
				} else {
					Intent finishRegistration = new Intent(Register.this,
							FinishRegistration.class);
					finishRegistration.putExtra("names", names);
					finishRegistration.putExtra("emails", emails);
					finishRegistration.putExtra("phones", phones);
					finishRegistration.putExtra("passwords", passwords);
					finishRegistration.putExtra("orgs", organizations);
					Register.this.startActivity(finishRegistration);
				}
			}
		});
		spinner1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) getApplicationContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(spinner1.getWindowToken(), 0);
				return false;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_register, menu);
		return true;
	}

	public void addItemsOnSpinner2(String newOrg) {

		spinner1 = (Spinner) findViewById(R.id.spinner1);
		list.add(newOrg);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(dataAdapter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		list.clear();
	}

	public void addOrg() {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		System.out.println("trying to post");
		// post nonsense to use exisitng code
		postParameters.add(new BasicNameValuePair("nonsense",
				"don't worry about this"));
		String response = null;

		try {
			response = CustomHttpClient.executeHttpPost(
					"http://ewb-calpoly.org/volunteerHomepage.php",
					postParameters);

			String result = response.toString();

			try {
				JSONArray jArray = new JSONArray(result);
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json_data = jArray.getJSONObject(i);
					Log.i("log_tag", "name: " + json_data.getString("org"));
					System.out.println("got right before the int conversion");
					// Get an output to the screen
					String org = json_data.getString("org");
					addItemsOnSpinner2(org);
				}
			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
				Toast.makeText(
						getApplicationContext(),
						"Error in parsing data. Call (408) 636-6662 \n"
								+ e.toString(), Toast.LENGTH_LONG).show();
			}

			try {
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
