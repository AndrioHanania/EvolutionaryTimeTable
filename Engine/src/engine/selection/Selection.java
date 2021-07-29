package engine.selection;

import engine.Solution;
import engine.Population;
import generated.ETTSelection;


public interface Selection
{
    //Methods
    public abstract Population execute(Population currentGeneration);

    public static Selection parse(ETTSelection eTTSelection)
    {
        Selection selection = null;
        switch(eTTSelection.getType())
        {
            case "Truncation":
                int topPercent = Integer.parseInt(eTTSelection.getConfiguration().substring(11));
                selection = new Truncation(topPercent);
                break;

            case "RouletteWheel":
                selection = new RouletteWheel();
                break;

                default:
                    //selection = null;
                    break;
        }
        return selection;
    }
}
