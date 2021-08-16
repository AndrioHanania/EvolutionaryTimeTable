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
        timeTable.getChromosomes().sort(Comparator.comparingInt(TimeTableChromosome::getDay)
                        .thenComparing(Comparator.comparingInt(TimeTableChromosome::getHour)
                                        .thenComparing(TimeTableChromosome::compareWithGrade)
                                                .thenComparing(TimeTableChromosome::compareWithTeacher)));
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
        for(int i=0;i<timeTable.getTeachers().size();i++)
        {
            timeTableForTeachers.add(i,new ArrayList<>());
        }
        for(TimeTableChromosome timeTableChromosome : timeTable.getChromosomes())
        {
            //gets teacher by id and adds to table(id) the data
            int teacherID = timeTableChromosome.getTeacher().getIdNumber();
            timeTableForTeachers.get(teacherID-1).add(timeTableChromosome);
        }
        for(int i=0;i<timeTableForTeachers.size();i++)
        {
            int k=i+1;
            System.out.println("Teacher " + k + ": ");
            timeTable.getChromosomes().sort(Comparator.comparingInt(TimeTableChromosome::getDay)
                    .thenComparing(Comparator.comparingInt(TimeTableChromosome::getHour)));
            for(int j=0;j<timeTableForTeachers.get(i).size();j++)
            {
                System.out.print(j + ".");
                System.out.println("day: " + timeTableForTeachers.get(i).get(j).getDay() + " Hour: " + timeTableForTeachers.get(i).get(j).getHourString());
                System.out.print("Class id: " + timeTableForTeachers.get(i).get(j).getGrade().getIdNumber());
                System.out.println(" Subject id: " + timeTableForTeachers.get(i).get(j).getSubject().getIdNumber());
                System.out.println(System.lineSeparator());
            }
        }
    }

    public static void printOptimalSolutionByClass(TimeTable timeTable)
    {
        List<List<TimeTableChromosome>> timeTableForGrades = new ArrayList<>(timeTable.getGrades().size());
        for(int i=0;i<timeTable.getGrades().size();i++)
        {
            timeTableForGrades.add(i,new ArrayList<>());
        }
        for(TimeTableChromosome timeTableChromosome : timeTable.getChromosomes())
        {
            int gradeID = timeTableChromosome.getGrade().getIdNumber();
            timeTableForGrades.get(gradeID-1).add(timeTableChromosome);
        }
        for(int i=0;i<timeTableForGrades.size();i++)
        {
            int k=i+1;
            System.out.println("Grade" + k + ": ");
            timeTable.getChromosomes().sort(Comparator.comparingInt(TimeTableChromosome::getDay)
                    .thenComparing(Comparator.comparingInt(TimeTableChromosome::getHour)));
            for(int j=0;j<timeTableForGrades.get(i).size();j++)
            {
                System.out.print(j + ".");
                System.out.println("day: " + timeTableForGrades.get(i).get(j).getDay() + " Hour: " + timeTableForGrades.get(i).get(j).getHourString());
                System.out.print("Teacher id: " + timeTableForGrades.get(i).get(j).getTeacher().getIdNumber());
                System.out.println(" Subject id: " + timeTableForGrades.get(i).get(j).getSubject().getIdNumber());
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
        System.out.println("Hard rules avg: " + timeTable.getHardRulesAvg());
        System.out.println("Soft rules avg: " + timeTable.getSoftRulesAvg());
    }
}
