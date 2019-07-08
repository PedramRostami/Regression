import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CSVGenerator {
    public static void main(String[] args) {
        int[] ratio = {10, 5, 12, -9};
        int errorTolerance = 10;
        List<Double[]> csv = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Random random = new Random();
            double x = i * 0.1;
            double y = ratio[0] * Math.pow(x, 3) +
                    ratio[1] * Math.pow(x, 2) +
                    ratio[2] * Math.pow(x, 1) +
                    ratio[3];
            double error = (Math.random() - 0.5) * errorTolerance;
            y += error;
            Double[] point = {x, y};
            csv.add(point);
        }
        try {
            File file = new File("test.csv");
            if (!file.exists())
                file.createNewFile();
            FileWriter csvWriter = new FileWriter("test.csv");
            csvWriter.append("x");
            csvWriter.append(",");
            csvWriter.append("y");
            csvWriter.append("\n");
            for (int i = 0; i < 100; i++) {
                csvWriter.append(csv.get(i)[0].toString());
                csvWriter.append(",");
                csvWriter.append(csv.get(i)[1].toString());
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
