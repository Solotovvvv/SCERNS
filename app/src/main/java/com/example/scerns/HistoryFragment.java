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
    private JSONArray jsonArray;

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

    private void showDetailDialog(JSONObject jsonObject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Report Details");

        StringBuilder detailsBuilder = new StringBuilder();
        detailsBuilder.append("Role: ").append(jsonObject.optString("Role", "")).append("\n")
                .append("Type Of Emergency: ").append(jsonObject.optString("TypeOfEmergency", "")).append("\n")
                .append("Address: ").append(jsonObject.optString("Address", "")).append("\n")
                .append("Landmark: ").append(jsonObject.optString("Landmark", "")).append("\n")
                .append("Level: ").append(jsonObject.optString("Level", "")).append("\n")
                .append("Date: ").append(jsonObject.optString("Date", "")).append("\n")
                .append("Status: ").append(jsonObject.optString("Status", ""));

        builder.setMessage(detailsBuilder.toString());
        builder.setPositiveButton("OK", null);
        builder.show();
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
                        jsonArray = jsonResponse.getJSONArray("reports"); // Assigning jsonArray
                        ArrayList<String> dataList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String role = jsonObject.optString("Role", "");
                            String reportDetails = "Role: " + role;
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

