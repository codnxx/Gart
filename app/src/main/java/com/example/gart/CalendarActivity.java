package com.example.gart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView textView;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        overridePendingTransition(R.anim.left_to_right_enter, R.anim.none);

        requestQueue = Volley.newRequestQueue(this);

        calendarView = findViewById(R.id.calendarView);
        textView = findViewById(R.id.textView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                requestEventData(selectedDate);
            }
        });
    }

    private void requestEventData(String selectedDate) {
        String url = "http://13.124.226.102:8080/api/events/range?date=" + selectedDate;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                StringBuilder messageBuilder = new StringBuilder();

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject event = response.getJSONObject(i);
                                    //String startDate = event.getString("startDate");
                                    //String endDate = event.getString("endDate");
                                    String eventName = event.getString("name");

                                    messageBuilder.append("Event ").append(i + 1).append(":\n")
                                            //.append("Start Date: ").append(startDate).append("\n")
                                            //.append("End Date: ").append(endDate).append("\n")
                                            .append("Name: ").append(eventName).append("\n\n");
                                }

                                textView.setText(messageBuilder.toString());
                            } else {
                                textView.setText("No events for this date.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showToast("Error parsing server response: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast("Error: " + error.getMessage());
                    }
                });


        requestQueue.add(jsonArrayRequest);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
