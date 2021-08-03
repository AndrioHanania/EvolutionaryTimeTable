package timeTable.Rules;

import java.util.ArrayList;
import java.util.List;
import generated.ETTRule;
import generated.ETTRules;
import timeTable.TimeTable;

import java.util.Map;
import java.util.Objects;

import static timeTable.RulesEnum.*;

public abstract class Rule {

    protected String m_RuleWeight;
    protected String m_Confg;
    protected int m_RuleGrade;

    public String getRuleWeight(){return m_RuleWeight;}

    public String getConfig(){return m_Confg;}

    public abstract void Execute(TimeTable timeTable);

    /*


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule aRule = (Rule) o;
        return m_RuleID == aRule.m_RuleID;
    }

    @Override
    public int hashCode(){return Objects.hash(m_RuleID);}

     */
}
