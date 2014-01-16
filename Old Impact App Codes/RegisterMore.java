package ewbcalpoly.impact.ewbimpact;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class RegisterMore extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_more);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		webPost();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_register_more, menu);
		return true;
	}

	public void webPost() {
		ArrayList<Event> regList = new ArrayList<Event>();
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("vid", Integer
				.toString(MainActivity.vid)));
		String response;

		try {
			response = CustomHttpClient.executeHttpPost(
					"http://ewb-calpoly.org/androidRegisterMore.php",
					postParameters);

			String result = response.toString();

			try {
				JSONArray jArray = new JSONArray(result);
				for (int i = 0; i < jArray.length(); i++) {
					System.out.println(result);
					JSONObject json_data = jArray.getJSONObject(i);
					// Get an output to the screen
					Event p = new Event(json_data.getString("ename"),
							json_data.getInt("eid"),
							json_data.getString("estart"),
							json_data.getString("estop"),
							json_data.getString("eunix"));
					if (!regList.contains(p)) {
						regList.add(p);
						System.out.println("Person" + p);
					}

				}
			} catch (JSONException e) {
				regList.clear();
			}

			try {
				Collections.sort(regList);

				System.out.println("Got to end of ListItems");
				ListView listview3 = (ListView) findViewById(R.id.listView1);
				System.out.println("Happy 1");
				CustomArrayAdapter adapter3 = new CustomArrayAdapter(this,
						R.id.listView1, regList);
				System.out.println("Happy2");
				listview3.setAdapter(adapter3);
				System.out.println("Happy3");
				listview3
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									final View view, int position, long id) {
								final Event item = (Event) parent
										.getItemAtPosition(position);
								Intent viewDetails = new Intent(
										RegisterMore.this, EventDetails.class);
								viewDetails.putExtra("eventid", item.getId());
								RegisterMore.this.startActivity(viewDetails);
							}
						});

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
