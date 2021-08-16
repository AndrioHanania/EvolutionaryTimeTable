package engine.stopCondition;

import engine.Engine;

public class BestFitnessCondition implements StopCondition{

    private double m_BestFitness;

    public BestFitnessCondition(double bestFitness)
    {
        m_BestFitness= bestFitness;
    }

    @Override
    public boolean execute(Engine.DataEngine dataEngine) {
        return dataEngine.getBestFitness() >= m_BestFitness;
    }
}
