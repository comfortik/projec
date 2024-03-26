package com.example.project;
import android.content.Context;

import com.example.project.CustomAdapter;
import com.example.project.MainActivity;
import com.example.project.Type;

import java.util.List;

public class Spisok {
    public static CustomAdapter createSpisok(Context context, List<Type> types){
        String [] typeName = new String[types.size()+1];
        String [] typeInterval =new String[types.size()+1];
        for(int i=0; i<types.size();i++){
            Type type = types.get(i);
            typeName[i] = type.getName();
            if(type.isInterval()) typeInterval[i]="I";
            else typeInterval[i]="N";
        }
        typeName[types.size()]= "Создать свой режим";
        CustomAdapter adapter = new CustomAdapter(context, typeName, typeInterval);
        return  adapter;
    }
    public static void addType(List<Type> types, Type newType) {
        types.add(newType);
        // Обновление списка после добавления нового элемента
    }
}
