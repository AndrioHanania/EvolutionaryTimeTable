package engine.Listeners;

@FunctionalInterface
public interface UpdateGenerationListener {
    public abstract void OnUpdateGeneration(double bestFitnessInCurrentGeneration, int numberOfGeneration);
}
