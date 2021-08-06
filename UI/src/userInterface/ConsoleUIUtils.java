package userInterface;

import generated.ETTDescriptor;
import timeTable.Rules.Rule;
import timeTable.TimeTable;
import timeTable.chromosome.TimeTableChromosome;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.*;

public class ConsoleUIUtils {

    public static Menu createMainMenu() {
        return new Menu("Main menu",
                new String[]{
                        "Load information from xml file",
                        "Show settings",
                        "Run evolutionary algorithm",
                        "Show optimal solution",
                        "View the algorithm process",
                        "Run small file;",
                        "exit"
                });
    }

    public static ETTDescriptor deserializeFrom(InputStream in) throws JAXBException
    {
        JAXBContext jc = JAXBContext.newInstance(ETTDescriptor.class);
        Unmarshaller u = jc.createUnmarshaller();
        return (ETTDescriptor)u.unmarshal(in);
    }

    public static void printOptimalSolutionByRaw(TimeTable timeTable)
    {
        timeTable.getChromosomes().sort(Comparator.comparingInt(TimeTableChromosome::getDay));
        timeTable.getChromosomes().sort(Comparator.comparingInt(TimeTableChromosome::getHour));
        timeTable.getChromosomes().sort(TimeTableChromosome::compareWithGrade);
        timeTable.getChromosomes().sort(TimeTableChromosome::compareWithTeacher);
        int sizeChromosomes = timeTable.getChromosomes().size();
        for (int i=0;i < sizeChromosomes;i++)
        {
            System.out.print(i + ".");
            System.out.print(timeTable.getChromosomes().get(i));
            System.out.println(System.lineSeparator());
        }
    }

    public static void printOptimalSolutionByTeacher(TimeTable timeTable)
    {
        List<List<TimeTableChromosome>> timeTableForTeachers = new ArrayList<>(timeTable.getTeachers().size());
        for(int i=0;i<timeTableForTeachers.size();i++)
        {
            timeTableForTeachers.set(i,new ArrayList<>());
        }
        for(TimeTableChromosome timeTableChromosome : timeTable.getChromosomes())
        {
            timeTableForTeachers.get(timeTableChromosome.getTeacher().getIdNumber()).add(timeTableChromosome);
        }
        for(int i=0;i<timeTableForTeachers.size();i++)
        {
            System.out.println("Teacher" + i + ": ");
            timeTableForTeachers.get(i).sort(Comparator.comparingInt(TimeTableChromosome::getDay));
            timeTableForTeachers.get(i).sort(Comparator.comparingInt(TimeTableChromosome::getHour));
            for(int j=0;j<timeTableForTeachers.get(i).size();j++)
            {
                System.out.print(j + ".");
                System.out.print("Class id: " + timeTableForTeachers.get(i).get(j).getGrade().getIdNumber());
                System.out.println("Subject id: " + timeTableForTeachers.get(i).get(j).getSubject().getIdNumber());
                System.out.println(System.lineSeparator());
            }
        }
    }

    public static void printOptimalSolutionByClass(TimeTable timeTable)
    {
        List<List<TimeTableChromosome>> timeTableForGrades = new ArrayList<>(timeTable.getGrades().size());
        for(int i=0;i<timeTableForGrades.size();i++)
        {
            timeTableForGrades.set(i,new ArrayList<>());
        }
        for(TimeTableChromosome timeTableChromosome : timeTable.getChromosomes())
        {
            timeTableForGrades.get(timeTableChromosome.getGrade().getIdNumber()).add(timeTableChromosome);
        }
        for(int i=0;i<timeTableForGrades.size();i++)
        {
            System.out.println("Grade" + i + ": ");
            timeTableForGrades.get(i).sort(Comparator.comparingInt(TimeTableChromosome::getDay));
            timeTableForGrades.get(i).sort(Comparator.comparingInt(TimeTableChromosome::getHour));
            for(int j=0;j<timeTableForGrades.get(i).size();j++)
            {
                System.out.print(j + ".");
                System.out.print("Teacher id: " + timeTableForGrades.get(i).get(j).getTeacher().getIdNumber());
                System.out.println("Subject id: " + timeTableForGrades.get(i).get(j).getSubject().getIdNumber());
                System.out.println(System.lineSeparator());
            }
        }
    }

    public static void printRules(TimeTable timeTable) {
        System.out.println("Rules: ");
        for (Rule rule : timeTable.getRules())
        {
            System.out.println(rule);
        }
        //•	ממוצע חוקי ה HARD וממוצע חוקי ה SOFT (יש להציג נקודה עשרונית אחת)
    }
}
