package JAVAFXUI;

import engine.Engine;
import engine.Listeners.UpdateGenerationListener;
import engine.Parse;
import generated.ETTDescriptor;
import timeTable.TimeTable;
import timeTable.TimeTableParse;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

public class JavaFxUI
{

    private boolean m_IsRunning;
    private boolean m_IsXmlFileLoad;
    private boolean m_IsEngineHasBeenRunning=false;
    private Engine m_Engine;
    private TimeTable m_TimeTable;
    private Map<Integer,Double> m_NumOfGeneration2BestFitness= new TreeMap<>();
    private Thread m_ThreadEngine = new Thread(m_Engine);

    public void loadInfoFromXmlFile(File selectedFile) throws Exception
    {

        Engine engine;
        TimeTable timeTable;
        Parse parse= new TimeTableParse();
        InputStream inputStream = new FileInputStream(selectedFile);
        String nameFileToLoad = selectedFile.getName();
        if(!nameFileToLoad.endsWith("xml"))
        {
            throw new Exception("The file is not a xml");
        }
        ETTDescriptor eTTEvolutionEngine = JavaFxUIUtils.deserializeFrom(inputStream);
        timeTable = (TimeTable) parse.parseSolution(eTTEvolutionEngine.getETTTimeTable());
        engine = parse.parseEngine(eTTEvolutionEngine.getETTEvolutionEngine());
        engine.setProblem(timeTable);
        if(m_IsXmlFileLoad)
        {
            m_IsEngineHasBeenRunning=false;
        }
        m_IsXmlFileLoad = true;
        m_Engine = engine;
        m_TimeTable = timeTable;
    }

    public boolean getIsXmlFileLoaded(){return  m_IsXmlFileLoad;}

    public Engine getEngine(){return  m_Engine;}

    public Map<Integer,Double> getNumOfGeneration2BestFitness(){return m_NumOfGeneration2BestFitness;}

    public Thread getThreadEngine(){return m_ThreadEngine;}

    public TimeTable getTimeTable() {
        return m_TimeTable;
    }
}
