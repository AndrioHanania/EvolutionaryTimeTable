package timeTable;

import generated.ETTClass;
import generated.ETTRule;
import generated.ETTRules;
import generated.ETTStudy;

import java.util.List;
import java.util.TreeMap;
public enum RulesEnum
{
HARD, SOFT;
public static RulesEnum getStrictness(String Val){
    switch (Val){
        case "Hard":
            return HARD;
        case "Soft":
            return SOFT;
        default:
            throw new IllegalArgumentException(
                    "UnKnown Rule Stricktness weight " + Val);
    }

}

    /*
     public Rules(ETTRules ettRules) {
        m_Name= eTTClass.getETTName();
        m_IdNumber = eTTClass.getId();
        m_MapIdSubjectToHoursInWeek = new TreeMap<>();
        List<ETTStudy> listStudy = eTTClass.getETTRequirements().getETTStudy();
        for(ETTStudy eTTStudy : listStudy)
        {
            m_MapIdSubjectToHoursInWeek.put(eTTStudy.getSubjectId(), eTTStudy.getHours());
        }
    }

     */

/*


 */

}
