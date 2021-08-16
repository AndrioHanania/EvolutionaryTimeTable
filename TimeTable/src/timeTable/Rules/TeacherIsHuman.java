package timeTable.Rules;

import generated.ETTRule;
import timeTable.Teacher;
import timeTable.TimeTable;

public class TeacherIsHuman extends Rule {

    //Contractors
    public TeacherIsHuman(ETTRule ettRule) {
        super(ettRule);
    }

    public TeacherIsHuman(Rule rule) {
        super(rule);
    }

    //Methods
    @Override
    public void Execute(TimeTable timeTable) {
        int numOfTeacher = timeTable.getTeachers().size();
        int rulePerTeacherScore = 100 / numOfTeacher;
        boolean isRulePassed = true;
        for(Teacher teacher : timeTable.getTeachers())
        {
            isRulePassed = true;
            for (int i=0; i<timeTable.getChromosomes().size()-1;i++)
            {
                Teacher T1 = timeTable.getChromosomes().get(i).getTeacher();
                int day1 = timeTable.getChromosomes().get(i).getDay();
                int hour1 = timeTable.getChromosomes().get(i).getHour();
                if (T1.equals(teacher))
                {
                    for (int j=i+1;j<timeTable.getChromosomes().size(); j++)
                    {
                        Teacher T2 = timeTable.getChromosomes().get(j).getTeacher();
                        int day2 = timeTable.getChromosomes().get(j).getDay();
                        int hour2 = timeTable.getChromosomes().get(j).getHour();
                        if(T2.equals(teacher))
                        {
                            if(day1==day2 && hour1 == hour2)
                            {
                                m_RuleGrade -= rulePerTeacherScore;
                                isRulePassed = false;
                                break;
                            }
                        }
                    }
                    if(!isRulePassed){break;}
                }
            }
        }
    }

    @Override
    public String toString() {
        return ( "Name: TeacherIsHuman, " + super.toString());
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
