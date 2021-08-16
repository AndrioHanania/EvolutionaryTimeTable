package timeTable;

import engine.Parse;
import timeTable.Rules.Rule;
import timeTable.Rules.RuleUtils;
import timeTable.chromosome.Chromosome;
import engine.Solution;
import engine.Problem;
import generated.*;
import timeTable.chromosome.TimeTableChromosome;

import java.util.*;

public class TimeTable extends Solution implements Problem
{
    //Members
    private List<TimeTableChromosome> m_Chromosomes;
    private List<Teacher> m_Teachers;
    private List<Subject> m_Subjects;
    private  List<Grade> m_Grades;
    private int m_DaysForStudy;
    private int m_HourStudyForDay;
    private List<Rule> m_Rules;
    private TimeTableParse m_Parse;
    private  int m_HardRulesWeight;
    private double m_SoftRulesAvg = 0;
    private double m_HardRulesAvg = 0;
    private  int m_SumMinRequirementsGrade=0;

    //Constructors
    public TimeTable()
    {
        m_Chromosomes = new ArrayList<>();
        m_Teachers = new ArrayList<>();
        m_Subjects = new ArrayList<>();
        m_Grades =  new ArrayList<>();
        m_Rules =  new ArrayList<>();
    }

    public TimeTable(TimeTable timeTable)
    {
        m_Chromosomes = timeTable.m_Chromosomes;
        m_Teachers = timeTable.m_Teachers;
        m_Subjects = timeTable.m_Subjects;
        m_Grades =  timeTable.m_Grades;
        m_DaysForStudy = timeTable.m_DaysForStudy;
        m_HourStudyForDay = timeTable.m_HourStudyForDay;
    }

    //Methods
    public void initializeSubjects(ETTTimeTable eTTTimeTable) throws Exception {
        m_Subjects = new ArrayList<>();
        List<ETTSubject> listETTSubject = eTTTimeTable.getETTSubjects().getETTSubject();
        for(ETTSubject eTTSubject : listETTSubject)
        {
            m_Subjects.add(new Subject(eTTSubject));
        }
        m_Subjects.sort(Comparator.comparingInt(Subject::getIdNumber));
        for(int i=0;i<m_Subjects.size();i++)
        {
            if(m_Subjects.get(i).getIdNumber() != i+1)
            {
                throw new Exception("Subjects' IDs don't make a running story");
            }
        }
    }

    public void initializeclasses(ETTTimeTable eTTTimeTable) throws Exception {
        m_Grades = new ArrayList<>();
        List<ETTClass> listETTClass = eTTTimeTable.getETTClasses().getETTClass();
        for(ETTClass eTTClass : listETTClass)
        {
            Grade grade =new Grade(eTTClass);
            boolean isGradeValid;
            for (Map.Entry<Integer,Integer> idSubject : grade.getMapIdSubjectToHoursInWeek().entrySet())
            {
                isGradeValid=false;
                for (int i =0;i<m_Subjects.size();i++)
                {
                    if(m_Subjects.get(i).getIdNumber() == idSubject.getKey())
                    {
                        isGradeValid=true;
                        break;
                    }
                }
                if(!isGradeValid){
                    throw new Exception("Error with grade: " + grade.getName() + " - learning subject with ID: " + idSubject.getKey() + " that does not exist");
                }
            }

            int sumRequirementsGrade=0;
            for (Map.Entry<Integer, Integer> entry : grade.getMapIdSubjectToHoursInWeek().entrySet())
            {
                sumRequirementsGrade +=entry.getValue();
            }

            if(sumRequirementsGrade > m_DaysForStudy * m_HourStudyForDay)
            {
                throw new Exception("The total study hours for all subjects in the class: " + grade.getName() +" exceed D*H");
            }

            m_Grades.add(grade);
        }
        m_Grades.sort(Comparator.comparingInt(Grade::getIdNumber));
        for(int i=0;i<m_Grades.size();i++)
        {
            if(m_Grades.get(i).getIdNumber() != i+1)
            {
                throw new Exception("Grades' IDs don't make a running story");
            }
        }


        for (Grade grade : m_Grades)
        {
            for (Map.Entry<Integer, Integer> entry : grade.getMapIdSubjectToHoursInWeek().entrySet())
            {
                m_SumMinRequirementsGrade += entry.getValue();
            }
        }

    }

    public void initializeTeachers(ETTTimeTable eTTTimeTable) throws Exception {
        m_Teachers = new ArrayList<>();
        List<ETTTeacher> listETTTeacher = eTTTimeTable.getETTTeachers().getETTTeacher();
        for(ETTTeacher eTTTeacher : listETTTeacher)
        {
            Teacher teacher =new Teacher(eTTTeacher);
            boolean isTeacherValid;
            for (int idSubject : teacher.getIdOfSubjectsTeachable())
            {
                isTeacherValid=false;
                for (int i =0;i<m_Subjects.size();i++)
                {
                    if(m_Subjects.get(i).getIdNumber() == idSubject)
                    {
                        isTeacherValid=true;
                        break;
                    }
                }
                if(!isTeacherValid){
                    throw new Exception("Error with teacher " + teacher.getName() + ": teaching subject " + idSubject + " that does not exist");
                }
            }

            m_Teachers.add(teacher);
        }
        m_Teachers.sort(Comparator.comparingInt(Teacher::getIdNumber));
        for(int i=0;i<m_Teachers.size();i++)
        {
            if(m_Teachers.get(i).getIdNumber() != i+1)
            {
                throw new Exception("Teachers' IDs don't make a running story");
            }
        }
    }

    public void initializeRules(ETTTimeTable ettTimeTable) throws Exception {
        m_Rules = new ArrayList<>();
        m_HardRulesWeight=ettTimeTable.getETTRules().getHardRulesWeight();
        List<ETTRule> listETTRules = ettTimeTable.getETTRules().getETTRule();
        for (ETTRule ettRule : listETTRules)
        {
            Rule rule = m_Parse.parseRules(ettRule);
            if(m_Rules.contains(rule))
            {
                throw new Exception("Error with the rule: " + ettRule.getETTRuleId() + ": appears more than once");
            }
            else
            {
                m_Rules.add(rule);
            }
        }
    }


    public String toString(){
        StringBuilder settings = new StringBuilder();
        settings.append("Time table: ");
        settings.append(System.lineSeparator());
        settings.append("Subjects: ");
        settings.append(System.lineSeparator());
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
        settings.append("Rules: ");
        settings.append(System.lineSeparator());
        for (Rule rule : m_Rules)
        {
            settings.append("Name: " + rule.getClass().getSimpleName() + ", Strictness: " + rule.getRuleWeight());
            settings.append(System.lineSeparator());
        }
        settings.append(System.lineSeparator());
        return settings.toString();
    }

    @Override
    public void calculateFitness()
    {
        int softRulesCount=0,hardRulesCount=0;
        int softRulesSum=0,hardRulesSum=0;
        for(Rule rule : m_Rules)
        {
           rule.Execute(this);
        }
        for(Rule rule : m_Rules)
        {
            if(rule.getRuleWeight().equals("Hard"))
            {
                hardRulesCount++;
                hardRulesSum += rule.getGrade();
            }
            else if(rule.getRuleWeight().equals("Soft"))
            {
                softRulesCount++;
                softRulesSum+=rule.getGrade();
            }
        }
        m_HardRulesAvg = (double)hardRulesSum / hardRulesCount;
        m_SoftRulesAvg = (double)softRulesSum / softRulesCount;
        m_Fitness = (m_HardRulesAvg * ( (double)m_HardRulesWeight / 100)) + (m_SoftRulesAvg * ( (double)(100 - m_HardRulesWeight) / 100));
    }

    public void randomizeAttributes(TimeTable timeTable)
    {
        Random random = new Random();
        int D=timeTable.getDay();
        int H=timeTable.getHour();
        int T=timeTable.getTeachers().size();
        int G=timeTable.getGrades().size();
        int S=timeTable.getSubjects().size();
        int numOfChromosome = random.nextInt(D * H * T * G * S - m_SumMinRequirementsGrade +1)+m_SumMinRequirementsGrade;
        for(int i=0;i<numOfChromosome;i++)
        {
            m_Chromosomes.add((TimeTableChromosome)timeTable.newRandomChromosome());
        }
    }

    @Override
    public TimeTable newRandomInstance(){

        TimeTable timeTable = new TimeTable();
        timeTable.m_Subjects = this.m_Subjects;
        timeTable.m_Grades = this.m_Grades;
        timeTable.m_Teachers = this.m_Teachers;
        timeTable.m_DaysForStudy =this.m_DaysForStudy;
        timeTable.m_HourStudyForDay = this.m_HourStudyForDay;
        timeTable.m_HardRulesWeight = this.m_HardRulesWeight;
        timeTable.m_SumMinRequirementsGrade = this.m_SumMinRequirementsGrade;
        for (Rule rule : this.m_Rules)
         {
             timeTable.m_Rules.add(RuleUtils.CreateRule(rule));
         }
        timeTable.randomizeAttributes(this);
        return timeTable;
    }

    public Chromosome newRandomChromosome()
    {
        Random random = new Random();
        Grade randomGrade=null;
        Subject randomSubject = null;
        Teacher randomTeacher = m_Teachers.get(random.nextInt(m_Teachers.size()));
        int randomIdSubject = randomTeacher.getIdOfSubjectsTeachable().get(random.nextInt(randomTeacher.getIdOfSubjectsTeachable().size()));
        while (true)
        {
            randomSubject = m_Subjects.get(random.nextInt(m_Subjects.size()));
            if(randomSubject.getIdNumber() == randomIdSubject)
            {
                break;
            }
        }
        while(true)
        {
            randomGrade = m_Grades.get(random.nextInt(m_Grades.size()));
            if(randomGrade.getMapIdSubjectToHoursInWeek().containsKey(randomIdSubject))
            {
                break;
            }
        }
        int randomHour = random.nextInt(m_HourStudyForDay ) + 1;
        int randomDay = random.nextInt(m_DaysForStudy) + 1;
        return new TimeTableChromosome(randomDay, randomHour, randomGrade, randomTeacher, randomSubject);
    }

    //Getters
    public List<TimeTableChromosome> getChromosomes(){return m_Chromosomes;}

    public int getDay() {return m_DaysForStudy;}

    public List<Teacher> getTeachers() { return m_Teachers;}

    public List<Grade> getGrades() {return m_Grades;}

    public int getHour() { return m_HourStudyForDay;}

    public List<Subject> getSubjects(){return m_Subjects;}

    public List<Rule> getRules(){return m_Rules;}

    public double getHardRulesAvg(){return m_HardRulesAvg;}

    public double getSoftRulesAvg(){return m_SoftRulesAvg;}

    //Setters
    public void setHourStudyForDay(int hour){m_HourStudyForDay = hour;}

    public void setDaysForStudy(int day){m_DaysForStudy = day;}

    public void setTeachers(List<Teacher> teachers){m_Teachers = teachers;}

    public void setSubjects(List<Subject> subjects){ m_Subjects = subjects;}

    public void setGrades(List<Grade> grades){ m_Grades = grades;}

    public void setRules(List<Rule> rules) { m_Rules = rules;
        for (Rule rule : m_Rules)
        {
            rule.setGrade(100);
        }
    }

    public void setParse(Parse parse)
    {
        m_Parse = (TimeTableParse) parse;
    }

    public  void setHardRulesWeight(int hardRulesWeight)
    {
        m_HardRulesWeight = hardRulesWeight;
    }

    public int getHardRulesWeight(){return m_HardRulesWeight;}

    public void setSumMinRequirementsGrade(int requirementsGrade){m_SumMinRequirementsGrade=requirementsGrade;}

    public int getSumMinRequirementsGrade(){ return m_SumMinRequirementsGrade;}


}