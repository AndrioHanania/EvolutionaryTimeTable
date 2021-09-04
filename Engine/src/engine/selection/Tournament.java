package engine.selection;

import engine.Solution;
import engine.Population;

public class Tournament extends Selection
{
    //Members
    private double m_Pte;

    //Constructors
    public Tournament(double pte)
    {
        m_Pte = pte;
    }

    //Methods
    @Override
    public String toString() {
        return "Name: Tournament, " + "PTE: " + m_Pte;
    }

    public void setPTE(int pte){m_Pte=pte;}

    @Override
    public Population execute(Population currentGeneration)
    {
        Population newGeneration = new Population();
        Solution parent1,parent2;
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
