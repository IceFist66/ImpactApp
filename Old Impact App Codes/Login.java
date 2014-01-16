package ewbcalpoly.impact.ewbimpact;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	private int id;
	final Context context = this;
	private String email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		id = MainActivity.vid;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		if (MainActivity.vid != 0) {
			Intent myIntent = new Intent(Login.this, MainActivity.class);
			Login.this.startActivity(myIntent);
			Toast.makeText(getApplicationContext(), "You're Already Logged In",
					Toast.LENGTH_LONG);
		}
		Button submit = (Button) findViewById(R.id.button1);
		submit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if (id == 0) {
					EditText username = (EditText) findViewById(R.id.email);
					EditText password = (EditText) findViewById(R.id.password);
					ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
					String sUsername = username.getText().toString();
					email = username.getText().toString();
					String sPassword = password.getText().toString();
					System.out.println(sUsername + sPassword);
					// define the parameter
					postParameters.add(new BasicNameValuePair("username",
							sUsername));
					postParameters.add(new BasicNameValuePair("password",
							sPassword));
					String response = null;

					try {
						response = CustomHttpClient.executeHttpPost(
								"http://ewb-calpoly.org/volunteerLogin.php",
								postParameters);

						String result = response.toString();

						try {
							JSONArray jArray = new JSONArray(result);
							for (int i = 0; i < jArray.length(); i++) {
								JSONObject json_data = jArray.getJSONObject(i);
								Log.i("log_tag",
										"id: " + json_data.getInt("id"));
								System.out
										.println("got right before the int conversion");
								// Get an output to the screen
								id = json_data.getInt("id");
							}
						} catch (JSONException e) {
							Log.e("log_tag",
									"Error parsing data " + e.toString());
							Toast.makeText(
									getApplicationContext(),
									"Error in parsing data. Call (408) 636-6662 \n"
											+ e.toString(), Toast.LENGTH_LONG)
									.show();
						}

						try {
							
						} catch (Exception e) {
							Log.e("log_tag", "Error in Display!" + e.toString());
							Toast.makeText(
									getApplicationContext(),
									"Error in Display. Call (408) 636-6662 \n"
											+ e.toString(), Toast.LENGTH_LONG)
									.show();

						}
					} catch (Exception e) {
						Log.e("log_tag",
								"Error in http connection!!" + e.toString());
						Toast.makeText(
								getApplicationContext(),
								"Error in Connection. Call (408) 636-6662 \n"
										+ e.toString(), Toast.LENGTH_LONG)
								.show();

					}
				}

				if (id == -2) {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							context);
			 
						// set title
						alertDialogBuilder.setTitle("Unrecognized Email, have you registered?");
			 
						// set dialog message
						alertDialogBuilder
							.setMessage("Click 'Cancel' to try again")
							.setCancelable(false)
							.setPositiveButton("Register",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									Intent myIntent = new Intent(Login.this, Register.class);
									Login.this.startActivity(myIntent);								}
							  })
							.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									System.out.println("No, I didn't");
									
								}
							});
			 
							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
			 
							// show it
							alertDialog.show();
					id = 0;
				} else if (id == -1) {
					System.out.println("Already Exists");
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							context);
			 
						// set title
						alertDialogBuilder.setTitle("Invalid Password, Did you forget it?");
			 
						// set dialog message
						alertDialogBuilder
							.setMessage("Click yes to reset your password")
							.setCancelable(false)
							.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									System.out.println("Yes");
									Password pw = new Password(email);
									String reply = pw.send();
									Toast.makeText(getApplicationContext(), reply, Toast.LENGTH_LONG).show();
									
								}
							  })
							.setNegativeButton("No",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									System.out.println("No, I didn't");
									MainActivity.vid = 0;
								}
							});
			 
							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
			 
							// show it
							alertDialog.show();
						
					id = 0;
				} else if (id != 0) {
					System.out.println("Trying to start a new activity");
					MainActivity.vid = id;
					Intent myIntent = new Intent(Login.this, MainActivity.class);
					Login.this.startActivity(myIntent);
				}
			}
		});
		Button submit1 = (Button) findViewById(R.id.button2);
		submit1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent myIntent = new Intent(Login.this, Register.class);
				Login.this.startActivity(myIntent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_events:
			MainActivity.vid = 0;
			Intent myIntent = new Intent(Login.this, RegisterMore.class);
			Login.this.startActivity(myIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
