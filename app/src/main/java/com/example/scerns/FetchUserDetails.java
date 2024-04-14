package com.example.scerns;

import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchUserDetails extends AsyncTask<Integer, Void, JSONObject> {
    private ProfileFragment profileFragment;

    public FetchUserDetails(ProfileFragment profileFragment) {
        this.profileFragment = profileFragment;
    }

    @Override
    protected JSONObject doInBackground(Integer... params) {
        int userId = params[0];
        JSONObject userDetails = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL("http://scerns.ucc-bscs.com/User/profile.php?userId=" + userId);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            userDetails = new JSONObject(buffer.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return userDetails;
    }

    @Override
    protected void onPostExecute(JSONObject userDetails) {
        if (userDetails != null) {
            // Populate the profile fragment with the fetched data
            profileFragment.populateProfile(userDetails);
        }
    }
}
