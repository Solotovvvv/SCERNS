package com.example.scerns;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddressSuggestionTask extends AsyncTask<String, Void, JSONArray> {

    private static final String TAG = AddressSuggestionTask.class.getSimpleName();
    private AddressSuggestionListener mListener;

    public interface AddressSuggestionListener {
        void onAddressSuggestionReceived(JSONArray suggestions);
    }

    public AddressSuggestionTask(AddressSuggestionListener listener) {
        this.mListener = listener;
    }

    @Override
    protected JSONArray doInBackground(String... params) {
        JSONArray suggestions = null;
        String address = params[0];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("https://nominatim.openstreetmap.org/search?format=json&q=" + address);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            StringBuilder buffer = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            String jsonResponse = buffer.toString();
            suggestions = new JSONArray(jsonResponse);
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error fetching address suggestions: " + e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }
        return suggestions;
    }

    @Override
    protected void onPostExecute(JSONArray suggestions) {
        if (mListener != null) {
            mListener.onAddressSuggestionReceived(suggestions);
        }
    }
}

