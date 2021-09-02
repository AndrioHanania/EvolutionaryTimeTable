package JAVAFXUI;

import engine.selection.RouletteWheel;
import engine.selection.Tournament;
import engine.selection.Truncation;
import engine.stopCondition.BestFitnessCondition;
import engine.stopCondition.MaxNumOfGenerationCondition;
import engine.stopCondition.TimeCondition;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import timeTable.Grade;
import timeTable.Rules.Rule;
import timeTable.Subject;
import timeTable.Teacher;
import timeTable.TimeTable;
import timeTable.chromosome.TimeTableChromosome;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


public class Controller implements Initializable///heloooooo
{

    public Controller()
    {

    }
    //note

    JavaFxUI ui = new JavaFxUI();

    // Values injected by FXMLLoader
    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private Button fileChooserButton;
    @FXML private TextField loadFileTextBox;
    @FXML private Label messageToUserTextBox;
    @FXML private Button runAlgorithmButton;
    @FXML private CheckBox bestFitnessCheckBox;
    @FXML private CheckBox timerCheckBox;
    @FXML private CheckBox numOfGenerationCheckBox;

    @FXML private CheckBox flippingCheckBox;
    @FXML private CheckBox sizerCheckBox;

    @FXML private TextField numberOfGenerationTextField;
    @FXML private TextField bestFitnessTextField;
    @FXML private TextField timerTextField;
    @FXML private TextField numOfGeneration4Update;
    @FXML private TextField sizerTopPercentTextField;
    @FXML private TextField flippingTopPercentTextField;
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
    @FXML private TextArea selectionTextArea;
    @FXML private Label hardRulesAvgLabel;
    @FXML private Label softRulesAvgLabel;
    @FXML private Label elitismLabel;
    @FXML private TextField changeElitismTextField;
    @FXML private Button changeElitismButton;
    @FXML private TableView<ProductRule> rulesTableView;
    @FXML private TableView<ProductUpdate> updatesTableView;
    @FXML private TableView<ProductRow> rowsTableView;
    @FXML private TableView<ProductTeacher> teachersTableView;
    @FXML private TableView<ProductGrade> gradesTableView;
    @FXML private CheckBox truncationCheckBox;
    @FXML private CheckBox rouletteWheelCheckBox;
    @FXML private CheckBox tournamentCheckBox;
    @FXML private TextField topPercentTextField;
    @FXML private TextField PTETextfield;
    @FXML private Button ChangeSelectionButton;

    @FXML private AnchorPane mutationSwitch;

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
    @FXML private ChoiceBox<String> teacherChoiceBox;
    @FXML private ChoiceBox<String> gradeChoiceBox;
    //   @FXML private ChoiceBox<String> myChoiceBox;

    @FXML private GridPane teacherGridOptimalSolution;
    @FXML private GridPane gradeGridOptimalSolution;





    BooleanProperty m_BPIsPause = new SimpleBooleanProperty(true);
    IntegerProperty m_SPElitism = new SimpleIntegerProperty();
    IntegerProperty m_IPCurrentGeneration = new SimpleIntegerProperty(0);
    DoubleProperty m_DPCurrentFitness = new SimpleDoubleProperty(0.0);
    StringProperty m_SPMessageToUser = new SimpleStringProperty();
    StringProperty m_SPNameLoadFile = new SimpleStringProperty("File: ");
    StringProperty m_SPBestFitness = new SimpleStringProperty();
    StringProperty  m_SPTeachersInfo = new SimpleStringProperty();
    StringProperty  m_SPSubjectsInfo = new SimpleStringProperty();
    StringProperty  m_SPGradesInfo = new SimpleStringProperty();
    StringProperty  m_SPRulesInfo = new SimpleStringProperty();
    StringProperty m_SPSelectionInfo = new SimpleStringProperty();
    DoubleProperty m_DPProgress = new SimpleDoubleProperty(0.0);


    //Casual reboot--Stop Conditions--//
    MaxNumOfGenerationCondition maxNumOfGenerationCondition =new MaxNumOfGenerationCondition(0);
    BestFitnessCondition bestFitnessCondition = new BestFitnessCondition(0);
    TimeCondition timeCondition = new TimeCondition(0);

    //Casual reboot
    Truncation truncation = new Truncation(1);
    RouletteWheel rouletteWheel = new RouletteWheel();
    Tournament tournament = new Tournament(0);


    ObservableList<ProductRule> observableListOfRules = FXCollections.observableArrayList();
    ObservableList<ProductRow> observableListOfRows = FXCollections.observableArrayList();
    ObservableList<String> observableListOfTeachers = FXCollections.observableArrayList();
    ObservableList<String> observableListOfGrades = FXCollections.observableArrayList();


    TimeTable m_Timetable;
    List<Teacher> m_Teachers=new ArrayList<>();
    List<String> m_TeachersNames =new ArrayList<>();
    List<Grade> m_Grades = new ArrayList<>();
    List<String> m_GradeNames = new ArrayList<>();



    //reminder that some codes need to be in synchronized!!!!!

    private void addToObservableListOfRules(ProductRule productRule){observableListOfRules.add(productRule);}
    private void addToObservableListOfRows(ProductRow productRow){observableListOfRows.add(productRow);}


    private void provideInfoAboutGradesFromOptimalSolution(TimeTable optimalTimetable,String grade)
    {
        String subjectName;
        String SubjectId;
   //     gradeGridOptimalSolution.getChildren().clear();
       // initialGrid(gradeGridOptimalSolution);
        if(optimalTimetable!=null)
        {
            List<TimeTableChromosome> chromosomes = optimalTimetable.getChromosomes();
            List<TimeTableChromosome> filteredByGradeChromosomes = chromosomes.stream().filter(t->
                    t.getGrade().getName().equals(grade)).collect(Collectors.toList());

            Map<String,TimeTableChromosome> dateToFive = new HashMap<>();
            filteredByGradeChromosomes.stream().forEach(t->{
                String key = String.format("%s%s", t.getDay(),t.getHour());
                if(!dateToFive.containsKey(key))
                {

                    Label label = new Label();
                    label.setWrapText(true);
                    dateToFive.put(key,t);
                    String day = key.substring(0,1);
                    String hour = key.substring(1);
                    Integer dayInt = Integer.valueOf(day);
                    Integer hourInt = Integer.valueOf(hour);
                    label.textProperty().bind(Bindings.concat("Teacher: ",t.getTeacher().getName()," ID: ",
                            t.getTeacher().getIdNumber(),System.lineSeparator(), "Subject: ", t.getSubject().getName()," ID: ",
                            t.getSubject().getIdNumber()));

                    gradeGridOptimalSolution.add(label,dayInt,hourInt);
                }
            });
        }
    }

        /*
        for (TimeTableChromosome chromosome : chromosomes)
        {
            if (chromosome.getTeacher().getName().equals(teacher))
            {
                subjectNameLabel.setText(chromosome.getSubject().getName());
                subjectIdLabel.setText(String.valueOf(chromosome.getSubject().getIdNumber()));
                gradeNameLabel.setText(chromosome.getGrade().getName());
                gradeIdLabel.setText(String.valueOf(chromosome.getGrade().getIdNumber()));
                int dayColumn = chromosome.getDay();
                int hourRow = chromosome.getHour();
                //teacherGridOptimalSolution.getChildren().remove(1,teacherGridOptimalSolution.getChildren().size());
      //          teacherGridOptimalSolution.isDisable()
             //   teacherGridOptimalSolution.setConstraints(subjectNameLabel,dayColumn,hourRow);
      //          teacherGridOptimalSolution.getChildren().add(subjectNameLabel);
            }
        }

         */
    //System.out.println("check");

    private void initialGrid(GridPane grid) {
        Label dayTimeLabel = new Label("Time , Day");
        Label sundayLabel = new Label("Sunday");
        Label mondayLabel = new Label("Monday");
        Label tuesdayLabel = new Label("Tuesday");
        Label wednesdayLabel = new Label("wednesday");
        Label thursdayLabel = new Label("Thursday");

        Label eightOclockLabel = new Label("8:00");
        Label nineOclockLabel = new Label("9:00");
        Label tentOclockLabel = new Label("10:00");
        Label elevenOclockLabel = new Label("11:00");
        Label twelveOclockLabel = new Label("12:00");
        Label oneOclockLabel = new Label("13:00");
        Label twoOclockLabel = new Label("14:00");
        Label threeOclockLabel = new Label("15:00");
        Label fourOclockLabel = new Label("16:00");

        grid.setConstraints(dayTimeLabel,0,0);
        grid.setConstraints(sundayLabel,1,0);
        grid.setConstraints(mondayLabel,2,0);
        grid.setConstraints(tuesdayLabel,3,0);
        grid.setConstraints(wednesdayLabel,4,0);
        grid.setConstraints(thursdayLabel,5,0);

        grid.setConstraints(eightOclockLabel,0,1);
        grid.setConstraints(nineOclockLabel,0,2);
        grid.setConstraints(tentOclockLabel,0,3);
        grid.setConstraints(elevenOclockLabel,0,4);
        grid.setConstraints(twelveOclockLabel,0,5);
        grid.setConstraints(oneOclockLabel,0,6);
        grid.setConstraints(twoOclockLabel,0,7);
        grid.setConstraints(threeOclockLabel,0,8);
        grid.setConstraints(fourOclockLabel,0,9);

        grid.getChildren().addAll(dayTimeLabel,sundayLabel,mondayLabel,tuesdayLabel
                ,wednesdayLabel,thursdayLabel,eightOclockLabel,nineOclockLabel,tentOclockLabel,elevenOclockLabel,
                twelveOclockLabel,oneOclockLabel,twoOclockLabel,threeOclockLabel,fourOclockLabel);


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
        //time table
        provideInfoAboutTeachers();
        provideInfoAboutSubjects();
        provideInfoAboutGrades();
        provideInfoAboutRules();
        //engine
        provideInfoAboutSelection();
    }

    private void provideInfoAboutSelection()
    {
        m_SPSelectionInfo.set(ui.getEngine().getSelection().toString());
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

    //note
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
        scale.play();

    }

    private void animationForRunAlgorithmButton()
    {
        runAlgorithmButton.setRotate(0);
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(runAlgorithmButton);
        rotate.setDuration(Duration.millis(1000));
        rotate.setCycleCount(2);
        rotate.setByAngle(360);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.play();
        runAlgorithmButton.setRotate(0);
    }

    private void handleControls2DisableBeforeRunning(boolean toDisable)
    {
        numOfGenerationCheckBox.setDisable(toDisable);
        bestFitnessCheckBox.setDisable(toDisable);
        timerCheckBox.setDisable(toDisable);
        numberOfGenerationTextField.setDisable(toDisable ? toDisable : !numOfGenerationCheckBox.isSelected());
        bestFitnessTextField.setDisable(toDisable ? toDisable : !bestFitnessCheckBox.isSelected());
        timerTextField.setDisable(toDisable ? toDisable : !timerCheckBox.isSelected());
        numOfGeneration4Update.setDisable(toDisable);
        runAlgorithmButton.setDisable(toDisable);
    }

    private boolean handleParametersBeforeRunning()
    {
        try
        {
            if(numOfGenerationCheckBox.isSelected() && Integer.parseInt(numberOfGenerationTextField.getText()) < 100)
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

        if(bestFitnessCheckBox.isSelected() && bestFitnessTextField.getText().equals(""))
        {
            m_SPMessageToUser.set("Please set number of maximum fitness");
            return false;
        }

        if(timerCheckBox.isSelected() && timerTextField.getText().equals(""))
        {
            m_SPMessageToUser.set("Please set number of minutes for the timer");
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

                bestFitnessCondition.setBestFitness(bestFitness);
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

    private class TaskService extends Service<Void> {

        @Override
        protected Task<Void> createTask() {

            Task<Void> task = new Task<Void>() {

                @Override
                protected Void call() throws Exception {

                    //while (ui.getThreadEngine().isAlive()) + קריאה ךמתודה הזאת רק אחרי הפעלת טריד האלגוריתם
                    while (!ui.getEngine().isFinishToRun())
                    {
                        double progress4Generation = 0;
                        double progress4Fitness = 0;
                        double progress4Timer = 0;

                        if (numOfGenerationCheckBox.isSelected()) {
                            progress4Generation = (double) ui.getEngine().getNumOfGeneration() / Integer.parseInt(numberOfGenerationTextField.getText());
                        }
                        if (bestFitnessCheckBox.isSelected()) {
                            progress4Fitness = ui.getEngine().getBestFitnessInCurrentGeneration() / Double.parseDouble(bestFitnessTextField.getText());
                        }
                        if (timerCheckBox.isSelected()) {
                            progress4Timer = (double) timeCondition.getSecondLeft() / (60 * Integer.parseInt(timerTextField.getText()));
                        }

                        double progress = Math.max(Math.max(progress4Generation, progress4Fitness), progress4Timer);
                        updateProgress(progress, 1);

                    }
                    return null;
                }
            };

            // This method allows us to handle any Exceptions thrown by the task
            task.setOnFailed(wse -> m_SPMessageToUser.set(wse.getSource().getException().getMessage()));

            return task;
        }
    }

    private void handleProgressBarBeforeRunning()
    {
        TaskService service = new TaskService();
        runProgressBar.progressProperty().bind(service.progressProperty());
        service.restart();
    }

    private void provideInfoFromOptimalSolution(TimeTable timeTable)
    {
        provideInfoAboutRulesAndFitnessFromOptimalSolution(timeTable);
        provideInfoAboutRowsFromOptimalSolution(timeTable);

    }

    private void setGradesChoiceBox() {
        m_Grades = m_Timetable.getGrades();
        m_GradeNames.clear();
        observableListOfGrades.clear();
        for(Grade grade: m_Grades)
        {
            m_GradeNames.add(grade.getName());
        }
        observableListOfGrades.addAll(m_GradeNames);
        gradeChoiceBox.getItems().clear();
        gradeChoiceBox.getItems().removeAll();
        gradeChoiceBox.getItems().setAll(observableListOfGrades);
        gradeChoiceBox.setValue(observableListOfGrades.get(0));
    }

    private void setTeachersChoiceBox() {
        m_Teachers = m_Timetable.getTeachers();
        m_TeachersNames.clear();
        observableListOfTeachers.clear();

        for (Teacher teacher:m_Teachers)
        {
            m_TeachersNames.add(teacher.getName());
        }

        observableListOfTeachers.addAll(m_TeachersNames);
        teacherChoiceBox.getItems().clear();
        teacherChoiceBox.getItems().removeAll();
        teacherChoiceBox.getItems().setAll(observableListOfTeachers);
        teacherChoiceBox.setValue(observableListOfTeachers.get(0));
        //teacherChoiceBox.setOnAction(this::OnOptimalByTeacher);

    }

    private void provideInfoAboutTeachersFromOptimalSolution(TimeTable optimalTimetable,String teacher)
    {
       // teacherGridOptimalSolution.getChildren().clear();
        //initialGrid(teacherGridOptimalSolution);
        List<TimeTableChromosome> chromosomes = optimalTimetable.getChromosomes();
        List<TimeTableChromosome> filteredByTeacherChromosome = chromosomes.stream().filter(t ->
                t.getTeacher().getName().equals(teacher)).collect(Collectors.toList());

        Map<String,TimeTableChromosome> dateToFive = new HashMap<>();
        filteredByTeacherChromosome.stream().forEach(t->{
            String key = String.format("%s%s",t.getDay(),t.getHour());

            if(!dateToFive.containsKey(key))
            {
                Label label = new Label();
                //subjectNameLabel = t.getSubject().getName();
                dateToFive.put(key,t);
                String day = key.substring(0,1);
                String hour = key.substring(1);
                Integer dayInt = Integer.valueOf(day);
                Integer hourInt = Integer.valueOf(hour);
                // subjectNameLabel.setText(t.getSubject().getName());
                label.textProperty().bind(Bindings.concat("Grade: ",t.getGrade().getName()," ID: ",
                        t.getGrade().getIdNumber(),System.lineSeparator(), "Subject: ", t.getSubject().getName()," ID: ",
                        t.getSubject().getIdNumber()));
                // teacherGridOptimalSolution.setConstraints.(subjectNameLabel,day,hour);
                label.setWrapText(true);
                teacherGridOptimalSolution.add(label,dayInt,hourInt);
            }

        });
    }

    @FXML void OnFileChooser(ActionEvent event)
    {
            fileChooserButton.setDisable(true);
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(null);
            if(selectedFile == null)
            {fileChooserButton.setDisable(false);
                return;}
            teacherGridOptimalSolution.getChildren().clear();
            teacherGridOptimalSolution.getChildren().removeAll();
            m_SPNameLoadFile.set("File: " + selectedFile.getName());

            teacherChoiceBox.setOnAction(this::OnOptimalByTeacher);
            gradeChoiceBox.setOnAction(this::OnOptimalByGrade);

            try
            {

                animationForLoadFileTextBox();
                ui.loadInfoFromXmlFile(selectedFile);
                m_Timetable = ui.getTimeTable();
                setTeachersChoiceBox();
                setGradesChoiceBox();

                ui.getEngine().addListenerToUpdateGeneration((bestFitnessInCurrentGeneration, numberOfGeneration) -> {
                     Platform.runLater(() -> {
                         //if(!bestFitnessCheckBox.isSelected() && bestFitnessInCurrentGeneration <= Double.parseDouble(bestFitnessTextField.getText()))
                                //m_DPCurrentFitness.set(Double.parseDouble(String.format("%.2f", bestFitnessInCurrentGeneration)));
                         updateOptimalByTeacher();
                         updateOptimalByGrade();

                         if(bestFitnessCheckBox.isSelected() && bestFitnessInCurrentGeneration > Double.parseDouble(bestFitnessTextField.getText())) {
                         }
                         else {
                             m_DPCurrentFitness.set(Double.parseDouble(String.format("%.2f", bestFitnessInCurrentGeneration)));
                         }

                         m_IPCurrentGeneration.set(numberOfGeneration);
                         ui.getNumOfGeneration2BestFitness().put(numberOfGeneration, bestFitnessInCurrentGeneration);
                         provideInfoFromOptimalSolution((TimeTable) ui.getEngine().getOptimalSolution());
                         updatesTableView.getItems().add(new ProductUpdate(((Integer)numberOfGeneration).toString(), String.format("%.2f", bestFitnessInCurrentGeneration))  );
                     });});

                provideInfoFromFile();

                m_DPProgress.unbind();
                m_DPProgress.set(ProgressBar.INDETERMINATE_PROGRESS);

                elitismLabel.textProperty().unbind();
                elitismLabel.textProperty().bind(ui.getEngine().getSelection().getElitismProperty().asString());
                ui.getEngine().getSelection().getElitismProperty().bind(m_SPElitism);

                m_SPSelectionInfo.unbind();
                m_SPSelectionInfo.bind(ui.getEngine().getSelection().toStringProperty());
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

            fileChooserButton.setDisable(false);
    }


    private void updateOptimalByGrade()
    {
        gradeGridOptimalSolution.getChildren().clear();
        initialGrid(gradeGridOptimalSolution);
        TimeTable optimalTimeTable = (TimeTable) ui.getEngine().getOptimalSolution();
        if(optimalTimeTable!=null)
        {
            String gradeChosen = gradeChoiceBox.getValue();
            provideInfoAboutGradesFromOptimalSolution(optimalTimeTable, gradeChosen);
        }
    }
    private void OnOptimalByGrade(ActionEvent actionEvent)
    {
        updateOptimalByGrade();
    }

    private void updateOptimalByTeacher() {
        teacherGridOptimalSolution.getChildren().clear();
        initialGrid(teacherGridOptimalSolution);
        TimeTable optimalTimeTable= (TimeTable) ui.getEngine().getOptimalSolution();
        if(optimalTimeTable!= null)
        {
            String teacherChosen=teacherChoiceBox.getValue();
            provideInfoAboutTeachersFromOptimalSolution(optimalTimeTable,teacherChosen);
        }
    }



    private void OnOptimalByTeacher(ActionEvent actionEvent)
    {
        updateOptimalByTeacher();
    }

    @FXML void OnRunAlgo(ActionEvent event)
    {
        if(!ui.getIsXmlFileLoaded())
        {
            m_SPMessageToUser.set("Can't run the algorithm before loading xml file");
        }
        else
        {
            if(handleParametersBeforeRunning()) {
                handleProgressBarBeforeRunning();
                handleStopConditionBeforeRunning(maxNumOfGenerationCondition, bestFitnessCondition, timeCondition);
                ui.getEngine().setNumberOfGenerationForUpdate(Integer.parseInt(numOfGeneration4Update.getText()));
                ui.getNumOfGeneration2BestFitness().clear();
                ui.getEngine().clear();
                animationForRunAlgorithmButton();
                handleControls2DisableBeforeRunning(true);
                updatesTableView.getItems().clear();


                ui.setTaskEngine(new Task() {
                    @Override
                    public Object call(){
                        try {
                            ui.getEngine().run();
                        }
                       catch (Exception e)
                       {
                           Platform.runLater(()->{
                               m_SPMessageToUser.set(e.getMessage());
                           });
                       }
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        handleControls2DisableBeforeRunning(false);
                        handleStopConditionAfterRunning(maxNumOfGenerationCondition, bestFitnessCondition, timeCondition);
                        m_SPMessageToUser.set("The algorithm has run successfully");
                    }

                    @Override
                    protected void running() {
                        m_SPMessageToUser.set("Process...");
                    }
                });

                ui.setThreadEngine(new Thread(ui.getTaskEngine()));
                ui.getThreadEngine().setName("ThreadEngine");
                ui.getThreadEngine().start();
            }
        }
    }

    @FXML void OnSizerChecked(ActionEvent event)
    {
        if(sizerCheckBox.isSelected())
        {
            sizerTopPercentTextField.setDisable(false);
        }
        else
        {
            sizerTopPercentTextField.setDisable(true);
        }
    }
    @FXML void OnFlippingChecked(ActionEvent event)
    {
        if(flippingCheckBox.isSelected())
        {
            flippingTopPercentTextField.setDisable(false);
        }
        else
        {
            flippingTopPercentTextField.setDisable(true);
        }
        //System.out.println("check");
    }


    @FXML void OnNumOfGenerationCheck(ActionEvent event)
    {
        numberOfGenerationTextField.setDisable(!numOfGenerationCheckBox.isSelected());
    }


    @FXML void OnBestFitnessCheck(ActionEvent event) {
        bestFitnessTextField.setDisable(!bestFitnessCheckBox.isSelected());
    }


    @FXML void OnTimerCheck(ActionEvent event) {
        timerTextField.setDisable(!timerCheckBox.isSelected());
    }


    @FXML void OnChangeElitism(ActionEvent event) {
        if(changeElitismTextField.getText()!=null && !changeElitismTextField.getText().equals(""))
        {
            int newElitism=Integer.parseInt(changeElitismTextField.getText());
            if(newElitism < ui.getEngine().getSizeOfFirstPopulation())
                m_SPElitism.set(newElitism);
            else m_SPMessageToUser.set("Elitism is bigger than size of population");
        }
    }


    @FXML void OnPauseResumeClick(ActionEvent event)
    {
        //////////////////////handle timer need to Pause-Resume////////////////////////////////
        if(ui.getThreadEngine()!=null && ui.getThreadEngine().isAlive())
        {
            if(m_BPIsPause.get())
            {
                ui.getEngine().pause();
                pauseResumeButton.setText("Resume");
                m_SPMessageToUser.set("Pause");
                m_BPIsPause.set(false);
            }
            else
            {
                ui.getEngine().resume();
                pauseResumeButton.setText("Pause");
                m_SPMessageToUser.set("Process...");
                m_BPIsPause.set(true);
            }
        }
        else
        {
            m_SPMessageToUser.set("Can't " + pauseResumeButton.getText() + " after or before running");
        }
    }


    @FXML void OnStopRunClick(ActionEvent event) {
        //////////////////////handle timer need to stop////////////////////////////////

        if(ui.getThreadEngine()==null || runProgressBar.getProgress() <=0)
        {
            m_SPMessageToUser.set("The algorithm hasn't started yet");
        }
        else if(!ui.getThreadEngine().isAlive())
        {
           m_SPMessageToUser.set("The algorithm already over");
        }
        else
        {
            if(!m_BPIsPause.get())
                {ui.getEngine().resume();}

            synchronized (ui.getThreadEngine())
            {  ui.getThreadEngine().interrupt();}
        }
    }

    @FXML void OnChangeSelection(ActionEvent event)
    {
        if(truncationCheckBox.isSelected()){
            String topPercent=topPercentTextField.getText();
            if(topPercent!=null && !topPercent.equals("")) {
                truncation.setTopPercent(Integer.parseInt(topPercent));
                truncation.setConfiguration("Top Percent= " + topPercent);
                truncation.setSizeOfElitism(ui.getEngine().getSelection().getSizeOfElitism());
                ui.getEngine().setSelection(truncation);
                m_SPMessageToUser.set("The Selection technique has changed");
            }
            else m_SPMessageToUser.set("The parameter 'Top percent' is empty");
        }

        if (rouletteWheelCheckBox.isSelected())
        {
            rouletteWheel.setConfiguration("");
            rouletteWheel.setSizeOfElitism(ui.getEngine().getSelection().getSizeOfElitism());
            ui.getEngine().setSelection(rouletteWheel);
            m_SPMessageToUser.set("The Selection technique has changed");
        }

        if(tournamentCheckBox.isSelected())
        {
            String pte=PTETextfield.getText();
            if(pte!=null && !pte.equals("")){
                tournament.setPTE(Integer.parseInt(pte));
                tournament.setConfiguration("PTE= " + pte);
                tournament.setSizeOfElitism(ui.getEngine().getSelection().getSizeOfElitism());
                ui.getEngine().setSelection(tournament);
                m_SPMessageToUser.set("The Selection technique has changed");
            }
            else m_SPMessageToUser.set("The parameter 'PTE' is empty");
        }

        m_SPSelectionInfo.unbind();
        m_SPSelectionInfo.bind(ui.getEngine().getSelection().toStringProperty());
    }



    @FXML void OnTruncationSelected(ActionEvent event)
    { truncationCheckBox.setSelected(truncationCheckBox.isSelected() &&
            !(rouletteWheelCheckBox.isSelected() || tournamentCheckBox.isSelected()));}

    @FXML void OnRouletteWheelSelected(ActionEvent event)
    { rouletteWheelCheckBox.setSelected(rouletteWheelCheckBox.isSelected() &&
            !(truncationCheckBox.isSelected() || tournamentCheckBox.isSelected()));}

    @FXML void OnTournamentSelected(ActionEvent event)
    { tournamentCheckBox.setSelected(tournamentCheckBox.isSelected() &&
            !(rouletteWheelCheckBox.isSelected() ||  truncationCheckBox.isSelected()));}


    @Override
    public void initialize(URL location1, ResourceBundle resources1) {
        location=location1;
        resources=resources1;
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
        selectionTextArea.textProperty().bind(m_SPSelectionInfo);
        changeElitismButton.disableProperty().bind(m_BPIsPause);
        ChangeSelectionButton.disableProperty().bind(m_BPIsPause);

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
                }
                if(!newValue.equals("") && Double.parseDouble(newValue) > 100) {
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

        changeElitismTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                changeElitismTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }});

        topPercentTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                topPercentTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if(newValue!=null && !newValue.equals("")){
            int value=Integer.parseInt(newValue);
            if (value > 100 || value<=0) {
                topPercentTextField.setText(oldValue);
            }}
        });

        PTETextfield.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                PTETextfield.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if(newValue!=null && !newValue.equals("")){
            if (Integer.parseInt(newValue) > 1) {
                PTETextfield.setText(oldValue);
            }}
        });

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

        public ProductRow(String day,  String hour, String teacher, String grade, String subject)
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