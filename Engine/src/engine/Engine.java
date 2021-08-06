package engine;

import Listeners.UpdateGenerationListener;
import engine.crossover.Crossover;
import engine.mutation.Mutation;
import engine.selection.Selection;
import generated.ETTEvolutionEngine;

import java.util.ArrayList;
import java.util.Comparator;
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
    private int m_NumberOfGenerationForUpdate;
    private Solution m_OptimalSolution;
    private boolean m_IsFinishToRun = false;
    private List<UpdateGenerationListener> listenersToUpdateGeneration = new ArrayList<>();

    //Constructors
    public Engine(Selection selection, Crossover crossover, List<Mutation> mutations,
                  Problem problem, int sizeOfFirstPopulation)
    {
        m_Selection = selection;
        m_Crossover = crossover;
        m_Mutations = mutations;
         m_Problem = problem;
        m_SizeOfFirstPopulation = sizeOfFirstPopulation;
    }

    public Engine(ETTEvolutionEngine eTTEvolutionEngine, Problem problem, Parse parse)
    {
        m_Selection = parse.parseSelection(eTTEvolutionEngine.getETTSelection());
        m_Crossover =parse.parseCrossover(eTTEvolutionEngine.getETTCrossover());
        m_Mutations = parse.parseMutation(eTTEvolutionEngine.getETTMutations());
        m_SizeOfFirstPopulation = eTTEvolutionEngine.getETTInitialPopulation().getSize();
        m_Problem = problem;
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
        int forUpdate = 0;
        m_IsFinishToRun = false;
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
            forUpdate++;
            if(forUpdate == m_NumberOfGenerationForUpdate)
            {
                for (UpdateGenerationListener listener : listenersToUpdateGeneration)
                {listener.OnUpdateGeneration(m_OptimalSolution.getFitness(), m_NumOfGeneration);}
                forUpdate = 0;
            }
            firstPopulation = nextGeneration;
        }
        m_IsFinishToRun = true;
        updateOptimalSolution(firstPopulation);
    }


    public String toString()
    {
        StringBuilder settings = new StringBuilder();
        settings.append("Engine: ");
        settings.append(System.lineSeparator());
        settings.append("size of population= ").append(m_SizeOfFirstPopulation);
        settings.append(m_Selection);
        settings.append(m_Crossover);
        for(Mutation mutation : m_Mutations)
        { settings.append(mutation);}
        return settings.toString();
    }


    public int getNumOfGeneration()
    {
        return m_NumOfGeneration;
    }

    public void setMaxNumOfGeneration(int num){m_MaxNumOfGeneration = num;}

    public void  setNumberOfGenerationForUpdate(int num){m_NumberOfGenerationForUpdate = num;}

    public Solution getOptimalSolution() {
        if(m_IsFinishToRun)
        { return m_OptimalSolution;}
        else{ return null;}
    }


    private void updateOptimalSolution(Population population){
        population.sort(Comparator.comparingInt(Solution::getFitness));
        m_OptimalSolution = population.get(0);
    }

    public void addListenerToUpdateGeneration(UpdateGenerationListener listenerToAdd)
    {
        listenersToUpdateGeneration.add(listenerToAdd);
    }

    public void removeListenerToUpdateGeneration(UpdateGenerationListener listenerToAdd)
    {
        listenersToUpdateGeneration.remove(listenerToAdd);
    }
}
