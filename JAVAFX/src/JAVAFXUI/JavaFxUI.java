package JAVAFXUI;

import engine.Engine;
import engine.Parse;
import generated.ETTDescriptor;
import timeTable.TimeTable;
import timeTable.TimeTableParse;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class JavaFxUI
{


    public void loadInfoFromXmlFile(File selectedFile)
    {

        Engine engine;
        TimeTable timeTable;
        Parse parse= new TimeTableParse();
        try
        {
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
            setEngine(engine);
            addListenerToUpdateGenerationAbstract(new UpdateGenerationListener() {
                @Override
                public void OnUpdateGeneration(double bestFitnessInCurrentGeneration, int numberOfGeneration) {
                    m_NumOfGeneration2BestFitness.put( numberOfGeneration, bestFitnessInCurrentGeneration);
                    System.out.println("Best fitness in Generation number "+ numberOfGeneration + ": " + String.format("%.2f", bestFitnessInCurrentGeneration));
                }
            });
            setTimeTable(timeTable);
            System.out.println("The xml file was loaded");
        }
        catch (JAXBException e)
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
        }
    }


}
