package JAVAFXUI;

import engine.Engine;
import engine.Parse;
import generated.ETTDescriptor;
import javafx.concurrent.Task;
import timeTable.TimeTable;
import timeTable.TimeTableParse;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class JavaFxUI
{
    private boolean m_IsXmlFileLoad=false;
    private Engine m_Engine;
    private TimeTable m_TimeTable;
    private Task m_TaskEngine;
    private Thread m_ThreadEngine;


    public void loadInfoFromXmlFile(File selectedFile) throws Exception
    {
        m_IsXmlFileLoad = false;
        m_Engine=null;
        m_TimeTable=null;
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
        m_IsXmlFileLoad = true;
        timeTable = (TimeTable) parse.parseSolution(eTTEvolutionEngine.getETTTimeTable());
        engine = parse.parseEngine(eTTEvolutionEngine.getETTEvolutionEngine());
        engine.setProblem(timeTable);
        m_Engine = engine;
        m_TimeTable = timeTable;
    }

    public boolean getIsXmlFileLoaded(){return  m_IsXmlFileLoad;}

    public Engine getEngine(){return  m_Engine;}

    public Task getTaskEngine(){return m_TaskEngine;}

    public void setTaskEngine(Task task){m_TaskEngine=task;}

    public Thread getThreadEngine(){return m_ThreadEngine;}

    public void setThreadEngine(Thread thread){m_ThreadEngine=thread;}

    public TimeTable getTimeTable() {
        return m_TimeTable;
    }
}
