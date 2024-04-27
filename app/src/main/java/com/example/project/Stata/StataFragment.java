package com.example.project.Stata;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.project.Main.DiaryEntry;
import com.example.project.Main.FirestoreGetId;
import com.example.project.Profile.ItemClickListener;
import com.example.project.Profile.ProfileAdapter;
import com.example.project.R;
import com.example.project.databinding.FragmentStataBinding;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class StataFragment extends Fragment {
    FragmentStataBinding binding;
    FirebaseFirestore fb;
    FirebaseAuth mAuth;
    List<DiaryEntry> diaryEntryList = new ArrayList<>();
    List<DiaryEntry> diaryEntryListDay = new ArrayList<>();
    List<DiaryEntry> diaryEntryListMounth = new ArrayList<>();


    public StataFragment(FirebaseFirestore fb, FirebaseAuth mAuth) {
        this.fb = fb;
        this.mAuth = mAuth;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    int mon, tue, wed, th, fri, sut, sun=0;
    int monId, tueId, wedId, thId,friId, sutId, sunId=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStataBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        List<PieEntry> barEntriesDay = new ArrayList<>();
        int barWeek[] = new int[7];
        List<BarEntry> barEntriesWeek = new ArrayList<>();
        List<Entry> barEntriesMounth = new ArrayList<>();
        FirestoreGetId firestoreGetId = new FirestoreGetId(fb);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        firestoreGetId.getId(mAuth.getCurrentUser().getUid(), userId -> {
            fb.collection("Users")
                    .document(userId)
                    .collection("Entry")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if(documentSnapshot!=null){
                                DiaryEntry diaryEntry = documentSnapshot.toObject(DiaryEntry.class);
                                diaryEntryList.add(diaryEntry);
                                Date input = diaryEntry.getDate();
                                LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                LocalDate today  = LocalDate.now();
                                if(date.equals(today)){
                                    diaryEntryListDay.add(diaryEntry);
                                }
                                if(date.isAfter(today.minusMonths(1))) diaryEntryListMounth.add(diaryEntry);
                                if(date.isAfter(today.minusDays(7))){
                                    c.setTime(diaryEntry.getDate());
                                    int day= c.get(Calendar.DAY_OF_WEEK);
                                    switch (day){
                                        case 1:
                                            mon++;
                                            monId+=diaryEntry.getEmotion().getId();
                                            barWeek[0]=monId/mon;
                                            break;
                                        case 2:
                                            tue++;
                                            tueId+=diaryEntry.getEmotion().getId();

                                            barWeek[1]=tueId/tue;
                                            break;
                                        case 3:
                                            wed++;
                                            wedId+=diaryEntry.getEmotion().getId();
                                            barWeek[2]=wedId/wed;
                                        case 4:
                                            th++;
                                            thId+=diaryEntry.getEmotion().getId();
                                            barWeek[3]=thId/th;
                                            break;
                                        case 5:
                                            fri++;
                                            friId+=diaryEntry.getEmotion().getId();
                                            barWeek[4]=friId/fri;
                                            break;
                                        case 6:
                                            sut++;
                                            sutId+=diaryEntry.getEmotion().getId();
                                            barWeek[5]= sutId/sut;
                                            break;
                                        case 7:
                                            sun++;
                                            sunId+=diaryEntry.getEmotion().getId();
                                            barWeek[6]=sunId/ sun;
                                            break;

                                    }
                                }
                            }
                        }
                        Collections.sort(diaryEntryListMounth, Comparator.comparing(DiaryEntry::getDate));
                        Collections.sort(diaryEntryListDay, Comparator.comparing(DiaryEntry::getDate));
                        Collections.sort(diaryEntryList, Comparator.comparing(DiaryEntry::getDate));

                        List<Integer> colorsList = new ArrayList<>();

                        for (int i = 0; i < diaryEntryListDay.size(); i++) {
                            int emotionId = diaryEntryListDay.get(i).getEmotion().getId();
                            int color = getColorForEmotionId(emotionId);
                            colorsList.add(color);

                            barEntriesDay.add(new PieEntry(emotionId));
                        }

                        PieDataSet barDataSetDay = new PieDataSet(barEntriesDay, "Today");
                        barDataSetDay.setColors(colorsList);
                        barDataSetDay.setSliceSpace(1f);
                        barDataSetDay.setDrawValues(false);
                        binding.barChart.getLegend().setEnabled(false);
                        barDataSetDay.setDrawIcons(false);

                        PieData barDataDay = new PieData(barDataSetDay);
                        binding.barChart.setData(barDataDay);
                        binding.barChart.invalidate();







                        for(int i=0; i<barWeek.length;i++){
                            barEntriesWeek.add(new BarEntry(i+1,barWeek[i]));
                        }
                        binding.barChartWeek.getAxisLeft().setDrawLabels(false);
                        binding.barChartWeek.getAxisRight().setMaxWidth(5f);
                        binding.barChartWeek.getAxisRight().setGranularity(1f);
                        binding.barChartWeek.getAxisRight().setMinWidth(0f);
                        binding.barChartWeek.getAxisRight().setValueFormatter(new MyValueFormatter());
                        binding.barChartWeek.setDrawValueAboveBar(false);
                        BarDataSet barDataSetWeek = new BarDataSet(barEntriesWeek, "Week");
                        BarData barDataWeek = new BarData(barDataSetWeek);
                        barDataSetWeek.setColor(Color.parseColor("#455A64"));
                        barDataSetWeek.setDrawValues(false);
                        binding.barChartWeek.setData(barDataWeek);
                        binding.barChartWeek.invalidate();


                        for(int i=0; i<diaryEntryListMounth.size();i++){
                            barEntriesMounth.add(new Entry(i, diaryEntryListMounth.get(i).getEmotion().getId()));
                        }

                        binding.barChartMounth.getAxisLeft().setDrawLabels(false);
                        binding.barChartMounth.getAxisRight().setMaxWidth(5f);
                        binding.barChartMounth.getAxisRight().setGranularity(1f);
                        binding.barChartMounth.getAxisRight().setMinWidth(0f);
                        binding.barChartMounth.getAxisRight().setValueFormatter(new MyValueFormatter());

                        LineDataSet barDataSetMounth = new LineDataSet(barEntriesMounth, "Mounth");
                        LineData barDataMounth = new LineData(barDataSetMounth);
                        barDataSetMounth.setColor(Color.parseColor("#455A64"));
                        barDataSetMounth.setDrawValues(false);
                        binding.barChartMounth.setData(barDataMounth);
                        binding.barChartMounth.invalidate();


                    });
        });






        return view;
    }
    private int getColorForEmotionId(int emotionId) {
        switch (emotionId) {
            case 1:
                return Color.parseColor("#455A64");
            case 2:
                return Color.parseColor("#607D8B");
            case 3:
                return Color.parseColor("#90A4AF");
            case 4:
                return Color.parseColor("#B0BEC5");
            case 5:
                return Color.parseColor("#90A4AC");
            default:
                return Color.BLACK;
        }
    }
}