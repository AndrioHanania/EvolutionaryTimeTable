package userInterface;

import engine.Engine;
import generated.ETTDescriptor;
import timeTable.TimeTable;
import timeTable.TimeTableParse;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.xml.bind.Unmarshaller;

public class ConsoleUI extends UI
{
    //Constructors
    public ConsoleUI()
    {

    }

    //Methods
    @Override
    public void run()
    {
        m_IsRunning = true;
        Menu mainMenu = createMainMenu();
        while(m_IsRunning)
        {
            mainMenu.Draw();
            int input = mainMenu.getInputFromMenu();
            switchCaseForInput(input);
        }
    }

    private Menu createMainMenu() {
        return new Menu("Main menu",
                new String[]{
                "Load information from xml file",
                "Show settings",
                "Run evolutionary algorithm",
                "Show optimal solution",
                "Show interim solution",
                "exit"
        });
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
                showInterimSolution();
                break;
            case 6:
                exit();
                break;

            case 7: //for debug
                showRules();
                break;
            default:
                System.out.println("Error with input");
        }
    }


    @Override
    public void loadInfoFromXmlFile() {
        Engine engine;
        TimeTable timeTable;
        try
        {
            InputStream inputStream = new FileInputStream(new File("Jaxb/src/schema/fileInfo.xml"));
            ETTDescriptor eTTEvolutionEngine = deserializeFrom(inputStream);
            timeTable = new TimeTable(eTTEvolutionEngine.getETTTimeTable());
            engine = new Engine(eTTEvolutionEngine.getETTEvolutionEngine(), timeTable, 100, new TimeTableParse());
            m_IsXmlFileLoad = true;
           setEngine(engine);
           setTimeTable(timeTable);
            //print to user xml file loaded succuessfully?
        } catch (JAXBException | FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private static ETTDescriptor deserializeFrom(InputStream in) throws JAXBException
    {
        JAXBContext jc = JAXBContext.newInstance(ETTDescriptor.class);
        Unmarshaller u = jc.createUnmarshaller();
        return (ETTDescriptor)u.unmarshal(in);
    }


    @Override
    public void showSettings() {
        if(m_IsXmlFileLoad) {
            StringBuilder settings = new StringBuilder();
            settings.append(getTimeTable());
            settings.append(getEngine());
            System.out.println(settings);
        }
        else {
            System.out.println("You need to load first info from xml file");
            return;
        }
    }

    @Override
    public void runEvolutionaryAlgorithm() {
        if(m_IsXmlFileLoad) {
            runEngine();
        }
        else{
            System.out.println("error loading XML file. please try again");
        }

    }

    @Override
    public void showOptimalSolution() {
        if(m_IsXmlFileLoad) {

        }
        else {
            System.out.println("You need to load first info from xml file");
            return;
        }

    }

    @Override
    public void showInterimSolution() {
        if(m_IsXmlFileLoad) {

        }
        else {
            System.out.println("You need to load first info from xml file");
            return;
        }

    }

    public void showRules()
    {

    }

    @Override
    public void exit() {
        m_IsRunning = false;
    }
}
