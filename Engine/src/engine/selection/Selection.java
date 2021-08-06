package engine.selection;

import engine.Population;
import generated.ETTSelection;

import java.util.Random;

public abstract class Selection
{
    Random m_Random = new Random();
    protected String m_Configuration;

    //Methods
    public abstract Population execute(Population currentGeneration);

    public void setConfiguration(String configuration){m_Configuration = configuration;}

    public String getConfiguration(){return  m_Configuration;}

    @Override
    public String toString() {
        return "Configuration: " + m_Configuration;
    }
}
