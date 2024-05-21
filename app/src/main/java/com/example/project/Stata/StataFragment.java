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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.MPPointF;
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
    int one, two, three, four, five, all=0;
    int barWeek[];
    int pieDay[];
    List<LegendEntry> legendEntries;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStataBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        List<PieEntry> barEntriesDay = new ArrayList<>();
        List<BarEntry> barEntriesWeek = new ArrayList<>();
        List<Entry> barEntriesMounth = new ArrayList<>();
        FirestoreGetId firestoreGetId = new FirestoreGetId(fb);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        barWeek = new int[7];
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
                                if (date.equals(today)) {
                                    // diaryEntryListDay.add(diaryEntry);
                                    int id = diaryEntry.getEmotion().getId();
                                    pieDay = new int[5];
                                    switch (id) {
                                        case 1:
                                            one++;
                                            all++;
                                            break;
                                        case 2:
                                            two++;
                                            all++;
                                            break;
                                        case 3:
                                            three++;
                                            all++;
                                            break;
                                        case 4:
                                            four++;
                                            all++;
                                            break;
                                        case 5:
                                            five++;
                                            all++;
                                            break;
                                    }
                                }
                                if(date.isAfter(today.minusMonths(1))) diaryEntryListMounth.add(diaryEntry);
                                if (date.isAfter(today.minusDays(7))) {
                                    c.setTime(diaryEntry.getDate());
                                    int day = c.get(Calendar.DAY_OF_WEEK);

                                    switch (day) {
                                        case 2:
                                            mon++;
                                            monId += diaryEntry.getEmotion().getId();
                                            barWeek[0] = monId / mon;
                                            break;
                                        case 3:
                                            tue++;
                                            tueId += diaryEntry.getEmotion().getId();
                                            barWeek[1] = tueId / tue;
                                            break;
                                        case 4:
                                            wed++;
                                            wedId += diaryEntry.getEmotion().getId();
                                            barWeek[2] = wedId / wed;
                                            break;
                                        case 5:
                                            th++;
                                            thId += diaryEntry.getEmotion().getId();
                                            barWeek[3] = thId / th;
                                            break;
                                        case 6:
                                            fri++;
                                            friId += diaryEntry.getEmotion().getId();
                                            barWeek[4] = friId / fri;
                                            break;
                                        case 7:
                                            sut++;
                                            sutId += diaryEntry.getEmotion().getId();
                                            barWeek[5] = sutId / sut;
                                            break;
                                        case 1:
                                            sun++;
                                            sunId += diaryEntry.getEmotion().getId();
                                            barWeek[6] = sunId / sun;
                                            break;
                                    }
                                }
                            }
                        }
                        Collections.sort(diaryEntryListMounth, Comparator.comparing(DiaryEntry::getDate));
                        Collections.sort(diaryEntryList, Comparator.comparing(DiaryEntry::getDate));

                        List<Integer> colorsList = new ArrayList<>();
                        if (pieDay!=null){
                            for (int i = 0; i < pieDay.length; i++) {
                                int id = i + 1;
                                int count = 0;
                                switch (id) {
                                    case 1:
                                        count = one;
                                        break;
                                    case 2:
                                        count = two;
                                        break;
                                    case 3:
                                        count = three;
                                        break;
                                    case 4:
                                        count = four;
                                        break;
                                    case 5:
                                        count = five;
                                        break;
                                }
                                float percent = (float) count / all * 100;
                                int color = getColorForEmotionId(id);
                                colorsList.add(color);
                                barEntriesDay.add(new PieEntry(percent, id));
                            }
                            legendEntries= new ArrayList<>();
                            ArrayList<String> labels = new ArrayList<>();
                            labels.add("1");
                            labels.add("2");
                            labels.add("3");
                            labels.add("4");
                            labels.add("5");

                            PieDataSet barDataSetDay = new PieDataSet(barEntriesDay, "Today");
                            barDataSetDay.setColors(colorsList);
                            barDataSetDay.setSliceSpace(1f);
                            barDataSetDay.setDrawValues(false);
                            barDataSetDay.setDrawIcons(false);
                            barDataSetDay.setSliceSpace(3f);
                            barDataSetDay.setSliceSpace(3f);
                            barDataSetDay.setValueTextSize(12f);
                            barDataSetDay.setIconsOffset(new MPPointF(0, 40));
                            barDataSetDay.setValueTextColor(Color.BLACK);

// Установка меток в легенде
                            Legend legend = binding.barChart.getLegend();
                            legend.setOrientation(Legend.LegendOrientation.VERTICAL);
                            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
                            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); // Расположение справа
                            legend.setDrawInside(false);
                            legend.setXEntrySpace(7f);
                            legend.setYEntrySpace(0f);
                            legend.setYOffset(0f);
                            legend.setTextSize(12f);
                            legend.setWordWrapEnabled(true);

// Установка меток для цветов и айди
                            for (int i = 0; i < labels.size(); i++) {
                                LegendEntry entry = new LegendEntry();
                                entry.formColor = colorsList.get(i);
                                entry.label = labels.get(i);
                                legendEntries.add(entry);
                            }
                            legend.setCustom(legendEntries);

                            PieData barDataDay = new PieData(barDataSetDay);
                            binding.barChart.setData(barDataDay);
                            binding.barChart.invalidate();


                        }




                        if(barWeek!=null){
                            for (int i = 0; i < barWeek.length; i++) {
                                int dayOfWeek = (i + 1) % 7;
                                if (dayOfWeek == 0) dayOfWeek = 7;
                                barEntriesWeek.add(new BarEntry(dayOfWeek, barWeek[i]));
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
                        }
                        if(diaryEntryListMounth!=null){
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
                        }
                    });
        });






        return view;
    }

    public void setDay(DiaryEntry diaryEntry){
        int id = diaryEntry.getEmotion().getId();
        pieDay= new int[5];
        switch (id){
            case 1:
                one++;
                all++;
                pieDay[0]=one;
                break;
            case 2:
                two++;
                all++;
                pieDay[1] = two;
                break;
            case 3:
                three++;
                all++;
                pieDay[2]=three;
                break;
            case 4:
                four++;
                all++;
                pieDay[3]=four;
                break;
            case 5:
                five++;
                all++;
                pieDay[4]=five;
                break;
        }
    }



    public static int getColorForEmotionId(int emotionId) {
        switch (emotionId) {
            case 1:
                return Color.parseColor("#455A64");
            case 2:
                return Color.parseColor("#607D8B");
            case 3:
                return Color.parseColor("#90A4AA");
            case 4:
                return Color.parseColor("#B0BEC5");
            case 5:
                return Color.parseColor("#D0E0E8");
            default:
                return Color.BLACK;
        }
    }
}