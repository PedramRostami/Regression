public class Individual {
    private double[] individual;

    public Individual() {
        individual = new double[4];
        for (int i = 0; i < 4; i++) {
            individual[i] = (Math.random() - 0.5) * 800;
        }
    }

    public double[] getIndividual() {
        return individual;
    }

    public void setIndividual(double[] individual) {
        this.individual = individual;
    }

    @Override
    protected Individual clone() throws CloneNotSupportedException {
        Individual individual = new Individual();
        individual.setIndividual(this.individual);
        return individual;
    }
}
