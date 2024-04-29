package com.example.scerns;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private int userId, reportId;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private JSONArray jsonArray;
    private Pusher pusher;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        if (userId != -1) {
            PusherOptions options = new PusherOptions().setCluster("ap1").setEncrypted(true);
            pusher = new Pusher("b26a50e9e9255fc95c8f", options);
            pusher.connect();

            Channel channel = pusher.subscribe("Scerns");

            SubscriptionEventListener eventListener = new SubscriptionEventListener() {
                @Override
                public void onEvent(PusherEvent event) {
                    try {
                        JSONObject eventData = new JSONObject(event.getData());

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fetchDataFromAPI();
                                }
                            });
                        } else {
                            // Handle the case where the fragment is not attached to an activity
                            Log.e("HistoryFragment", "Fragment is not attached to an activity");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };


            channel.bind("user-history-report", eventListener);

            listView = view.findViewById(R.id.list_view);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        JSONObject clickedItem = jsonArray.getJSONObject(position);
                        showDetailDialog(clickedItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(requireContext(), "User ID not found", Toast.LENGTH_SHORT).show();
        }

        listView = view.findViewById(R.id.list_view);

        fetchDataFromAPI();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject clickedItem = jsonArray.getJSONObject(position);
                    int clickedReportId = clickedItem.optInt("Id", -1); // Assuming "Id" is the key for reportId in your JSON
                    if (clickedReportId != -1) {
                        reportId = clickedReportId;
                    }
                    showDetailDialog(clickedItem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    private void showDetailDialog(JSONObject jsonObject) throws JSONException {
        Intent intent = new Intent(requireContext(), DetailsActivity.class);
        intent.putExtra("jsonData", jsonObject.toString());
        intent.putExtra("userId", userId);
        intent.putExtra("Id", reportId);
        startActivity(intent);
    }

    void fetchDataFromAPI() {
        String url = "http://scerns.ucc-bscs.com/User/history.php?User_Id=" + userId;

        // Pass the context to the VolleyRequestManager constructor
        VolleyRequestManager volleyRequestManager = new VolleyRequestManager(requireContext());

        volleyRequestManager.get(url, new VolleyRequestManager.VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                Log.d("JSON_RESPONSE", response);

                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("reports")) {
                        jsonArray = jsonResponse.getJSONArray("reports");
                        ArrayList<String> dataList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            reportId = jsonObject.optInt("Id", -1);
                            String role = jsonObject.optString("Role", "");
                            String datetime = jsonObject.optString("Date", "");
                            String reportDetails = "Role: " + role +
                                    ", Date: " + datetime;
                            dataList.add(reportDetails);
                        }

                        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, dataList);
                        listView.setAdapter(adapter);

                    } else {
                        Toast.makeText(requireContext(), "No reports found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
