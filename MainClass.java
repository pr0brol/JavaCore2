
public class MainClass {

    public static void sumArray(String[][] str) throws MyArraySizeExeption, MyArrayDataExeption {
        int sum = 0;
        for(int i = 0; i < str.length; i++){
            if(str.length != 4) throw new MyArraySizeExeption("Ошибка размера массива ");
            for(int j = 0; j < str[i].length; j++){
                if(str[i].length != 4) throw new MyArraySizeExeption("Ошибка размера массива ");
                try {
                    sum += Integer.parseInt(str[i][j]);
                }catch (NumberFormatException ex){
                    throw new MyArrayDataExeption(i, j);
                }
            }
        }
        System.out.println("сумма массива равна " + sum);
    }


    public static void main(String[] args) {
        String[][] str1 = new String[4][4];
        String[][] str2 = new String[4][4];
        String[][] str3 = new String[4][5];

        for (int i = 0; i < str1.length; i++) {
            for (int j = 0; j < str1[i].length; j++) {
                str1[i][j] = "1";
            }
        }
        for (int i = 0; i < str2.length; i++) {
            for (int j = 0; j < str2[i].length; j++) {
                if (i + j == 6) str2[i][j] = "a";
                else str2[i][j] = "2";
            }
        }
        for(int i = 0; i < str3.length; i++) {
            for (int j = 0; j < str3[i].length; j++) {
                str3[i][j] = "3";
            }
        }
        try{
            sumArray(str1);
        }catch (MyArraySizeExeption ex) {
            System.out.println(ex);
        } catch (MyArrayDataExeption ex) {
            System.out.println(ex);
        }
        try{
            sumArray(str2);
        }catch (MyArraySizeExeption ex) {
            System.out.println(ex);
        } catch (MyArrayDataExeption ex) {
            System.out.println(ex);
        }
        try{
            sumArray(str3);
        }catch (MyArraySizeExeption ex) {
            System.out.println(ex);
        } catch (MyArrayDataExeption ex) {
            System.out.println(ex);
        }
    }
}
