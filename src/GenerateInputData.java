import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class GenerateInputData {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int numberOfTimeSteps = sc.nextInt();
        int width = sc.nextInt();
        int height = sc.nextInt();

        StringBuilder row = new StringBuilder();

        row.append(String.format("%d %d %d%n", numberOfTimeSteps, width, height));

        for (int time = 0; time < numberOfTimeSteps; time++) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    for (int i = 0; i < 3; i++) {
                        row.append(String.format("%.6f ", (-1 + Math.random()*2)));
                    }
                }
            }
            row.append("\n");
        }

        writeResults("generatedInput20.txt", row.toString());
    }

    static void writeResults(String fileName, String s) {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(s);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
