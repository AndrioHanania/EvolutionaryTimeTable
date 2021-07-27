package timeTable;

import generated.ETTTeacher;
import generated.ETTTeaches;

import java.util.*;

public class Teacher
{
    //Members
    private int m_IdNumber;
    private String m_Name;
    private List<Integer> m_IdOfSubjectsTeachable;

    //Constructors
    public Teacher()
    {
        m_Name= new String();
        m_IdOfSubjectsTeachable = new ArrayList<>();
    }

    public Teacher(int idNumber, String name, List<Integer> idOfProfessionsTeachable)
    {
        m_IdNumber = idNumber;
        m_Name = name;
        m_IdOfSubjectsTeachable = idOfProfessionsTeachable;
    }

    public Teacher(ETTTeacher eTTTeacher) {
        m_IdNumber = eTTTeacher.getId();
        m_Name = eTTTeacher.getETTName();
        m_IdOfSubjectsTeachable = new ArrayList<>();
        List<ETTTeaches> listETTTeaches = eTTTeacher.getETTTeaching().getETTTeaches();
        for(ETTTeaches eTTTeaches : listETTTeaches){
            m_IdOfSubjectsTeachable.add(eTTTeaches.getSubjectId());
        }
    }

    //Methods


    @Override
    public String toString()
    {
        return "ID= " + m_IdNumber + ", Name= " + m_Name +
                ", Id Of Subjects Teachable= " + m_IdOfSubjectsTeachable;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return m_IdNumber == teacher.m_IdNumber && Objects.equals(m_Name, teacher.m_Name) &&
                Objects.equals(m_IdOfSubjectsTeachable, teacher.m_IdOfSubjectsTeachable);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(m_IdNumber, m_Name, m_IdOfSubjectsTeachable);
    }

    //Getters
    public int getIdNumber()
    { return m_IdNumber; }

    public String getName()
    { return m_Name; }

    public List<Integer> getIdOSubjectsTeachable()
    { return m_IdOfSubjectsTeachable; }

    //Setters
    public void setIdNumber(int newIdNumber)
    { m_IdNumber = newIdNumber; }

    public void setName(String newName)
    { m_Name = newName; }

    public void setIdOfSubjectsTeachable(List<Integer> newIdOfSubjectsTeachable)
    { m_IdOfSubjectsTeachable = newIdOfSubjectsTeachable; }
}
