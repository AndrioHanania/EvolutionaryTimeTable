package timeTable.Rules;

import generated.ETTRule;
import timeTable.Grade;
import timeTable.Subject;
import timeTable.TimeTable;
import timeTable.chromosome.TimeTableChromosome;

import java.util.List;

public class Sequentiality extends Rule{

    int m_TotalHours;


    public Sequentiality(ETTRule ettRule, int hours) {
        super(ettRule);
        m_TotalHours = hours;
    }

    public Sequentiality(Rule rule) {
        super(rule);
        String[] conf = rule.m_Confg.split("=");
        m_TotalHours = Integer.parseInt(conf[1]);
    }


    @Override
    public void Execute(TimeTable timeTable) {
        List<Grade> grades = timeTable.getGrades();
        List<Subject> subjects = timeTable.getSubjects();
        int days=timeTable.getDay();
        List<TimeTableChromosome> timeTableChromosomes = timeTable.getChromosomes();
        int hours = timeTable.getHour();
        boolean[] hoursForSubject = new boolean[hours];
        double scorePerGrade = 100/grades.size();
        double scorePerSubject = scorePerGrade / subjects.size();
        double scorePerDay = scorePerSubject / days;

        for (Grade grade : grades)
        {
            for (Subject subject : subjects)
            {
                //changed from int day=1 to int day=0
                for(int day = 1;day <= days;day++)
                {
                    for (int i=1;i<=hours;i++)
                    {
                        //IsSubjectTeachedInHouri
                        hoursForSubject[i-1]=false;
                    }

                    //check if the class is studying this subject more then rule defines in a raw
                    for(int k=0; k<hours;k++)
                    {
                        for (TimeTableChromosome timeTableChromosome : timeTableChromosomes) {
                            Grade grade1 = timeTableChromosome.getGrade();
                            Subject subject1 = timeTableChromosome.getSubject();
                            int day1 = timeTableChromosome.getDay();

                            if (grade1.equals(grade) && subject1.equals(subject) && day1 == day) {
                                hoursForSubject[k] = true;
                                break;
                            }
                        }
                    }
                    int countHoursInRaw=0;
                    for (Boolean Studying:hoursForSubject)
                    {
                        if(Studying)
                        {
                            countHoursInRaw++;
                            if (countHoursInRaw >= m_TotalHours)
                            {
                                //rule faild
                                m_RuleGrade -= scorePerDay;
                                break;
                            }
                        }
                        else //not a raw anymore
                        {
                            countHoursInRaw=0;
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return ("Name: Sequentiality, " + super.toString());
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
