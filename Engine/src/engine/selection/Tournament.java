package engine.selection;

import engine.Solution;
import engine.Population;

import java.util.Random;

public class Tournament implements Selection
{
    //Members
    private double m_Pte;
    Random m_Random;

    //Constructors
    public Tournament(double pte)
    {
        m_Pte = pte;
        m_Random = new Random();
    }

    //Methods
    @Override
    public Population execute(Population currentGeneration)
    {
        Population newGeneration = new Population();
        Solution parent1,parent2;
        double randomPte;
        int sizeOfCurrentGeneration = currentGeneration.getPopulation().size();
        for (int i=0;i<currentGeneration.getPopulation().size();i++){
            parent1 = currentGeneration.getPopulation().get(m_Random.nextInt(sizeOfCurrentGeneration));
            parent2 = currentGeneration.getPopulation().get(m_Random.nextInt(sizeOfCurrentGeneration));
            randomPte = m_Random.nextDouble();
            if(randomPte > m_Pte)
            {
                if(parent1.getFitness() > parent2.getFitness()){
                    newGeneration.getPopulation().add(parent1);
                }
                else  newGeneration.getPopulation().add(parent2);;
            }
            else
            {
                if(parent1.getFitness() < parent2.getFitness()){
                    newGeneration.getPopulation().add(parent1);
                }
                else  newGeneration.getPopulation().add(parent2);;
            }
        }
        return newGeneration;
    }
}
