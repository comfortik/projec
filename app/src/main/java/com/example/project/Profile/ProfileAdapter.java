package com.example.project.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Main.DiaryEntry;
import com.example.project.R;
import com.example.project.databinding.ItemDiaryBinding;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileHolder> {
    ItemClickListener itemClickListener;
    List<DiaryEntry> diaryEntryList = new ArrayList<>();


    @NonNull
    @Override
    public ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemDiaryBinding binding = ItemDiaryBinding.inflate(inflater, parent, false);
        return new ProfileHolder(binding, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileHolder holder, int position) {
        DiaryEntry diaryEntry= diaryEntryList.get(position);
        holder.bindData(diaryEntry);
    }

    @Override
    public int getItemCount() {
        return diaryEntryList.size();
    }
    public void setItems(List<DiaryEntry> diaryEntryList) {
        int itemCount = getItemCount();
        this.diaryEntryList = new ArrayList<>(diaryEntryList);
        notifyItemRangeChanged(0, Math.max(itemCount, getItemCount()));
    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener =itemClickListener;
    }

    public void openFragment(DiaryEntry diaryEntry) {

    }
}
