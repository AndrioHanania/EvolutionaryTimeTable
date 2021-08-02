package engine.selection;



import engine.Solution;
import engine.Population;




public class RouletteWheel implements Selection
{
    public RouletteWheel()
    {

    }

    @Override
    public Population execute(Population currentGeneration)
    {
        Population rouletteWheel = new Population();
        Population newGeneration = new Population();
        for(int i=0;i<currentGeneration.size();i++)
        {
            Solution solution = currentGeneration.get(i);
            for(int j = 0;j<solution.getFitness();j++)
            {
                rouletteWheel.add(solution);
            }
        }

        int sizeOfRouletteWheel = rouletteWheel.size();
        for(int i = 0;i<currentGeneration.size();i++){
            newGeneration.add(rouletteWheel.get(m_Random.nextInt(sizeOfRouletteWheel)));
        }
        return newGeneration;
    }
}
