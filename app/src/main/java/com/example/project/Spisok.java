package com.example.project;
import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.List;

public class Spisok {

    List<Type> types;


     FirestoreGetId  firestoreGetId;
     String [] typeName ;
     String [] typeInterval ;
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
                            typeInterval = new String[types.size() + 1];
                            typeName = new String[types.size() + 1];
                            for (int i = 0; i < types.size(); i++) {
                                Type type = types.get(i);
                                typeName[i] = type.getName();
                                if (type.isInterval()) typeInterval[i] = "I";
                                else typeInterval[i] = "N";
                            }
                            typeName[types.size()] = "Создать свой режим";

                            CustomAdapter adapter = new CustomAdapter(context, typeName, typeInterval);
                            listener.onDataLoaded(adapter);
                        });
            } else {
                listener.onDataLoaded(null); // обработка случая, когда userId равен null
            }
        });
    }
    public static void addType(List<Type> types, Type newType) {
        types.add(newType);
        // Обновление списка после добавления нового элемента
    }
    public void getTypes() {

    }
}
