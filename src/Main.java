import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("ALL")
public class Main {


    public static void main(String[] args) {

        int numberOfGenerations, populationSize, tournamentSize;
        double mutationRate, variance;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Number of Generations : ");
        numberOfGenerations = scanner.nextInt();
        System.out.println("Population Size : ");
        populationSize = scanner.nextInt();
        System.out.println("Tournament Size : ");
        tournamentSize = scanner.nextInt();
        System.out.println("Mutation Rate : ");
        mutationRate = scanner.nextDouble();
        System.out.println("Variance : ");
        variance = scanner.nextDouble();
        List<float[]> csv = readCSV("test.csv");
        GA regressionGA = new GA(populationSize, tournamentSize, mutationRate, variance);
        regressionGA.setInstances(csv);
        System.out.println("Mode : ");
        int mode = scanner.nextInt();
        long choosingTime = 0L;
        long funcTime = 0L;
        if (mode == 0) {
            regressionGA.generatePopulation();
            regressionGA.savePopulation();
        }
        if (mode == 2) {
            regressionGA.loadPopulation();
            List<Double> worstCase = new ArrayList<>();
            List<Double> bestCase = new ArrayList<>();
            List<Double> avgCase = new ArrayList<>();
            for (int i = 0; i < numberOfGenerations; i++) {
                Individual[] newPopulation = new Individual[populationSize];
                for (int j = 0; j < populationSize; j++) {
                    Individual x = regressionGA.randomSelection();
                    Individual y = regressionGA.randomSelection();
                    Individual[] children = regressionGA.reproduce(x, y);
                    double randomProbability = Math.random();
                    if (randomProbability < mutationRate)
                        children[0] = regressionGA.mutation(children[0]);
                    newPopulation[j] = children[0];
                }
                try {
                    regressionGA.chooseBestIndividuals(newPopulation);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                worstCase.add(regressionGA.worstFitness());
                bestCase.add(regressionGA.bestFitness());
                avgCase.add(regressionGA.avgFitness());


            }
            System.out.println("Best ratio for equation is : ");
            System.out.println("X^3 ratio = " + regressionGA.getPopulation()[0].getIndividual()[0]);
            System.out.println("X^2 ratio = " + regressionGA.getPopulation()[0].getIndividual()[1]);
            System.out.println("X^1 ratio = " + regressionGA.getPopulation()[0].getIndividual()[2]);
            System.out.println("X^0 ratio = " + regressionGA.getPopulation()[0].getIndividual()[3]);


            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Plotter.createAndShowGui(bestCase, avgCase, worstCase);
                }
            });

            createResultCSV(regressionGA.getPopulation()[0]);
        }
        if (mode == 1) {
            regressionGA.generatePopulation();
            List<Double> worstCase = new ArrayList<>();
            List<Double> bestCase = new ArrayList<>();
            List<Double> avgCase = new ArrayList<>();
            for (int i = 0; i < numberOfGenerations; i++) {
                Individual[] newPopulation = new Individual[populationSize];
                for (int j = 0; j < populationSize; j++) {
//                    Long s = System.currentTimeMillis();
                    Individual x = regressionGA.randomSelection();
                    Individual y = regressionGA.randomSelection();
//                    funcTime += System.currentTimeMillis() - s;
                    Individual[] children = regressionGA.reproduce(x, y);
                    double randomProbability = Math.random();
                    if (randomProbability < mutationRate)
                        children[0] = regressionGA.mutation(children[0]);
                    newPopulation[j] = children[0];

                }
                try {
//                    long s = System.currentTimeMillis();
                    regressionGA.chooseBestIndividuals(newPopulation);
//                    choosingTime += System.currentTimeMillis() - s;
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                worstCase.add(regressionGA.worstFitness());
                bestCase.add(regressionGA.bestFitness());
                avgCase.add(regressionGA.avgFitness());


            }
            System.out.println("Best ratio for equation is : ");
            System.out.println("X^3 ratio = " + regressionGA.getPopulation()[0].getIndividual()[0]);
            System.out.println("X^2 ratio = " + regressionGA.getPopulation()[0].getIndividual()[1]);
            System.out.println("X^1 ratio = " + regressionGA.getPopulation()[0].getIndividual()[2]);
            System.out.println("X^0 ratio = " + regressionGA.getPopulation()[0].getIndividual()[3]);
//            System.out.println("Function Time = " + funcTime / 1000);
//            System.out.println("Choosing Time = " + choosingTime / 1000);
            createResultCSV(regressionGA.getPopulation()[0]);


            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Plotter.createAndShowGui(bestCase, avgCase, worstCase);
                }
            });
        }


    }

    public static void createResultCSV(Individual individual) {
        try {
            File file = new File("result.csv");
            if (!file.exists())
                file.createNewFile();
            FileWriter csvWriter = new FileWriter("result.csv");
            csvWriter.append("x");
            csvWriter.append(",");
            csvWriter.append("y");
            csvWriter.append("\n");
            for (int i = 0; i < 100; i++) {
                Float x = i * 0.1F;
                Float y = (float) (individual.getIndividual()[0] * Math.pow(x, 3)
                        + individual.getIndividual()[1] * Math.pow(x, 2)
                        + individual.getIndividual()[2] * Math.pow(x, 1)
                        + individual.getIndividual()[3]);
                csvWriter.append(x.toString());
                csvWriter.append(",");
                csvWriter.append(y.toString());
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<float[]> readCSV(String path) {
        List<float[]> csv = new ArrayList<>();
        BufferedReader csvReader = null;
        String row;
        try {
            csvReader = new BufferedReader(new FileReader("test.csv"));
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
