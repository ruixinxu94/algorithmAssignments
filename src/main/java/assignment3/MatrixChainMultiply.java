package assignment3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * @author ruixin xu
 * Accepts as input the shapes of a chain of matrices
 * and calculates the most efficient sequence in which they can be multiplied as well as the minimum number
 * of scalar multiplications necessary.
 */
public class MatrixChainMultiply {
    private final static String ERROR_Ordered_ROW_COL = "The matrices cannot be multiplied";
    private final static String ERROR_INPUT_FORMAT = "error: the format of the input line is wrong";
    private final static String ERROR_IO = "Failed to open input file.";
    private final static String HELP_MSG = "Usage: java MatrixChainMultiply <input file>";
    private final static String ERROR_ARRAY_BOUNDARY = "error: index is out of boundary";
    private DpELement[][] dp;

    private final List<Integer> orderedRowCol = new ArrayList<>();
    private final List<String> matrixNames = new ArrayList<>();

    static class DpELement {
        int min = Integer.MAX_VALUE;

        int splitIndex = -1;
    }

    public boolean doMatrixChainMultiply() {
        if (orderedRowCol.size() < 2) {
            System.out.println(ERROR_Ordered_ROW_COL);
            return false;
        }
        int length = orderedRowCol.size() - 1;
        dp = new DpELement[length][length];
        for (int i = 0; i < dp.length; i++) {
            dp[i][i] = new DpELement();
            dp[i][i].min = 0;
            dp[i][i].splitIndex = i;
        }
        // iterate map and calculate depends on the dp rule
        for (int rangeSize = 2; rangeSize <= length; rangeSize++) {
            for (int i = 0; i < length - rangeSize + 1; i++) {
                int j = i + rangeSize - 1;
                dp[i][j] = new DpELement();
                for (int k = i; k < j; k++) {
                    int count = dp[i][k].min + dp[k + 1][j].min +
                            orderedRowCol.get(i) * orderedRowCol.get(k + 1) * orderedRowCol.get(j + 1);
                    if (count < dp[i][j].min) {
                        dp[i][j].min = count;
                        dp[i][j].splitIndex = k;
                    }
                }
            }
        }
        return true;
    }


    public String buildString() {
        if (dp == null || dp.length < 1) {
            return null;
        }
        return dfs(0, dp.length - 1).toString().trim();
    }

    private StringBuilder dfs(int leftIndex, int rightIndex) {
        StringBuilder sb = new StringBuilder();
        if (leftIndex > rightIndex) {
            throw new RuntimeException(ERROR_ARRAY_BOUNDARY + ":leftIndex is greater than rightIndex");
        }
        if (leftIndex == rightIndex) {
            return sb.append(matrixNames.get(rightIndex));
        }
        if (rightIndex - leftIndex == 1) {
            return sb.append("(")
                    .append(matrixNames.get(leftIndex))
                    .append(" ")
                    .append(matrixNames.get(rightIndex))
                    .append(")");
        }
        int splitIndex = dp[leftIndex][rightIndex].splitIndex;
        if (splitIndex < 0 || splitIndex >= dp.length) {
            throw new RuntimeException(ERROR_ARRAY_BOUNDARY + ":splitIndex out of boundary");
        }
        sb.append("(")
                .append(dfs(leftIndex, splitIndex))
                .append(" ")
                .append(dfs(splitIndex + 1, rightIndex))
                .append(")");

        return sb;
    }

    public boolean scanFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lastCol = -1;
            while ((line = reader.readLine()) != null) {
                String[] array = line.split(" ");
                if (array.length != 3) {
                    System.out.println(ERROR_INPUT_FORMAT);
                    return false;
                }
                String matrixName = array[0];
                int row = Integer.parseInt(array[1]);
                int col = Integer.parseInt(array[2]);
                if (lastCol != -1 && lastCol != row) {
                    System.out.println(ERROR_Ordered_ROW_COL);
                    return false;
                }
                if (lastCol == -1) {
                    orderedRowCol.add(row);
                }
                orderedRowCol.add(col);
                matrixNames.add(matrixName);
                lastCol = col;
            }
        } catch (Exception e) {
            System.out.println(ERROR_IO);
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        if (args == null || args.length != 1 || "--help".equals(args[0]) || "-h".equals(args[0])) {
            System.out.println(HELP_MSG);
            return;
        }
        String filePath = args[0];
        MatrixChainMultiply matrixChainMultiply = new MatrixChainMultiply();
        if (!matrixChainMultiply.scanFile(filePath)) {
            return;
        }
        boolean isSuccess = matrixChainMultiply.doMatrixChainMultiply();
        if (!isSuccess) {
            return;
        }
        int multiplicity = matrixChainMultiply.dp[0][matrixChainMultiply.dp.length-1].min;
        System.out.println(multiplicity);
        String str = matrixChainMultiply.buildString();
        System.out.println(str);
    }

}
