package engine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Population
{
    //Members
    List<Solution> m_Population;

    //Constructors
    public Population()
    {
       m_Population = new ArrayList<>();
    }

    public Population(Population population)
    {
        m_Population = population.m_Population;
    }

    //Methods
    public void initializePopulation(int sizeOfFirstPopulation, Problem problem)
    {
        for (int i = 0;i < sizeOfFirstPopulation;i++) {
            m_Population.add(problem.newRandomInstance());
        }
    }

    public void calculateFitnessToAll()
    {
        for (Solution solution: m_Population) {
            solution.calculateFitness();
        }
    }

    public void add(Solution solution)
    {
        m_Population.add(solution);
    }

    public int size()
    {
        return m_Population.size();
    }

    public Solution get(int index)
    {
        return m_Population.get(index);
    }

    public void sort(Comparator comparator)
    {
        m_Population.sort(comparator);
    }
}
