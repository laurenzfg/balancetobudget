/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.studieren_ohne_grenzen.balancetobudget;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class App {

    private File categoryFile;
    private File accountingFile;
    private File outputFile;
    
    private static CSVFormat fmt = CSVFormat.EXCEL.withDelimiter(';'); // CHOOSE

    private ArrayList<Category> categories = new ArrayList<>();
    private Map<String, Category> categoryMap = new HashMap<>();

    // Weniger String nutzen!
    public App (File categoryFile, File accountingFile, File outputFile) {
        this.categoryFile = categoryFile;
        this.accountingFile = accountingFile;
        this.outputFile = outputFile;

        // read categories
        try {
            CSVParser csvParser = CSVParser.parse(categoryFile, StandardCharsets.UTF_8, fmt);
            for (CSVRecord csvRecord : csvParser) {
                // Accessing Values by Column Index
                String name = csvRecord.get(0);
                Category category = new Category(name);
                categories.add(category);
                // Read all maped thingys
                for(int i = 1; i < csvRecord.size(); i++) {
                    if (csvRecord.isSet(i) && csvRecord.get(i).length() != 0) {
                        System.out.println(csvRecord.get(i) + ": " + name);
                        categoryMap.put(csvRecord.get(i), category);
                    }
                }
            }
            csvParser.close();
        }  catch (Exception e) {
            System.err.println("Hickup while reading categories");
        }

        // read account statement
        // WARNUNG BEI UNMATCHBAR IMPLEMENTEIREN
        try {
            CSVParser csvParser = CSVParser.parse(accountingFile, StandardCharsets.UTF_8, fmt);
            for (CSVRecord csvRecord : csvParser) {
                if (csvRecord.isSet(1) && !csvRecord.get(1).isBlank()) {
                    // Accessing Values by Column Index
                    String name = csvRecord.get(0);
                    String valueS = csvRecord.get(1);
                    int value = 0;
                    System.out.println(name + ": " + valueS);
                    // Normalize name
                    name = name.replace("-", "");
                    name = name.replace(" ", "");
                    // Normalize value
                    // that is not safe by any means, do some checking
                    // we assume too much
                    valueS = valueS.replace(".", "");
                    int multiplier;
                    if (valueS.contains(",")) {
                        // has cent
                        int length = valueS.length();
                        if (valueS.charAt(length-2) == ',') {
                            // xxx,x
                            multiplier = 10;
                        } else {
                            // xxx,xx
                            multiplier = 1;
                        }
                        valueS = valueS.replace(",", "");
                    } else {
                        // has no cent
                        multiplier = 100;
                    }
                    value = Integer.parseInt(valueS);
                    value *= multiplier;
                    if (categoryMap.containsKey(name)) {
                        categoryMap.get(name).add(value);
                    } else {
                        System.err.println(name + "ist ungemappt!");
                    }
                }
            }
            csvParser.close();
        }  catch (Exception e) {
            System.err.println("Hickup while account statement");
        }

        try (CSVPrinter printer = new CSVPrinter(new FileWriter(outputFile, StandardCharsets.UTF_8), fmt)) {
            for (Category c : categories) {
                printer.printRecord(c.getName(), c.getGermanAmount());
            }
            printer.close(true);
        } catch (Exception e) {

        }
    }
    
    public static void main(String[] args) {
        File categoryFile = Util.pickFile(false);
        File accountingFile = Util.pickFile(false);
        File outputFile = Util.pickFile(true);
        new App(categoryFile, accountingFile, outputFile);
    }
}
