package com.base;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FirstTest {

//    int totalRows = 0;
//    int unknownColumns = 0; // problem rows

    public static class ColumnInfo {
        String columnName;
        String dataType;

        public ColumnInfo(String columnName, String dataType) {
            this.columnName = columnName;
            this.dataType = dataType;
        }

    }


    public static List<ColumnInfo> readFileHeader(String filePath) {

        List<ColumnInfo> columnInfos = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] header = reader.readNext(); // Read the first line (header)

            if (header == null) {
                System.out.println("File is empty or has no header.");
                return null; // Return null if the file is empty or has no header
            }

            for (String columnName : header) {
                if (columnName == null || columnName.trim().isEmpty()) {
                    System.out.println("Invalid header: Empty column name found.");
                    return null; // Return null if any column name is empty
                }
                columnInfos.add(new ColumnInfo(columnName, "Unknown")); // Initialize with "Unknown" type

            }

            String[] row;
            int rowCount = 0;
            while ((row = reader.readNext()) != null) {
                for (int i = 0; i < row.length; i++) {
                    if (i < columnInfos.size()) {
                        String value = row[i];
                        ColumnInfo columnInfo = columnInfos.get(i);
                        if (columnInfo.dataType.equals("Unknown")) {
                            columnInfo.dataType = getDataType(value); // Infer datatype
                        }
                    }
                }
                rowCount++;
            }

            return columnInfos;

        } catch (Exception e) {
            System.out.println(e); // Handle exceptions appropriately
            return null; // Return null in case of an error
        }
    }

    public static String getDataType(String value) {
        if (value == null || value.isEmpty()) {
            return "String"; // Default to String for empty values
        }

        if (value.matches("-?\\d+")) {
            return "Integer";
        } else if (value.matches("-?\\d+(\\.\\d+)?")) {
            return "Double";
        } else if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return "Boolean";
        } else {
            return "String";
        }
    }

    public static boolean matchingFileName(String fileName, String filePath) {

        Path path = Paths.get(filePath);
        String nameFromPath = path.getFileName().toString();

        if (nameFromPath.equals(fileName)) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {

        if (args.length > 0) { // only 1 arguments string
            // Necessary: input file name. input file type. input file.
            // Optional: schema name. schema file type. schema file.
            int numArgs = args.length;

            if (numArgs == 3 || numArgs == 6) {
                System.out.println("Supplied arguments:");

                for (int i = 0; i < numArgs; i++) {
                    System.out.println((i + 1) + ": " + args[i]);
                } // trailing white space?
            } else {
                System.out.println("Too many or too few arguments (" + numArgs + ").");
            }
        } else {
            System.out.println("Please provide arguments as a single string using -Pargs=\"arg1 arg2 ...\".");
        }

        String csv1FileName = args[0];
        String csv1FileType = args[1];
        String csv1Path = args[2];
//        String csv1FileName = args[3];
//        String csv1FileType = args[4];
//        String csv1Path = args[5];

        System.out.println();

        if (matchingFileName(csv1FileName, csv1Path)) {

            List<ColumnInfo> columnInfos = readFileHeader(csv1Path);

            if (columnInfos != null) {

                for (ColumnInfo columnInfo : columnInfos) {
                    System.out.println(columnInfo.columnName + " : " + columnInfo.dataType);
                }
            } else {
                System.out.println("Could not retrieve header information.");
            }
        } else {
            System.out.println("File name mismatch.");
        }

    }
}