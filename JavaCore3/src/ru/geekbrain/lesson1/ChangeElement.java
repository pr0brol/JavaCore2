package ru.geekbrain.lesson1;


import java.util.Arrays;
import java.util.List;

public class ChangeElement<T> {

    private List<T> obj;

    public ChangeElement(T... obj){

        this.obj = Arrays.asList(obj);
    }

    public T getObj(){
        return getObj();
    }

    public void change(){
        T first = obj.get(0);
        obj.set(0, obj.get(obj.size()-1));
        obj.set((obj.size()-1), first);
    }
}
