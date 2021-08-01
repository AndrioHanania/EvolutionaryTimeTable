package timeTable;

import engine.Chromosome;
import engine.Solution;
import engine.Problem;
import generated.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class TimeTable extends Solution implements Problem
{
    //Members
    private List<TimeTableChromosome> m_Chromosomes;
    private List<Teacher> m_Teachers;
    private List<Subject> m_Subjects;
    private  List<Grade> m_Grades;
    private int m_DaysForStudy;
    private int m_HourStudyForDay;
    //rules
    private List<Rule> m_Rules;

    //Constructors
    public TimeTable()
    {
        m_Chromosomes = new ArrayList<>();
        m_Teachers = new ArrayList<>();
        m_Subjects = new ArrayList<>();
        m_Grades =  new ArrayList<>();

    }

    public TimeTable(ETTTimeTable eTTTimeTable)
    {
        m_DaysForStudy = eTTTimeTable.getDays();
        m_HourStudyForDay = eTTTimeTable.getHours();
        initializeSubjects(eTTTimeTable);
        initializeclasses(eTTTimeTable);
        initializeTeachers(eTTTimeTable);
        initializeRules(eTTTimeTable);
    }

    //Methods

    private void initializeSubjects(ETTTimeTable eTTTimeTable)
    {
        m_Subjects = new ArrayList<>();
        List<ETTSubject> listETTSubject = eTTTimeTable.getETTSubjects().getETTSubject();
        for(ETTSubject eTTSubject : listETTSubject) {
            m_Subjects.add(new Subject(eTTSubject));
        }
        m_Subjects.sort(Comparator.comparingInt(Subject::getIdNumber));
    }

    private void initializeclasses(ETTTimeTable eTTTimeTable){
        m_Grades = new ArrayList<>();
        List<ETTClass> listETTClass = eTTTimeTable.getETTClasses().getETTClass();
        for(ETTClass eTTClass : listETTClass) {
            m_Grades.add(new Grade(eTTClass));
        }
        m_Grades.sort(Comparator.comparingInt(Grade::getIdNumber));
    }

    private void initializeTeachers(ETTTimeTable eTTTimeTable){
        m_Teachers = new ArrayList<>();
        List<ETTTeacher> listETTTeacher = eTTTimeTable.getETTTeachers().getETTTeacher(); //why get teacher at the end?
        for(ETTTeacher eTTTeacher : listETTTeacher) {
            m_Teachers.add(new Teacher(eTTTeacher));
        }
        m_Teachers.sort(Comparator.comparingInt(Teacher::getIdNumber));
    }

    private  void initializeRules(ETTTimeTable ettTimeTable)
    {
        m_Rules = new ArrayList<>();
        List<ETTRule> listETTRules = ettTimeTable.getETTRules().getETTRule();
        for (ETTRule ettRule : listETTRules)
        {
            m_Rules.add(new Rule((ettRule)));
        }
       // m_Rules.sort((Comparator.comparingInt(Rule::get))); //compared by rule strickness
    }
    public List<Subject> getSubjects(){return m_Subjects;}



    public String toString(){
        StringBuilder settings = new StringBuilder();
      ///  settings.append("Time table: ");
      //  settings.append(System.lineSeparator());
        //settings.append("Subjects: ");
       // settings.append(System.lineSeparator());
    //    settings.append("Rules: ");
    //   settings.append(System.lineSeparator());
        for(Subject subject : m_Subjects)
        {
            settings.append(subject);
            settings.append(System.lineSeparator());
        }
        settings.append("Teachers: ");
        settings.append(System.lineSeparator());
        for(Teacher teacher : m_Teachers)
        {
            settings.append(teacher);
            settings.append(System.lineSeparator());
        }
        settings.append("Classes: ");
        settings.append(System.lineSeparator());
        for(Grade clazz : m_Grades)
        {
            settings.append(clazz);
            settings.append(System.lineSeparator());
        }
        //rules
        settings.append("Rules: ");
        settings.append(System.lineSeparator());
        for (Rule rule : m_Rules)
        {
            settings.append(rule);
            settings.append(System.lineSeparator());
        }
        settings.append(System.lineSeparator());
        return settings.toString();
    }


    @Override
    public void calculateFitness() {

    }




    public void randomizeAttributes(TimeTable timeTable)
    {
        Random random = new Random();
        int numOfChromosome = random.nextInt(m_DaysForStudy * m_HourStudyForDay * Math.max(m_Teachers.size(),m_Subjects.size()) +1);
        for(int i=0;i<numOfChromosome;i++)
        {
            timeTable.m_Chromosomes.add((TimeTableChromosome) newRandomChromosome());
        }
    }
    @Override
    public TimeTable newRandomInstance() {

        TimeTable timeTable = new TimeTable();
        timeTable.randomizeAttributes(timeTable);
        timeTable.m_Subjects = this.m_Subjects;
        timeTable.m_Grades = this.m_Grades;
        timeTable.m_Teachers = this.m_Teachers;
        timeTable.m_DaysForStudy =this.m_DaysForStudy;
        timeTable.m_HourStudyForDay = this.m_HourStudyForDay;
        return timeTable;
    }

    @Override
    public Chromosome newRandomChromosome() {
        Random random = new Random();
        Teacher randomTeacher = m_Teachers.get(random.nextInt(m_Teachers.size()));
        int randomIdSubject = randomTeacher.getIdOSubjectsTeachable().get(random.nextInt(randomTeacher.getIdOSubjectsTeachable().size()));
        Subject randomSubject = null;
        for(Subject subject : m_Subjects)
        {
            if(subject.getIdNumber() == randomIdSubject)
            {
                randomSubject = subject;
                break;
            }
        }
        Grade clazz= null;
        for(Grade Class : m_Grades)
        {
            if( Class.getMapIdSubjectToHoursInWeek().containsKey(randomIdSubject))
            {
                clazz = Class;
                break;
            }
        }
        int randomHour = random.nextInt(m_HourStudyForDay + 1) + 8;
        int randomDay = random.nextInt(m_DaysForStudy) + 1;
        return new TimeTableChromosome(randomDay, randomHour, clazz, randomTeacher, randomSubject);
    }


    public List<TimeTableChromosome> getChromosomes(){return m_Chromosomes;}

    public int getDay() {return m_DaysForStudy;
    }

    public List<Teacher> getTeachers() { return m_Teachers;
    }

    public List<Grade> getGrades() {return m_Grades;
    }

    public int getHour() { return m_HourStudyForDay;
    }
}