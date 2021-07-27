package engine.selection;



import engine.Solution;
import engine.Population;

import java.util.Random;


public class RouletteWheel<T extends Solution> implements Selection<T>
{
    Random m_Random;

    public RouletteWheel()
    {
        m_Random = new Random();
    }

    @Override
    public Population<T> execute(Population<T> currentGeneration) {
        Population<T> rouletteWheel = new Population<>();
        Population<T> newGeneration = new Population<>();
        for (T item:currentGeneration) {
            for(int i = 0;i<item.getFitness();i++){
                rouletteWheel.add(item);
            }
        }
        int sizeOfRouletteWheel = rouletteWheel.size();
        for(int i = 0;i<currentGeneration.size();i++){
            newGeneration.add(rouletteWheel.get(m_Random.nextInt(sizeOfRouletteWheel)));
        }
        return newGeneration;
    }
}
