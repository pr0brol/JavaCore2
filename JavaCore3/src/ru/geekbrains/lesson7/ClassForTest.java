package ru.geekbrains.lesson7;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface AfterSuite {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface BeforeSuite {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface TestSuite {
    int priority();
}

public class ClassForTest {

    public static void start(Class<?> testClass) {
        Method beforeMethod = null;
        Method afterMethod = null;
        List<Method> listMethods = new ArrayList<>();

        for(Method m: testClass.getMethods()){
            if(m.isAnnotationPresent(BeforeSuite.class)){
                if(beforeMethod == null){
                    beforeMethod = m;
                }else {
                    throw new RuntimeException("Повтор метода BeforeSuite");
                }
            }else if(m.isAnnotationPresent(AfterSuite.class)){
                if(afterMethod == null){
                    afterMethod = m;
                }else{
                    throw new RuntimeException("Повтор метода AfterSuite");
                }
            }else if(m.isAnnotationPresent(TestSuite.class)){
                listMethods.add(m);
            }
        }
        listMethods.sort(Comparator.comparingInt(a -> a.getAnnotation(TestSuite.class).priority()));


        try {
            Object object = testClass.newInstance();
            beforeMethod.invoke(object);
            for(Method m: listMethods){
                m.invoke(object);
            }
            if(afterMethod != null){
                afterMethod.invoke(object);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        ClassForTest.start(TestClass.class);
    }
}
