package com.example.project.Main;
import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class Spisok {

    List<Type> types;


     FirestoreGetId  firestoreGetId;
     String [] typeName ;
     String [] typeInterval ;
     String [] timeWorkList;
    String [] timeRestList;
    Context context;
    FirebaseFirestore fb;
    FirebaseUser user;
     public Spisok(Context context, FirebaseFirestore fb, FirebaseUser user){
         this.context = context;
         this.fb = fb;
         this.user = user;
     }

    public void createSpisok(DataLoadListener listener) {
        firestoreGetId = new FirestoreGetId(fb);
        firestoreGetId.getId(user.getUid(), userId -> {
            if (userId != null) {
                fb.collection("Users")
                        .document(userId)
                        .collection("Types")
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            List<Type> types = documentSnapshot.toObjects(Type.class);
                            if (types.size() == 0) {
                                typeName = new String[1];
                                typeName[0] = "Создать новый режим";
                                typeInterval = new String[1];
                                typeInterval[0] = "";
                                timeWorkList = new String[1];
                                timeRestList = new String[1];
                                timeWorkList[0]="";
                                timeRestList[0]="";
                                CustomAdapter adapter = new CustomAdapter(context, typeName, typeInterval, timeWorkList, timeRestList);
                                listener.onDataLoaded(adapter);
                            } else {
                                typeInterval = new String[types.size() + 1];
                                typeName = new String[types.size() + 1];
                                timeWorkList= new String[types.size()+1];
                                timeRestList= new String[types.size()+1];
                                for (int i = 0; i < types.size(); i++) {
                                    Type type = types.get(i);
                                    typeName[i] = type.getName();
                                    long totalSeconds= type.getTimeWork()/1000;
                                    long hour = totalSeconds / 3600;
                                    long min = (totalSeconds % 3600) / 60;
                                    long sec = totalSeconds % 60;
                                    NumberFormat f = new DecimalFormat("00");
                                    String timeW= ("Работа: "+f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                                    timeWorkList[i] = timeW;
                                    totalSeconds= type.getTimeRest()/1000;
                                    hour = totalSeconds / 3600;
                                    min = (totalSeconds % 3600) / 60;
                                    sec = totalSeconds % 60;
                                    String timeR= ("Отдых: "+f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                                    timeRestList[i]= timeR;
                                    if (type.isInterval()) typeInterval[i] = "I";
                                    else typeInterval[i] = "N";
                                }
                                typeName[types.size()] = "Создать свой режим";
                                typeInterval[types.size()]="";
                                timeWorkList[types.size()]="";
                                timeRestList[types.size()]="";
                                CustomAdapter adapter = new CustomAdapter(context, typeName, typeInterval, timeWorkList, timeRestList);
                                listener.onDataLoaded(adapter);
                            }
                        });
            } else {
                typeName = new String[1];
                typeName[0] = "Создать новый режим";
                typeInterval[0] = "";
                timeWorkList[0]="";
                timeRestList[0]="";
                CustomAdapter adapter = new CustomAdapter(context, typeName, typeInterval, timeWorkList, timeRestList);
                listener.onDataLoaded(adapter);
            }
        });
    }
    public static void addType(List<Type> types, Type newType) {
        types.add(newType);
        // Обновление списка после добавления нового элемента
    }

}
