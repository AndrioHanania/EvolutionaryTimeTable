package engine.stopCondition;

import engine.Engine;

@FunctionalInterface
public interface StopCondition {

    public abstract boolean execute(Engine.DataEngine dataEngine);

}
