package timeTable.Rules;

import timeTable.Teacher;
import timeTable.TimeTable;

public class TeacherIsHuman extends Rule {
    boolean isPassRule =true;
    @Override
    public void Execute(TimeTable timeTable) {
        for(int i=0; i<timeTable.getChromosomes().size()-1;i++)
        {
            Teacher T1 =  timeTable.getChromosomes().get(i).getTeacher();
            int day1 = timeTable.getChromosomes().get(i).getDay();
            int hour1 = timeTable.getChromosomes().get(i).getHour();

            for (int j=i+1;j<timeTable.getChromosomes().size();j++)
            {
                Teacher T2 = timeTable.getChromosomes().get(j).getTeacher();
                int day2 = timeTable.getChromosomes().get(i).getDay();
                int hour2 = timeTable.getChromosomes().get(i).getHour();

                if(day1==day2 && hour1 == hour2)
                {
                    if (T1.equals(T2))
                    {
                        isPassRule = false;
                    }
                }
            }
        }
        RuleUtils.evaluteGrade(isPassRule, this);
        timeTable.setFitness(this.m_RuleGrade);
    }
}
