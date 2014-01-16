package ewbcalpoly.impact.ewbimpact;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static int vid = 0;
	private String pname = "";
	private String ename = "Event Fail";
	private String start = "start Fail";
	private String stop = "stop Fail";
	private int firsteid = 0;
	private String captainP = "Captain Phone";
	private String captainE = "captain Email";
	private String location = "1 Grand Ave";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@SuppressLint("NewApi")
	protected void onResume() {
		super.onResume();
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		setContentView(R.layout.activity_main);
		if (vid == 0) {
			Intent myIntent = new Intent(MainActivity.this, Login.class);
			MainActivity.this.startActivity(myIntent);
		} else {
			webPost();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.item1:
			vid = 0;
			Intent myIntent = new Intent(MainActivity.this, Login.class);
			MainActivity.this.startActivity(myIntent);
			return true;
		case R.id.item2:
			Intent myIntent2 = new Intent(MainActivity.this, AccountInfo.class);
			MainActivity.this.startActivity(myIntent2);

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void webPost() {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		System.out.println("trying to post");
		// post nonsense to use exisitng code
		postParameters
				.add(new BasicNameValuePair("vid", Integer.toString(vid)));
		String response = null;

		try {
			response = CustomHttpClient.executeHttpPost(
					"http://ewb-calpoly.org/androidHomepage.php",
					postParameters);

			String result = response.toString();

			try {
				System.out.println(result);
				JSONArray jArray = new JSONArray(result);
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json_data = jArray.getJSONObject(i);
					Log.i("log_tag",
							"name: " + json_data.getString("pName")
									+ json_data.getString("fEvent")
									+ json_data.getString("timeStart")
									+ json_data.getString("timeStop")
									+ json_data.getString("cEmail")
									+ json_data.getString("cPhone")
									+ json_data.getString("location")
									+ json_data.getInt("eventID"));
					pname = json_data.getString("pName");
					ename = json_data.getString("fEvent");
					start = json_data.getString("timeStart");
					stop = json_data.getString("timeStop");
					captainE = json_data.getString("cEmail");
					captainP = json_data.getString("cPhone");
					location = json_data.getString("location");
					firsteid = json_data.getInt("eventID");

				}
			} catch (JSONException e) {
				captainE = "ewb.calpoly@gmail.com";
				captainP = "4086366662";
				ename = "You Have No Events";
				firsteid = 0;
				location = "1 Grand Ave, San Luis Obispo";
				start = "";
				stop = "";
				Toast.makeText(getApplicationContext(), "You have no events, \n click 'Register for More Events'", Toast.LENGTH_LONG  ).show();
			}

			try {
				setMainScreen();
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

	public void setMainScreen() {
		TextView hello = (TextView) findViewById(R.id.hello);
		hello.setText("Hello " + pname);
		TextView eventname = (TextView) findViewById(R.id.textViewEName);
		eventname.setText("Next Event: " + ename);
		TextView starts = (TextView) findViewById(R.id.textView5);
		starts.setText(start);
		TextView stops = (TextView) findViewById(R.id.textView6);
		stops.setText(stop);
		ImageButton map = (ImageButton) findViewById(R.id.imageButton3);
		map.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				String uri = "geo:0,0?q=" + location;
				startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
			}
		});
		ImageButton call = (ImageButton) findViewById(R.id.imageButton1);
		call.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				String url = "tel:" + captainP;
			    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
			    MainActivity.this.startActivity(intent);
			}
		});
		ImageButton email = (ImageButton) findViewById(R.id.imageButton2);
		email.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{captainE});
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "IMPACT");
				startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			}
		});
		Button detail = (Button) findViewById(R.id.details);
		detail.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent myIntent = new Intent(MainActivity.this, EventDetails.class);
				myIntent.putExtra("eventid", firsteid);
				MainActivity.this.startActivity(myIntent);
			}
		});
		Button registered = (Button) findViewById(R.id.button1);
		registered.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent myIntent = new Intent(MainActivity.this, RegisteredList.class);
				MainActivity.this.startActivity(myIntent);
			}
		});
		Button registermore = (Button) findViewById(R.id.button2);
		registermore.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent myIntent = new Intent(MainActivity.this, RegisterMore.class);
				MainActivity.this.startActivity(myIntent);
			}
		});
	}

}
