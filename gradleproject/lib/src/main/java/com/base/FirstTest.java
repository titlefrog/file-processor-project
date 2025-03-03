package com.base;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

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

    public static List<List<String>> readData(String filePath) {
        try {
            List<List<String>> data = new ArrayList<>();

            FileReader fr = new FileReader(filePath);
            CSVReader reader = new CSVReader(fr); // need this?

//            // custom separator semi-colon
//            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
//
//            // create csvReader object with parameter
//            // filereader and parser
//            CSVReader csvReader = new CSVReaderBuilder(filereader)
//                    .withCSVParser(parser)
//                    .build();

            reader.readNext(); // Skip the header line

            String[] lineData = reader.readNext();
            while (lineData != null) {
                data.add(Arrays.asList(lineData));
                lineData = reader.readNext();
            }

            for (List<String> list : data) { // list of lists of strings
                for (String str : list) {
                    System.out.print(str + " ");
                }
                System.out.println();
            }

            reader.close();

            return data;

        } catch (Exception e) {
            System.out.println(e);
            return null;
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
        return nameFromPath.equals(fileName);
    }

    public static boolean performAverage(int index, List<List<String>> data, List<ColumnInfo> columnInfos) {

        // list[0][columnIndex]
        if (data == null) {
            return false; // Handle null input
        }

        for (List<String> innerList : data) {
            if (innerList != null && innerList.size() >= 2) {
                System.out.println(innerList.get(index)); // Access the second item (index 1)
            } else {
                // Handle cases where the inner list is null or has fewer than 2 elements
                //System.out.println("Error at column:row: " +  + "");
                System.out.println("Inner list too short (<2) or null"); //Or any other error handling you need.
            }
        }

        return true;

    }

    public static int getColumnIndex(List<ColumnInfo> columnInfos, String columnName) {
        if (columnInfos == null || columnName == null) {
            return -1;
        }
        for (int i = 0; i < columnInfos.size(); i++) {
            if (columnName.equals(columnInfos.get(i).columnName)) {
                return i;
            }
        }
        return -1;
    }


    public static void main(String[] args) {

        if (args.length > 0) { // only 1 arguments string
            // Necessary: input file name. input file type. input file.
            // Optional: schema name. schema file type. schema file.
            int numArgs = args.length;

            if (numArgs >= 3 ) {
                System.out.println("Arguments:");

                for (int i = 0; i < numArgs; i++) {
                    System.out.print((i + 1) + ": " + args[i] + ", ");
                }
            } else {
                System.out.println("Too many or too few arguments (" + numArgs + ").");
            }
        } else {
            System.out.println("Please provide arguments as a single string using -Pargs=\"arg1 arg2 ...\".");
        }

        String csv1FileName = args[0];
        String csv1FileType = args[1];
        String csv1Path = args[2];
        String hasHeader = args[3];
        String aggFunction = args[4];
        String selectedColName = args[5];


        //List<String> groupingCols = Arrays.asList(args[6].split(","));
        String groupingCols = args[6];

//        String csv1FileName = args[3];
//        String csv1FileType = args[4];
//        String csv1Path = args[5];

        System.out.println("\n\nSupplied arguments:\n");
        System.out.println("Input file name:" + csv1FileName);
        System.out.println("Input file type:" + csv1FileType);
        System.out.println("Input file path:" + csv1Path);
        System.out.println("hasHeader?:" + hasHeader);

        System.out.println("Aggregate function to perform:" + aggFunction);
        System.out.println("Column to perform function on:" + selectedColName);
        System.out.println("Grouping columns:" + groupingCols);


        System.out.println();

        if (matchingFileName(csv1FileName, csv1Path)) {

            if (hasHeader.equals("true")) {

                List<ColumnInfo> columnInfos = readFileHeader(csv1Path);

                if (columnInfos != null) {
                    System.out.println("\nFile header:");

                    for (ColumnInfo columnInfo : columnInfos) {
                        System.out.println(columnInfo.columnName + " : " + columnInfo.dataType);
                    }

                    int columnIndex = getColumnIndex(columnInfos, selectedColName);
                    if (columnIndex != -1) {
                        //double average = calculateAverage(fileData, columnIndex);
                        System.out.println("Column index: " + columnIndex + " - \"" + selectedColName + "\"");

                        System.out.println("\nFile contents:");
                        List<List<String>> fileData = readData(csv1Path);
                        performAverage(columnIndex, fileData, columnInfos);

                    } else {
                        System.out.println("Column '" + selectedColName + "' not found.");
                    }

                } else {
                    System.out.println("Could not retrieve header information.");
                }
            } else if (hasHeader.equals("false")) {
                System.out.println("HasHeader: false");
            } else {
                System.out.println("Header existence not specified");
            }

        } else {
            System.out.println("File name mismatch.");
        }

    }
}