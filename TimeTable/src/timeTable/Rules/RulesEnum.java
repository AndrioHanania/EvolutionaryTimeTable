package timeTable.Rules;

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
}
