public class MyArrayDataExeption extends Exception {
    public MyArrayDataExeption(int i, int j){
        System.out.print(String.format("Ошибка значения массива в %d строке, %d столбце  ", i, j));
    }
}
