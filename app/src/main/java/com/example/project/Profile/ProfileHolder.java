package com.example.project.Profile;



import android.graphics.drawable.Drawable;
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
                if(!(diaryEntry.getFocusMode().isInterval())){
                    binding.tvFocusTime.setText(diaryEntry.getFocusMode().getTime());
            }
                setImage(diaryEntry.getEmotion().getId());
                binding.time.setText(diaryEntry.getDate().toString());
                break;
            case 2:
                binding.tvFocus.setText(diaryEntry.getFocusMode().getName());
                if(!(diaryEntry.getFocusMode().isInterval())){
                    binding.tvFocusTime.setText(diaryEntry.getFocusMode().getTime()+" "+ diaryEntry.getFocusMode().getTimeInterval());
                }
                setImage(diaryEntry.getEmotion().getId());
                binding.time.setText(diaryEntry.getDate().toString());
                break;
            case 3:
                setImage(diaryEntry.getEmotion().getId());
                binding.time.setText(diaryEntry.getDate().toString());
                break;
            case 4:
                setImage(diaryEntry.getEmotion().getId());
                binding.time.setText(diaryEntry.getDate().toString());
                break;
        }
        binding.getRoot().setOnClickListener(v -> itemClickListener.onItemClick(diaryEntry));
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


        }
    }

}
