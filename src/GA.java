import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("ALL")
public class GA {
    private Integer tournamentSize;
    private Individual[] population;
    private Integer populationSize;
    private double mutationRate;
    private double variance;
    private double[] x;
    private double[] y;

    public GA(Integer populationSize, Integer tournamentSize, double mutationRate, double variance) {
        this.tournamentSize = tournamentSize;
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.variance = variance;
        this.population = new Individual[populationSize];
    }

    public void setInstances(List<float[]> data) {
        x = new double[data.size()];
        y = new double[data.size()];
        for (int i = 0; i < data.size(); i++) {
            x[i] = data.get(i)[0];
            y[i] = data.get(i)[1];
        }
    }

    public void generatePopulation() {
        for (int i = 0; i < population.length; i++) {
            population[i] = new Individual();
        }
    }

    private double fitnessFunction(Individual individual) {
        double result = 0;
        for (int i = 0; i < x.length; i++) {
            double predictedY = individual.getIndividual()[0] * Math.pow(x[i], 3) +
                                individual.getIndividual()[1] * Math.pow(x[i], 2) +
                                individual.getIndividual()[2] * Math.pow(x[i], 1) +
                                individual.getIndividual()[3];
//            result += Math.sqrt(Math.pow(y[i] - predictedY, 2));
            result += Math.pow(y[i] - predictedY, 2);
        }
        result /= x.length;
        result = 1 / (1 + result);
        return result;
    }


    public Individual randomSelection() {
        List<Individual> populations = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            populations.add(population[i]);
        }

//        Random rand = new Random();
//        int[] randomIndex = new int[tournamentSize];
//        for (int i = 0; i < tournamentSize; i++) {
//            randomIndex[i] = -1;
//        }
//        int counter = 0;
//        while (true) {
//            int random = rand.nextInt(50);
//            boolean isDuplicated = false;
//            for (int i = 0; i < counter; i++) {
//                if (randomIndex[i] == random) {
//                    isDuplicated = true;
//                    break;
//                }
//            }
//            if (!isDuplicated) {
//                randomIndex[counter] = random;
//                counter++;
//            }
//            if (counter == tournamentSize)
//                break;
//        }
        Individual[] individuals = new Individual[tournamentSize];
        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = (int) Math.floor(Math.random() * populations.size());
            individuals[i] = populations.get(randomIndex);
            populations.remove(randomIndex);
        }
        int maxIndex = 0;
        double maxFitness = Double.MIN_VALUE;
        for (int i = 0; i < tournamentSize; i++) {
            if (maxFitness < fitnessFunction(individuals[i])) {
                maxFitness = fitnessFunction(individuals[i]);
                maxIndex = i;
            }
        }
        return individuals[maxIndex];
    }

    public Individual[] reproduce(Individual x, Individual y) {
        int crossOverPoint = (int) Math.floor(Math.random() * 4);
        Individual[] children = new Individual[2];
        children[0] = new Individual();
        children[1] = new Individual();
        for (int i = 0; i < crossOverPoint; i++) {
            children[0].getIndividual()[i] = x.getIndividual()[i];
            children[1].getIndividual()[i] = y.getIndividual()[i];
        }
        for (int i = crossOverPoint; i < 4; i++) {
            children[0].getIndividual()[i] = y.getIndividual()[i];
            children[1].getIndividual()[i] = x.getIndividual()[i];
        }
        return children;
    }

    public Individual mutation(Individual individual) {
        for (int i = 0; i < individual.getIndividual().length; i++) {
            individual.getIndividual()[i] += GA.gaussianNoise(variance);
        }
        return individual;
    }

    public static double gaussianNoise(double variance) {
        java.util.Random r = new java.util.Random();
        double noise = r.nextGaussian() * Math.sqrt(variance) + 0;
        return noise;
    }

    public void chooseBestIndividuals(Individual[] newPopulation) throws CloneNotSupportedException {
        Individual[] allPopulation = new Individual[2 * populationSize];
        double[] fitnessScores = new double[2 * populationSize];
        for (int i = 0; i < populationSize; i++) {
            allPopulation[i] = population[i];
            fitnessScores[i] = fitnessFunction(population[i]);
        }
        for (int i = 0; i < populationSize; i++) {
            allPopulation[populationSize + i] = newPopulation[i];
            fitnessScores[populationSize + i] = fitnessFunction(newPopulation[i]);
        }
        quickSort(fitnessScores, allPopulation, 2 * populationSize - 1, 0);
        for (int i = 0; i < populationSize; i++) {
            population[i] = allPopulation[i];
        }
    }



    public void quickSort(double[] fitnessScores, Individual[] individuals, int h, int l) throws CloneNotSupportedException {
        int p;
        if (h > l) {
            p = partitioning(fitnessScores, individuals, h, l);
            quickSort(fitnessScores, individuals, p - 1, l);
            quickSort(fitnessScores, individuals, h, p + 1);
        }
    }

    private int partitioning(double[] fitnessScores, Individual[] individuals, int h, int l) throws CloneNotSupportedException {
        int p, firstHigh;
        p = h;
        firstHigh = l;
        for (int i = l; i < h; i++) {
            if (fitnessScores[i] > fitnessScores[p]) {
                double tmp = fitnessScores[i];
                fitnessScores[i] = fitnessScores[firstHigh];
                fitnessScores[firstHigh] = tmp;
                Individual individualTmp = individuals[i].clone();
                individuals[i] = individuals[firstHigh].clone();
                individuals[firstHigh] = individualTmp;
                firstHigh++;
            }
        }
        double tmp = fitnessScores[p];;
        fitnessScores[p] = fitnessScores[firstHigh];
        fitnessScores[firstHigh] = tmp;
        Individual individualTmp = individuals[p].clone();
        individuals[p] = individuals[firstHigh].clone();
        individuals[firstHigh] = individualTmp;

        return firstHigh;
    }

    public double avgFitness() {
        double avg = 0;
        for (int i = 0; i < populationSize; i++)
            avg += fitnessFunction(population[i]);
        avg /= populationSize;
        return avg;
    }

    public double bestFitness() {
        double max = Float.MIN_VALUE;
        for (int i = 0; i < populationSize; i++)
            max = Math.max(max, fitnessFunction(population[i]));
        return max;
    }

    public double worstFitness() {
        double min = Float.MAX_VALUE;
        for (int i = 0; i < populationSize; i++)
            min = Math.min(min, fitnessFunction(population[i]));
        return min;
    }

    public Individual[] getPopulation() {
        return population;
    }

    public boolean savePopulation() {
        try {
            File file = new File("population.csv");
            if (!file.exists())
                file.createNewFile();
            FileWriter csvWriter = new FileWriter("population.csv");
            csvWriter.append("x");
            csvWriter.append(",");
            csvWriter.append("y");
            csvWriter.append("\n");
            for (int i = 0; i < populationSize; i++) {
                csvWriter.append(Double.toString(population[i].getIndividual()[0]));
                csvWriter.append(",");
                csvWriter.append(Double.toString(population[i].getIndividual()[1]));
                csvWriter.append(",");
                csvWriter.append(Double.toString(population[i].getIndividual()[2]));
                csvWriter.append(",");
                csvWriter.append(Double.toString(population[i].getIndividual()[3]));
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loadPopulation() {
        BufferedReader csvReader = null;
        String row;
        int counter = 0;
        try {
            csvReader = new BufferedReader(new FileReader("population.csv"));
            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                double[] dataDouble = {Double.parseDouble(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3])};
                Individual temp = new Individual();
                temp.setIndividual(dataDouble);
                population[counter] = temp;
                counter++;
            }
            csvReader.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}
