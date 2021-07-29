package engine;

import java.util.ArrayList;

public class Population<T extends Solution> extends ArrayList<T>
{
    public Population()
    {
        super();
    }

    public Population(Population<T> population)
    {
        super(population);
    }

    public void initializePopulation(int sizeOfFirstPopulation, Problem<T> problem)
    {
        for (int i = 0;i < sizeOfFirstPopulation;i++) {
            this.add(problem.newRandomInstance());
        }
    }

    public void calculateFitness()
    {
        for (T item: this) {
            item.calculateFitness();
        }
    }
}
