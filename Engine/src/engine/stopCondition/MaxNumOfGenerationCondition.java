package engine.stopCondition;

import engine.Engine;

public class MaxNumOfGenerationCondition implements StopCondition{

    private int m_MaxNumOfGeneration;

    public MaxNumOfGenerationCondition(int maxNumOfGeneration)
    {
        m_MaxNumOfGeneration = maxNumOfGeneration;
    }

    @Override
    public boolean execute(Engine.DataEngine dataEngine)
    {
       return dataEngine.getNumberOfCurrentGeneration() >= m_MaxNumOfGeneration;
    }
}
