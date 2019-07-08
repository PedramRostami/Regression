import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegressionPlotter extends Application {
    private static double[] coefficient;
    @Override
    public void start(Stage stage) {
        //Defining the axes
        NumberAxis xAxis = new NumberAxis(0, 10, 0.1);
        xAxis.setLabel("X");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Y");

        //Creating the Scatter chart
        ScatterChart<String, Number> scatterChart = new ScatterChart(xAxis, yAxis);

        //Prepare XYChart.Series objects by setting data
        XYChart.Series real = new XYChart.Series();
        List<float[]> realData = readCSV("test.csv");
        for (int i = 0; i < realData.size(); i++) {
            real.getData().add(new XYChart.Data(realData.get(i)[0], realData.get(i)[1]));
        }
        real.setName("Real");

        XYChart.Series predicted = new XYChart.Series();
        List<float[]> predictedData = readCSV("result.csv");
        for (int i = 0; i < predictedData.size(); i++) {
            predicted.getData().add(new XYChart.Data(predictedData.get(i)[0], predictedData.get(i)[1]));
        }
        predicted.setName("Predicted");

        float maxDist = 0;
        for (int i = 0; i < predictedData.size(); i++) {
            if (Math.abs(predictedData.get(i)[1] - realData.get(i)[1]) > maxDist)
                maxDist = Math.abs(predictedData.get(i)[1] - realData.get(i)[1]);
        }

        scatterChart.getData().addAll(real, predicted);


        //Creating a Group object
        Group root = new Group(scatterChart);

        //Creating a scene object
        Scene scene = new Scene(root, 1400, 800);
        scatterChart.setPrefSize(1400, 800);

        //Setting title to the Stage
        stage.setTitle("GA");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }

    public static void main(String args[]) {
        launch(args);
    }

    public static void run(String args[], double[] genes) {
        coefficient = genes;
        launch(args);
    }

    public static List<float[]> readCSV(String path) {
        List<float[]> csv = new ArrayList<>();
        BufferedReader csvReader = null;
        String row;
        try {
            csvReader = new BufferedReader(new FileReader(path));
            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                float[] dataDouble = {Float.parseFloat(data[0]), Float.parseFloat(data[1])};
                csv.add(dataDouble);
            }
            csvReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csv;
    }
}