package JAVAFXUI;

import engine.Engine;
import engine.Listeners.UpdateGenerationListener;
import engine.Parse;
import generated.ETTDescriptor;
import timeTable.TimeTable;
import timeTable.TimeTableParse;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class JavaFxUI
{

    private boolean m_IsRunning;
    private boolean m_IsXmlFileLoad;
    private boolean m_IsEngineHasBeenRunning=false;
    private Engine m_Engine;
    private TimeTable m_TimeTable;
    Map<Integer,Double> m_NumOfGeneration2BestFitness= new TreeMap<>();

    public void loadInfoFromXmlFile(File selectedFile) throws Exception
    {

        Engine engine;
        TimeTable timeTable;
        Parse parse= new TimeTableParse();
        //try
        //{
            InputStream inputStream = new FileInputStream(selectedFile);
            String nameFileToLoad = selectedFile.getName();
            if(!nameFileToLoad.substring(nameFileToLoad.length()-3).equals("xml"))
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
            m_Engine.addListenerToUpdateGeneration(new UpdateGenerationListener() {
                @Override
                public void OnUpdateGeneration(double bestFitnessInCurrentGeneration, int numberOfGeneration) {
                    m_NumOfGeneration2BestFitness.put( numberOfGeneration, bestFitnessInCurrentGeneration);
                    System.out.println("Best fitness in Generation number "+ numberOfGeneration + ": " + String.format("%.2f", bestFitnessInCurrentGeneration));

                }
            });
            m_TimeTable = timeTable;
       // }
        /*catch (JAXBException e)
        {
            System.out.println("Error with generating data from xml file");
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }*/
    }


}
