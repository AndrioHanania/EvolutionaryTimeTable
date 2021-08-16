package timeTable.chromosome;

import timeTable.Grade;
import timeTable.Subject;
import timeTable.Teacher;
import timeTable.TimeTable;

import java.util.Comparator;
import java.util.Objects;

public class TimeTableChromosome implements Chromosome {
    //Members
    private int m_Day;
    private int m_Hour;
    private Grade m_Class;
    private Teacher m_Teacher;
    private Subject m_Subject;

    public TimeTableChromosome(int day, int hour, int idGrade, int idTeacher, int idSubject, TimeTable timeTable)
    {
        m_Day =day;
        m_Hour=hour;
        timeTable.getGrades().sort(Comparator.comparingInt(Grade::getIdNumber));
        m_Class= timeTable.getGrades().get(idGrade-1);
        timeTable.getTeachers().sort(Comparator.comparingInt(Teacher::getIdNumber));
        m_Teacher=timeTable.getTeachers().get(idTeacher-1);
        timeTable.getSubjects().sort(Comparator.comparingInt(Subject::getIdNumber));
        m_Subject = timeTable.getSubjects().get(idSubject-1);
    }

    @Override
    public String toString() {
        int time=m_Hour+8;
        return "Day: " + m_Day +
                ", Time: " + time +":00"+
                ", Grade: " + m_Class +
                ", Teacher: " + m_Teacher +
                ", Subject: " + m_Subject;
    }

    public TimeTableChromosome(int day, int hour, Grade clazz, Teacher teacher, Subject subject) {
        m_Day = day;
        m_Hour = hour;
        m_Class = clazz;
        m_Teacher = teacher;
        m_Subject = subject;
    }

    public void setDay(int day) {
        m_Day = day;
    }

    public void setTeacher(Teacher teacher) {
        m_Teacher = teacher;
    }

    public void setSubject(Subject subjet) {
        m_Subject = subjet;
    }

    public void setGrade(Grade grade) {
        m_Class = grade;
    }

    public void setHour(int hour) {
        m_Hour = hour;
    }

    public int getDay() {
        return m_Day;
    }

    public int getHour() {
        return m_Hour;
    }

    public String getHourString()
    {
        int hour=m_Hour+8;
        return hour + ":00";
    }

    public Grade getGrade() {
        return m_Class;
    }

    public Teacher getTeacher() {
        return m_Teacher;
    }

    public Subject getSubject() {
        return m_Subject;
    }

    public static int compareWithTeacher(TimeTableChromosome timeTableChromosome1, TimeTableChromosome timeTableChromosome2) {
        return Integer.compare(timeTableChromosome1.getTeacher().getIdNumber(), timeTableChromosome2.getTeacher().getIdNumber());
    }

    public static int compareWithGrade(TimeTableChromosome timeTableChromosome1, TimeTableChromosome timeTableChromosome2) {
        return Integer.compare(timeTableChromosome1.getGrade().getIdNumber(), timeTableChromosome2.getGrade().getIdNumber());
    }

    public static int compareWithSubjects(TimeTableChromosome timeTableChromosome1, TimeTableChromosome timeTableChromosome2) {
        return  Integer.compare(timeTableChromosome1.getSubject().getIdNumber(), timeTableChromosome2.getSubject().getIdNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTableChromosome that = (TimeTableChromosome) o;
        return m_Day == that.m_Day && m_Hour == that.m_Hour && Objects.equals(m_Class, that.m_Class) && Objects.equals(m_Teacher, that.m_Teacher) && Objects.equals(m_Subject, that.m_Subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_Day, m_Hour, m_Class, m_Teacher, m_Subject);
    }
}
