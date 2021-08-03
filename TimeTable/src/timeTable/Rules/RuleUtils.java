package timeTable.Rules;

public class RuleUtils
{

    public static void evaluteGrade(boolean isPassRule, Rule rule)
    {
        if(isPassRule)
        {
            if(rule.m_RuleWeight == "Hard")
            { rule.m_RuleGrade += 5;}
            else if(rule.m_RuleWeight == "Soft")
            { rule.m_RuleGrade +=3;}
        }
        else
        {
            if(rule.m_RuleWeight == "Hard")
            {  rule.m_RuleGrade += 0;}
            else if(rule.m_RuleWeight == "Soft")
            {rule.m_RuleGrade +=1;}
        }
    }
}
