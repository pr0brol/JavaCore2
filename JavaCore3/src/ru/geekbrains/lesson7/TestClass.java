package ru.geekbrains.lesson7;

public class TestClass {




    @BeforeSuite
    public void beforeSuite(){
        System.out.println("beforeSuite");
    }

    @TestSuite(priority = 2)
    public void testSuite2(){
        System.out.println("testSuite2" );
    }
    @TestSuite(priority = 1)
    public void testSuite1(){
        System.out.println("testSuite1");
    }
    @TestSuite(priority = 3)
    public void testSuite3(){
        System.out.println("testSuite3");
    }
    @AfterSuite
    public void afterSuite(){
        System.out.println("afterSuite");
    }
}
