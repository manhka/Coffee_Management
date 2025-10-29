package com.app.coffeemanagementapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.coffeemanagementapplication.databinding.ItemFeedbackBinding;
import com.app.coffeemanagementapplication.models.Feedback;
import com.app.coffeemanagementapplication.models.Users;
import com.app.coffeemanagementapplication.repositories.IUserRepo;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProductFeedbackAdapter extends RecyclerView.Adapter<ProductFeedbackAdapter.FeedbackViewHolder> {

    private final Context context;
    private final List<Feedback> feedbackList;
    private final IUserRepo userRepo;

    public ProductFeedbackAdapter(Context context, List<Feedback> feedbackList, IUserRepo userRepo) {
        this.context = context;
        this.feedbackList = feedbackList;
        this.userRepo = userRepo;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFeedbackBinding binding = ItemFeedbackBinding.inflate(
                LayoutInflater.from(context), parent, false
        );
        return new FeedbackViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Feedback feedback = feedbackList.get(position);
        Users user = userRepo.getUserById(feedback.getUserId());

        holder.binding.tvUserName.setText(user.getFullName());
        holder.binding.tvComment.setText(feedback.getComment());

        holder.binding.tvDate.setText(feedback.getCreatedAt());

        holder.binding.ratingBar.setRating(feedback.getRating());

        if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
            Glide.with(context)
                    .load(user.getAvatarUrl())
                    .placeholder(com.app.coffeemanagementapplication.R.drawable.ic_user_default)
                    .circleCrop()
                    .into(holder.binding.imgAvatar);
        } else {
            holder.binding.imgAvatar.setImageResource(
                    com.app.coffeemanagementapplication.R.drawable.ic_user_default
            );
        }
    }

    @Override
    public int getItemCount() {
        return feedbackList != null ? feedbackList.size() : 0;
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        ItemFeedbackBinding binding;

        public FeedbackViewHolder(ItemFeedbackBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}