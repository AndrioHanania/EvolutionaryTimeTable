package userInterface;

import engine.Engine;
import generated.ETTDescriptor;
import timeTable.TimeTable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.xml.bind.Unmarshaller;

public abstract class UI implements Runnable
{
    //Members
    protected Engine<TimeTable> m_Engine;
    protected boolean m_IsRunning;
    protected boolean m_IsXmlFileLoad;
    protected TimeTable m_TimeTable;

    public UI()
    {
        m_IsRunning = false;
        m_IsXmlFileLoad = false;
    }

    //Methods
    @Override
    public abstract void run();

    public abstract void loadInfoFromXmlFile();

    public abstract void showSettings();

    public abstract void runEvolutionaryAlgorithm();

    public abstract void showOptimalSolution();

    public abstract void showInterimSolution();

    public abstract void exit();
}
