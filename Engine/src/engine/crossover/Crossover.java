package engine.crossover;

import engine.Solution;
import generated.ETTCrossover;

import java.util.Random;

public interface Crossover
{

    //Methods
    public abstract Solution execute(Solution parent1, Solution parent2);
}
