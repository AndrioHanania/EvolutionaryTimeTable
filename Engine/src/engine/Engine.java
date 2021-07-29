package engine;

import engine.crossover.Crossover;
import engine.mutation.Mutation;
import engine.selection.Selection;
import generated.ETTEvolutionEngine;

import java.util.Random;

public class Engine<T extends Solution > implements Runnable
{
    private int check=5;
    //Members
    private Crossover<T> m_Crossover;
    private Mutation<T> m_Mutation;
    private Selection<T> m_Selection;
    private Problem<T> m_Problem;
    private int m_NumOfGeneration = 0;
    private int m_MaxNumOfGeneration;
    private Random m_Random = new Random();
    private int m_SizeOfFirstPopulation;

    //Constructors
    public Engine(Selection<T> selection, Crossover<T> crossover, Mutation<T> mutation,
                  Problem<T> problem, int sizeOfFirstPopulation, int maxNumOfGeneration)
    {
        m_Selection = selection;
        m_Crossover = crossover;
        m_Mutation = mutation;
         m_Problem = problem;
        m_SizeOfFirstPopulation = sizeOfFirstPopulation;
        m_MaxNumOfGeneration = maxNumOfGeneration;
    }

    public Engine(ETTEvolutionEngine eTTEvolutionEngine, Problem<T> problem, int maxNumOfGeneration)
    {
        m_Selection = Selection.parse(eTTEvolutionEngine.getETTSelection());
        m_Crossover = Crossover.parse(eTTEvolutionEngine.getETTCrossover());
        m_Mutation = Mutation.parse(eTTEvolutionEngine.getETTMutations());
        m_SizeOfFirstPopulation = eTTEvolutionEngine.getETTInitialPopulation().getSize();
        m_Problem = problem;
        m_MaxNumOfGeneration = maxNumOfGeneration;
    }

    public Engine()
    {

    }

    //Methods
    public void run()
    {
        T randomParent1, randomParent2;
        T solution1, solution2;
        Population<T> firstPopulation = new Population<>();
        firstPopulation.initializePopulation(m_SizeOfFirstPopulation, m_Problem);
        firstPopulation.calculateFitness();

        while(m_NumOfGeneration < m_MaxNumOfGeneration)
        {
           Population<T> selectedParents = new Population<>(m_Selection.execute(firstPopulation));
            Population<T> nextGeneration = new Population<>();
           while(nextGeneration.size() < m_SizeOfFirstPopulation)
            {
                randomParent1 = selectedParents.get(m_Random.nextInt(m_SizeOfFirstPopulation));
                randomParent2 = selectedParents.get(m_Random.nextInt(m_SizeOfFirstPopulation));
                //נייצר 2 פתרונות
                //Crossover
                solution1 = m_Crossover.execute(randomParent1, randomParent2);
                solution2 =m_Crossover.execute(randomParent1, randomParent2);
                //Mutation
                //בהסתברות מסויימת
                if(m_Random.nextDouble() < 0.001)////stam
                { m_Mutation.execute(solution1);}
                if(m_Random.nextDouble() < 0.001)////
                {  m_Mutation.execute(solution2);}
                nextGeneration.add(solution1);
                nextGeneration.add(solution2);
            }
            m_NumOfGeneration++;
            firstPopulation = nextGeneration;
        }
    }


    public String toString()
    {
        StringBuilder settings = new StringBuilder();
        settings.append("Engine: ");
        settings.append(System.lineSeparator());
        settings.append("size of population= " + m_SizeOfFirstPopulation);
        settings.append(m_Selection);///
        settings.append(m_Crossover);///
        settings.append(m_Mutation);///
        return settings.toString();
    }


    public int getNumOfGeneration()
    {
        return m_NumOfGeneration;
    }
}
