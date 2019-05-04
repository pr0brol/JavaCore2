package ru.geekbrain.lesson1;

import java.util.ArrayList;

public class FormatToArrayList<T> {
    private T obj;

    public FormatToArrayList(){
        this.obj = obj;
    }

    public ArrayList format(T... array){
        ArrayList arrayList = new ArrayList();
        for (T elem : array){
            arrayList.add(elem);
        }
        return arrayList;
    }
}