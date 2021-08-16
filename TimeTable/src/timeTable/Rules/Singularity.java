package timeTable.Rules;

import generated.ETTRule;
import timeTable.*;

public class Singularity extends Rule {
    public Singularity(ETTRule ettRule) {
        super(ettRule);
    }

    public Singularity(Rule rule) {
        super(rule);
    }

    @Override
    public void Execute(TimeTable timeTable) {
        boolean isPassRule = true;
        int rulePerTeacherOrSubjectScore = 100 / (timeTable.getTeachers().size() + timeTable.getSubjects().size());

        for (Teacher teacher : timeTable.getTeachers())
        {
            for (int i = 0; i < timeTable.getChromosomes().size() - 1; i++)
            {
                Teacher teacher1 = timeTable.getChromosomes().get(i).getTeacher();

                if (teacher.equals(teacher1))
                {
                    int day1 = timeTable.getChromosomes().get(i).getDay();
                    int hour1 = timeTable.getChromosomes().get(i).getHour();
                    Grade grade1 = timeTable.getChromosomes().get(i).getGrade();

                    for (int j = i + 1; j < timeTable.getChromosomes().size(); j++)
                    {
                        Teacher teacher2 = timeTable.getChromosomes().get(j).getTeacher();
                        if (teacher.equals(teacher2))
                        {
                            int day2 = timeTable.getChromosomes().get(j).getDay();
                            int hour2 = timeTable.getChromosomes().get(j).getHour();
                            Grade grade2 = timeTable.getChromosomes().get(j).getGrade();

                            if ((day1 == day2) && (hour1 == hour2) && (grade1.equals(grade2))) {
                                isPassRule = false;
                                m_RuleGrade -= rulePerTeacherOrSubjectScore;
                                break;
                            }

                        }
                    }
                    if (!isPassRule)
                    {
                        break;
                    }
                }
            }
        }

        isPassRule =true;
        for(Subject subject : timeTable.getSubjects())
        {
            for (int i = 0; i < timeTable.getChromosomes().size() - 1; i++)
            {
                Subject subject1 = timeTable.getChromosomes().get(i).getSubject();

                if(subject.equals(subject1))
                {
                    int day1 = timeTable.getChromosomes().get(i).getDay();
                    int hour1 = timeTable.getChromosomes().get(i).getHour();
                    Grade grade1 = timeTable.getChromosomes().get(i).getGrade();

                    for(int j=i+1; j<timeTable.getChromosomes().size(); j++)
                    {
                        Subject subject2 = timeTable.getChromosomes().get(j).getSubject();
                        if(subject.equals(subject2))
                        {
                            int day2 = timeTable.getChromosomes().get(j).getDay();
                            int hour2 = timeTable.getChromosomes().get(j).getHour();
                            Grade grade2 = timeTable.getChromosomes().get(j).getGrade();

                            if ((day1 == day2) && (hour1 == hour2) && (grade1.equals(grade2)))
                            {
                                isPassRule = false;
                                m_RuleGrade -= rulePerTeacherOrSubjectScore;
                                break;
                            }
                        }
                    }
                    if (!isPassRule)
                    {
                        break;
                    }
                }
            }
        }
    }


    @Override
    public String toString() {
        return (  "Name: Singularity, " + super.toString());
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
