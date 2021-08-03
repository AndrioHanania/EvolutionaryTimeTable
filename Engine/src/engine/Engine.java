package engine;

import engine.crossover.Crossover;
import engine.mutation.Mutation;
import engine.selection.Selection;
import generated.ETTEvolutionEngine;

import java.util.List;
import java.util.Random;

public class Engine implements Runnable
{
    //Members
    private Crossover m_Crossover;
    private List<Mutation> m_Mutations;
    private Selection m_Selection;
    private Problem m_Problem;
    private int m_NumOfGeneration = 0;
    private int m_MaxNumOfGeneration;
    private Random m_Random = new Random();
    private int m_SizeOfFirstPopulation;

    //Constructors
    public Engine(Selection selection, Crossover crossover, List<Mutation> mutations,
                  Problem problem, int sizeOfFirstPopulation, int maxNumOfGeneration)
    {
        m_Selection = selection;
        m_Crossover = crossover;
        m_Mutations = mutations;
         m_Problem = problem;
        m_SizeOfFirstPopulation = sizeOfFirstPopulation;
        m_MaxNumOfGeneration = maxNumOfGeneration;
    }

    public Engine(ETTEvolutionEngine eTTEvolutionEngine, Problem problem, int maxNumOfGeneration, Parse parse)
    {
        m_Selection = parse.parseSelection(eTTEvolutionEngine.getETTSelection());
        m_Crossover =parse.parseCrossover(eTTEvolutionEngine.getETTCrossover());
        m_Mutations = parse.parseMutation(eTTEvolutionEngine.getETTMutations());
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
        Solution randomParent1;
        Solution randomParent2;
        Solution solution1, solution2;
        Population firstPopulation = new Population();
        firstPopulation.initializePopulation(m_SizeOfFirstPopulation, m_Problem);
        firstPopulation.calculateFitnessToAll();

        while(m_NumOfGeneration < m_MaxNumOfGeneration)
        {
           Population selectedParents = new Population(m_Selection.execute(firstPopulation));
            Population nextGeneration = new Population();
           while(nextGeneration.size() < m_SizeOfFirstPopulation)
            {
                randomParent1 = selectedParents.get(m_Random.nextInt(m_SizeOfFirstPopulation));
                randomParent2 = selectedParents.get(m_Random.nextInt(m_SizeOfFirstPopulation));
                solution1 = m_Crossover.execute(randomParent1, randomParent2);
                solution2 =m_Crossover.execute(randomParent1, randomParent2);
                for(Mutation mutation : m_Mutations)
                {
                    mutation.execute(solution1);
                    mutation.execute(solution2);
                }
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
        for(Mutation mutation : m_Mutations)
        { settings.append(mutation);}///
        return settings.toString();
    }


    public int getNumOfGeneration()
    {
        return m_NumOfGeneration;
    }
}
