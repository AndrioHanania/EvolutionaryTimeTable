package engine.selection;

import engine.Solution;
import engine.Population;

import java.util.Random;

public class Tournament<T extends Solution> implements Selection<T>
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
    public Population<T> execute(Population<T> currentGeneration)
    {
        Population<T> newGeneration = new Population<>();
        T parent1,parent2;
        double randomPte;
        int sizeOfCurrentGeneration = currentGeneration.size();
        for (int i=0;i<currentGeneration.size();i++){
            parent1 = currentGeneration.get(m_Random.nextInt(sizeOfCurrentGeneration));
            parent2 = currentGeneration.get(m_Random.nextInt(sizeOfCurrentGeneration));
            randomPte = m_Random.nextDouble();
            if(randomPte > m_Pte)
            {
                if(parent1.getFitness() > parent2.getFitness()){
                    newGeneration.add(parent1);
                }
                else  newGeneration.add(parent2);;
            }
            else
            {
                if(parent1.getFitness() < parent2.getFitness()){
                    newGeneration.add(parent1);
                }
                else  newGeneration.add(parent2);;
            }
        }
        return newGeneration;
    }
}
