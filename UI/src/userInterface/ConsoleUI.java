package userInterface;

import Listeners.UpdateGenerationListener;
import engine.Engine;
import generated.ETTDescriptor;
import timeTable.TimeTable;
import timeTable.TimeTableParse;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class ConsoleUI extends UI  implements UpdateGenerationListener
{
    //Constructors
    Map<Integer,Integer> m_NumOfGeneration2BestFitness= new TreeMap<>();

    public ConsoleUI()
    {
        addListenerToUpdateGenerationAbstract(this);
    }

    //Methods
    @Override
    public void run()
    {
        m_IsRunning = true;
        Menu mainMenu =  ConsoleUIUtils.createMainMenu();
        while(m_IsRunning)
        {
            mainMenu.Draw();
            int input = mainMenu.getInputFromMenu();
            switchCaseForInput(input);
        }
    }

    private void switchCaseForInput(int input)
    {
        switch (input)
        {
            case 1:
                loadInfoFromXmlFile();
                break;
            case 2:
                showSettings();
                break;
            case 3:
                runEvolutionaryAlgorithm();
                break;
            case 4:
                showOptimalSolution();
                break;
            case 5:
                viewAlgorithmProcess();
                break;
            case 6:
                exit();
                break;
            case 7:
                runSmallFile();
                break;
            default:
                System.out.println("Error with input");
        }
    }

    private  void runMyEngine()
    {
        getAndSetMaxNumOfGenerationInEngine();
        getAndSetNumberOfGenerationForUpdateInEngine();
        m_NumOfGeneration2BestFitness.clear();
        runEngine();
    }

    private void getAndSetMaxNumOfGenerationInEngine()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("For stop condition please enter number of generation To produce (integer bigger than 100):");
        while(true)
        {
            int maxNumOfGenerationInEngine = 0;
            boolean hasBeenCatch = false;
            try
            {
                maxNumOfGenerationInEngine = scanner.nextInt();
            }
            catch (InputMismatchException e)
            {
                System.out.println("Error with input. The input is not integer");
                hasBeenCatch = true;
            }
            catch (IllegalStateException e)
            {
                System.out.println("Scanner was closed");
                hasBeenCatch = true;
            }
            catch (NoSuchElementException e)
            {
                System.out.println("Error with input. The input is exhausted");
                hasBeenCatch = true;
            }
            if(!hasBeenCatch) {
                if (maxNumOfGenerationInEngine < 100) {
                    System.out.println("Error with input. The input is not bigger than 100");
                } else {
                    setMaxNumOfGenerationInEngine(maxNumOfGenerationInEngine);
                    break;
                }
            }
        }
    }

    private void getAndSetNumberOfGenerationForUpdateInEngine()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("For Updates please enter number of generation to support for details(integer bigger than 0):");
        while(true)
        {
            int numberOfGenerationForUpdateInEngine = 0;
            boolean hasBeenCatch = false;
            try
            {
                numberOfGenerationForUpdateInEngine = scanner.nextInt();
            }
            catch (InputMismatchException e)
            {
                System.out.println("Error with input. The input is not integer");
                hasBeenCatch = true;
            }
            catch (IllegalStateException e)
            {
                System.out.println("Scanner was closed");
                hasBeenCatch = true;
            }
            catch (NoSuchElementException e)
            {
                System.out.println("Error with input. The input is exhausted");
                hasBeenCatch = true;
            }
            if(!hasBeenCatch) {
                if (numberOfGenerationForUpdateInEngine < 0) {
                    System.out.println("Error with input. The input is not bigger than 0");
                } else {
                    setNumberOfGenerationForUpdateInEngine(numberOfGenerationForUpdateInEngine);
                    break;
                }
            }
        }
    }

    @Override
    public void OnUpdateGeneration(int bestFitnessInCurrentGeneration, int numberOfGeneration)
    {
        m_NumOfGeneration2BestFitness.put( numberOfGeneration, bestFitnessInCurrentGeneration);
        System.out.println("Best fitness in Generation number "+ numberOfGeneration + ": " + bestFitnessInCurrentGeneration);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void loadInfoFromXmlFile() {
        Engine engine;
        TimeTable timeTable;
        try
        {
            InputStream inputStream = new FileInputStream(new File("Jaxb/src/schema/fileInfo.xml"));
            ETTDescriptor eTTEvolutionEngine = ConsoleUIUtils.deserializeFrom(inputStream);
            timeTable = new TimeTable(eTTEvolutionEngine.getETTTimeTable());
            engine = new Engine(eTTEvolutionEngine.getETTEvolutionEngine(), timeTable, new TimeTableParse());
            m_IsXmlFileLoad = true;
           setEngine(engine);
           setTimeTable(timeTable);
            System.out.println("The xml file was loaded");
        } catch (JAXBException | FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void showSettings() {
        if(m_IsXmlFileLoad) {
            StringBuilder settings = new StringBuilder();
            settings.append(getTimeTable());
            settings.append(getEngine());
            System.out.println(settings);
            System.out.println("Done showing settings");
        }
        else {
            System.out.println("You need to load first info from xml file");
            return;
        }
    }

    @Override
    public void runEvolutionaryAlgorithm() {
        if(m_IsXmlFileLoad) {
            if(m_IsEngineHasBeenRunning)
            {
                Menu runEngineMenu = new Menu("The engine has been running. Are you sure that you want to run it again?",
                        new String[]{"Run anyway", "No"});
                runEngineMenu.Draw();
                int input = runEngineMenu.getInputFromMenu();
                switch (input)
                {
                    case 1:
                        //בהפעלה מחודשת כל המידעים הקודמים נמחקים
                        runMyEngine();
                        System.out.println("Done run algorithm");
                        break;
                    case 2:
                        return;
                    default:
                        System.out.println("Error with input");
                        break;
                }
            }
            else
            {
                //בהפעלה מחודשת כל המידעים הקודמים נמחקים
                runMyEngine();
                System.out.println("Done run algorithm");
            }
        }
        else{
            System.out.println("You need to load first info from xml file");
        }
    }

    @Override
    public void showOptimalSolution()
    {
        if(m_IsXmlFileLoad)
        {
            if (m_IsEngineHasBeenRunning)
            {
                Menu optimalSolutionMenu = new Menu("Please enter option way to show the optimal solution",
                        new String[]{"By raw", "By teacher", "By class"});
                optimalSolutionMenu.Draw();
                int input = optimalSolutionMenu.getInputFromMenu();
                TimeTable timeTable = getOptimalSolutionFromEngine();
                switch (input)
                {
                    case 1:
                        ConsoleUIUtils.printOptimalSolutionByRaw(timeTable);
                        break;

                    case 2:
                        ConsoleUIUtils.printOptimalSolutionByTeacher(timeTable);
                        break;

                    case 3:
                        ConsoleUIUtils.printOptimalSolutionByClass(timeTable);
                        break;

                    default:
                        System.out.println("Error with input");
                        return;
                }
                System.out.println("The fitness of the optimal solution: " + timeTable.getFitness());
                ConsoleUIUtils.printRules(timeTable);
                System.out.println("Done showing optimal solution");
            }
            else
            {
                System.out.println("You have to run the algorithm first");
            }
        }
        else
        {
            System.out.println("You need to load first info from xml file");
        }

    }

    @Override
    public void viewAlgorithmProcess() {
        if(m_IsXmlFileLoad) {
            if (m_IsEngineHasBeenRunning) {
                Map<Integer, Integer> m_numOfGeneration2BestFitness = m_NumOfGeneration2BestFitness;
                for (Map.Entry<Integer, Integer> entry   : m_NumOfGeneration2BestFitness.entrySet())
                {
                    System.out.println("Best fitness in Generation number "+ entry.getKey() + ": " + entry.getValue());
                }
                System.out.println("Done show algorithm process");
            }
            else
            {
                System.out.println("You have to run the algorithm first");
            }
        }
        else {
            System.out.println("You need to load first info from xml file");
        }
    }

    @Override
    public void runSmallFile()
    {
        ////
        ////
        System.out.println("Done run small file");
    }

    @Override
    public void exit() {
        m_IsRunning = false;
        System.out.println("Exit");
    }
}
