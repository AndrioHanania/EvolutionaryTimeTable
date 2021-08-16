package engine;

import engine.crossover.Crossover;
import engine.mutation.Mutation;
import engine.selection.Selection;
import generated.*;

import java.util.List;

public abstract class Parse
{
    public abstract Selection parseSelection(ETTSelection eTTSelection, int sizeOfFirstPopulation) throws Exception;

    public abstract Crossover parseCrossover(ETTCrossover eTTCrossover) throws Exception;

    public abstract List<Mutation> parseMutation(ETTMutations eTTMutations) throws Exception;

    public abstract Engine parseEngine(ETTEvolutionEngine eTTEvolutionEngine) throws Exception;

    public abstract Solution parseSolution(ETTTimeTable eTTTimeTable) throws Exception;
}
