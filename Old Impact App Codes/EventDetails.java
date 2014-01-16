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
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class EventDetails extends Activity {
	private int eventid;
	private String fEvent;
	private String timeStart;
	private String timeStop;
	private String captainP = "4086366662";
	private String captainE = "ewb.calpoly@gmail.com";
	private String location;
	private String locationh;
	private String desc;
	private String bring;
	private boolean registered = false;
	private int numN;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_details);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		Intent intent = getIntent();
		eventid = intent.getExtras().getInt("eventid");
		webPost();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_event_details, menu);
		return true;
	}

	public void webPost() {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		System.out.println("trying to post");
		// post nonsense to use exisitng code
		postParameters.add(new BasicNameValuePair("vid", Integer
				.toString(MainActivity.vid)));
		postParameters.add(new BasicNameValuePair("eid", Integer
				.toString(eventid)));
		String response = null;

		try {
			response = CustomHttpClient.executeHttpPost(
					"http://ewb-calpoly.org/androidEvent.php", postParameters);

			String result = response.toString();

			try {
				System.out.println(result);
				JSONArray jArray = new JSONArray(result);
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json_data = jArray.getJSONObject(i);
					captainE = json_data.getString("cEmail");
					captainP = json_data.getString("cPhone");
					desc = json_data.getString("desc");
					fEvent = json_data.getString("fEvent");
					location = json_data.getString("location");
					locationh = json_data.getString("locationh");
					timeStart = json_data.getString("timeStart");
					timeStop = json_data.getString("timeStop");
					bring = json_data.getString("bring");
					registered = json_data.getBoolean("registered");
					numN = json_data.getInt("num");

				}
			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
				Toast.makeText(
						getApplicationContext(),
						"Error in parsing data. Call (408) 636-6662 \n"
								+ e.toString(), Toast.LENGTH_LONG).show();
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
		bring = bring.replaceAll("<br>", "\n");
		desc = desc.replaceAll("<br>", "\n");
		TextView eventname = (TextView) findViewById(R.id.eventName);
		eventname.setText(fEvent);
		TextView desc1 = (TextView) findViewById(R.id.desc);
		desc1.setText(desc);
		TextView location1 = (TextView) findViewById(R.id.location);
		location1.setText(locationh);
		TextView start = (TextView) findViewById(R.id.starttime);
		start.setText(timeStart);
		TextView stop = (TextView) findViewById(R.id.endtime);
		stop.setText(timeStop);
		TextView bring1 = (TextView) findViewById(R.id.bring);
		bring1.setText(bring);
		TextView needed = (TextView) findViewById(R.id.textView5);
		needed.setText("[" + numN + " Spaces Avalible]");
		if (registered) {
			Button cancelreg = (Button) findViewById(R.id.button1);
			cancelreg.setText("Cancel");
		} else if (numN < 1) {
			Button cancelreg = (Button) findViewById(R.id.button1);
			cancelreg.setText("All Full");
		}

		ImageButton map = (ImageButton) findViewById(R.id.imageButton3);
		map.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				String uri = "geo:0,0?q=" + location;
				startActivity(new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse(uri)));
			}
		});
		ImageButton call = (ImageButton) findViewById(R.id.imageButton1);
		call.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				String url = "tel:" + captainP;
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
				EventDetails.this.startActivity(intent);
			}
		});
		ImageButton email = (ImageButton) findViewById(R.id.imageButton2);
		email.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				final Intent emailIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
						new String[] { captainE });
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						"IMPACT");
				startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			}
		});
		if (numN > 0 || registered) {

			Button reg = (Button) findViewById(R.id.button1);
			reg.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {
					if (MainActivity.vid == 0) {
						Intent login = new Intent(EventDetails.this,
								Login.class);
						EventDetails.this.startActivity(login);
					} else {
						Register();
					}
				}
			});

		}
	}

	public void Register() {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		System.out.println("trying to post");
		// post nonsense to use exisitng code
		postParameters.add(new BasicNameValuePair("vid", Integer
				.toString(MainActivity.vid)));
		postParameters.add(new BasicNameValuePair("event", Integer
				.toString(eventid)));
		String response = null;

		try {
			response = CustomHttpClient.executeHttpPost(
					"http://ewb-calpoly.org/androidRegisterEvent.php",
					postParameters);

			String result = response.toString();

			try {
				System.out.println(result);
				JSONArray jArray = new JSONArray(result);
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json_data = jArray.getJSONObject(i);
				}
			} catch (JSONException e) {

			}

			try {
				finish();
				startActivity(getIntent());
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
