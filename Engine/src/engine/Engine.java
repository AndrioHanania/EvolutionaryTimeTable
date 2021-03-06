package engine;

import engine.Listeners.UpdateGenerationListener;
import engine.crossover.Crossover;
import engine.mutation.Mutation;
import engine.selection.Selection;
import engine.stopCondition.StopCondition;
import engine.stopCondition.TimeCondition;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class  Engine  implements Runnable
{
    //Members
    private Crossover m_Crossover;
    private List<Mutation> m_Mutations;
    private Selection m_Selection;
    private Problem m_Problem;
    private int m_NumOfGeneration = 0;
    private Random m_Random = new Random();
    private int m_SizeOfFirstPopulation;
    private int m_NumberOfGenerationForUpdate;
    private Solution m_OptimalSolution;
    private boolean m_IsFinishToRun = false;
    private List<UpdateGenerationListener> listenersToUpdateGeneration = new ArrayList<>();
    private double m_BestFitnessInCurrentGeneration=0;
    private List<StopCondition> m_StopConditions = new ArrayList<>();
    DataEngine m_DataEngine = new DataEngine();
    private Boolean m_IsPause=false;

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

    public Engine()
    {
        m_Selection = new Selection() {
            @Override
            public Population execute(Population currentGeneration) {
                return null;
            }
        };

        m_Crossover = new Crossover() {
            @Override
            public List<Solution> execute(Solution parent1, Solution parent2) {
                return null;
            }
        };
        m_Mutations = new ArrayList<Mutation>();

    }

    //Methods
    @Override
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
        firstPopulation.sort(Comparator.comparingDouble(Solution::getFitness));
        m_BestFitnessInCurrentGeneration = firstPopulation.get(firstPopulation.size()-1).m_Fitness;


        while (isDone())
        {
           int sizeOfElitism = m_Selection.getSizeOfElitism();
           Population selectedParents = new Population(m_Selection.execute(firstPopulation));
           Population nextGeneration = new Population(firstPopulation.getElita(sizeOfElitism));


           while(nextGeneration.size() < m_SizeOfFirstPopulation)
            {
                randomParent1 = selectedParents.get(m_Random.nextInt(selectedParents.size()));
                randomParent2 = selectedParents.get(m_Random.nextInt(selectedParents.size()));

                List<Solution> solutions = m_Crossover.execute(randomParent1, randomParent2);
                solution1 = solutions.get(0);
                solution2 = solutions.get(1);

                for(Mutation mutation : m_Mutations)
                {
                    mutation.execute(solution1);
                    mutation.execute(solution2);
                }

                nextGeneration.add(solution1);
                nextGeneration.add(solution2);
            }

            m_NumOfGeneration++;
            forUpdate++;
            nextGeneration.calculateFitnessToAll();
            nextGeneration.sort(Comparator.comparingDouble(Solution::getFitness));
            m_BestFitnessInCurrentGeneration = nextGeneration.get(nextGeneration.size()-1).m_Fitness;
            updateOptimalSolution(nextGeneration);
            if(forUpdate == m_NumberOfGenerationForUpdate)
            {
                for (UpdateGenerationListener listener : listenersToUpdateGeneration)
                {listener.OnUpdateGeneration(m_BestFitnessInCurrentGeneration, m_NumOfGeneration);}
                forUpdate = 0;
            }

            firstPopulation = nextGeneration;

            synchronized (this)
            {
                while(m_IsPause)
                {
                    try {this.wait();}
                    catch (InterruptedException | IllegalMonitorStateException  e) {}
                }
            }
        }

        m_IsFinishToRun = true;
        updateOptimalSolution(firstPopulation);
    }

    private boolean isDone() {
        if(Thread.currentThread().isInterrupted())
        {
            for(StopCondition stopCondition : m_StopConditions)
            {
                if(stopCondition.getClass().equals(TimeCondition.class))
                {
                    ((TimeCondition)stopCondition).toFinish();
                }
            }
        }
        return !checkStopConditions() && !Thread.currentThread().isInterrupted();
    }

    public String toString()
    {
        StringBuilder settings = new StringBuilder();
        settings.append("Engine: ");
        settings.append(System.lineSeparator());
        settings.append("size of population: ").append(m_SizeOfFirstPopulation);
        settings.append(System.lineSeparator());
        settings.append(m_Selection);
        settings.append(System.lineSeparator());
        settings.append(m_Crossover);
        settings.append(System.lineSeparator());
        for(Mutation mutation : m_Mutations)
        {
            settings.append(mutation);
            settings.append(System.lineSeparator());
        }
        return settings.toString();
    }

    public int getNumOfGeneration()
    {
        return m_NumOfGeneration;
    }

    public void  setNumberOfGenerationForUpdate(int num){m_NumberOfGenerationForUpdate = num;}

    public Solution getOptimalSolution() {
        return m_OptimalSolution;
    }


    private void updateOptimalSolution(Population population){
        population.sort(Comparator.comparingDouble(Solution::getFitness));
        m_OptimalSolution = population.get(population.size()-1);
    }

    public void addListenerToUpdateGeneration(UpdateGenerationListener listenerToAdd)
    {
        listenersToUpdateGeneration.add(listenerToAdd);
    }

    public void removeListenerToUpdateGeneration(UpdateGenerationListener listenerToAdd)
    {
        listenersToUpdateGeneration.remove(listenerToAdd);
    }

    public void clear()
    {
        m_NumOfGeneration = 0;
        m_OptimalSolution = null;
        m_IsFinishToRun=false;
    }

    public void setSelection(Selection selection) {
        m_Selection = selection;
    }

    public void setCrossover(Crossover crossover) {
        m_Crossover=crossover;
    }

    public void setMutations(List<Mutation> mutations) {
       m_Mutations=mutations;
    }

    public void setSizeOfFirstPopulation(int size) {
        m_SizeOfFirstPopulation = size;
    }

    public void setProblem(Problem problem){
        m_Problem = problem;
    }

    public void addStopCondition(StopCondition stopCondition)
    {
        m_StopConditions.add(stopCondition);
    }

    public void removeStopCondition(StopCondition stopCondition)
    {
        m_StopConditions.remove(stopCondition);
    }

    public void removeAllStopCondition()
    {
        m_StopConditions.clear();
    }

    private boolean checkStopConditions()
    {
        if(m_StopConditions.isEmpty())
        {
            return true;
        }

       boolean condition = false;

        for (StopCondition stopCondition : m_StopConditions)
        {
            condition = stopCondition.execute(m_DataEngine);
            if(condition) {
                break;
            }
        }

        return condition;
    }

    public int getSizeOfFirstPopulation(){return  m_SizeOfFirstPopulation;}

    public boolean isFinishToRun(){return m_IsFinishToRun;}

    public List<Mutation> getMutations()
    {
        return m_Mutations;
    }
    public Selection getSelection()
    {
        return m_Selection;
    }

    public void pause()
    {
        for(StopCondition stopCondition : m_StopConditions)
        {
            if(stopCondition.getClass().equals(TimeCondition.class))
            {
                ((TimeCondition)stopCondition).pause();
            }
        }
        synchronized (this){
        m_IsPause=true;}
    }

    public void resume()
    {
        for(StopCondition stopCondition : m_StopConditions)
        {
            if(stopCondition.getClass().equals(TimeCondition.class))
            {
                ((TimeCondition)stopCondition).resume();
            }
        }

        synchronized (this){
        m_IsPause=false;
        this.notifyAll();}
    }

    public double getBestFitnessInCurrentGeneration(){return m_BestFitnessInCurrentGeneration;}

    public Crossover getCrossover() {
        return m_Crossover;
    }

    public class DataEngine
    {

        public int getNumberOfCurrentGeneration()
        {
            return m_NumOfGeneration;
        }

        public double getBestFitness()
        {
            return m_BestFitnessInCurrentGeneration;
        }
    }
}
