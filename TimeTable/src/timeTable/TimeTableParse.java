package timeTable;

import engine.Parse;
import engine.Solution;
import engine.crossover.Crossover;
import engine.mutation.Mutation;
import engine.selection.RouletteWheel;
import engine.selection.Selection;
import engine.selection.Truncation;
import generated.ETTCrossover;
import generated.ETTMutation;
import generated.ETTMutations;
import generated.ETTSelection;
import timeTable.Mutation.Flipping;
import timeTable.Mutation.Sizer;
import timeTable.crossover.AspectOriented;
import timeTable.crossover.DayTimeOriented;

import java.util.ArrayList;
import java.util.List;

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

            //case "Tournament":

              //  break;

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
    public List<Mutation> parseMutation(ETTMutations eTTMutations)
    {
        List<Mutation> mutations = new ArrayList<>(eTTMutations.getETTMutation().size());
        double probability;
        for(ETTMutation eTTMutation : eTTMutations.getETTMutation())
        {
            probability = eTTMutation.getProbability();
            String configuration = eTTMutation.getConfiguration();

            switch (eTTMutation.getName())
            {

                case "Flipping":
                    Flipping flipping = new Flipping(Integer.parseInt(configuration.substring(11)), configuration.charAt(23), probability);
                    mutations.add(flipping);
                    flipping.setConfiguration(configuration);
                    break;

                case "Sizer":
                    Sizer sizer = new Sizer(Integer.parseInt(configuration.substring(13)), probability);
                    mutations.add(sizer);
                    sizer.setConfiguration(configuration);
                    break;

                default:
/////////////////////////////
                    break;
            }
        }
        return mutations;
    }
}
