package timeTable;

import java.util.ArrayList;
import java.util.List;
import generated.ETTRule;
import generated.ETTRules;

import java.util.Map;
import java.util.Objects;

import static timeTable.RulesEnum.*;

public class Rule {

    private String m_RuleID; //teacherIsHuman
    private String m_RuleName;
    private List<ETTRule> m_RulesList;
    private String m_RuleWeight;
    private String m_Confg;



    public Rule()
    {
        m_RuleName = new String();
    }

    public Rule(ETTRule ettRule)
    {
        m_RuleID = ettRule.getETTRuleId();
        m_Confg = ettRule.getETTConfiguration(); // to check
        m_RuleWeight = ettRule.getType();

    }
    @Override
    public String toString() {
        return "Rule ID: " + m_RuleID + " " +
        "Type: " + m_RuleWeight + "Config: " + m_Confg;
    }

    public String getRuleID(){return m_RuleID;}

    public String getRuleWeight(){return m_RuleWeight;}

    public String getConfig(){return m_Confg;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule aRule = (Rule) o;
        return m_RuleID == aRule.m_RuleID;
    }

    @Override
    public int hashCode(){return Objects.hash(m_RuleID);}
}
