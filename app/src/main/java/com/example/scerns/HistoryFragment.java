package com.example.scerns;

import android.app.AlertDialog;
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

    private int userId;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private JSONArray jsonArray;
//    private Pusher pusher;

    private TextView textViewStatus;
    private LinearLayout loadingLayout;
    private ProgressBar loadingProgressBar;
    private TextView textViewWaiting;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        if (userId != -1) {
//            PusherOptions options = new PusherOptions().setCluster("ap1").setEncrypted(true);
//            pusher = new Pusher("b26a50e9e9255fc95c8f", options);
//            pusher.connect();
//
//            Channel channel = pusher.subscribe("Scerns");
//
//            SubscriptionEventListener eventListener = new SubscriptionEventListener() {
//                @Override
//                public void onEvent(PusherEvent event) {
//                    try {
//                        JSONObject eventData = new JSONObject(event.getData());
//                        String updatedStatus = eventData.optString("status", "");
//
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                textViewStatus.setText("Status: " + updatedStatus);
//
//                                if (updatedStatus.equalsIgnoreCase("Pending")) {
//                                    loadingLayout.setVisibility(View.VISIBLE);
//                                    loadingProgressBar.setVisibility(View.VISIBLE);
//                                    textViewWaiting.setVisibility(View.VISIBLE);
//                                } else {
//                                    loadingLayout.setVisibility(View.GONE);
//                                    loadingProgressBar.setVisibility(View.GONE);
//                                    textViewWaiting.setVisibility(View.GONE);
//                                }
//                            }
//                        });
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//
//            channel.bind("user-history-report", eventListener);
//
//            listView = view.findViewById(R.id.list_view);
//
//            fetchDataFromAPI();
//
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    try {
//                        JSONObject clickedItem = jsonArray.getJSONObject(position);
//                        showDetailDialog(clickedItem);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
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
                        showDetailDialog(clickedItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        return view;
    }

    private void showDetailDialog(JSONObject jsonObject) throws JSONException {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);

        TextView textViewTitle = dialogView.findViewById(R.id.textViewTitle);
        TextView textViewType = dialogView.findViewById(R.id.textViewType);
        TextView textViewAddress = dialogView.findViewById(R.id.textViewAddress);
        TextView textViewLandmark = dialogView.findViewById(R.id.textViewLandmark);
        TextView textViewLevel = dialogView.findViewById(R.id.textViewLevel);
        textViewStatus = dialogView.findViewById(R.id.textViewStatus);
        loadingLayout = dialogView.findViewById(R.id.loadingLayout);
        loadingProgressBar = dialogView.findViewById(R.id.loadingProgressBar);
        textViewWaiting = dialogView.findViewById(R.id.textViewWaiting);

        textViewTitle.setText("Report's Details");
        textViewType.setText("Emergency Type: " + jsonObject.optString("TypeOfEmergency", ""));
        textViewAddress.setText("Address: " + jsonObject.optString("Address", ""));
        textViewLandmark.setText("Landmark: " + jsonObject.optString("Landmark", ""));
        textViewLevel.setText("Level: " + jsonObject.optString("Level", ""));
        textViewStatus.setText("Status: " + jsonObject.optString("Status", ""));

        String status = jsonObject.optString("Status", "");
        if (status.equalsIgnoreCase("Pending")) {
            loadingLayout.setVisibility(View.VISIBLE);
            loadingProgressBar.setVisibility(View.VISIBLE);
            textViewWaiting.setVisibility(View.VISIBLE);
        } else {
            loadingLayout.setVisibility(View.GONE);
            loadingProgressBar.setVisibility(View.GONE);
            textViewWaiting.setVisibility(View.GONE);
        }

        String address = jsonObject.optString("Address", "");

        MapView mapView = dialogView.findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        String mapUrl = "https://nominatim.openstreetmap.org/search?format=json&q=" + address;
        VolleyRequestManager volleyRequestManager = new VolleyRequestManager(requireContext());
        volleyRequestManager.get(mapUrl, new VolleyRequestManager.VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        JSONObject result = jsonArray.getJSONObject(0);
                        double lat = result.getDouble("lat");
                        double lon = result.getDouble("lon");
                        GeoPoint point = new GeoPoint(lat, lon);

                        mapView.getController().setZoom(19);
                        mapView.getController().setCenter(point);

                        Marker marker = new Marker(mapView);
                        marker.setPosition(point);
                        mapView.getOverlays().add(marker);
                        mapView.invalidate();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(dialogView).setPositiveButton("OK", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void fetchDataFromAPI() {
        String url = "http://scerns.ucc-bscs.com/User/history.php?User_Id=" + userId;

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
