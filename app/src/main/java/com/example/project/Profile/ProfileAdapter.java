package com.example.project.Profile;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Main.DiaryEntry;
import com.example.project.R;
import com.example.project.databinding.FragmentDiaryEntryBinding;
import com.example.project.databinding.FragmentEmotionDiaryBinding;
import com.example.project.databinding.ItemDiaryBinding;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileHolder> {
    ItemClickListener itemClickListener;
    List<DiaryEntry> diaryEntryList = new ArrayList<>();
    FragmentDiaryEntryBinding binding;

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



    public void openFragment(Context context, DiaryEntry diaryEntry, LayoutInflater layoutInflater) {
        alertDiary(context, diaryEntry,layoutInflater);
    }
    public void alertDiary(Context context, DiaryEntry diaryEntry, LayoutInflater layoutInflater){
        AlertDialog alertDialog;
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        binding= FragmentDiaryEntryBinding.inflate(layoutInflater, null , false);
        binding.layout.setClipToOutline(true);
        switch (diaryEntry.getId()){
            case 1:
                if(diaryEntry.getFocusMode().isInterval()){
                    setImage(diaryEntry.getEmotion().getId());
                    binding.tvFocus.setText(diaryEntry.getFocusMode().getTime());
                    binding.tvFocusInterval.setText(diaryEntry.getFocusMode().getTimeInterval());
                    binding.time.setText(diaryEntry.getDate().toString());
                }
                else{
                    setImage(diaryEntry.getEmotion().getId());
                    binding.tvFocus.setText(diaryEntry.getFocusMode().getTime());
                    binding.time.setText(diaryEntry.getDate().toString());
                }
                break;
            case 2:
                if(diaryEntry.getFocusMode().isInterval()){
                    setImage(diaryEntry.getEmotion().getId());
                    binding.tvFocus.setText(diaryEntry.getFocusMode().getTime());
                    binding.tvFocusInterval.setText(diaryEntry.getFocusMode().getTimeInterval());
                    binding.note.setText(diaryEntry.getNote().getNote());
                    binding.time.setText(diaryEntry.getDate().toString());
                }
                else{
                    setImage(diaryEntry.getEmotion().getId());
                    binding.tvFocus.setText(diaryEntry.getFocusMode().getTime());
                    binding.note.setText(diaryEntry.getNote().getNote());
                    binding.time.setText(diaryEntry.getDate().toString());
                }
                break;
            case 3:
                setImage(diaryEntry.getEmotion().getId());
                binding.time.setText(diaryEntry.getDate().toString());
                break;
            case 4:
                setImage(diaryEntry.getEmotion().getId());
                binding.note.setText(diaryEntry.getNote().getNote());
                binding.time.setText(diaryEntry.getDate().toString());
                break;
        }
        View view = binding.getRoot();

        alertDialog= builder.setView(view).show();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

    }
    void deleteItem(int index) {
        diaryEntryList.remove(index);
        notifyItemRemoved(index);
    }
    private void setImage(int id){
        switch(id){
            case 1:
                binding.imageView.setBackgroundResource(R.drawable.ic_launcher_background);
                break;
            case 2:
                binding.imageView.setBackgroundResource(R.drawable.ic_launcher_background);
                break;
            case 3:
                binding.imageView.setBackgroundResource(R.drawable.ic_launcher_background);
                break;
            case 4:
                binding.imageView.setBackgroundResource(R.drawable.ic_launcher_background);
                break;
            case 5:
                binding.imageView.setBackgroundResource(R.drawable.ic_launcher_background);
                break;

        }
    }


}
