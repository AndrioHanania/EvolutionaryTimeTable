package userInterface;

import engine.Listeners.UpdateGenerationListener;
import engine.Engine;
import engine.Parse;
import engine.stopCondition.BestFitnessCondition;
import engine.stopCondition.MaxNumOfGenerationCondition;
import engine.stopCondition.TimeCondition;
import generated.ETTDescriptor;
import timeTable.TimeTable;
import timeTable.TimeTableParse;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class ConsoleUI extends UI
{
    //Constructors
    Map<Integer,Double> m_NumOfGeneration2BestFitness= new TreeMap<>();

    public ConsoleUI()
    {

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
            default:
                System.out.println("Error with input");
        }
    }

    private  void runMyEngine(){
        getAndSetStopCondition();
        getAndSetNumberOfGenerationForUpdateInEngine();
        m_NumOfGeneration2BestFitness.clear();
        clearEngine();
        runEngine();
    }

    private void getMaxNumOfGenerationCondition()
    {
        Scanner scanner = new Scanner(System.in);
        while(true)
        {
            int maxNumOfGenerationInEngine = 0;
            try
            {
                System.out.print("Please enter integer (bigger or equal to 100) of generation for stop condition: ");
                maxNumOfGenerationInEngine  = Integer.parseInt(scanner.nextLine());
                if (maxNumOfGenerationInEngine < 100)
                {
                    System.out.println("Error with input. The input is not bigger or equal to 100");
                } else
                {
                    addStopConditionToEngine(new MaxNumOfGenerationCondition(maxNumOfGenerationInEngine));
                    break;
                }
            }
            catch (IllegalStateException e)
            {
                System.out.println("Scanner was closed");
            }
            catch (NoSuchElementException e)
            {
                System.out.println("Error with input. The input is exhausted");
            }
            catch (NumberFormatException e)
            {
                System.out.println("Error with input. The input is not integer");
            }
        }
    }

    private void getAndSetNumberOfGenerationForUpdateInEngine()
    {
        Scanner scanner = new Scanner(System.in);
        while(true)
        {
            int numberOfGenerationForUpdateInEngine = 0;
            try
            {
                System.out.println("For Updates please enter number of generation to support for details(integer bigger than 0):");
                numberOfGenerationForUpdateInEngine  = Integer.parseInt(scanner.nextLine());
                if (numberOfGenerationForUpdateInEngine < 0)
                {
                    System.out.println("Error with input. The input is not bigger than 0");
                }
                else
                {
                    setNumberOfGenerationForUpdateInEngine(numberOfGenerationForUpdateInEngine);
                    break;
                }
            }
            catch (IllegalStateException e)
            {
                System.out.println("Scanner was closed");
            }
            catch (NoSuchElementException e)
            {
                System.out.println("Error with input. The input is exhausted");
            }
            catch (NumberFormatException e)
            {
                System.out.println("Error with input. The input is not integer");
            }
        }
    }
    // לנסות לעשות שיהיה אפשר להוסיף יותר מתנאי עצירה 1
    private void getAndSetStopCondition()
    {
        Menu stopConditionsMenu = new Menu("Stop condition for the engine",
                new String[]{
                        "To stop the algorithm by fitness threshold",
                        "To stop the algorithm by number of generation",
                        "To stop the algorithm by timer"
                });
        stopConditionsMenu.Draw();
        int input = stopConditionsMenu.getInputFromMenu();
        switch (input)
        {
            case 1:
                getFitnessThresholdCondition();
                break;
            case 2:
                getMaxNumOfGenerationCondition();
                break;
            case 3:
                getTimeCondition();
                break;
        }
    }

    private void getTimeCondition()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter number of second for stop condition(integer): ");
        int second;
        while (true)
        {
            try
            {
                second = Integer.parseInt(scanner.nextLine());
                if (second < 0) {
                    System.out.println("Error with input. Please enter number of second for stop condition(integer): ");
                } else {
                    addStopConditionToEngine(new TimeCondition(second));
                    break;
                }
            }
            catch (IllegalStateException e)
            {
                System.out.println("Scanner was closed");
            }
            catch (NoSuchElementException e)
            {
                System.out.println("Error with input. The input is exhausted");
            }
            catch (NumberFormatException e)
            {
                System.out.println("Error with input. The input is not integer");
            }
        }
    }

    private  void getFitnessThresholdCondition()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a double between 0 - 100 to be the fitness threshold for stop condition: ");
        double maxFitness;
         while (true)
         {
             try
             {
                 maxFitness = Double.parseDouble(scanner.nextLine());
                  if (maxFitness < 0 || maxFitness > 100) {
                         System.out.println("Error with input. please enter a double between 0 - 100 as fitness threshold: ");
                  } else {
                         addStopConditionToEngine(new BestFitnessCondition(maxFitness));
                          break;
                  }
             }
            catch (IllegalStateException e)
             {
                  System.out.println("Scanner was closed");
             }
             catch (NoSuchElementException e)
            {
                  System.out.println("Error with input. The input is exhausted");
            }
            catch (NumberFormatException e)
            {
                  System.out.println("Error with input. The input is not double");
            }
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void loadInfoFromXmlFile() {
        Engine engine = new Engine();
        TimeTable timeTable;
        Scanner scanner = new Scanner(System.in);
        Parse parse= new TimeTableParse();
        try
        {
            System.out.println("Please enter full name of file to load: ");
            String nameFileToLoad = scanner.nextLine();
            InputStream inputStream = new FileInputStream(new File(nameFileToLoad));
            if(!nameFileToLoad.substring(nameFileToLoad.length()-3).equals("xml"))
            {
                throw new Exception("The file is not a xml");
            }
            ETTDescriptor eTTEvolutionEngine = ConsoleUIUtils.deserializeFrom(inputStream);
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
        catch (JAXBException  e)
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
                Map<Integer, Double> m_numOfGeneration2BestFitness = m_NumOfGeneration2BestFitness;
                for (Map.Entry<Integer, Double> entry   : m_NumOfGeneration2BestFitness.entrySet())
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
    public void exit() {
        m_IsRunning = false;
        System.out.println("Exit...");

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            System.out.println("Error exiting program (Thread.sleep)");
        }
    }
}
