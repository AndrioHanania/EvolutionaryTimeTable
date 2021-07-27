package engine.crossover;

import engine.Solution;
import generated.ETTCrossover;

public interface Crossover<T extends Solution>
{
    static <T extends Solution> Crossover parse(ETTCrossover ettCrossover) {
        return null;
    }

    //Methods
    public abstract T execute(T parent1, T parent2);
}
