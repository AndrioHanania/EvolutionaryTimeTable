package timeTable.Rules;

import timeTable.*;

public class Singularity extends Rule {
    @Override
    public void Execute(TimeTable timeTable) {
        boolean isPassRule =true;
        for(int i=0; i<timeTable.getChromosomes().size()-1;i++)
        {
            Teacher teacher1 =  timeTable.getChromosomes().get(i).getTeacher();
            int day1 = timeTable.getChromosomes().get(i).getDay();
            int hour1 = timeTable.getChromosomes().get(i).getHour();
            Grade grade1 = timeTable.getChromosomes().get(i).getGrade();
            Subject subject1 = timeTable.getChromosomes().get(i).getSubject();

            for (int j=i+1;j<timeTable.getChromosomes().size();j++) {
                Teacher teacher2 = timeTable.getChromosomes().get(i).getTeacher();
                int day2 = timeTable.getChromosomes().get(i).getDay();
                int hour2 = timeTable.getChromosomes().get(i).getHour();
                Grade grade2 = timeTable.getChromosomes().get(i).getGrade();
                Subject subject2 = timeTable.getChromosomes().get(i).getSubject();

                //teachers are teaching simultaniously in the same class
                if ((day1 == day2) && (hour1 == hour2) && (grade1.equals(grade2)))
                {
                    if(!(teacher1.equals(teacher2)) || !(subject1.equals(subject2)))
                    {
                        isPassRule =false;
                    }
                }
            }
        }
        RuleUtils.evaluteGrade(isPassRule, this);
        timeTable.setFitness(this.m_RuleGrade);
    }


    @Override
    public String toString() {
        return (super.toString() + "Name: Singularity");
    }
}
