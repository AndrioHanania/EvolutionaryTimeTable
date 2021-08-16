package timeTable.Rules;

import generated.ETTRule;
import timeTable.Grade;
import timeTable.TimeTable;
import timeTable.chromosome.TimeTableChromosome;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class Satisfactory extends Rule {

    //Contractors
    public Satisfactory(ETTRule ettRule) {
        super(ettRule);
    }

    public Satisfactory(Rule rule) {
        super(rule);
    }

    //Methods
    public void Execute(TimeTable timeTable)
    {
        List<Grade> grades = timeTable.getGrades();
        List<TimeTableChromosome> timeTableChromosomeList = timeTable.getChromosomes();
        int rulePerGradeScore = 100 / grades.size();
        //assuming the number of subjects equals the largest id of them
        int sizeOfHourInWeekForSubjects = timeTable.getSubjects().size();
        Integer[] data = new Integer[sizeOfHourInWeekForSubjects];
        List<Integer> HourInWeekForSubjects = Arrays.asList(data);
        for(Grade grade : grades)
        {
            Arrays.fill(data,new Integer(0));
            for (TimeTableChromosome timeTableChromosome : timeTableChromosomeList)
            {
                if(timeTableChromosome.getGrade().equals(grade))
                {
                    int index=timeTableChromosome.getSubject().getIdNumber()-1;
                    int value=HourInWeekForSubjects.get(timeTableChromosome.getSubject().getIdNumber()-1)+1;
                    HourInWeekForSubjects.set(index, value);
                }
            }
            Map<Integer,Integer> map = grade.getMapIdSubjectToHoursInWeek();
            for(Map.Entry<Integer,Integer> entry : map.entrySet())
            {
                if(!HourInWeekForSubjects.get(entry.getKey()-1).equals(entry.getValue())){
                    m_RuleGrade -= rulePerGradeScore;
                    break;
                }
            }
        }
    }

    @Override
    public String toString() {
        return ( "Name: Satisfactory, " + super.toString());
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
