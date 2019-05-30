package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.geekbrains.lesson6.MainClass;
import java.util.Collection;
import java.util.Arrays;

@RunWith(Parameterized.class)
public class TestClass {

    @Parameterized.Parameters
    public static Collection<Object[]> data(){

      return Arrays.asList(new Object[][][] {
              {{1, 2, 3, 4, 5, 6, 7, 8, 9}, {5, 6, 7, 8, 9}, {true}},
              {{1, 1, 2, 2, 3, 3, 5}, {}, {true}},
              {{6, 7, 2, 2, 3, 3, 5, 6}, {}, {false}},
              {{1, 2, 3, 4, 5, 6, 4, 8, 9}, {8, 9}, {true}}
      });
    }

    private int[] in;
    private int[] out;
    private boolean result;


    public TestClass(int[] in, int[] out, boolean result){
        this.in = in;
        this.out = out;
        this.result = result;
    }

    private MainClass mainClass;

    @Before
    public void init(){
        mainClass = new MainClass();
    }

    @Test
    public void testSomeArray(){
        Assert.assertArrayEquals(out, mainClass.someArray(in));
    }

    @Test
    public void testTestArray(){
        if(result){
            Assert.assertTrue(mainClass.testArray(in));
        }else {
            Assert.assertFalse(mainClass.testArray(in));
        }
    }
}
