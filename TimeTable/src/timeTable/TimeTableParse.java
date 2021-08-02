package timeTable;

import engine.Parse;
import engine.crossover.Crossover;
import engine.mutation.Mutation;
import engine.selection.RouletteWheel;
import engine.selection.Selection;
import engine.selection.Truncation;
import generated.ETTCrossover;
import generated.ETTMutations;
import generated.ETTSelection;
import timeTable.crossover.AspectOriented;
import timeTable.crossover.DayTimeOriented;

public class TimeTableParse extends Parse
{
    @Override
    public Selection parseSelection(ETTSelection eTTSelection) {
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
/////////////////////
                break;
        }
        return selection;
    }

    @Override
    public Crossover parseCrossover(ETTCrossover eTTCrossover)
    {
        Crossover crossover = null;
        int cuttingPoint = eTTCrossover.getCuttingPoints();
        switch(eTTCrossover.getName())
        {
            case "AspectOriented":
                crossover = new AspectOriented(cuttingPoint);
                break;
            case "DayTimeOriented":
                crossover = new DayTimeOriented(cuttingPoint);
                break;

            default:
/////////////////////////////
                break;
        }
        return crossover;
    }

    @Override
    public Mutation parseMutation(ETTMutations eTTMutations)
    {
        return null;
    }
}
