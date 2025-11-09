package com.app.coffeemanagementapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.coffeemanagementapplication.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateSelectorAdapter extends RecyclerView.Adapter<DateSelectorAdapter.DateViewHolder> {

    private Context context;
    private List<Date> dateList;
    private int selectedPosition = 0;
    private OnDateSelectedListener listener;

    public interface OnDateSelectedListener {
        void onDateSelected(Date date);
    }

    public DateSelectorAdapter(Context context, List<Date> dateList, OnDateSelectedListener listener) {
        this.context = context;
        this.dateList = dateList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_date_selector, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        Date date = dateList.get(position);

        SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("E", new Locale("vi", "VN"));
        holder.tvDayOfWeek.setText(dayOfWeekFormat.format(date));

        SimpleDateFormat dateFormat = new SimpleDateFormat("d/M", new Locale("vi", "VN"));
        holder.tvDate.setText(dateFormat.format(date));

        if (position == selectedPosition) {
            holder.container.setBackground(ContextCompat.getDrawable(context, R.drawable.date_selector_selected_bg));
            holder.tvDayOfWeek.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            holder.tvDate.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        } else {
            holder.container.setBackground(ContextCompat.getDrawable(context, R.drawable.date_selector_bg));
            holder.tvDayOfWeek.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            holder.tvDate.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        }
        // --- Hết phần an toàn ---


        // --- SỬA LỖI TRONG CLICK LISTENER ---
        holder.container.setOnClickListener(v -> {
            // Luôn lấy position mới nhất tại thời điểm click
            int currentPosition = holder.getAdapterPosition();

            // Kiểm tra an toàn, tránh trường hợp item đã bị xóa
            if (currentPosition == RecyclerView.NO_POSITION) {
                return;
            }

            if (currentPosition != selectedPosition) {
                int oldPosition = selectedPosition;
                selectedPosition = currentPosition; // Dùng position mới

                // Thông báo cho adapter vẽ lại 2 item (cũ và mới)
                notifyItemChanged(oldPosition);
                notifyItemChanged(selectedPosition);

                // Báo cho Fragment biết ngày mới được chọn
                listener.onDateSelected(dateList.get(currentPosition)); // Dùng position mới
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    static class DateViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container;
        TextView tvDayOfWeek, tvDate;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.item_date_container);
            tvDayOfWeek = itemView.findViewById(R.id.tvDayOfWeek);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}