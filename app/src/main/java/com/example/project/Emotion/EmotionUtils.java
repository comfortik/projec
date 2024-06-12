package com.example.project.Emotion;

import static com.example.project.Stata.StataFragment.getColorForEmotionId;

import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.project.Main.AddUserToFirebase;
import com.example.project.Main.DiaryEntry;
import com.example.project.Main.FirestoreGetId;
import com.example.project.Main.FocusMode;
import com.example.project.NewAppWidget;
import com.example.project.Note.Note;
import com.example.project.OnNote;
import com.example.project.R;
import com.example.project.databinding.AlertNoteBinding;
import com.example.project.databinding.FragmentEmotionDiaryBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class EmotionUtils {
     ReactForEmotions reactForEmotions;
     OnNote onNote;
    DiaryEntry diaryEntry;
    static int pieDay[];
    private String url="http://172.20.10.2:5000";
    private String POST="POST";
    private static final String[][] moods = {
            {
                    "Депрессивный", "Угнетенный", "Опустошенный", "Разочарованный", "Безнадежный",
                    "Отчаявшийся", "Тоскливый", "Меланхоличный", "Печальный", "Несчастный",
                    "Раздраженный", "Злой", "Гневный", "Разозленный", "В отчаянии",
                    "Обескураженный", "Разочарованный", "Удрученный", "Загнанный в угол", "Подавленный",
                    "Беспокойный", "Неуверенный", "Тревожный", "Страдающий", "Мучимый",
                    "Одинокий", "Изолированный", "Нелюбимый", "Неудовлетворенный", "Разочарованный",
                    "Несчастный", "Разбитый", "Беспомощный", "Беззащитный", "Униженный"
            },
            {
                    "Грустный", "Печальный", "Тоскливый", "Разочарованный", "Уставший",
                    "Скучающий", "Безразличный", "Нервный", "Обеспокоенный", "Неуверенный",
                    "Одинокий", "Изолированный", "Несчастный", "Разочарованный", "Недовольный",
                    "Огорченный", "Расстроенный", "Сокрушенный", "Разочарованный", "Подавленный",
                    "Задумчивый", "Меланхоличный", "Сентиментальный", "Тихий", "Спокойный",
                    "Неуверенный", "Нерешительный", "Смущенный", "Неловкий", "Осторожный",
                    "Немного подавленный", "Разочарованный", "Скучающий", "Немного тревожный", "Утомленный"
            },
            {
                    "Спокойный", "Расслабленный", "Нейтральный", "Тихий", "Безразличный",
                    "Задумчивый", "Сконцентрированный", "Сосредоточенный", "Обычный", "Средний",
                    "Невозмутимый", "Уверенный", "Равнодушный", "Невозбудимый", "Сдержанный",
                    "Созерцательный", "Ожидающий", "Открытый", "Готовый", "Оптимистичный",
                    "Заинтересованный", "Любопытный", "Свободный", "Уравновешенный", "Сбалансированный",
                    "Независимый", "Самостоятельный", "Независимый", "Успокоенный", "Мирно",
                    "Безмятежный", "Отдохнувший", "Свежий", "Ясный", "Чистый"
            },
            {
                    "Счастливый", "Радостный", "Веселый", "Взволнованный", "Восхищенный",
                    "Уверенный", "Энергичный", "Оптимистичный", "Воодушевленный", "Вдохновленный",
                    "Благодарный", "Счастливый", "Довольный", "Удовлетворенный", "Счастливый",
                    "Заинтересованный", "Любопытный", "Сострадательный", "Сочувствующий", "Дружелюбный",
                    "Общительный", "Влюбленный", "Преданный", "Верный", "Заботливый",
                    "Отзывчивый", "Щедрый", "Доброжелательный", "Сотрудничающий", "Поддерживающий",
                    "Сильный", "Смелый", "Мотивированный", "Создающий", "Успешный"
            },
            {
                    "Возбужденный", "В восторге", "Взволнованный", "Радивый", "Эйфорический",
                    "Триумфальный", "Победоносный", "Восхитительный", "Превосходный", "Великолепный",
                    "Прекрасный", "Чудесный", "Захватывающий", "Волнующий", "Вдохновляющий",
                    "Любящий", "Преданный", "Благодарный", "Счастливый", "Счастливый",
                    "Уверенный", "Свободный", "Могущественный", "Непобедимый", "Непревзойденный",
                    "Креативный", "Талантливый", "Успешный", "Сильный", "Мощный",
                    "Живой", "Энергичный", "Веселый", "Счастливый", "Свободный"
            }
    };
    ArrayList<String> emotionWords = new ArrayList<>();


    boolean isFirstClick = true;
    static  int one, two, three, four, five, all=0;
    static List<LegendEntry> legendEntries;
    FirestoreGetId firestoreGetId;
    static Bitmap chartBitmap;
    private OnCloseDialogEmotionListener onCloseDialogEmotionListener;
    private Context context;
    FragmentEmotionDiaryBinding binding;
    private static FirebaseFirestore fb;
    private static FirebaseAuth mAuth;

    public void setOnCloseDialogEmotionListener(OnCloseDialogEmotionListener listener) {
        this.onCloseDialogEmotionListener = listener;
    }

    public EmotionUtils( ReactForEmotions reactForEmotions) {
        this.reactForEmotions = reactForEmotions;
    }


    public  void setListeners(Context context, FragmentEmotionDiaryBinding binding) {
        binding.imgPloho.setOnClickListener(v -> {
            Emotion emotion = new Emotion(1);
            emotionBtns(context, emotion);
        });
        binding.imgTakoe.setOnClickListener(v -> {
            Emotion emotion= new Emotion(2);
            emotionBtns(context, emotion);
        });
        binding.imgNorm.setOnClickListener(v -> {
            Emotion emotion= new Emotion(3);
            emotionBtns(context, emotion);
        });
        binding.imgWow.setOnClickListener(v -> {
            Emotion emotion= new Emotion(4);
            emotionBtns(context, emotion);
        });
        binding.imgSuper.setOnClickListener(v -> {
            Emotion emotion= new Emotion(5);
            emotionBtns(context, emotion);
        });
        this.binding = binding;
    }

    public  void emotionBtns(Context context, Emotion emotion) {
        int emotionId = emotion.getId();
        switch (emotionId){
            case 1:
                setButtons(moods[0], context, binding, emotion);
                break;
            case 2:
                setButtons(moods[1], context, binding, emotion);
                break;
            case 3:
                setButtons(moods[2], context, binding, emotion);
                break;
            case 4:
                setButtons(moods[3], context, binding, emotion);
                break;
            case 5:
                setButtons(moods[4], context, binding, emotion);
                break;
        }
    }
    public void setButtons(String[] words, Context context, FragmentEmotionDiaryBinding binding, Emotion emotion) {
        binding.flexbox.removeAllViews();
        for (String word : words) {
            MaterialButton button = new MaterialButton(context);
            button.setText(word);
            button.setCornerRadius(25);
            button.setTextColor(Color.WHITE);
            button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            button.setTypeface(Typeface.MONOSPACE);

            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 12, 0);
            button.setLayoutParams(params);

            button.setOnClickListener(v -> {
                button.setBackgroundColor(ContextCompat.getColor(context, R.color.bynBackgroungLight));
                if(!emotionWords.contains(word)) emotionWords.add(word);
            });
            binding.flexbox.addView(button);
        }

        binding.scrollView.setVisibility(View.VISIBLE);
        binding.btnOk.setVisibility(View.VISIBLE);

        binding.btnOk.setOnClickListener(v -> {
            emotion.setEmotionWords(emotionWords);
            AlertDialogEmotionDiary(context, reactForEmotions.emotionOne(), emotion);
            binding.btnOk.setVisibility(View.GONE);

            binding.flexbox.removeAllViews();
        });
    }


    private void AlertDialogEmotionDiary(Context context, AddUserToFirebase.HelpEmotion helpEmotion, Emotion emotion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogText);
        builder.setTitle(helpEmotion.getHelp())
                .setTitle("")
                .setMessage("Хочешь оставить заметку?")
                .setCancelable(false)
                .setPositiveButton("Да", (dialog, which) -> {
                    onNote.onNote(emotion);
                    dialog.cancel();
                })
                .setNegativeButton("Нет", (dialog, which) -> {
                    onCloseDialogEmotionListener.onHideDialog(emotion);
                    dialog.cancel();
                });
                this.context=context;
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(Color.parseColor("#90A4AE"));
        gradientDrawable.setCornerRadius(10);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(gradientDrawable);
        alertDialog.show();
    }
    public void alertEmotion(FirebaseFirestore fb, FirebaseAuth mAuth, FocusMode focusMode, Emotion emotion){
        FirestoreGetId  firestoreGetId = new FirestoreGetId(fb);
        this.fb = fb;
        this.mAuth  = mAuth;
        if(focusMode!=null){
            diaryEntry = new DiaryEntry( focusMode,  emotion);
            Log.e("AAAAAAA", String.valueOf(diaryEntry.getFocusMode().getSecRest()));
        }
        else{
            diaryEntry = new DiaryEntry(emotion);
        }
        firestoreGetId.getId(Objects.requireNonNull(mAuth.getCurrentUser()).getUid(), userId -> fb.collection("Users")
                .document(userId)
                .collection("Entry")
                .add(diaryEntry)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        createBarChartBitmap(context);
                    }
                }));


    }

    public void alertNote(Context context, LayoutInflater layoutInflater, FirebaseFirestore fb, FirebaseAuth mAuth, FocusMode focusMode, Emotion emotion){
        firestoreGetId = new FirestoreGetId(fb);
        this.context = context;
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setCancelable(false);
        AlertNoteBinding binding2 = AlertNoteBinding.inflate(layoutInflater, null, false);
        View view = binding2.getRoot();
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(Color.parseColor("#90A4AE"));
        gradientDrawable.setCornerRadius(10);
        builder1.setView(view);
        AlertDialog alertDialog = builder1.create();
        alertDialog.getWindow().setBackgroundDrawable(gradientDrawable);
        alertDialog.show();
        binding2.btnOk.setOnClickListener(v -> {
            if(binding2.etNote.getText().length()==0){
                Toast.makeText(context, "Введите заметку", Toast.LENGTH_SHORT).show();
            } else if (binding2.etNote.getText().toString().isEmpty()) {
                Toast.makeText(context, "Введите заметку", Toast.LENGTH_SHORT).show();
            }
            else{
                Note note = new Note(binding2.etNote.getText().toString());
                if(focusMode!=null) {
                    diaryEntry = new DiaryEntry(note, focusMode, emotion);
                }
                else{
                    diaryEntry = new DiaryEntry(note, emotion);
                }
                firestoreGetId.getId(Objects.requireNonNull(mAuth.getCurrentUser()).getUid(), userId -> fb.collection("Users")
                        .document(userId)
                        .collection("Entry")
                        .add(diaryEntry)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                createBarChartBitmap(context);
                                Toast.makeText(context, "Запись добавлена", Toast.LENGTH_SHORT).show();
                            }
                            else Toast.makeText(context, "Не удалось добавить запись", Toast.LENGTH_SHORT).show();
                        }));

                alertDialog.cancel();
            }

        });
    }

    private static void updateWidget( Context context, Bitmap bitmap) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, NewAppWidget.class));
        for (int appWidgetId : appWidgetIds) {
            NewAppWidget.updateAppWidget(context, appWidgetManager, appWidgetId, bitmap);
        }
    }
    public static void createBarChartBitmap(Context context) {
        mAuth = FirebaseAuth.getInstance();
        fb = FirebaseFirestore.getInstance();
        PieChart pieChart = new PieChart(context);
        List<PieEntry> barEntriesDay = new ArrayList<>();
        FirestoreGetId firestoreGetId = new FirestoreGetId(fb);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        pieDay = new int[5];
        firestoreGetId.getId(mAuth.getCurrentUser().getUid(), userId -> {
            fb.collection("Users")
                    .document(userId)
                    .collection("Entry")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if(documentSnapshot!=null){
                                DiaryEntry diaryEntry = documentSnapshot.toObject(DiaryEntry.class);
                                Date input = diaryEntry.getDate();
                                LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                LocalDate today  = LocalDate.now();
                                if (date.equals(today)) {
                                    int id = diaryEntry.getEmotion().getId();
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

                            }
                        }

                        List<Integer> colorsList = new ArrayList<>();

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


                        PieDataSet barDataSetDay = new PieDataSet(barEntriesDay, "Today");
                        barDataSetDay.setColors(colorsList);
                        barDataSetDay.setSliceSpace(1f);
                        barDataSetDay.setDrawValues(false);
                        barDataSetDay.setDrawIcons(false);
                        barDataSetDay.setValueTextSize(12f);
                        barDataSetDay.setIconsOffset(new MPPointF(0, 40));
                        barDataSetDay.setValueTextColor(Color.BLACK);
                        pieChart.getLegend().setEnabled(false);
                        pieChart.setHoleColor(Color.TRANSPARENT);


                        pieChart.getDescription().setEnabled(false);
                        PieData barDataDay = new PieData(barDataSetDay);
                        pieChart.setData(barDataDay);
                        pieChart.invalidate();


                        pieChart.layout(0, 0, 280, 280);
                        pieChart.setDrawingCacheEnabled(true);
                        pieChart.buildDrawingCache();
                        chartBitmap = Bitmap.createBitmap(pieChart.getDrawingCache());
                        pieChart.setDrawingCacheEnabled(false);
                        updateWidget(context, chartBitmap);
                    });
        });


    }



    public void setOnNote(OnNote onNote){
        this.onNote=onNote;
    }


}
