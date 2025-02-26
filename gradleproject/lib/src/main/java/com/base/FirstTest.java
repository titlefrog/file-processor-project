package com.base;

public class FirstTest {
    public static void main(String[] args) {
        System.out.println("This is my first task output.");

        if (args.length > 0) { // only 1 arguments string
            // Necessary: input file name. input file type. input file.
            // Optional: schema name. schema file type. schema file.
            int numArgs = args.length;

            if (numArgs == 3 || numArgs == 6) {
                System.out.println("Supplied arguments:");

                for (int i = 0; i < numArgs; i++) {
                    System.out.println((i + 1) + ": " + args[i]);
                }
            } else {
                System.out.println("Too many or too few arguments ("+numArgs+").");
            }
        } else {
            System.out.println("Please provide arguments as a single string using -Pargs=\"arg1 arg2 ...\".");
        }

    }
}