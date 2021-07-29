package engine.crossover;

import engine.Solution;
import generated.ETTCrossover;

public interface Crossover
{
    static <T extends Solution> Crossover parse(ETTCrossover ettCrossover) {
        return null;
    }

    //Methods
    public abstract Solution execute(Solution parent1, Solution parent2);
}
