package timeTable.Rules;

import generated.ETTRule;
import timeTable.Grade;
import timeTable.Subject;
import timeTable.TimeTable;
import timeTable.chromosome.TimeTableChromosome;

import java.util.Arrays;
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

    /*
        תיאור: מקצוע לא נלמד יותר מכמות שעות רצוף. כמות השעות תינתן כפרמטר חיצוני.
    חישוב רצף השעות כמובן מדבר במסגרת יום מסויים (ולא חוצה ימים).
    האמור מדבר כמובן על היביט הכיתה. (אם מורה מלמד ספרות 7 שעות רצוף ביום – זה בסדר).
    פרמטרים: TotalHours - כמות השעות שאין ללמד את המקצוע בצורה רצופה יותר ממנה. מספר חיובי שלם.

        * */
    @Override
    public void Execute(TimeTable timeTable) {
        List<Grade> grades = timeTable.getGrades();
        List<Subject> subjects = timeTable.getSubjects();
        int days=timeTable.getDay();
        List<TimeTableChromosome> timeTableChromosomes = timeTable.getChromosomes();
        int hours = timeTable.getHour();
        boolean[] hoursForSubject = new boolean[hours];
        boolean isRulePassedPerGrade = true;
        boolean isRulePassedPerSubject=true;
        boolean isRulePassedPerDay=true;


        double scorePerGrade = 100/grades.size();
        for (Grade grade : grades)
        {
            isRulePassedPerGrade = true;
            double scorePerSubject = scorePerGrade / subjects.size();
            for (Subject subject : subjects)
            {
                isRulePassedPerSubject=true;
                double scorePerDay = scorePerSubject / days;
                //changed from int day=1 to int day=0
                for(int day = 0;day < days;day++)
                {
                    isRulePassedPerDay=true;
                    for (int i=0;i<hours;i++)
                    {
                        //IsSubjectTeachedInHouri
                        hoursForSubject[i]=false;
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
                                isRulePassedPerDay = false;
                                break;
                            }
                        }
                        else //not a raw anymore
                        {
                            countHoursInRaw=0;
                        }

                    }

                    if(!isRulePassedPerDay){
                        break;}
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
