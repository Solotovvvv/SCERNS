package com.example.scerns;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private int userId;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        if (userId != -1) {
            Toast.makeText(requireContext(), "User ID: " + userId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "User ID not found", Toast.LENGTH_SHORT).show();
        }

        listView = view.findViewById(R.id.list_view);

        fetchDataFromAPI();

        return view;
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
                        JSONArray jsonArray = jsonResponse.getJSONArray("reports");
                        ArrayList<String> dataList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String typeOfEmergency = jsonObject.optString("TypeOfEmergency", "");
                            String address = jsonObject.optString("Address", "");
                            String landmark = jsonObject.optString("Landmark", "");
                            String level = jsonObject.optString("Level", "");
                            String date = jsonObject.optString("Date", "");
                            String status = jsonObject.optString("Status", "");

                            String reportDetails = "Type Of Emergency: " + typeOfEmergency + "\n"
                                    + "Address: " + address + "\n"
                                    + "Landmark: " + landmark + "\n"
                                    + "Level: " + level + "\n"
                                    + "Date: " + date + "\n"
                                    + "Status: " + status;

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

