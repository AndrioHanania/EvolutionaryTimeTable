package engine;

import engine.crossover.Crossover;
import engine.mutation.Mutation;
import engine.selection.Selection;
import generated.ETTCrossover;
import generated.ETTMutations;
import generated.ETTSelection;

import java.util.List;

public abstract class Parse
{
    public abstract Selection parseSelection(ETTSelection eTTSelection);

    public abstract Crossover parseCrossover(ETTCrossover eTTCrossover);

    public abstract List<Mutation> parseMutation(ETTMutations eTTMutations);
}
