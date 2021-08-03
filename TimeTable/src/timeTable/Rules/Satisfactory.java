package timeTable.Rules;

import timeTable.Grade;
import timeTable.TimeTable;
import timeTable.chromosome.TimeTableChromosome;

import java.util.ArrayList;
import java.util.List;


public class Satisfactory extends Rule {

    public void Execute(TimeTable timeTable)
    {
        boolean isPassRule = true;
        List<Grade> grades = timeTable.getGrades();
        List<TimeTableChromosome> timeTableChromosomeList = timeTable.getChromosomes();
        for(Grade grade : grades)
        {
            List<Integer> HourInWeekForSubjects = new ArrayList<>(grade.getMapIdSubjectToHoursInWeek().size());
            for(Integer value : HourInWeekForSubjects){
                value = 0;
            }
            for (TimeTableChromosome timeTableChromosome : timeTableChromosomeList)
            {
                if(timeTableChromosome.getGrade().equals(grade))
                {
                    HourInWeekForSubjects.set(timeTableChromosome.getSubject().getIdNumber(), HourInWeekForSubjects.get(timeTableChromosome.getSubject().getIdNumber())+1);
                }

            }
            for(int id=0;id<HourInWeekForSubjects.size();id++){
                if(HourInWeekForSubjects.get(id) != grade.getMapIdSubjectToHoursInWeek().get(id)){
                    isPassRule = false;
                    break;
                }
            }
            if(!isPassRule){break;}
        }
        RuleUtils.evaluteGrade(isPassRule, this);
        timeTable.setFitness(this.m_RuleGrade);
    }


    @Override
    public String toString() {
        return (super.toString() + "Name: Satisfactory");
    }
}
