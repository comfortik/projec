package com.example.project.Profile;



import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Main.AddUserToFirebase;
import com.example.project.Main.DiaryEntry;
import com.example.project.Main.FirestoreGetId;
import com.example.project.R;
import com.example.project.TypeItemClickListener;
import com.example.project.databinding.ItemDiaryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ProfileHolder extends RecyclerView.ViewHolder {
    ItemDiaryBinding binding;
    ItemClickListener itemClickListener;

    public ProfileHolder(ItemDiaryBinding binding, ItemClickListener itemClickListener) {
        super(binding.getRoot());
        this.binding=binding;
        this.itemClickListener=itemClickListener;
    }
    public void bindData(DiaryEntry diaryEntry){
        switch (diaryEntry.getId()){
            case 1:
                binding.tvFocus.setText(diaryEntry.getFocusMode().getName());
                setText(diaryEntry);
                setImage(diaryEntry.getEmotion().getId());
                binding.time.setText(convertToLocalDateViaInstant(diaryEntry.getDate()));
                break;
            case 2:

                binding.tvFocus.setText(diaryEntry.getFocusMode().getName());
                setText(diaryEntry);
                setImage(diaryEntry.getEmotion().getId());
                binding.time.setText(convertToLocalDateViaInstant(diaryEntry.getDate()));
            case 3:
                setText(diaryEntry);
                setImage(diaryEntry.getEmotion().getId());
                binding.time.setText(convertToLocalDateViaInstant(diaryEntry.getDate()));
                break;
            case 4:
                setText(diaryEntry);
                setImage(diaryEntry.getEmotion().getId());
                binding.time.setText(convertToLocalDateViaInstant(diaryEntry.getDate()));
                break;
        }
        binding.getRoot().setOnClickListener(v -> itemClickListener.onItemClick(diaryEntry));
    }
    private String convertToLocalDateViaInstant(Date dateToConvert) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return dateToConvert.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString();
        }
        else return dateToConvert.toString();
    }
    public void setText(DiaryEntry diaryEntry){
        if(diaryEntry.getEmotion().getEmotionWords()!=null && !diaryEntry.getEmotion().getEmotionWords().isEmpty())    {
            binding.tvFocus.setText(diaryEntry.getEmotion().getEmotionWords().get(0));
            binding.tvFocusTime.setVisibility(View.GONE);
            binding.tvFocus.setBackgroundResource(R.color.bynBackground);
            binding.tvFocus.setTextColor(Color.WHITE);
            ViewGroup.LayoutParams layoutParams = binding.tvFocus.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            binding.tvFocus.setLayoutParams(layoutParams);
            binding.tvFocus.setPadding(25, 12,25 ,12 );
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadius(10);
            shape.setColor(Color.parseColor("#546E7A"));
            binding.tvFocus.setBackground(shape);
        }
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


        }
    }

}
