package assignment1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ruixin Xu
 * @date 9/18/2023
 * @apiNote Algorithm Assignment 1
 */
public class Sorter {
    private final static String HELP_MSG = "Usage: java Sorter <input file> <number of entries> <mode> <output file>\n";
    private final static String FILE_OPEN_ERR_MSG = "ERROR: could not open input file";
    private final static String ENTRIES_NEGATIVE_MSG = "ERROR: number of entries must be positive";

    private final static String ENTRIES_INVALID_MSG = "ERROR: number of entries should be a number.";

    private final static String COUNT_INVALID_MSG = "ERROR: count in the file should be a number";
    private final static String MODE_INVALID_MSG = "ERROR: mode must be name, count, nameThenCount, or countThenName.";
    private final static String OUTPUT_INVALID_MSG = "ERROR: an error occurred when output the file";

    private Mode currentMode;

    private Record[] records;

    private void setCurrentMode(Mode currentMode) {
        this.currentMode = currentMode;
    }

    private void setRecords(Record[] records) {
        this.records = records;
    }

    private static class Record {
        String name;
        int count;

        Record(String name, int count) {
            this.name = name;
            this.count = count;
        }

        @Override
        public String toString() {
            return "*****name:" + name + " count:" + count + "******\n";
        }
    }

    enum Mode {
        NAME("name"),
        COUNT("count"),
        NAMETHENCOUNT("nameThenCount"),
        COUNTTHENNAME("countThenName");

        final String mode;

        Mode(String mode) {
            this.mode = mode;
        }


    }

    private void doSort() {
        if (records.length <= 1) {
            return;
        }
        mergeSort(0, records.length - 1);
    }

    /**
     * @apiNote recursively mergeSort records array
     */
    private void mergeSort(int left, int right) {
        // base case
        if (right <= left) {
            return;
        }
        int mid = left + (right - left) / 2;
        mergeSort(left, mid);
        mergeSort(mid + 1, right);
        merge(left, mid, right);
    }

    /**
     * @apiNote merge current sorted subarray
     */
    private void merge(int left, int mid, int right) {
        // create a temp array to store the new sorted subarray
        Record[] temp = new Record[right - left + 1];
        int leftSubArrayIndex = left;
        int rightSubArrayIndex = mid + 1;
        int tempIndex = 0;
        while (leftSubArrayIndex <= mid && rightSubArrayIndex <= right) {
            boolean isOrdered = selectedModeCompare(records[leftSubArrayIndex], records[rightSubArrayIndex]);
            if (isOrdered) {
                temp[tempIndex++] = records[leftSubArrayIndex++];
            } else {
                temp[tempIndex++] = records[rightSubArrayIndex++];
            }
        }

        while (leftSubArrayIndex <= mid) {
            temp[tempIndex++] = records[leftSubArrayIndex++];
        }
        while (rightSubArrayIndex <= right) {
            temp[tempIndex++] = records[rightSubArrayIndex++];
        }
        for (int j = 0; j < temp.length; j++) {
            records[left + j] = temp[j];
        }
    }

    /**
     * @apiNote compare by currentMode
     */
    private boolean selectedModeCompare(Record record1, Record record2) {
        switch (currentMode) {
            case NAME:
                return compareAlphabeticalOrder(record1.name, record2.name) <= 0;
            case COUNT:
                return record1.count <= record2.count;
            case NAMETHENCOUNT:
                return isNameThenCountSorted(record1, record2);
            case COUNTTHENNAME:
                return isCountThenNameSorted(record1, record2);
            default:
                throw new IllegalArgumentException(MODE_INVALID_MSG);
        }
    }

    /**
     * @apiNote check if the mode contains in the enum
     */
    private boolean containsMode(String mode) {
        for (Mode m : Mode.values()) {
            if (m.mode.equals(mode)) {
                setCurrentMode(m);
                return true;
            }
        }
        System.out.println(MODE_INVALID_MSG);
        return false;
    }

    private boolean isNameThenCountSorted(Record record1, Record record2) {
        int code = compareAlphabeticalOrder(record1.name, record2.name);
        if (code == 0) {
            return record1.count < record2.count;
        }
        return code < 0;
    }

    private boolean isCountThenNameSorted(Record record1, Record record2) {
        if (record1.count == record2.count) {
            return compareAlphabeticalOrder(record1.name, record2.name) <= 0;
        }
        return record1.count < record2.count;
    }

    /**
     * @return -1 if it's in alphabetical order
     * 0 if they are same
     * 1 if it's not in alphabetical order
     * @apiNote a string cmparator to compare whether these two words in alphabetical order
     */
    private int compareAlphabeticalOrder(String name1, String name2) {
        int length1 = name1.length();
        int length2 = name2.length();
        for (int i = 0; i < Math.min(length1, length2); i++) {
            if (name1.charAt(i) > name2.charAt(i)) {
                return 1;
            } else if (name1.charAt(i) < name2.charAt(i)) {
                return -1;
            }
        }
        if (length1 < length2) {
            return -1;
        } else if (length1 > length2) {
            return 1;
        }
        return 0;
    }

    private Integer tryParseInt(String str, String errorMsg) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            System.out.println(errorMsg);
            return null;
        }
    }

    /**
     * @apiNote convert the txt file line by line
     * assume to only convert the defined numEntries of data
     * if numEntries is larger than the lines in the file, convert all the file
     */
    public boolean convertFileToRecord(String filepath, int numEntries) {
        List<Record> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            int lineNumber = 1;
            while (lineNumber <= numEntries && (line = br.readLine()) != null) {
                String[] record = line.split(",");
                String name = record[0].trim();
                Integer count = tryParseInt(record[1].trim(), COUNT_INVALID_MSG + " in line " + lineNumber);
                lineNumber++;
                if (count == null) {
                    return false;
                }
                Record newRecord = new Record(name, count);
                records.add(newRecord);
            }
        } catch (Exception e) {
            System.out.println(FILE_OPEN_ERR_MSG);
            return false;
        }

        setRecords(records.toArray(new Record[0]));
        return true;
    }

    public void outputRecordsToFile(String filepath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            for (Record record : records) {
                bw.write(record.name + "," + record.count);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println(OUTPUT_INVALID_MSG);
        }
    }

    public void doProcess(String[] args) {
        if (args.length == 1 && "--help".equals(args[0])) {
            System.out.println(HELP_MSG);
            return;
        }
        if (args.length != 4) {
            System.out.println(HELP_MSG);
            return;
        }
        String inputFile = args[0];
        String numOfEntries = args[1];
        String mode = args[2];
        String outputFile = args[3];
        Sorter sorter = new Sorter();
        Integer numEntries = sorter.tryParseInt(numOfEntries, ENTRIES_INVALID_MSG + "/n" + HELP_MSG);
        if (null == numEntries || !sorter.containsMode(mode)) {
            return;
        }
        if (numEntries < 0) {
            System.out.println(ENTRIES_NEGATIVE_MSG);
            return;
        }
        boolean isConvertingValid = sorter.convertFileToRecord(inputFile, numEntries);
        if (!isConvertingValid) {
            return;
        }
        sorter.doSort();
        sorter.outputRecordsToFile(outputFile);
    }


    public static void main(String[] args) {
        Sorter sorter = new Sorter();
        sorter.doProcess(args);
    }

}
