package engine.selection;

import engine.Population;

import java.util.Random;

public abstract class Selection
{
    Random m_Random = new Random();
    protected String m_Configuration;
    protected int m_SizeOfElitism = 0;

    //Methods
    public abstract Population execute(Population currentGeneration);

    public void setConfiguration(String configuration){m_Configuration = configuration;}

    public String getConfiguration(){return  m_Configuration;}

    public int getSizeOfElitism(){return  m_SizeOfElitism;}

    public void setSizeOfElitism(int size){m_SizeOfElitism = size;}

    @Override
    public String toString() {
        return "Configuration: " + m_Configuration;
    }
}
