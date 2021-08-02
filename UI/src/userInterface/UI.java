package userInterface;

import engine.Engine;

import timeTable.TimeTable;

public abstract class UI implements Runnable
{
    //Members
    private Engine m_Engine;
    protected boolean m_IsRunning;
    protected boolean m_IsXmlFileLoad;
    private TimeTable m_TimeTable;

    public UI()
    {
        m_IsRunning = false;
        m_IsXmlFileLoad = false;
    }

    //Methods
    protected void runEngine()
    {
        if(m_IsXmlFileLoad)
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



    @Override
    public abstract void run();

    public abstract void loadInfoFromXmlFile();

    public abstract void showSettings();

    public abstract void runEvolutionaryAlgorithm();

    public abstract void showOptimalSolution();

    public abstract void showInterimSolution();

    public abstract void exit();
}
