package com.example.project.Profile;



import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
                if(diaryEntry.getFocusMode().isInterval()){
                    binding.tvFocusTime.setText(diaryEntry.getFocusMode().getTime() +"\n"+diaryEntry.getFocusMode().getTimeInterval());
                } else if (!diaryEntry.getFocusMode().isInterval()) {
                    binding.tvFocusTime.setText(diaryEntry.getFocusMode().getTime());
                }
                setImage(diaryEntry.getEmotion().getId());
                binding.time.setText(convertToLocalDateViaInstant(diaryEntry.getDate()));
                break;
            case 2:
                binding.tvFocus.setText(diaryEntry.getFocusMode().getName());
                if(diaryEntry.getFocusMode().isInterval()){
                    binding.tvFocusTime.setText(diaryEntry.getFocusMode().getTime() +"\n"+diaryEntry.getFocusMode().getTimeInterval());
                } else if (!diaryEntry.getFocusMode().isInterval()) {
                    binding.tvFocusTime.setText(diaryEntry.getFocusMode().getTime());
                }
                setImage(diaryEntry.getEmotion().getId());
                binding.time.setText(convertToLocalDateViaInstant(diaryEntry.getDate()));
            case 3:
                setImage(diaryEntry.getEmotion().getId());
                binding.time.setText(convertToLocalDateViaInstant(diaryEntry.getDate()));
                break;
            case 4:
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
