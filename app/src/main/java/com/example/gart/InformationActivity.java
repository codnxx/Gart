package com.example.gart;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class InformationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GridAdapter adapter;
    private RequestQueue requestQueue;
    private Button button;

    public static Intent newIntent(Context context) {
        return new Intent(context, InformationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        overridePendingTransition(R.anim.left_to_right_enter, R.anim.none);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new GridAdapter();
        recyclerView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);
        fetchDataFromServer();

        initView();

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(InformationActivity.this);
                dlg.setTitle("카테고리를 선택해 주세요."); // 제목
                final String[] categoryArray = new String[] {"공연", "전시", "행사"};

                dlg.setSingleChoiceItems(categoryArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 선택된 카테고리에 따라 서버에서 데이터를 가져오는 메서드 호출
                        fetchCategoryData(categoryArray[which]);
                    }
                });

                // 확인 버튼 클릭시 동작
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 토스트 메시지
                        Toast.makeText(InformationActivity.this, "변경되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                dlg.show();
            }
        });

    }

    private void fetchDataFromServer() {
        String url = "http://13.124.226.102:8080/gart/culture-names"; // 서버 주소
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<String> cultureNames = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                String cultureName = response.getString(i);
                                Log.d("Culture Name", cultureName);
                                cultureNames.add(cultureName);
                            }
                            adapter.setData(cultureNames); // 데이터 설정
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // 에러 처리
                            Log.e("JSON Parsing Error", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러 처리
                Log.e("Volley Error", error.toString());
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    private void fetchCategoryData(String selectedCategory) {
        String url = "http://13.124.226.102:8080/gart/culture-names?category=" + selectedCategory;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<String> cultureNames = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                String cultureName = response.getString(i);
                                Log.d("Culture Name", cultureName);
                                cultureNames.add(cultureName);
                            }
                            // 가져온 데이터를 리사이클러뷰 어댑터에 설정
                            adapter.setData(cultureNames);
                            // 어댑터 업데이트
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // 에러 처리
                            Log.e("JSON Parsing Error", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러 처리
                Log.e("Volley Error", error.toString());
            }
        });

        // 요청을 큐에 추가
        requestQueue.add(jsonArrayRequest);
        adapter.notifyDataSetChanged();
    }


    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbarlayout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.left:
                break;
            case R.id.right:
                Intent intent = new Intent(this, CalendarActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(InformationActivity.this, CalendarActivity.class));
        overridePendingTransition(R.anim.right_to_left_enter, R.anim.right_to_left_exit);
    }


}
