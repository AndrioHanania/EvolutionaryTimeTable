package timeTable.Rules;

import generated.ETTRule;
import timeTable.Grade;
import timeTable.Subject;
import timeTable.TimeTable;
import timeTable.chromosome.TimeTableChromosome;

import java.util.List;

public class Sequentiality extends Rule{

    int m_TotalHours;

    public Sequentiality(ETTRule ettRule) {
        super(ettRule);
    }

    public Sequentiality(Rule rule) {
        super(rule);
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
        int rulePerSubjectScore = 100 / subjects.size();
        int hours = timeTable.getHour();
        boolean[] hoursForSubject = new boolean[hours];
        boolean isValid;

        for (Subject subject : subjects)
        {

            for (Grade grade : grades)
            {

                for(int day = 1;day < days;day++)
                {

                    for (int i=0;i<hours;i++)
                    {
                        hoursForSubject[i]=false;
                    }

                    for (TimeTableChromosome timeTableChromosome: timeTableChromosomes)
                    {
                        Grade grade1 = timeTableChromosome.getGrade();
                        Subject subject1 = timeTableChromosome.getSubject();
                        int day1 = timeTableChromosome.getDay();

                        if(grade1.equals(grade) && subject1.equals(subject) && day1 == day)
                        {



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
