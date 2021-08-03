package engine.selection;

import engine.Population;
import generated.ETTSelection;

import java.util.Random;

@FunctionalInterface
public interface Selection
{
    Random m_Random = new Random();

    //Methods
    public abstract Population execute(Population currentGeneration);
}
