package engine;


public interface Problem<T extends Solution>
{
    public T newRandomInstance();
}
