package com.example.scerns;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EContactsFragment extends Fragment {

    private int userId;
    private Spinner spinner;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> allData;
    private List<String> filteredData;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_e_contacts, container, false);

        // Show userId in toast
        if (userId != -1) {
//            Toast.makeText(requireContext(), "User ID: " + userId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "User ID not found", Toast.LENGTH_SHORT).show();
        }

        spinner = view.findViewById(R.id.dropdown_spinner);
        listView = view.findViewById(R.id.list_view);

        allData = new ArrayList<>();
        filteredData = new ArrayList<>();

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, filteredData);
        listView.setAdapter(adapter);

        fetchCategories();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                filterData(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        return view;
    }

    private void fetchCategories() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = "http://scerns.ucc-bscs.com/User/getRespondent.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        allData.clear();

                        allData.add("All Category");

                        Set<String> uniqueCategories = new HashSet<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String category = jsonObject.getString("Category");
                                uniqueCategories.add(category);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        allData.addAll(uniqueCategories);

                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, allData);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(spinnerAdapter);

                        spinner.setSelection(0);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle errors
                error.printStackTrace();
            }
        });

        queue.add(jsonArrayRequest);
    }

    private void fetchContacts(String category) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        String url;
        if (category.equals("All Category")) {
            url = "http://scerns.ucc-bscs.com/User/getRespondent.php";
        } else {
            url = "http://scerns.ucc-bscs.com/User/getRespondent.php?category=" + category;
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        filteredData.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String name = jsonObject.getString("Name");
                                String hotlineNumber = jsonObject.getString("HotlineNumber");
                                filteredData.add(name + ": " + hotlineNumber);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(jsonArrayRequest);
    }

    private void filterData(String category) {
        fetchContacts(category);
    }
}