package com.example.project.Profile;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Emotion.Emotion;
import com.example.project.Main.DiaryEntry;
import com.example.project.R;
import com.example.project.databinding.FragmentDiaryEntryBinding;
import com.example.project.databinding.FragmentEmotionDiaryBinding;
import com.example.project.databinding.ItemDiaryBinding;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.button.MaterialButton;

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

                setText(diaryEntry.getEmotion().getEmotionWords(), context, binding,diaryEntry.getEmotion());
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

                setText(diaryEntry.getEmotion().getEmotionWords(), context, binding,diaryEntry.getEmotion());
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

                setText(diaryEntry.getEmotion().getEmotionWords(), context, binding,diaryEntry.getEmotion());
                setImage(diaryEntry.getEmotion().getId());
                binding.time.setText(diaryEntry.getDate().toString());
                break;
            case 4:

                setText(diaryEntry.getEmotion().getEmotionWords(), context, binding,diaryEntry.getEmotion());
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
    public void setText(ArrayList<String> words, Context context, FragmentDiaryEntryBinding binding, Emotion emotion) {
        binding.flexbox.removeAllViews();
        for (String word : words) {
            TextView button = new TextView(context);
            button.setText(word);
            button.setTextColor(Color.WHITE);
            button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            button.setTypeface(Typeface.MONOSPACE);
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            button.setPadding(25, 12, 25, 12);
            params.setMargins(0, 0 , 0, 12);
            button.setLayoutParams(params);
            binding.flexbox.addView(button);
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadius(10);
            shape.setColor(Color.parseColor("#546E7A"));
            button.setBackground(shape);

        }
        binding.scrollView.setVisibility(View.VISIBLE);

    }

    void deleteItem(int index) {
        diaryEntryList.remove(index);
        notifyItemRemoved(index);
    }
    private void setImage(int id){
        switch(id){
            case 1:
                binding.imageView.setBackgroundResource(R.drawable.emotion_one);
                break;
            case 2:
                binding.imageView.setBackgroundResource(R.drawable.emotion_two);
                break;
            case 3:
                binding.imageView.setBackgroundResource(R.drawable.emotion_three);
                break;
            case 4:
                binding.imageView.setBackgroundResource(R.drawable.emotion_four);
                break;
            case 5:
                binding.imageView.setBackgroundResource(R.drawable.emotion_five);
                break;

        }
    }


}
