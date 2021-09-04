package JAVAFXUI;

import engine.mutation.Mutation;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import timeTable.Grade;
import timeTable.Mutation.Flipping;
import timeTable.Mutation.Sizer;
import timeTable.Rules.Rule;
import timeTable.Subject;
import timeTable.Teacher;
import timeTable.TimeTable;
import timeTable.chromosome.TimeTableChromosome;
import timeTable.crossover.AspectOriented;
import timeTable.crossover.DayTimeOriented;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


public class Controller implements Initializable
{

    public Controller()
    {

    }

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


    @FXML private TableView<ProductSizer> sizerTableView;
    @FXML private TableColumn<ProductSizer,String> sizerTableColumn;
    @FXML private TableColumn<ProductSizer,String> sizerProbabilityTableCol;
    @FXML private TableColumn<ProductSizer,String> sizerTotalTupplesTableCol;

    @FXML private TableView<ProductFlipping> flippingTableView;
    @FXML private TableColumn<ProductFlipping,String> flippingProbabilityTableCol;
    @FXML private TableColumn<ProductFlipping,String> flippingComponentTableCol;
    @FXML private TableColumn<ProductFlipping,String> flippingMaxTupplesTableCol;


    @FXML private CheckBox truncationCheckBox;
    @FXML private CheckBox rouletteWheelCheckBox;
    @FXML private CheckBox tournamentCheckBox;
    @FXML private TextField topPercentTextField;
    @FXML private TextField PTETextfield;
    @FXML private Button changeSelectionButton;
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



    @FXML private TextArea crossoverTextArea;
    @FXML private Button changeCrossoverButton;
    @FXML private CheckBox dayTimeOrientedCheckBox;
    @FXML private CheckBox aspectOrientedCheckBox;
    @FXML private CheckBox orientedByClass;
    @FXML private CheckBox orientedByTeacher;
    @FXML private TextField cuttingPointsTextField;
    @FXML private javafx.scene.chart.LineChart<String, Number> LineChart;
    @FXML private NumberAxis fitnessNumberAxis;
    @FXML private Label sizeOfPopulationLabel;
    @FXML private TextArea messageToUserTextArea;

    private BooleanProperty m_BPIsPause = new SimpleBooleanProperty(false);
    private IntegerProperty m_IPCurrentGeneration = new SimpleIntegerProperty(0);
    private DoubleProperty m_DPCurrentFitness = new SimpleDoubleProperty(0.0);
    private StringProperty m_SPMessageToUser = new SimpleStringProperty();
    private StringProperty m_SPNameLoadFile = new SimpleStringProperty("File: ");
    private StringProperty m_SPBestFitness = new SimpleStringProperty();
    private StringProperty m_SPTeachersInfo = new SimpleStringProperty();
    private StringProperty m_SPSubjectsInfo = new SimpleStringProperty();
    private StringProperty m_SPGradesInfo = new SimpleStringProperty();
    private StringProperty m_SPRulesInfo = new SimpleStringProperty();
    private StringProperty m_SPSelectionInfo = new SimpleStringProperty();
    private StringProperty m_SPCrossoverInfo = new SimpleStringProperty();
    private StringProperty m_SPPTE = new SimpleStringProperty();
    private StringProperty m_SPElitism = new SimpleStringProperty();
    private DoubleProperty m_DPProgress = new SimpleDoubleProperty(0.0);

    //Casual reboot
    //-Stop Conditions--//
    private MaxNumOfGenerationCondition maxNumOfGenerationCondition =new MaxNumOfGenerationCondition(0);
    private BestFitnessCondition bestFitnessCondition = new BestFitnessCondition(0);
    private TimeCondition timeCondition = new TimeCondition(0);

    //-Selections--//
    private Truncation truncation = new Truncation(1);
    private RouletteWheel rouletteWheel = new RouletteWheel();
    private Tournament tournament = new Tournament(0);
    //-Crossovers--//
    private DayTimeOriented dayTimeOriented = new DayTimeOriented(0);
    private AspectOriented aspectOriented = new AspectOriented(0, "");



    private List<Teacher> m_Teachers=new ArrayList<>();
    private List<String> m_TeachersNames =new ArrayList<>();
    private List<Grade> m_Grades = new ArrayList<>();
    private List<String> m_GradeNames = new ArrayList<>();

    ObservableList<ProductRule> observableListOfRules = FXCollections.observableArrayList();
    ObservableList<ProductRow> observableListOfRows = FXCollections.observableArrayList();
    ObservableList<String> observableListOfTeachers = FXCollections.observableArrayList();
    ObservableList<String> observableListOfGrades = FXCollections.observableArrayList();
    ObservableList<ProductFlipping> observableListOfFlipping = FXCollections.observableArrayList();
    ObservableList<ProductSizer> observableListOfSizer = FXCollections.observableArrayList();


    private TimeTable m_Timetable;
    private int maxSizeChromosomes = 0;
    private String saveErrorMessage;

    //reminder that some codes need to be in synchronized!!!!!






    private void addToObservableListOfRules(ProductRule productRule){observableListOfRules.add(productRule);}
    private void addToObservableListOfRows(ProductRow productRow){observableListOfRows.add(productRow);}
    private void addToObservableListOfFlipping(ProductFlipping flipping){observableListOfFlipping.add(flipping);}
    private void addToObservableListOfSizer(ProductSizer sizer){observableListOfSizer.add(sizer);}

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



    private void provideInfoAboutMutations()
    {
        sizerTableView.editableProperty().unbind();
        flippingTableView.editableProperty().unbind();
        sizerTableView.editableProperty().bindBidirectional(m_BPIsPause);
        flippingTableView.editableProperty().bindBidirectional(m_BPIsPause);
        flippingTableView.getItems().clear();
        sizerTableView.getItems().clear();
        observableListOfFlipping.clear();
        observableListOfSizer.clear();
        flippingTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        sizerTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

       // flippingTableView.getColumns().add(flippingProbabilityTableCol);
      //  flippingTableView.getColumns().add(flippingComponentTableCol);
      //  flippingTableView.getColumns().add(flippingMaxTupplesTableCol);

        List<Mutation> mutations =  ui.getEngine().getMutations();
        for(Mutation mutation : mutations)
        {
            if(mutation.getClass().equals(Flipping.class))
            {
                Flipping flip = (Flipping) mutation;
                String probability = String.valueOf(flip.getProbability());
                String component   = String.valueOf(flip.getComponent());
                String maxTupples  = String.valueOf(flip.getMaxTupples());
                ProductFlipping flippingProduct = new ProductFlipping(probability,component,maxTupples);
                addToObservableListOfFlipping(flippingProduct);
                flippingTableView.setItems(observableListOfFlipping);
                flippingProbabilityTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
                flippingComponentTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
                flippingMaxTupplesTableCol.setCellFactory(TextFieldTableCell.forTableColumn());

                if ((m_BPIsPause.get())) {
                    flippingTableView.setEditable(true);
                    sizerTableView.setEditable(true);
                }
                else{
                    flippingTableView.setEditable(false);
                    sizerTableView.setEditable(false);

                }
                onEditingFlippingValues(flip);

            }

            else if(mutation.getClass().equals(Sizer.class))
            {
                Sizer sizer = (Sizer)mutation;
                String probability = String.valueOf(sizer.getProbability());
                String totalTupples   = String.valueOf(sizer.getTotalTupples());
                ProductSizer sizerProduct = new ProductSizer(probability,totalTupples);
                addToObservableListOfSizer(sizerProduct);
                sizerTableView.setItems(observableListOfSizer);
                sizerProbabilityTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
                sizerTotalTupplesTableCol.setCellFactory(TextFieldTableCell.forTableColumn());

                if ((m_BPIsPause.get())) {
                    flippingTableView.setEditable(true);
                    sizerTableView.setEditable(true);
                }
                else{
                    flippingTableView.setEditable(false);
                    sizerTableView.setEditable(false);

                }
                onEditingSizerValues(sizer);


            }

            //flippingTableView.editableProperty().bind(m_BPIsPause.not());
            //sizerProbabilityTableCol.onEditCommitProperty();

        }
    }

    private void onEditingSizerValues(Sizer sizer) {

        sizerProbabilityTableCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProductSizer, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ProductSizer, String> event) {
                try{
                    Double val = Double.parseDouble(event.getNewValue());
                    if(val<0 || val>1)
                    {
                        m_SPMessageToUser.set("probability is not vetween 0 and 1");
                    }
                    else
                    {
                        ProductSizer sizerProduct = event.getRowValue();
                        sizerProduct.setProbability(event.getNewValue());
                        sizer.setProbability(Double.parseDouble(sizerProduct.getProbability()));
                    }
                }
                catch (NumberFormatException e) {
                    m_SPMessageToUser.set("Probability must be an Integer");
                    // not an integer!
                }
            }
        });


        sizerTotalTupplesTableCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProductSizer, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ProductSizer, String> event) {
                String str = event.getNewValue();
                try {
                    int num = Integer.parseInt(str);
                    ProductSizer sizerProduct = event.getRowValue();
                    sizerProduct.setTotalTupples(event.getNewValue());
                    sizer.setTotalTupples(num);

                } catch (NumberFormatException e)
                {
                    m_SPMessageToUser.set("Total Tupples must be an Integer");
                    // not an integer!
                }
                //      ProductSizer sizerProduct = event.getRowValue();
                //    sizerProduct.setTotalTupples(event.getNewValue());
                //       sizer.setTotalTupples(Integer.parseInt(sizerProduct.getTotalTupples()));
            }
        });
    }

    private void onEditingFlippingValues(Flipping flip)
    {
        flippingProbabilityTableCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProductFlipping,String>>() {

            @Override public void handle(TableColumn.CellEditEvent<ProductFlipping, String> event) {
                try
                {
                    Double val = Double.parseDouble(event.getNewValue());
                    if(val<0 || val>1)
                    {
                        m_SPMessageToUser.set("probability is not between 0 and 1");
                    }
                    else
                    {
                        ProductFlipping flippingProduct = event.getRowValue();
                        flippingProduct.setProbability(event.getNewValue());
                        //validate prarameters
                        flip.setProbability(Double.parseDouble(flippingProduct.getProbability()));
                    }
                }
                catch (NumberFormatException e) {
                    m_SPMessageToUser.set("Probability must be an Integer");
                    // not an integer!
                }
            }
        });

        flippingComponentTableCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProductFlipping,String>>() {

            @Override public void handle(TableColumn.CellEditEvent<ProductFlipping, String> event) {
                String component = event.getNewValue();
                int size = component.length();
                Character c = component.charAt(0);
                if (size != 1) {
                    m_SPMessageToUser.set("please enter one character for component");
                }
                else if (c != 'D' || c != 'S' || c != 'T' || c != 'H' || c != 'C')
                {
                    m_SPMessageToUser.set("Component has to be 'D' 'H' 'T' 'S' or 'C'");
                }
                else
                {
                    ProductFlipping flippingProduct = event.getRowValue();
                    flippingProduct.setComponent(event.getNewValue());
                    //changeFlippingInEngine(flip);
                    flip.setComponent((flippingProduct.getComponent().charAt(0)));
                }

            }
        });

        flippingMaxTupplesTableCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProductFlipping,String>>() {

            @Override public void handle(TableColumn.CellEditEvent<ProductFlipping, String> event) {
                String str = event.getNewValue();
                try{
                    int num = Integer.parseInt(str);
                    ProductFlipping flippingProduct = event.getRowValue();
                    flippingProduct.setMaxTupples(event.getNewValue());
                    //changeFlippingInEngine(flip);
                    flip.setMaxTupples(num);
                    // is an integer!

                } catch (NumberFormatException e) {
                    m_SPMessageToUser.set("Max Tupples must be an Integer");
                    // not an integer!
                }
            }
        });
    }


    //  flippingProbabilityTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
      //  flippingComponentTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
      //  flippingMaxTupplesTableCol.setCellFactory(TextFieldTableCell.forTableColumn());

       // flippingMaxTupplesTableCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));


       // flippingMaxTupplesTableCol.setCellFactory(TextFieldTableCell.<Flipping, String>forTableColumn(new DefaultStringConverter()));



    private void changeFlippingInEngine(Flipping flipping)
    {
        Double probability = flipping.getProbability();
        Character component   = flipping.getComponent();
        Integer maxTupples  = flipping.getMaxTupples();

        for (Mutation mutation : ui.getEngine().getMutations())
        {
            if (mutation.getClass().equals(Flipping.class))
            {
                Flipping flipping1 = (Flipping) mutation;
                flipping1.setProbability(probability);
               // flipping1.setComponent();
            }
        }

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
        provideInfoAboutMutations();
        //engine
        m_SPSelectionInfo.set(ui.getEngine().getSelection().toString());
        m_SPCrossoverInfo.set(ui.getEngine().getCrossover().toString());
        sizeOfPopulationLabel.setText(((Integer)ui.getEngine().getSizeOfFirstPopulation()).toString());
        m_SPElitism.set(((Integer)ui.getEngine().getSelection().getSizeOfElitism()).toString());
    }

    private void provideInfoAboutRules() {
        StringBuilder sb =new StringBuilder();
        List<Rule> rules= ui.getTimeTable().getRules();
        int size = rules.size();
        for(int i=1;i<=size;i++)
        {
            sb.append("Rule ").append(i).append(": ");
            sb.append(System.lineSeparator());
            sb.append("Name: ").append(rules.get(i - 1).getClass().getSimpleName()).
                    append(", Strictness: ").append(rules.get(i - 1).getRuleWeight());
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
        for(int i=1;i<=size;i++)
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
        for(int i=1;i<=size;i++)
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
        for(int i=1;i<=size;i++)
        {
            sb.append("Teacher ").append(i).append(": ");
            sb.append(System.lineSeparator());
            Collections.sort(teachers.get(i-1).getIdOfSubjectsTeachable());
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
        fileChooserButton.setDisable(toDisable);
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
            if(!(numOfGenerationCheckBox.isSelected() || bestFitnessCheckBox.isSelected() ||
                    timerCheckBox.isSelected()))
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




    private void handleStopConditionBeforeRunning(MaxNumOfGenerationCondition maxNumOfGenerationCondition,
                                                  BestFitnessCondition bestFitnessCondition,
                                                  TimeCondition timeCondition)
    {
        try
        {
            if(numOfGenerationCheckBox.isSelected())
            {
                maxNumOfGenerationCondition.setMaxNumOfGeneration(Integer.parseInt(
                        numberOfGenerationTextField.getText()));
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

    private void handleStopConditionAfterRunning(MaxNumOfGenerationCondition maxNumOfGenerationCondition,
                                                 BestFitnessCondition bestFitnessCondition,
                                                 TimeCondition timeCondition) {
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

                    double progress4Generation = 0;
                    double progress4Fitness = 0;
                    double progress4Timer = 0;
                    double progress=0;

                    while (!ui.getEngine().isFinishToRun())
                    {
                        //if(!m_BPIsPause.get()) //isnot
                        {

                            if (numOfGenerationCheckBox.isSelected()) {
                                progress4Generation = (double) ui.getEngine().getNumOfGeneration() /
                                        Integer.parseInt(numberOfGenerationTextField.getText());
                            }
                            if (bestFitnessCheckBox.isSelected()) {
                                progress4Fitness = ui.getEngine().getBestFitnessInCurrentGeneration() /
                                        Double.parseDouble(bestFitnessTextField.getText());
                            }

                            if (timerCheckBox.isSelected()) {

                                progress4Timer = (double) timeCondition.getSecondLeft() /
                                        (60 * Integer.parseInt(timerTextField.getText()));
                            }

                           progress = Math.max(Math.max(progress4Generation, progress4Fitness),
                                   progress4Timer);
                            updateProgress(progress, 1);
                        }
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

    XYChart.Series<String, Number> series = new XYChart.Series<>();

    @FXML void OnFileChooser(ActionEvent event)
    {
            m_BPIsPause.set(false);
            fileChooserButton.setDisable(true);
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(null);
            if(selectedFile == null)
            {fileChooserButton.setDisable(false);
                return;}
            clearInfoFromUser();
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

                ui.getEngine().addListenerToUpdateGeneration((bestFitnessInCurrentGeneration,
                                                              numberOfGeneration) -> {

                     Platform.runLater(() -> {
                         if (!bestFitnessCheckBox.isSelected() ||
                                 !(bestFitnessInCurrentGeneration > Double.parseDouble(
                                         bestFitnessTextField.getText()))) {
                             m_DPCurrentFitness.set(Double.parseDouble(
                                     String.format("%.2f", bestFitnessInCurrentGeneration)));
                         }
                         updateOptimalByTeacher();
                         updateOptimalByGrade();

                         m_IPCurrentGeneration.set(numberOfGeneration);
                         provideInfoFromOptimalSolution((TimeTable) ui.getEngine().getOptimalSolution());
                         updatesTableView.getItems().add(new ProductUpdate(((Integer)numberOfGeneration).
                                 toString(), String.format("%.2f", bestFitnessInCurrentGeneration))  );
                         updatesTableView.scrollTo(updatesTableView.getItems().size());


                         series.getData().add(new XYChart.Data(((Integer)numberOfGeneration).toString(),
                                 Double.parseDouble(String.format("%.2f", bestFitnessInCurrentGeneration))));
                    });});

                provideInfoFromFile();

                //runProgressBar.progressProperty().unbind();
               // runProgressBar.progressProperty().bind(m_DPProgress);
               // m_DPProgress.set(ProgressBar.INDETERMINATE_PROGRESS);


                maxSizeChromosomes=ui.getTimeTable().getMaxSizeChromosomes();
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

            saveErrorMessage=m_SPMessageToUser.get();
            fileChooserButton.setDisable(false);
    }

    private void clearInfoFromUser() {
        m_SPMessageToUser.set("");
        //time table
        m_SPTeachersInfo.set("");
        m_SPSubjectsInfo.set("");
        m_SPGradesInfo.set("");
        m_SPRulesInfo.set("");
        teacherChoiceBox.getItems().clear();
        gradeChoiceBox.getItems().clear();
        teacherGridOptimalSolution.getChildren().clear();
        gradeGridOptimalSolution.getChildren().clear();
        updatesTableView.getItems().clear();
        rulesTableView.getItems().clear();
        series.getData().clear();
        hardRulesAvgLabel.setText("0.0");
        softRulesAvgLabel.setText("0.0");
        rowsTableView.getItems().clear();

        runProgressBar.progressProperty().unbind();
        runProgressBar.progressProperty().bind(m_DPProgress);
        m_DPProgress.set(ProgressBar.INDETERMINATE_PROGRESS);

        //engine
        if(ui.getEngine()!=null)
            ui.getEngine().clear();
        m_SPElitism.set("");
        sizeOfPopulationLabel.setText("");
        m_SPSelectionInfo.set("");
        m_SPCrossoverInfo.set("");
        //mutations
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
        else if(ui.getEngine()==null)
        {
            m_SPMessageToUser.set(saveErrorMessage);
            return;
        }
        else
        {
            if(handleParametersBeforeRunning()) {

                m_IPCurrentGeneration.set(0);
                m_DPCurrentFitness.set(0.0);
                handleStopConditionBeforeRunning(maxNumOfGenerationCondition, bestFitnessCondition,
                        timeCondition);
                ui.getEngine().setNumberOfGenerationForUpdate(Integer.parseInt(numOfGeneration4Update.
                        getText()));
                ui.getEngine().clear();
                animationForRunAlgorithmButton();
                handleControls2DisableBeforeRunning(true);
                updatesTableView.getItems().clear();
                series.getData().clear();

                ui.setTaskEngine(makeTaskForEngine());
                ui.setThreadEngine(new Thread(ui.getTaskEngine()));
                ui.getThreadEngine().setName("ThreadEngine");
                handleProgressBarBeforeRunning();
                ui.getThreadEngine().start();
            }
        }
    }

    private Task makeTaskForEngine()
    {
        return new Task() {
            @Override
            public Object call(){
                try {
                    ui.getEngine().run();
                }
                catch (Exception e)
                {
                    Platform.runLater(()->{
                        m_SPMessageToUser.set("Exception in engine: " + e.getMessage());});
                    this.setOnSucceeded(null);
                }
                return null;
            }

            private void finish()
            {
                handleControls2DisableBeforeRunning(false);
                handleStopConditionAfterRunning(maxNumOfGenerationCondition, bestFitnessCondition,
                        timeCondition);
            }

            @Override
            protected void succeeded() {
                finish();
                m_SPMessageToUser.set("The algorithm has run successfully");
            }

            @Override
            protected void running() {
                m_SPMessageToUser.set("Process...");
            }

            @Override
            protected void failed() {
                finish();
            }
        };
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
            {
                m_SPElitism.set(changeElitismTextField.getText());
                ui.getEngine().getSelection().setSizeOfElitism(newElitism);
                m_SPMessageToUser.set("Elitism was changed");
            }
            else m_SPMessageToUser.set("Elitism is bigger than size of population");
        }
    }


    @FXML void OnPauseResumeClick(ActionEvent event)
    {
        if(ui.getThreadEngine()!=null && ui.getThreadEngine().isAlive())
        {
            if(!m_BPIsPause.get())
            {
                ui.getEngine().pause();
                pauseResumeButton.setText("Resume");
                m_SPMessageToUser.set("Pause");
                m_BPIsPause.set(true);

            }
            else
            {
                ui.getEngine().resume();
                pauseResumeButton.setText("Pause");
                m_SPMessageToUser.set("Process...");
                m_BPIsPause.set(false);
            }
        }
        else
        {
            m_SPMessageToUser.set("Can't " + pauseResumeButton.getText() + " after or before running");
        }
    }


    @FXML void OnStopRunClick(ActionEvent event)
    {
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
            if(m_BPIsPause.get())
                {ui.getEngine().resume();}

            synchronized (ui.getThreadEngine())
            {  ui.getThreadEngine().interrupt();}

            //to check
            if(m_BPIsPause.get())
            {
                pauseResumeButton.setText("Pause");
                m_BPIsPause.set(true);
            }

            m_SPMessageToUser.set("The algorithm has stopped");
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
                tournament.setPTE(Double.parseDouble(pte));
                tournament.setConfiguration("PTE= " + pte);
                tournament.setSizeOfElitism(ui.getEngine().getSelection().getSizeOfElitism());
                ui.getEngine().setSelection(tournament);
                m_SPMessageToUser.set("The Selection technique has changed");
            }
            else m_SPMessageToUser.set("The parameter 'PTE' is empty");
        }

        m_SPSelectionInfo.set(ui.getEngine().getSelection().toString());
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

    @FXML void OnChangeCrossover(ActionEvent event)
    {
        String cuttingPoints = cuttingPointsTextField.getText();

        if(cuttingPoints == null || cuttingPoints.equals(""))
        {
            m_SPMessageToUser.set("The parameter 'cutting point' is empty");
            return;
        }

        int cuttingPointsInt = Integer.parseInt(cuttingPoints);

        if(dayTimeOrientedCheckBox.isSelected())
        {
            dayTimeOriented.setCuttingPoints(cuttingPointsInt);
            ui.getEngine().setCrossover(dayTimeOriented);
            m_SPMessageToUser.set("The Crossover mechanism has changed");
        }

        if(aspectOrientedCheckBox.isSelected())
        {
            aspectOriented.setCuttingPoints(cuttingPointsInt);

            if(orientedByClass.isSelected())
            {
                aspectOriented.setOrientedBy("CLASS");
            }
            else if(orientedByTeacher.isSelected())
            {
                aspectOriented.setOrientedBy("TEACHER");
            }
            else
            {
                m_SPMessageToUser.set("The parameter 'oriented' was not selected");
                return;
            }

            ui.getEngine().setCrossover(aspectOriented);
            m_SPMessageToUser.set("The Crossover mechanism has changed");
        }

        m_SPCrossoverInfo.set(ui.getEngine().getCrossover().toString());
    }

    @FXML private void OnDayTimeOrientedSelected(ActionEvent event)
    {
        dayTimeOrientedCheckBox.setSelected(dayTimeOrientedCheckBox.isSelected() &&
                !aspectOrientedCheckBox.isSelected());
    }

    @FXML private void OnAspectOrientedSelected(ActionEvent event)
    {
        aspectOrientedCheckBox.setSelected(aspectOrientedCheckBox.isSelected() &&
                !dayTimeOrientedCheckBox.isSelected());
    }

    @FXML private void OnOrientedByClassSelected(ActionEvent event)
    {
        orientedByClass.setSelected(orientedByClass.isSelected() &&
                !orientedByTeacher.isSelected());
    }

    @FXML private void OnOrientedByTeacherSelected(ActionEvent event)
    {
        orientedByTeacher.setSelected(orientedByTeacher.isSelected() &&
                !orientedByClass.isSelected());
    }

    @FXML private void OnClearGraph(ActionEvent event)
    {series.getData().clear();}

    @Override
    public void initialize(URL location1, ResourceBundle resources1) {
        location=location1;
        resources=resources1;
        myInitialize();
    }

    private void myInitialize()
    {
        messageToUserTextArea.textProperty().bind(m_SPMessageToUser);
        loadFileTextBox.textProperty().bind(m_SPNameLoadFile);
        generation4Update.textProperty().bind(m_IPCurrentGeneration.asString());
        fitness4Update.textProperty().bind(m_DPCurrentFitness.asString());
        teacherTextArea.textProperty().bind(m_SPTeachersInfo);
        subjectsTextArea.textProperty().bind(m_SPSubjectsInfo);
        gradesTexArea.textProperty().bind(m_SPGradesInfo);
        rulesTextArea.textProperty().bind(m_SPRulesInfo);
        selectionTextArea.textProperty().bind(m_SPSelectionInfo);
        changeElitismButton.disableProperty().bind(m_BPIsPause.not());
        changeElitismTextField.disableProperty().bind(m_BPIsPause.not());
        changeSelectionButton.disableProperty().bind(m_BPIsPause.not());
        crossoverTextArea.textProperty().bind(m_SPCrossoverInfo);
        changeCrossoverButton.disableProperty().bind(m_BPIsPause.not());
        elitismLabel.textProperty().bind(m_SPElitism);

        numberOfGenerationTextField.setDisable(true);
        bestFitnessTextField.setDisable(true);
        timerTextField.setDisable(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        LineChart.getData().add(series);
        LineChart.setLegendVisible(false);


        numberOfGenerationTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                numberOfGenerationTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            else if(newValue!=null && !newValue.equals(""))
            {
                int value=Integer.parseInt(newValue);
                if (value > 100000) {
                    numberOfGenerationTextField.setText(oldValue);
                }
            }
        });

        bestFitnessTextField.textProperty().addListener((observable, oldValue, newValue) -> {

                m_SPBestFitness.set(newValue);
                bestFitnessTextField.textProperty().bind(m_SPBestFitness);
                if (!newValue.matches("\\d*")) {
                    if (newValue.substring(0, newValue.length() - 1).contains(".") && newValue.endsWith(".")
                            || newValue.endsWith("100.") || newValue.startsWith(".")) {
                        newValue = oldValue;
                    }

                    m_SPBestFitness.set(newValue.replaceAll("[^\\d.]", ""));
                }
                else if(!newValue.equals("") && Double.parseDouble(newValue) > 100) {
                    m_SPBestFitness.set(oldValue);
                }
                bestFitnessTextField.textProperty().unbind();
        });

        PTETextfield.textProperty().addListener((observable, oldValue, newValue) -> {

            m_SPPTE.set(newValue);
                PTETextfield.textProperty().bind( m_SPPTE);
                if (!newValue.matches("\\d*")) {
                    if (newValue.substring(0, newValue.length() - 1).contains(".") && newValue.endsWith(".")
                    || newValue.endsWith("1.") || newValue.startsWith(".")) {
                        newValue = oldValue;
                    }
                    m_SPPTE.set(newValue.replaceAll("[^\\d.]", ""));
                }
                else if(!newValue.equals("") && Double.parseDouble(newValue) > 1) {
                    m_SPPTE.set(oldValue);
                }
                PTETextfield.textProperty().unbind();
        });

        timerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                timerTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            else if(newValue!=null && !newValue.equals(""))
            {
                int value=Integer.parseInt(newValue);
                if (value > 60) {
                    timerTextField.setText(oldValue);
                }
            }
        });

        numOfGeneration4Update.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                numOfGeneration4Update.setText(newValue.replaceAll("[^\\d]", ""));
            }
            else if(newValue!=null && !newValue.equals(""))
            {
                int value=Integer.parseInt(newValue);
                if (value > 100000) {
                    numOfGeneration4Update.setText(oldValue);
                }
            }
        });

        changeElitismTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                changeElitismTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            else if(ui.getEngine()!=null)
            {
                if(newValue!=null && !newValue.equals(""))
                {
                    int value=Integer.parseInt(newValue);
                    if (value > ui.getEngine().getSizeOfFirstPopulation()) {
                        changeElitismTextField.setText(oldValue);
                    }
                }
            }
        });

        topPercentTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                topPercentTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            else if(newValue!=null && !newValue.equals("")){
            int value=Integer.parseInt(newValue);
            if (value > 100 || value<=0) {
                topPercentTextField.setText(oldValue);
            }}
        });

        cuttingPointsTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cuttingPointsTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            else if(newValue!=null && !newValue.equals("")){
                int value=Integer.parseInt(newValue);
                if (value > maxSizeChromosomes || value<=0) {
                    cuttingPointsTextField.setText(oldValue);
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

        flippingProbabilityTableCol.setCellValueFactory(new PropertyValueFactory("Probability"));
        flippingComponentTableCol.setCellValueFactory(new PropertyValueFactory("Component"));
        flippingMaxTupplesTableCol.setCellValueFactory(new PropertyValueFactory("MaxTupples"));

        sizerProbabilityTableCol.setCellValueFactory(new PropertyValueFactory("Probability"));
        sizerTotalTupplesTableCol.setCellValueFactory(new PropertyValueFactory("TotalTupples"));

   //    flippingProbabilityTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //flippingComponentTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
       // flippingComponentTableCol.setCellFactory(TextFieldTableCell.forTableColumn());

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

    public class ProductSizer
    {
        StringProperty m_Probability;
        StringProperty m_TotalTupples;

        public ProductSizer(String probability, String totalTupples) {
            m_Probability = new SimpleStringProperty(probability);
            m_TotalTupples = new SimpleStringProperty(totalTupples);
        }

        public String getProbability() {
            return m_Probability.get();
        }

        public StringProperty m_ProbabilityProperty() {
            return m_Probability;
        }

        public String getTotalTupples() {
            return m_TotalTupples.get();
        }

        public StringProperty m_TotalTupplesProperty() {
            return m_TotalTupples;
        }

        public void setProbability(String m_Probability) {
            this.m_Probability.set(m_Probability);
        }

        public void setTotalTupples(String m_TotalTupples) {
            this.m_TotalTupples.set(m_TotalTupples);
        }
    }

    public class ProductFlipping
    {
        StringProperty m_Probability;
        StringProperty m_Component;
        StringProperty m_MaxTupples;

        public ProductFlipping(String probability, String component, String tupples)
        {
            m_Probability = new SimpleStringProperty(probability);
            m_Component   = new SimpleStringProperty(component);
            m_MaxTupples  = new SimpleStringProperty(tupples);
        }

        public String getProbability() { return m_Probability.get(); }

        public StringProperty m_ProbabilityProperty() { return m_Probability; }

        public String getComponent() { return m_Component.get(); }

        public StringProperty m_ComponentProperty() { return m_Component; }

        public String getMaxTupples() { return m_MaxTupples.get(); }

        public StringProperty m_MaxTupplesProperty() { return m_MaxTupples; }

        public void setProbability(String m_Probability) { this.m_Probability.set(m_Probability); }

        public void setComponent(String m_Component) { this.m_Component.set(m_Component); }

        public void setMaxTupples(String m_MaxTupples) { this.m_MaxTupples.set(m_MaxTupples); }
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