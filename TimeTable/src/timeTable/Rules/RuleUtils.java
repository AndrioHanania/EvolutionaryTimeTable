package timeTable.Rules;

public class RuleUtils
{

    public static Rule CreateRule(Rule rule){
        Rule newRule=null;
        switch (rule.getClass().getSimpleName())
        {
            case "Knowledgeable":
                newRule = new Knowledgeable(rule);
                break;
            case "Satisfactory":
                newRule = new Satisfactory(rule);
                break;
            case "Singularity":
                newRule = new Singularity(rule);
                break;
            case "TeacherIsHuman":
                newRule = new TeacherIsHuman(rule);
                break;
            case "DayOffTeacher":
                newRule = new DayOffTeacher(rule);
                break;
            case "Sequentiality":
                newRule = new Sequentiality(rule);
            default:
                //throw new Exception("Type of rule does not exist");
        }
        newRule.setGrade(100);
        return newRule;
    }
}
