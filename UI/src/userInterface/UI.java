package userInterface;

import Listeners.UpdateGenerationListener;
import engine.Engine;

import timeTable.TimeTable;

public abstract class UI implements Runnable
{
    //Members
    private Engine m_Engine;
    protected boolean m_IsRunning;
    protected boolean m_IsXmlFileLoad;
    protected boolean m_IsEngineHasBeenRunning;
    private TimeTable m_TimeTable;

    public UI()
    {
        m_IsRunning = false;
        m_IsXmlFileLoad = false;
    }

    //Methods

    protected int getNumberOfGenerationInEngine(){
        return m_Engine.getNumOfGeneration();
    }


    protected void addListenerToUpdateGenerationAbstract(UpdateGenerationListener listener)
    {
        m_Engine.addListenerToUpdateGeneration(listener);
    }


    protected TimeTable getOptimalSolutionFromEngine() {
        return (TimeTable)m_Engine.getOptimalSolution();
    }


    protected void runEngine()
    {
        if(m_IsXmlFileLoad)///
        { m_Engine.run();}
        else
        {
            ////////
        }
    }

    protected void setEngine(Engine engine)
    {
        m_Engine = engine;
    }

    protected void setTimeTable(TimeTable timeTable){
        m_TimeTable = timeTable;
    }

    protected String getEngine(){return String.valueOf(m_Engine);}

    protected String getTimeTable(){return String.valueOf(m_TimeTable);}

    protected void setMaxNumOfGenerationInEngine(int num){m_Engine.setMaxNumOfGeneration(num);}

    protected void setNumberOfGenerationForUpdateInEngine(int num){m_Engine.setNumberOfGenerationForUpdate(num);}


    @Override
    public abstract void run();

    public abstract void loadInfoFromXmlFile();

    public abstract void showSettings();

    public abstract void runEvolutionaryAlgorithm();

    public abstract void showOptimalSolution();

    public abstract void viewAlgorithmProcess();

    public abstract void runSmallFile();

    public abstract void exit();
}
