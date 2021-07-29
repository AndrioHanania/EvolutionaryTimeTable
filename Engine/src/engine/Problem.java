package engine;


public interface Problem
{
    public Solution newRandomInstance();

    public Chromosome newRandomChromosome();
}
