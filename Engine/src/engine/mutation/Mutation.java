package engine.mutation;

import engine.Solution;
import generated.ETTMutations;

import java.util.Random;

public interface Mutation
{
    //Members
    Random m_Random = new Random();



    //Methods
    public abstract void execute(Solution item);
}
