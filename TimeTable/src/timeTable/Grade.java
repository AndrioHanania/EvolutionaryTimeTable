package timeTable;


import generated.ETTClass;
import generated.ETTStudy;

import java.util.*;

public class Grade
{
    //Members
    private int m_IdNumber;
    private String m_Name;
    private Map<Integer, Integer> m_MapIdSubjectToHoursInWeek;

    //Constructors
    public Grade()
    {
        m_Name= new String();
        m_MapIdSubjectToHoursInWeek = new TreeMap<>();
    }

    public Grade(int idNumber, String name, Map<Integer, Integer> map)
    {
        m_IdNumber = idNumber;
        m_Name= name;
        m_MapIdSubjectToHoursInWeek = map;
    }

    public Grade(ETTClass eTTClass) {
        m_Name= eTTClass.getETTName();
        m_IdNumber = eTTClass.getId();
        m_MapIdSubjectToHoursInWeek = new TreeMap<>();
        List<ETTStudy> listStudy = eTTClass.getETTRequirements().getETTStudy();
        for(ETTStudy eTTStudy : listStudy)
        {
            m_MapIdSubjectToHoursInWeek.put(eTTStudy.getSubjectId(), eTTStudy.getHours());
        }
    }

    //Methods
    @Override
    public String toString() {
        return "ID: " + m_IdNumber +
                ", Name: " + m_Name +
                ", MapIdSubjectToHoursInWeek: " + m_MapIdSubjectToHoursInWeek;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade aClass = (Grade) o;
        return m_IdNumber == aClass.m_IdNumber && Objects.equals(m_Name, aClass.m_Name) &&
                Objects.equals(m_MapIdSubjectToHoursInWeek, aClass.m_MapIdSubjectToHoursInWeek);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_IdNumber, m_Name, m_MapIdSubjectToHoursInWeek);
    }

    //Getters
    public Map<Integer, Integer> getMapIdSubjectToHoursInWeek(){return m_MapIdSubjectToHoursInWeek;}


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
