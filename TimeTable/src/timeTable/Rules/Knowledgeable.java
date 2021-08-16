package timeTable.Rules;

import generated.ETTRule;
import timeTable.Teacher;
import timeTable.TimeTable;

import java.util.List;

public class Knowledgeable extends Rule {


    public Knowledgeable(ETTRule ettRule)
    {super(ettRule);}

    public Knowledgeable(Rule rule) {
        super(rule);
    }

    public void Execute(TimeTable timeTable) {

        for (Teacher teacher1 : timeTable.getTeachers())
        {
            int numOfTeacher = timeTable.getTeachers().size();
            int rulePerTeacherScore = 100 / numOfTeacher;
            List<Integer> subjectsIDTeaching1 = teacher1.getIdOfSubjectsTeachable();

            for (int i = 0; i < timeTable.getChromosomes().size(); i++)
            {
                Teacher teacher2 = timeTable.getChromosomes().get(i).getTeacher();
                if (teacher1.equals(teacher2))
                {
                    if(!subjectsIDTeaching1.contains(timeTable.getChromosomes().get(i).getSubject().getIdNumber()))
                    {
                        m_RuleGrade -= rulePerTeacherScore;
                        break;
                    }
                }
            }
        }
    }


    @Override
    public String toString() {
        return ( "Name: Knowledgeable, " + super.toString());
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
