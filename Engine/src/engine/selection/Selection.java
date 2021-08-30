package engine.selection;

import engine.Population;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Random;

public abstract class Selection
{
    Random m_Random = new Random();
    protected String m_Configuration;
    protected IntegerProperty m_SizeOfElitism = new SimpleIntegerProperty(0);

    //Methods
    public Selection()
    {

    }
    public abstract Population execute(Population currentGeneration);

    public void setConfiguration(String configuration){m_Configuration = configuration;}

    public String getConfiguration(){return  m_Configuration;}

    public int getSizeOfElitism(){return  m_SizeOfElitism.get();}

    public void setSizeOfElitism(int size){m_SizeOfElitism.set(size);}

    public IntegerProperty getElitismProperty(){return m_SizeOfElitism;}

    @Override
    public String toString() {
        return "Configuration: " + m_Configuration;
    }
}
