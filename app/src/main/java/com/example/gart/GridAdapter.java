package com.example.gart;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private List<String> data = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 이미지 및 텍스트를 설정
        holder.img.setImageResource(R.drawable.ic_launcher_foreground); // 이미지 설정
        holder.title.setText("Title " + (position + 1)); // 텍스트 설정

        if (position < data.size()) {
            String cultureName = data.get(position); // 데이터 가져오기
            holder.title.setText(cultureName); // 데이터를 항목에 설정

            // 이미지 로딩 라이브러리를 사용하여 이미지 설정
            String imageUrl = "http://13.124.226.102:8080/gart/" + (position-1);
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.img);
        }
    }

    public void setData(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 5; // 항목 수를 변경하려면 원하는 숫자로 수정
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.title);
        }

    }
}