package ewbcalpoly.impact.ewbimpact;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class Password {

	String gemail;

	public Password(String email) {
		gemail = email;
	}

	public String send() {
		String reply = "Failed to Reset Password";
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("email", gemail));
		String response = null;

		try {
			response = CustomHttpClient.executeHttpPost(
					"http://ewb-calpoly.org/androidPassword.php",
					postParameters);

			String result = response.toString();

			try {
				JSONArray jArray = new JSONArray(result);
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json_data = jArray.getJSONObject(i);
					Log.i("log_tag", "id: " + json_data.getString("reply"));
					System.out.println("got right before the int conversion");
					reply = json_data.getString("reply");
				}
			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + "[" + result + "]");
			}

			try {
				
			} catch (Exception e) {
				Log.e("log_tag", "Error in Display!" + e.toString());
			}
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection!!" + e.toString());

		}
		return reply;
	}

}
