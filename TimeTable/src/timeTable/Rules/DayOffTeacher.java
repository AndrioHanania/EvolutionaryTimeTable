package timeTable.Rules;

import generated.ETTRule;
import timeTable.Teacher;
import timeTable.TimeTable;
import timeTable.chromosome.TimeTableChromosome;

import java.util.List;

public class DayOffTeacher extends Rule{

    //Contractors
    public DayOffTeacher(ETTRule ettRule) {
        super(ettRule);
    }

    public DayOffTeacher(Rule rule) {
        super(rule);
    }

    //Methods
    @Override
    public void Execute(TimeTable timeTable) {
        int numOfTeacher = timeTable.getTeachers().size();
        int rulePerTeacherScore = 100 / numOfTeacher;
        List<Teacher> teachers = timeTable.getTeachers();
        List<TimeTableChromosome> timeTableChromosomes = timeTable.getChromosomes();
        int daysToStudyInAWeek=timeTable.getDay();
        boolean[] days = new boolean[daysToStudyInAWeek];

        for(Teacher teacher : teachers)
        {
            for (int i=0;i<daysToStudyInAWeek;i++)
            {
                days[i]=false;
            }

            for (TimeTableChromosome timeTableChromosome : timeTableChromosomes)
            {
                if(timeTableChromosome.getTeacher().getIdNumber() == teacher.getIdNumber())
                {
                    days[timeTableChromosome.getDay() - 1] = true;
                    //speed things up
/*                    boolean flag = true;
                    for (Boolean val: days)
                    {
                        flag = val;
                        if(!flag)
                    }
 */
                }
            }

            boolean HasNoDayOff=true;
            for (int i=0;i<daysToStudyInAWeek;i++)
            {
                HasNoDayOff = HasNoDayOff && days[i];
                if(!HasNoDayOff){
                    break;
                }
            }

            if (HasNoDayOff){
                m_RuleGrade -= rulePerTeacherScore;
            }
        }
    }

    @Override
    public String toString() {
        return ("Name: DayOffTeacher, " + super.toString());
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
