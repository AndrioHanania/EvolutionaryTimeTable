package engine.mutation;

import engine.Solution;
import generated.ETTMutations;

import java.util.Random;

public interface Mutation<T extends Solution>
{
    //Members
    Random m_Random = new Random();

    static <T extends Solution> Mutation parse(ETTMutations ettMutations) {
        return null;
    }

    //Methods
    public abstract void execute(T item);
}
