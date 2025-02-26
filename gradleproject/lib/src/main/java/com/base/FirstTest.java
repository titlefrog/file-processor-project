package com.base;

public class FirstTest {
    public static void main(String[] args) {
        System.out.println("This is my first task output.");

        if (args.length > 0) {

            System.out.println("Supplied arguments:");

//            for (String val : args) {
//                System.out.println("Argument " + args + ": " + val); }
//
//            }
            for (int i = 0; i < args.length; i++) {
                System.out.println("Argument " + i + ": " + args[i]);
            }
        }
        else {
            System.out.println("No arguments provided.");

        }

    }
}