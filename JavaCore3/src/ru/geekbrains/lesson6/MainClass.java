package ru.geekbrains.lesson6;

import java.util.Arrays;

public class MainClass {
    public static void main(String[] args) {

    }

    public static int[] someArray(int... nums){

        int index = 0;

        for(int i = 0; i < nums.length; i++){
            if(nums[i] == 4){
                index = i;
            }
        }
        if(index == 0){index = nums.length - 1;}

        return Arrays.copyOfRange(nums, index + 1, nums.length);
    }

    public static boolean testArray(int... nums){
        for(int i: nums){
            if(i == 1 || i == 4){
                return true;
            }
        }
        return false;
    }
}
