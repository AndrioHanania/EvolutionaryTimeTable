package timeTable;

import engine.Solution;
import engine.Problem;
import generated.ETTClass;
import generated.ETTSubject;
import generated.ETTTeacher;
import generated.ETTTimeTable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class TimeTable extends Solution implements Problem<TimeTable>
{
    //htrsfhd
    //asdfghjk
    //Members
    private List<TimeTableChromosome> m_Chromosomes;
    private List<Teacher> m_Teachers;
    private List<Subject> m_Subjects;
    private  List<Class> m_Classes;
    private int m_DaysForStudy;
    private int m_HourStudyForDay;
    //rules

    //Constructors
    public TimeTable()
    {
        m_Chromosomes = new ArrayList<>();
        m_Teachers = new ArrayList<>();
        m_Subjects = new ArrayList<>();
        m_Classes =  new ArrayList<>();

    }

    public TimeTable(ETTTimeTable eTTTimeTable)
    {
        m_DaysForStudy = eTTTimeTable.getDays();
        m_HourStudyForDay = eTTTimeTable.getHours();
        initializeSubjects(eTTTimeTable);
        initializeclasses(eTTTimeTable);
        initializeTeachers(eTTTimeTable);
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
        m_Classes = new ArrayList<>();
        List<ETTClass> listETTClass = eTTTimeTable.getETTClasses().getETTClass();
        for(ETTClass eTTClass : listETTClass) {
            m_Classes.add(new Class(eTTClass));
        }
        m_Classes.sort(Comparator.comparingInt(Class::getIdNumber));
    }

    private void initializeTeachers(ETTTimeTable eTTTimeTable){
        m_Teachers = new ArrayList<>();
        List<ETTTeacher> listETTTeacher = eTTTimeTable.getETTTeachers().getETTTeacher();
        for(ETTTeacher eTTTeacher : listETTTeacher) {
            m_Teachers.add(new Teacher(eTTTeacher));
        }
        m_Teachers.sort(Comparator.comparingInt(Teacher::getIdNumber));
    }

    public List<Subject> getSubjects(){return m_Subjects;}



    public String toString(){
        StringBuilder settings = new StringBuilder();
        settings.append("Time table:");
        settings.append(System.lineSeparator());
        settings.append("Subjects:");
        settings.append(System.lineSeparator());
        for(Subject subject : m_Subjects)
        {
            settings.append(subject);
        }
        settings.append("Teachers:");
        settings.append(System.lineSeparator());
        for(Teacher teacher : m_Teachers)
        {
            settings.append(teacher);
        }
        settings.append("Classes:");
        settings.append(System.lineSeparator());
        for(Class clazz : m_Classes)
        {
            settings.append(clazz);
        }
        //rules
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
            Class clazz= null;
            for(Class Class : m_Classes)
            {
                if( Class.getMapIdSubjectToHoursInWeek().containsKey(randomIdSubject))
                {
                    clazz = Class;
                    break;
                }
            }
            int randomHour = random.nextInt(m_HourStudyForDay + 1) + 8;
            int randomDay = random.nextInt(m_DaysForStudy) + 1;
            timeTable.m_Chromosomes.add(new TimeTableChromosome(randomDay, randomHour, clazz, randomTeacher, randomSubject));
        }
    }
    @Override
    public TimeTable newRandomInstance() {

        TimeTable timeTable = new TimeTable();
        timeTable.randomizeAttributes(timeTable);
        timeTable.m_Subjects = this.m_Subjects;
        timeTable.m_Classes = this.m_Classes;
        timeTable.m_Teachers = this.m_Teachers;
        timeTable.m_DaysForStudy =this.m_DaysForStudy;
        timeTable.m_HourStudyForDay = this.m_HourStudyForDay;
        return timeTable;
    }
}
