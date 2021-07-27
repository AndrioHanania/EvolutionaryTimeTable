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

public class TimeTable extends Solution implements Problem<TimeTable>
{
    //Members
    private List<TimeTableChromosome> m_Chromosomes;
    private List<Teacher> m_Teachers;
    private List<Subject> m_Subjects;
    private  List<Class> m_classes;
    private int m_DaysForStudy;
    private int m_HourStudyForDay;
    //rules

    //Constructors
    public TimeTable()
    {
        List<TimeTableChromosome> m_Chromosomes = new ArrayList<>();
        List<Teacher> m_Teachers = new ArrayList<>();
        List<Subject> m_Subjects = new ArrayList<>();
        List<Class> m_classes =  new ArrayList<>();
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
        m_classes = new ArrayList<>();
        List<ETTClass> listETTClass = eTTTimeTable.getETTClasses().getETTClass();
        for(ETTClass eTTClass : listETTClass) {
            m_classes.add(new Class(eTTClass));
        }
        m_classes.sort(Comparator.comparingInt(Class::getIdNumber));
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
        for(Class clazz : m_classes)
        {
            settings.append(clazz);
        }
        //rules
        return settings.toString();
    }


    @Override
    public void calculateFitness() {

    }

    @Override
    public TimeTable newRandomInstance() {
        return null;
    }
}
