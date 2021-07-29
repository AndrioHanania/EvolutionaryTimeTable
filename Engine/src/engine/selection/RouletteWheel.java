package engine.selection;



import engine.Solution;
import engine.Population;

import java.util.Random;


public class RouletteWheel implements Selection
{
    Random m_Random;

    public RouletteWheel()
    {
        m_Random = new Random();
    }

    @Override
    public Population execute(Population currentGeneration) {
        Population rouletteWheel = new Population();
        Population newGeneration = new Population();
        for (Solution solution : currentGeneration.getPopulation()) {
            for(int i = 0;i<solution.getFitness();i++){
                rouletteWheel.getPopulation().add(solution);
            }
        }
        int sizeOfRouletteWheel = rouletteWheel.getPopulation().size();
        for(int i = 0;i<currentGeneration.getPopulation().size();i++){
            newGeneration.getPopulation().add(rouletteWheel.getPopulation().get(m_Random.nextInt(sizeOfRouletteWheel)));
        }
        return newGeneration;
    }
}
