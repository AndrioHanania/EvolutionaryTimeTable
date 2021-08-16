package timeTable.Rules;

import generated.ETTRule;
import timeTable.TimeTable;

import java.util.Objects;


public abstract class Rule {

    protected String m_RuleWeight;
    protected String m_Confg;
    protected int m_RuleGrade=100;

    public Rule(Rule rule) {
        m_RuleWeight = rule.m_RuleWeight;
        m_Confg = rule.m_Confg;
        m_RuleGrade = rule.m_RuleGrade;
    }

    public String getRuleWeight(){return m_RuleWeight;}

    public String getConfig(){return m_Confg;}

    public void setGrade(int grade){m_RuleGrade = grade;}

    public  Rule(ETTRule ettRule)
    {
        m_Confg = ettRule.getETTConfiguration();
        m_RuleWeight = ettRule.getType();
    }



    public abstract void Execute(TimeTable timeTable);

    @Override
    public String toString() {return "Strictness: " + m_RuleWeight + " Rule Grade: " + m_RuleGrade;}

    public int getGrade(){return m_RuleGrade;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return m_RuleGrade == rule.m_RuleGrade && Objects.equals(m_RuleWeight, rule.m_RuleWeight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_RuleWeight, m_RuleGrade);
    }
}
