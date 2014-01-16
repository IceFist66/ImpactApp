package ewbcalpoly.impact.ewbimpact;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FinishRegistration extends Activity {
	private TextView other;
	private Button submit;
	private int id;
	final Context context = this;
	private String email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finish_registration);

		System.out.println(intent.getExtras().getString("names"));
		System.out.println(intent.getExtras().getString("orgs"));
	}

	@SuppressLint("NewApi")
	protected void onResume() {
		super.onResume();
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		Intent intent = getIntent();
		setContentView(R.layout.activity_finish_registration);
		other = (TextView) findViewById(R.id.editText1);
		if (intent.getExtras().getString("orgs").equalsIgnoreCase("Other")) {
			System.out.println("Do Someting Here");
			other.setVisibility(View.VISIBLE);
		}
		Button submit = (Button) findViewById(R.id.button1);
		submit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				CheckBox cb = (CheckBox) findViewById(R.id.checkBox1);
				if (cb.isChecked()) {
					int returnID = webPost();
					if (returnID == -1) {
						System.out.println("Already Exists");
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);

						// set title
						alertDialogBuilder
								.setTitle("This email has already been used, did you forget your password?");

						// set dialog message
						alertDialogBuilder
								.setMessage("Click yes to reset your password")
								.setCancelable(false)
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												System.out.println("Yes");
												Password pw = new Password(
														email);
												String reply = pw.send();
												Toast.makeText(
														getApplicationContext(),
														reply,
														Toast.LENGTH_LONG)
														.show();
												Intent main = new Intent(
														FinishRegistration.this,
														Login.class);
												FinishRegistration.this
														.startActivity(main);
											}
										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												System.out
														.println("No, I didn't");
												MainActivity.vid = 0;
												Intent myIntent = new Intent(
														FinishRegistration.this,
														Login.class);
												FinishRegistration.this
														.startActivity(myIntent);
											}
										});

						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();

						// show it
						alertDialog.show();

					} else {
						MainActivity.vid = returnID;
						System.out.println("Clicked");
						Intent main = new Intent(FinishRegistration.this,
								MainActivity.class);
						FinishRegistration.this.startActivity(main);
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"You Must Accept the Terms and Conditions",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_finish_registration, menu);
		return true;
	}

	public int webPost() {
		Intent intent = getIntent();
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

		if (intent.getExtras().getString("orgs").equalsIgnoreCase("Other")) {
			EditText org = (EditText) findViewById(R.id.editText1);
			String orgString = org.getText().toString();
			postParameters
					.add(new BasicNameValuePair("organization", orgString));
		} else {
			postParameters.add(new BasicNameValuePair("organization", intent
					.getExtras().getString("orgs")));
		}
		postParameters.add(new BasicNameValuePair("username", intent
				.getExtras().getString("names")));
		postParameters.add(new BasicNameValuePair("email", intent.getExtras()
				.getString("emails")));
		email = intent.getExtras().getString("emails");
		postParameters.add(new BasicNameValuePair("phone", intent.getExtras()
				.getString("phones")));
		postParameters.add(new BasicNameValuePair("password", intent
				.getExtras().getString("passwords")));

		String response = null;

		try {
			response = CustomHttpClient.executeHttpPost(
					"http://ewb-calpoly.org/androidRegister.php",
					postParameters);

			String result = response.toString();

			try {
				JSONArray jArray = new JSONArray(result);
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json_data = jArray.getJSONObject(i);
					Log.i("log_tag", "id: " + json_data.getInt("id"));
					System.out.println("got right before the int conversion");
					// Get an output to the screen
					id = json_data.getInt("id");
				}
			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + "[" + result + "]");
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
		return id;
	}

}
