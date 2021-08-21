package JAVAFXUI;


import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

import engine.stopCondition.BestFitnessCondition;
import engine.stopCondition.MaxNumOfGenerationCondition;
import engine.stopCondition.TimeCondition;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.property.*;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import timeTable.Grade;
import timeTable.Rules.Rule;
import timeTable.Subject;
import timeTable.Teacher;
import timeTable.TimeTable;
import timeTable.chromosome.TimeTableChromosome;

import javax.xml.bind.JAXBException;


public class Controller {


    public Controller()/////////////////
    {

    }

    JavaFxUI ui = new JavaFxUI();

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    // Values injected by FXMLLoader


    @FXML private Button fileChooserButton;
    @FXML private TextField loadFileTextBox;
    @FXML private Label messageToUserTextBox;
    @FXML private Button runAlgorithmButton;
    @FXML private CheckBox bestFitnessCheckBox;
    @FXML private CheckBox timerCheckBox;
    @FXML private CheckBox numOfGenerationCheckBox;
    @FXML private TextField numberOfGenerationTextField;
    @FXML private TextField bestFitnessTextField;
    @FXML private TextField timerTextField;
    @FXML private TextField numOfGeneration4Update;
    @FXML private Label fitness4Update;
    @FXML private Label generation4Update;
    @FXML private ProgressBar runProgressBar;
    @FXML private Button pauseResumeButton;
    @FXML private Button stopRunButton;
    @FXML private ScrollPane scrollPane;
    @FXML private TextArea teacherTextArea;
    @FXML private TextArea subjectsTextArea;
    @FXML private TextArea gradesTexArea;
    @FXML private TextArea rulesTextArea;
    @FXML private Label hardRulesAvgLabel;
    @FXML private Label softRulesAvgLabel;
    @FXML private Label OptimalFitnessLabel;
    @FXML private TableView<ProductRule> rulesTableView;
    @FXML private TableView<ProductUpdate> updatesTableView;
    @FXML private TableView<ProductRow> rowsTableView;




    IntegerProperty m_IPCurrentGeneration = new SimpleIntegerProperty(0);
    DoubleProperty m_DPCurrentFitness = new SimpleDoubleProperty(0.0);
    StringProperty m_SPMessageToUser = new SimpleStringProperty();
    StringProperty m_SPNameLoadFile = new SimpleStringProperty("File: ");
    StringProperty m_SPBestFitness = new SimpleStringProperty();
    StringProperty  m_SPTeachersInfo = new SimpleStringProperty();
    StringProperty  m_SPSubjectsInfo = new SimpleStringProperty();
    StringProperty  m_SPGradesInfo = new SimpleStringProperty();
    StringProperty  m_SPRulesInfo = new SimpleStringProperty();

    //Casual reboot
    MaxNumOfGenerationCondition maxNumOfGenerationCondition =new MaxNumOfGenerationCondition(0);
    BestFitnessCondition bestFitnessCondition = new BestFitnessCondition(0);
    TimeCondition timeCondition = new TimeCondition(0);


    //reminder that some codes need to be in synchronized!!!!!




    private void provideInfoFromOptimalSolution(TimeTable timeTable)
    {
        provideInfoAboutRulesAndFitnessFromOptimalSolution(timeTable);
        provideInfoAboutRowsFromOptimalSolution(timeTable);
    }

    private void provideInfoAboutRowsFromOptimalSolution(TimeTable timeTable)
    {
        rowsTableView.getItems().clear();
        List<TimeTableChromosome> chromosomes = timeTable.getChromosomes();
        chromosomes.sort(Comparator.comparingInt(TimeTableChromosome::getDay)
                .thenComparing(Comparator.comparingInt(TimeTableChromosome::getHour)
                        .thenComparing(TimeTableChromosome::compareWithGrade)
                        .thenComparing(TimeTableChromosome::compareWithTeacher)));
        for(TimeTableChromosome chromosome : chromosomes)
        {
            rowsTableView.getItems().add(new ProductRow(chromosome.getDay(),
                    chromosome.getHour(), chromosome.getTeacher().getName(),
                    chromosome.getGrade().getName(), chromosome.getSubject().getName()));
        }
    }

    private void provideInfoAboutRulesAndFitnessFromOptimalSolution(TimeTable timeTable)
    {
        rulesTableView.getItems().clear();
        List<Rule> rules = timeTable.getRules();
        for(Rule rule : rules)
        {
            rulesTableView.getItems().add(new ProductRule(rule.getClass().getSimpleName(),
                    rule.getRuleWeight() , rule.getGrade(), rule.getConfig()));
        }

        hardRulesAvgLabel.setText(((Double)timeTable.getHardRulesAvg()).toString());
        softRulesAvgLabel.setText(((Double)timeTable.getSoftRulesAvg()).toString());
        OptimalFitnessLabel.setText(((Double)timeTable.getFitness()).toString());
    }

    private void provideInfoFromFile()
    {
        provideInfoAboutTeachers();
        provideInfoAboutSubjects();
        provideInfoAboutGrades();
        provideInfoAboutRules();
        //engine....
    }

    private void provideInfoAboutRules() {
        StringBuilder sb =new StringBuilder();
        List<Rule> rules= ui.getTimeTable().getRules();
        int size = rules.size();
        for(int i=1;i<size;i++)
        {
            sb.append("Rule ").append(i).append(": ");
            sb.append(System.lineSeparator());
            sb.append("Name: ").append(rules.get(i - 1).getClass().getSimpleName()).append(", Strictness: ").append(rules.get(i - 1).getRuleWeight());
            sb.append(System.lineSeparator());
            sb.append(System.lineSeparator());
        }
        m_SPRulesInfo.set(sb.toString());
    }

    private void provideInfoAboutGrades()
    {
        StringBuilder sb =new StringBuilder();
        List<Grade> grades= ui.getTimeTable().getGrades();
        int size = grades.size();
        for(int i=1;i<size;i++)
        {
            sb.append("Grade ").append(i).append(": ");
            sb.append(System.lineSeparator());
            sb.append(grades.get(i-1));
            sb.append(System.lineSeparator());
            sb.append(System.lineSeparator());
        }
        m_SPGradesInfo.set(sb.toString());
    }

    private void provideInfoAboutSubjects()
    {
        StringBuilder sb =new StringBuilder();
        List<Subject> subjects= ui.getTimeTable().getSubjects();
        int size = subjects.size();
        for(int i=1;i<size;i++)
        {
            sb.append("Subject ").append(i).append(": ");
            sb.append(System.lineSeparator());
            sb.append(subjects.get(i-1));
            sb.append(System.lineSeparator());
            sb.append(System.lineSeparator());
        }
        m_SPSubjectsInfo.set(sb.toString());
    }

    private void provideInfoAboutTeachers()
    {
        StringBuilder sb =new StringBuilder();
        List<Teacher> teachers= ui.getTimeTable().getTeachers();
        int size = teachers.size();
        for(int i=1;i<size;i++)
        {
            sb.append("Teacher ").append(i).append(": ");
            sb.append(System.lineSeparator());
            sb.append(teachers.get(i-1));
            sb.append(System.lineSeparator());
            sb.append(System.lineSeparator());
        }
        m_SPTeachersInfo.set(sb.toString());
    }

    private void animationForLoadFileTextBox()
    {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(loadFileTextBox);
        scale.setDuration(Duration.millis(500));
        scale.setCycleCount(2);
        scale.setInterpolator(Interpolator.LINEAR);
        scale.setByX(2.0);
        scale.setByY(2.0);
        scale.setByZ(2.0);
        scale.setAutoReverse(true);
        //loadFileTextBox.toFront();
        scale.play();
    }

    private void animationForRunAlgorithmButton()
    {
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(runAlgorithmButton);
        rotate.setDuration(Duration.millis(1000));
        rotate.setCycleCount(2);
        rotate.setByAngle(360);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.play();
    }

    private void handleControls2DisableBeforeRunning(boolean toDisable)
    {
        numOfGenerationCheckBox.setDisable(toDisable);
        numberOfGenerationTextField.setDisable(toDisable);
        bestFitnessCheckBox.setDisable(toDisable);
        bestFitnessTextField.setDisable(toDisable);
        timerCheckBox.setDisable(toDisable);
        timerTextField.setDisable(toDisable);
        numOfGeneration4Update.setDisable(toDisable);
    }

    private boolean handleParametersBeforeRunning()
    {
        try
        {
            if(Integer.parseInt(numberOfGenerationTextField.getText()) < 100 && numOfGenerationCheckBox.isSelected())
            {
                m_SPMessageToUser.set("Number of generation is need to be bigger than 99");
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            m_SPMessageToUser.set("Number of generation is need to be bigger than 99");
            return false;
        }
        try
        {
            if(Integer.parseInt(numOfGeneration4Update.getText()) <1)
            {
                m_SPMessageToUser.set("Number of generation for update is need to be positive");
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            m_SPMessageToUser.set("Number of generation for update is need to be positive");
            return false;
        }
        try
        {
            if(!(numOfGenerationCheckBox.isSelected() || bestFitnessCheckBox.isSelected() || timerCheckBox.isSelected()))
            {
                m_SPMessageToUser.set("Need to choose at lest 1 stop condition");
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            m_SPMessageToUser.set("Need to choose at lest 1 stop condition");
            return false;
        }
        return true;
    }

    private void handleStopConditionBeforeRunning(MaxNumOfGenerationCondition maxNumOfGenerationCondition, BestFitnessCondition bestFitnessCondition ,TimeCondition timeCondition)
    {
        if(numOfGenerationCheckBox.isSelected())
        {
            ui.getEngine().addStopCondition(maxNumOfGenerationCondition);
        }
        if(bestFitnessCheckBox.isSelected())
        {
            ui.getEngine().addStopCondition(bestFitnessCondition);
        }
        if(timerCheckBox.isSelected())
        {
            ui.getEngine().addStopCondition(timeCondition);
        }
    }

    private void handleProgressBarBeforeRunning()
    {
        Task<Void> task = new Task<Void>()
        {
            @Override
            protected Void call()
            {
                /*
                                 //בתכלס לא צריך כי גם ככה לוקח לזה זמן
                                 Thread.sleep(10); // Pause briefly
                                 updateProgress(i, steps);
                */
                double progress4Generation=0;
                double progress4Fitness=0;
                double progress4Timer=0;

                if(numOfGenerationCheckBox.isSelected())
                {
                    progress4Generation=(double)m_IPCurrentGeneration.get()/Integer.parseInt(numberOfGenerationTextField.getText());
                }
                if(bestFitnessCheckBox.isSelected())
                {
                    progress4Fitness=m_DPCurrentFitness.get()/Double.parseDouble(bestFitnessTextField.getText());
                }
                if(timerCheckBox.isSelected())
                {
                    progress4Timer= Integer.parseInt(timerTextField.toString())-timeCondition.getTimeLeft();
                }

                runProgressBar.setProgress(Math.max(Math.max(progress4Generation, progress4Fitness),progress4Timer));
                return null;
            }
        };

        // This method allows us to handle any Exceptions thrown by the task
        task.setOnFailed(wse -> m_SPMessageToUser.set(wse.getSource().getException().getMessage()));

        // If the task completed successfully, perform other updates here
        task.setOnSucceeded(wse -> {
            handleControls2DisableBeforeRunning(true);
            m_SPMessageToUser.set("The algorithm has run successfully");
        });

        // Before starting our task, we need to bind our values to the properties on the task
        runProgressBar.progressProperty().bind(task.progressProperty());
        // Now, start the task on a background thread
        new Thread(task).start();
    }

    @FXML
    void OnFileChooser(ActionEvent event)
    {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(null);
            if(selectedFile == null)
                return;
            m_SPNameLoadFile.set("File: " + selectedFile.getName());
            try
            {
                 ui.loadInfoFromXmlFile(selectedFile);
                 ui.getEngine().addListenerToUpdateGeneration((bestFitnessInCurrentGeneration, numberOfGeneration) -> {
                     m_DPCurrentFitness.set(Double.parseDouble(String.format("%.2f", bestFitnessInCurrentGeneration)));
                     m_IPCurrentGeneration.set(numberOfGeneration);
                     ui.getNumOfGeneration2BestFitness().put( numberOfGeneration, bestFitnessInCurrentGeneration);
                     provideInfoFromOptimalSolution((TimeTable) ui.getEngine().getOptimalSolution());
                     updatesTableView.getItems().add(new ProductUpdate(numberOfGeneration, bestFitnessInCurrentGeneration));
                 });

                provideInfoFromFile();
                animationForLoadFileTextBox();
                runProgressBar.progressProperty().unbind();
                runProgressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
                m_SPMessageToUser.set("The xml file was loaded");
            }
            catch (JAXBException e)
            {
                m_SPMessageToUser.set("Error with generating data from xml file");
            }
            catch (FileNotFoundException e)
            {
                m_SPMessageToUser.set("File not found");
            }
            catch (Exception e)
            {
                m_SPMessageToUser.set(e.getMessage());
            }
    }

    @FXML
    void OnRunAlgo(ActionEvent event)
    {
        if(!ui.getIsXmlFileLoaded())
        {
            m_SPMessageToUser.set("Can't run the algorithm before loading xml file");
        }
        else
        {
            if(handleParametersBeforeRunning())
            {
                handleProgressBarBeforeRunning();

                handleStopConditionBeforeRunning(maxNumOfGenerationCondition, bestFitnessCondition, timeCondition);

                ui.getEngine().setNumberOfGenerationForUpdate(Integer.parseInt(numOfGeneration4Update.getText()));

                ui.getNumOfGeneration2BestFitness().clear();
                ui.getEngine().clear();
                animationForRunAlgorithmButton();
                handleControls2DisableBeforeRunning(false);
                updatesTableView.getItems().clear();
                //need to close before start if we run during another running
                ui.getThreadEngine().start();

                ui.getEngine().removeStopCondition(maxNumOfGenerationCondition);
                ui.getEngine().removeStopCondition(bestFitnessCondition);
                ui.getEngine().removeStopCondition(timeCondition);
            }
        }
    }

    @FXML
    void OnNumOfGenerationCheck(ActionEvent event)
    {
        numberOfGenerationTextField.setDisable(!numOfGenerationCheckBox.isSelected());
    }

    @FXML
    void OnBestFitnessCheck(ActionEvent event) {
        bestFitnessTextField.setDisable(!bestFitnessCheckBox.isSelected());
    }

    @FXML
    void OnTimerCheck(ActionEvent event) {
        timerTextField.setDisable(!timerCheckBox.isSelected());
    }

    @FXML
    void OnPauseResumeClick(ActionEvent event)
    {
        //////////////////////handle timer need to Pause-Resume////////////////////////////////
        if(runProgressBar.getProgress() == 1)
        {
            if(pauseResumeButton.getText().equals("Pause"))
            {
                try {
                    ui.getThreadEngine().wait();
                } catch (InterruptedException ignored) {

                }
                pauseResumeButton.setText("Resume");
            }
            else if(pauseResumeButton.getText().equals("Resume"))
            {
                ui.getThreadEngine().notify();
                pauseResumeButton.setText("Pause");
            }
        }
        else
        {
            m_SPMessageToUser.set("Can't " + pauseResumeButton.getText() + " after or before running");
        }
    }

    @FXML
    void OnStopRunClick(ActionEvent event) {
        double progress=runProgressBar.getProgress();
        if(progress == 1)
        {
           m_SPMessageToUser.set("The algorithm already over");
        }
        else if(progress == 0 ||progress==-1)
        {
            m_SPMessageToUser.set("The algorithm hasn't started yet");
        }
        else
        {
            if(pauseResumeButton.getText().equals("Resume"))
            {
                ui.getThreadEngine().notify();
            }
            ui.getThreadEngine().interrupt();
            runProgressBar.setProgress(0);
        }
    }



    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize()
    {
        myInitialize();
    }

    private void myInitialize()
    {
        messageToUserTextBox.textProperty().bind(m_SPMessageToUser);
        loadFileTextBox.textProperty().bind(m_SPNameLoadFile);
        generation4Update.textProperty().bind(m_IPCurrentGeneration.asString());
        fitness4Update.textProperty().bind(m_DPCurrentFitness.asString());

        teacherTextArea.textProperty().bind(m_SPTeachersInfo);
        subjectsTextArea.textProperty().bind(m_SPSubjectsInfo);
        gradesTexArea.textProperty().bind(m_SPGradesInfo);
        rulesTextArea.textProperty().bind(m_SPRulesInfo);

        numberOfGenerationTextField.setDisable(true);
        bestFitnessTextField.setDisable(true);
        timerTextField.setDisable(true);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);


        numberOfGenerationTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                numberOfGenerationTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }});

        bestFitnessTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue.length() <= newValue.length()) {
                m_SPBestFitness.set(newValue);
                bestFitnessTextField.textProperty().bind(m_SPBestFitness);
                if (!newValue.matches("\\d*")) {
                    if (newValue.substring(0, newValue.length() - 1).contains(".") && newValue.endsWith(".")) {
                        newValue = oldValue;
                    }

                    m_SPBestFitness.set(newValue.replaceAll("[^\\d.]", ""));
                }if (Double.parseDouble(newValue) > 100) {
                    m_SPBestFitness.set(oldValue);
                }
                bestFitnessTextField.textProperty().unbind();
            }});


        timerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                timerTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        numOfGeneration4Update.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                timerTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }});
    }

    private class ProductRule {
        String m_Name;
        String m_Weight;
        double m_Score;
        String m_Config;
        public ProductRule(String name, String weight, double score, String config) {
            m_Name=name;
            m_Weight=weight;
            m_Score=score;
            m_Config=config;
        }
    }

    private class ProductUpdate
    {
        double m_Fitness;
        int m_Generation;
        public ProductUpdate(int generation, double fitness)
        {
            m_Generation= generation;
            m_Fitness= fitness;
        }
    }

    private class ProductRow
    {
        int m_Day;
        int m_Hour;
        String m_Teacher;
        String m_Grade;
        String m_Subject;

        public ProductRow(int day, int hour, String teacher, String grade, String subject)
        {
            m_Day=day;
            m_Hour=hour;
            m_Teacher=teacher;
            m_Grade=grade;
            m_Subject=subject;
        }
    }
}