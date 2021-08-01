package timeTable;

import generated.ETTSubject;

import java.util.Objects;

public class Subject
{
    //Members
    private int m_IdNumber;
    private String m_Name;

    //Constructors
    public Subject()
    {
        m_Name= new String();
    }

    public Subject(int idNumber, String name)
    {
        m_IdNumber = idNumber;
        m_Name = name;
    }

    public Subject(ETTSubject eTTSubject) {
        m_IdNumber = eTTSubject.getId();
        m_Name = eTTSubject.getName();
    }

    //Methods
    @Override
    public String toString()
    {
        return "name: " + m_Name + ", ID: " + m_IdNumber;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject that = (Subject) o;
        return m_IdNumber == that.m_IdNumber && Objects.equals(m_Name, that.m_Name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_IdNumber, m_Name);
    }

    //Getters
    public int getIdNumber()
    { return m_IdNumber; }

    public String getName()
    { return m_Name; }

    //Setters
    public void setIdNumber(int newIdNumber)
    { m_IdNumber = newIdNumber; }

    public void setName(String newName)
    { m_Name = newName; }



}
