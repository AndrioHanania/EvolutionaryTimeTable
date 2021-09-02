package timeTable.crossover;

import engine.Solution;
import engine.crossover.Crossover;
import timeTable.TimeTable;

import java.util.List;

public class AspectOriented extends Crossover {

    private String m_OrientedBy=new String();

    //Constructors
    public AspectOriented(int cuttingPoints, String orientedBy)
    {
        m_NumOfCuttingPoints =  cuttingPoints;
        m_OrientedBy=orientedBy;
    }

    //Methods
    public void setOrientedBy(String orientedBy){m_OrientedBy=orientedBy;}

    public String getOrientedBy(){return m_OrientedBy;}

    @Override
    public String toString() {
        return  "Name: AspectOriented, Oriented By: " + m_OrientedBy + ", " +  super.toString();
    }

    public List<Solution> execute(Solution parent1, Solution parent2)
    {
        TimeTable TTableParent1 = (TimeTable) parent1;
        TimeTable TTableParent2 = (TimeTable) parent2;
        if(m_OrientedBy.equals("TEACHER"))
            return CrossoverUtils.AspectOrientedByTeacher(TTableParent1,TTableParent2, m_NumOfCuttingPoints);
        else if (m_OrientedBy.equals("CLASS"))
            return CrossoverUtils.AspectOrientedByClass(TTableParent1,TTableParent2, m_NumOfCuttingPoints);
        return null;
    }
}
