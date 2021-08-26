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
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import timeTable.Grade;
import timeTable.Rules.Rule;
import timeTable.Subject;
import timeTable.Teacher;
import timeTable.TimeTable;
import timeTable.chromosome.TimeTableChromosome;

import javax.xml.bind.JAXBException;

public class Controller implements Initializable { //implements Initializable


    public Controller()/////////////////
    {
        m_Timetable = ui.getTimeTable();
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
    @FXML private ChoiceBox<Teacher> teacherChoiceBox;
    @FXML private ChoiceBox<Grade> gradeChoiceBox;
    @FXML private ComboBox<Teacher> teacherComboBox;
 //   @FXML private ChoiceBox<String> myChoiceBox;


    @FXML private TableView<ProductRule> rulesTableView;
    @FXML private TableView<ProductUpdate> updatesTableView;
    @FXML private TableView<ProductRow> rowsTableView;
    @FXML private TableView<ProductTeacher> teachersTableView;
    @FXML private TableView<ProductGrade> gradesTableView;
    @FXML private TableColumn<ProductTeacher,String> teacherDayTableColumn;
    @FXML private TableColumn<ProductTeacher,String> teacherSundayTableColumn;
    @FXML private TableColumn<ProductTeacher,String> teacherMondayTableColumn;
    @FXML private TableColumn<ProductTeacher,String> teacherTuesdayTableColumn;
    @FXML private TableColumn<ProductTeacher,String> teacherWednesdayTableColumn;
    @FXML private TableColumn<ProductTeacher,String> teacherThursdayTableColumn;

    @FXML private TableColumn<ProductRule, String> nameRulTableColumn;
    @FXML private TableColumn<ProductRule, String> weightRuleTableColumn;
    @FXML private TableColumn<ProductRule, String> scoreRuleTableColumn;
    @FXML private TableColumn<ProductRule, String> configRuleTableColumn;
    @FXML private TableColumn<ProductUpdate, String> updateGenerationTableColumn;
    @FXML private TableColumn<ProductUpdate, String> updateFitnessTableColumn;
    @FXML private TableColumn<ProductRow, String> dayRowTableColumn;
    @FXML private TableColumn<ProductRow, String> hourRowTableColumn;
    @FXML private TableColumn<ProductRow, String> teacherRowTableColumn;
    @FXML private TableColumn<ProductRow, String> gradeRowTableColumn;
    @FXML private TableColumn<ProductRow, String> subjectRowTableColumn;


    IntegerProperty m_IPCurrentGeneration = new SimpleIntegerProperty(0);
    DoubleProperty m_DPCurrentFitness = new SimpleDoubleProperty(0.0);
    StringProperty m_SPMessageToUser = new SimpleStringProperty();
    StringProperty m_SPNameLoadFile = new SimpleStringProperty("File: ");
    StringProperty m_SPBestFitness = new SimpleStringProperty();
    StringProperty  m_SPTeachersInfo = new SimpleStringProperty();
    StringProperty  m_SPSubjectsInfo = new SimpleStringProperty();
    StringProperty  m_SPGradesInfo = new SimpleStringProperty();
    StringProperty  m_SPRulesInfo = new SimpleStringProperty();
    DoubleProperty m_DPProgress = new SimpleDoubleProperty(0.0);

    //Casual reboot
    MaxNumOfGenerationCondition maxNumOfGenerationCondition =new MaxNumOfGenerationCondition(0);
    BestFitnessCondition bestFitnessCondition = new BestFitnessCondition(0);
    TimeCondition timeCondition = new TimeCondition(0);

    ObservableList<ProductRule> observableListOfRules = FXCollections.observableArrayList();
    ObservableList<ProductRow> observableListOfRows = FXCollections.observableArrayList();
    ObservableList<ProductTeacher> observableListOfTeachers = FXCollections.observableArrayList();

    TimeTable m_Timetable;


    //reminder that some codes need to be in synchronized!!!!!






    private void addToObservableListOfRules(ProductRule productRule){observableListOfRules.add(productRule);}
    private void addToObservableListOfRows(ProductRow productRow){observableListOfRows.add(productRow);}
    private void addToObservableListOfTeachers(ProductTeacher productTeacher) { observableListOfTeachers.add(productTeacher);}

    private void provideInfoFromOptimalSolution(TimeTable timeTable)
    {
        provideInfoAboutRulesAndFitnessFromOptimalSolution(timeTable);
        provideInfoAboutRowsFromOptimalSolution(timeTable); //row גולמי
        //provideInfoAboutTeachersFromOptimalSolution(timeTable);
    }


    private void provideInfoAboutGradesFromOptimalSolution(TimeTable timeTable, Grade gradeChosen)
    {
        gradesTableView.getItems().clear();
    }

    private void provideInfoAboutTeachersFromOptimalSolution(TimeTable timeTable, Teacher teacher)
    {
        //TimeTable timeTable = ui.getTimeTable();
        teachersTableView.getItems().clear();
        observableListOfTeachers.clear();
        List<TimeTableChromosome> chromosomes = timeTable.getChromosomes();
        chromosomes.sort(Comparator.comparingInt(TimeTableChromosome::getDay)
            .thenComparing(Comparator.comparingInt(TimeTableChromosome::getHour)));

        for(TimeTableChromosome chromosome : chromosomes)
        {
            if(chromosome.getTeacher().equals(teacher))
            {
                addToObservableListOfTeachers(new ProductTeacher(((Integer)chromosome.getDay()).toString(),
                        ((Integer)chromosome.getHour()).toString(), chromosome.getGrade().getName(),
                        ((Integer)chromosome.getGrade().getIdNumber()).toString(), chromosome.getSubject().getName(),
                        ((Integer)chromosome.getSubject().getIdNumber()).toString()));
            }
        }
        teachersTableView.setItems(observableListOfTeachers);
    }


    //private void provideInfoAboutTeacher
    private void provideInfoAboutRowsFromOptimalSolution(TimeTable timeTable)
    {
        rowsTableView.getItems().clear();
        observableListOfRows.clear();
        List<TimeTableChromosome> chromosomes = timeTable.getChromosomes();
        chromosomes.sort(Comparator.comparingInt(TimeTableChromosome::getDay)
                .thenComparing(Comparator.comparingInt(TimeTableChromosome::getHour)
                        .thenComparing(TimeTableChromosome::compareWithGrade)
                        .thenComparing(TimeTableChromosome::compareWithTeacher)));
        for(TimeTableChromosome chromosome : chromosomes)
        {
            addToObservableListOfRows(new ProductRow(((Integer)chromosome.getDay()).toString(),
                    ((Integer)chromosome.getHour()).toString(), chromosome.getTeacher().getName(),
                    chromosome.getGrade().getName(), chromosome.getSubject().getName()));
        }
        rowsTableView.setItems(observableListOfRows);
    }

    private void provideInfoAboutRulesAndFitnessFromOptimalSolution(TimeTable timeTable)
        {
        rulesTableView.getItems().clear();
        observableListOfRules.clear();
        List<Rule> rules = timeTable.getRules();
        for(Rule rule : rules)
        {
            addToObservableListOfRules(new ProductRule(rule.getClass().getSimpleName(),
                    rule.getRuleWeight() , ((Integer)rule.getGrade()).toString(), rule.getConfig()));
        }

        rulesTableView.setItems(observableListOfRules);
        hardRulesAvgLabel.setText(String.format("%.2f", timeTable.getHardRulesAvg()));
        softRulesAvgLabel.setText(String.format("%.2f", timeTable.getSoftRulesAvg()));
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
        runAlgorithmButton.setDisable(toDisable);
    }

    private boolean handleParametersBeforeRunning()
    {
        try
        {
            if(Integer.parseInt(numberOfGenerationTextField.getText()) < 100 && numOfGenerationCheckBox.isSelected())
            {
                m_SPMessageToUser.set("Number of generation needs to be at least a 100");
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            m_SPMessageToUser.set("Please set number of generations");
            return false;
        }
        try
        {
            String txt = numOfGeneration4Update.getText();
            int gensToUpdate = Integer.parseInt(txt);
            if(gensToUpdate <1)
            {
                m_SPMessageToUser.set("Number of generations to update needs to be a positive integer");
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            m_SPMessageToUser.set("Please set number of generations to update");
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
        try
        {
            if(numOfGenerationCheckBox.isSelected())
            {
                maxNumOfGenerationCondition.setMaxNumOfGeneration(Integer.parseInt(numberOfGenerationTextField.getText()));
                ui.getEngine().addStopCondition(maxNumOfGenerationCondition);
            }
            if(bestFitnessCheckBox.isSelected())
            {
                String bestFitnessStr = m_SPBestFitness.getValue();
                double bestFitness = Double.parseDouble(bestFitnessStr);

                bestFitnessCondition.setBestFitness(bestFitness);///????
                ui.getEngine().addStopCondition(bestFitnessCondition);
            }
            if(timerCheckBox.isSelected())
            {
                timeCondition.setMinutes(Integer.parseInt(timerTextField.getText()));
                ui.getEngine().addStopCondition(timeCondition);
            }
        }
        catch (Exception e)
        {
            m_SPMessageToUser.set("Stop condition chosen is empty");
        }

    }

    private void handleStopConditionAfterRunning(MaxNumOfGenerationCondition maxNumOfGenerationCondition, BestFitnessCondition bestFitnessCondition, TimeCondition timeCondition) {
        if(numOfGenerationCheckBox.isSelected())
        {
            ui.getEngine().removeStopCondition(maxNumOfGenerationCondition);
        }
        if(bestFitnessCheckBox.isSelected())
        {
            ui.getEngine().removeStopCondition(bestFitnessCondition);
        }
        if(timerCheckBox.isSelected())
        {
            ui.getEngine().removeStopCondition(timeCondition);
        }
    }

    private void handleProgressBarBeforeRunning()///!!!!!!
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




                double progress = Math.max(Math.max(progress4Generation, progress4Fitness),progress4Timer);
                //runProgressBar.progressProperty().unbind();
                //runProgressBar.setProgress(progress);
                //updateProgress(progress,1);
                //updateProgress(progress,1);
                m_DPProgress.set(progress);
                return null;
            }
        };

        // This method allows us to handle any Exceptions thrown by the task
        task.setOnFailed(wse -> m_SPMessageToUser.set(wse.getSource().getException().getMessage()));

        // If the task completed successfully, perform other updates here
        task.setOnSucceeded(wse -> {
            handleControls2DisableBeforeRunning(true);
            handleStopConditionAfterRunning(maxNumOfGenerationCondition, bestFitnessCondition, timeCondition);
            m_SPMessageToUser.set("The algorithm has run successfully");
        });

        // Before starting our task, we need to bind our values to the properties on the task
        //runProgressBar.progressProperty().bind(task.progressProperty());
        //m_DPProgress.bind(task.progressProperty());
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
                 m_Timetable = ui.getTimeTable();
                 List<Teacher> teachers = m_Timetable.getTeachers();
                 List<Grade> grades = m_Timetable.getGrades();
                 teacherChoiceBox.getItems().addAll(teachers);
                 gradeChoiceBox.getItems().addAll(grades);
              //  teacherComboBox.getItems().addAll(teachers);


                 ui.getEngine().addListenerToUpdateGeneration((bestFitnessInCurrentGeneration, numberOfGeneration) -> {

                     Platform.runLater(() -> {
                         m_DPCurrentFitness.set(Double.parseDouble(String.format("%.2f", bestFitnessInCurrentGeneration)));
                         m_IPCurrentGeneration.set(numberOfGeneration);
                         ui.getNumOfGeneration2BestFitness().put(numberOfGeneration, bestFitnessInCurrentGeneration);
                         provideInfoFromOptimalSolution((TimeTable) ui.getEngine().getOptimalSolution());
                         teacherChoiceBox.setOnAction(this::OnOptimalByTeacher);
                         gradeChoiceBox.setOnAction(this::OnOptimalByGrade);

                         updatesTableView.getItems().add(new ProductUpdate(((Integer)numberOfGeneration).toString(), String.format("%.2f", bestFitnessInCurrentGeneration))  );
                     });});

                provideInfoFromFile();
                animationForLoadFileTextBox();

                m_DPProgress.unbind();
                m_DPProgress.set(ProgressBar.INDETERMINATE_PROGRESS);
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

    private void OnOptimalByGrade(ActionEvent actionEvent)
    {
        TimeTable timeTable = (TimeTable) ui.getEngine().getOptimalSolution();
        Grade gradeChosen = gradeChoiceBox.getValue();
        provideInfoAboutGradesFromOptimalSolution(timeTable, gradeChosen);
    }


    private void OnOptimalByTeacher(ActionEvent actionEvent)
    {
        TimeTable timeTable = (TimeTable) ui.getEngine().getOptimalSolution();
        Teacher teacherChosen=teacherChoiceBox.getValue();
        provideInfoAboutTeachersFromOptimalSolution(timeTable,teacherChosen);
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
                //handleProgressBarBeforeRunning();

                handleStopConditionBeforeRunning(maxNumOfGenerationCondition, bestFitnessCondition, timeCondition);

                ui.getEngine().setNumberOfGenerationForUpdate(Integer.parseInt(numOfGeneration4Update.getText()));

                ui.getNumOfGeneration2BestFitness().clear();
                ui.getEngine().clear();
                animationForRunAlgorithmButton();
                handleControls2DisableBeforeRunning(false);
                m_DPCurrentFitness.set(0);
                m_IPCurrentGeneration.set(0);
                updatesTableView.getItems().clear();

                ui.setThreadEngine(new Thread(ui.getEngine()));
                ui.getThreadEngine().start();
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
        //if(m_DPProgress.get() == 1)
        if(ui.getThreadEngine().isAlive())
        {
            if(pauseResumeButton.getText().equals("Pause"))
            {
                try {
                    ui.getThreadEngine().wait();
                } catch (InterruptedException | IllegalMonitorStateException ignored) {

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

    @FXML//חקא קוד
    void OnStopRunClick(ActionEvent event) {
        double progress=m_DPProgress.get();
        //if(progress == 1)
        if(!ui.getThreadEngine().isAlive())
        {
           m_SPMessageToUser.set("The algorithm already over");
        }
        else if(progress <=0)///////////////
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
            m_DPProgress.set(0);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

        runProgressBar.progressProperty().bind(m_DPProgress);

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
                numOfGeneration4Update.setText(newValue.replaceAll("[^\\d]", ""));
            }});


        nameRulTableColumn.setCellValueFactory(new PropertyValueFactory("Name"));
        weightRuleTableColumn.setCellValueFactory(new PropertyValueFactory("Weight"));
        scoreRuleTableColumn.setCellValueFactory(new PropertyValueFactory("Score"));
        configRuleTableColumn.setCellValueFactory(new PropertyValueFactory("Configoration"));

        updateGenerationTableColumn.setCellValueFactory(new PropertyValueFactory("Generation"));
        updateFitnessTableColumn.setCellValueFactory(new PropertyValueFactory("Fitness"));

        dayRowTableColumn.setCellValueFactory(new PropertyValueFactory("Day"));
        hourRowTableColumn.setCellValueFactory(new PropertyValueFactory("Hour"));
       teacherRowTableColumn.setCellValueFactory(new PropertyValueFactory("Teacher"));
       gradeRowTableColumn.setCellValueFactory(new PropertyValueFactory("Grade"));
        subjectRowTableColumn.setCellValueFactory(new PropertyValueFactory("Subject"));

        //static data for dynamic data teacher / grade tableView
        teacherDayTableColumn.setText("Day");
        teacherSundayTableColumn.setText("Sunday");
        teacherMondayTableColumn.setText("Monday");
        teacherTuesdayTableColumn.setText("Tuesday");
        teacherWednesdayTableColumn.setText("Wednesday");
        teacherThursdayTableColumn.setText("Thursday");

        teacherDayTableColumn.setMinWidth(200);
        teacherSundayTableColumn.setMinWidth(200);
        teacherMondayTableColumn.setMinWidth(200);
        teacherTuesdayTableColumn.setMinWidth(200);
        teacherWednesdayTableColumn.setMinWidth(200);
        teacherThursdayTableColumn.setMinWidth(200);

        teacherSundayTableColumn.setCellValueFactory(new PropertyValueFactory("sunday"));


    }



    public class ProductRule {
        StringProperty m_Name;
        StringProperty m_Weight;
        StringProperty m_Score;
        StringProperty m_Config;

        public ProductRule(String name, String weight, String score, String config) {
            m_Name = new SimpleStringProperty(name);
            m_Weight= new SimpleStringProperty(weight);
            m_Score= new SimpleStringProperty(score);
            m_Config= new SimpleStringProperty(config);
        }

        public String getName(){return m_Name.get();}
        public String getWeight(){return m_Weight.get();}
        public String getScore(){return m_Score.get();}
        public String getConfigoration(){return m_Config.get();}

        public void setName(String name){m_Name.set(name);}
        public void setWeight(String name){m_Weight.set(name);}
        public void setScore(String name){m_Score.set(name);}
        public void setConfigoration(String name){m_Config.set(name);}
    }

    public class ProductUpdate
    {
        StringProperty m_Fitness;
        StringProperty m_Generation;

        public ProductUpdate(String generation, String fitness)
        {
            m_Generation= new SimpleStringProperty(generation);
            m_Fitness= new SimpleStringProperty(fitness);
        }

        public String getFitness(){return m_Fitness.get();}
        public String getGeneration(){return m_Generation.get();}

        public void setFitness(String fitness){m_Fitness.set(fitness);}
        public void setGeneration(String generation){m_Generation.set(generation);}
    }

    public class ProductRow
    {
        StringProperty m_Day;
        StringProperty m_Hour;
        StringProperty m_Teacher;
        StringProperty m_Grade;
        StringProperty m_Subject;

        public ProductRow( String day,  String hour, String teacher, String grade, String subject)
        {
            m_Day= new SimpleStringProperty(day);
            m_Hour= new SimpleStringProperty(hour);
            m_Teacher= new SimpleStringProperty(teacher);
            m_Grade= new SimpleStringProperty(grade);
            m_Subject= new SimpleStringProperty(subject);
        }

        public String getDay(){return m_Day.get();}
        public String getHour(){return m_Hour.get();}
        public String getTeacher(){return m_Teacher.get();}
        public String getGrade(){return m_Grade.get();}
        public String getSubject(){return m_Subject.get();}


        public void setDay(String day){m_Day.set(day);}
        public void setHour(String hour){m_Hour.set(hour);}
        public void setTeacher(String teacher){m_Teacher.set(teacher);}
        public void setGrade(String grade){m_Grade.set(grade);}
        public void setSubject(String subject){m_Subject.set(subject);}
    }

    public class ProductTeacher
    {
        StringProperty m_Day;
        StringProperty m_Hour;
        StringProperty m_GradeName;
        StringProperty m_GradeID;
        StringProperty m_SubjectName;
        StringProperty m_SubjectID;


        public ProductTeacher( String day,  String hour, String gradeName, String gradeID, String subjectName, String subjectID)
        {
            m_Day= new SimpleStringProperty(day);
            m_Hour= new SimpleStringProperty(hour);
            m_GradeName= new SimpleStringProperty(gradeName);
            m_GradeID= new SimpleStringProperty(gradeID);
            m_SubjectName= new SimpleStringProperty(subjectName);
            m_SubjectID = new SimpleStringProperty(subjectID);
        }

        public String getDay(){return m_Day.get();}
        public String getHour(){return m_Hour.get();}
        public String getGradeName(){return m_GradeName.get();}
        public String getGradeID(){return m_GradeID.get();}
        public String getSubjectName(){return m_SubjectName.get();}
        public String getSubjectID(){return m_SubjectID.get();}

        public void setDay(String day){m_Day.set(day);}
        public void setHour(String hour){m_Hour.set(hour);}
        public void setTeacher(String gradeName){m_GradeName.set(gradeName);}
        public void setGrade(String gradeID){m_GradeID.set(gradeID);}
        public void setSubjectName(String subjectName){m_SubjectName.set(subjectName);}
        public void setSubjectID(String subjectID){m_SubjectID.set(subjectID);}

    }

    public class ProductGrade
    {

        public ProductGrade() {

        }

    }
}