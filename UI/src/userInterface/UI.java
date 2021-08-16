package userInterface;

import engine.Listeners.UpdateGenerationListener;
import engine.Engine;

import engine.stopCondition.StopCondition;
import timeTable.TimeTable;

public abstract class UI implements Runnable
{
    //Members
    private Engine m_Engine;
    protected boolean m_IsRunning;
    protected boolean m_IsXmlFileLoad;
    protected boolean m_IsEngineHasBeenRunning=false;
    private TimeTable m_TimeTable;
    protected Thread m_ThreadEngine = new Thread(m_Engine);

    public UI()
    {
        m_IsRunning = false;
        m_IsXmlFileLoad = false;
    }

    //Methods

    protected void addListenerToUpdateGenerationAbstract(UpdateGenerationListener listener)
    {
        m_Engine.addListenerToUpdateGeneration(listener);
    }


    protected TimeTable getOptimalSolutionFromEngine() {
        return (TimeTable)m_Engine.getOptimalSolution();
    }


    protected void runEngine() {
        if(m_IsXmlFileLoad)
        {
            m_Engine.run();
            m_IsEngineHasBeenRunning=true;
        }
    }

    protected void clearEngine() {
        m_Engine.clear();
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

    protected void setNumberOfGenerationForUpdateInEngine(int num){m_Engine.setNumberOfGenerationForUpdate(num);}


    protected void addStopConditionToEngine(StopCondition stopCondition)
    {
        m_Engine.addStopCondition(stopCondition);
    }

    @Override
    public abstract void run();

    public abstract void loadInfoFromXmlFile();

    public abstract void showSettings();

    public abstract void runEvolutionaryAlgorithm();

    public abstract void showOptimalSolution();

    public abstract void viewAlgorithmProcess();

    public abstract void exit();
}
