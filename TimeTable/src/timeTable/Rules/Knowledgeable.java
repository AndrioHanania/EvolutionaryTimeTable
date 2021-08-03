package timeTable.Rules;

import timeTable.TimeTable;
import timeTable.chromosome.TimeTableChromosome;

import java.util.List;

public class Knowledgeable extends Rule {

    public void Execute(TimeTable timeTable) {
        boolean isPassRule = false;
        List<TimeTableChromosome> timeTableChromosomeList = timeTable.getChromosomes();
        for (TimeTableChromosome timeTableChromosome : timeTableChromosomeList) {
            isPassRule = false;
            List<Integer> idSubjects = timeTableChromosome.getTeacher().getIdOSubjectsTeachable();
            for (int idSubject : idSubjects) {
                if (idSubject == timeTableChromosome.getSubject().getIdNumber()) {
                    isPassRule = true;
                    break;
                }
                if (isPassRule) {
                    break;
                }
            }
            RuleUtils.evaluteGrade(isPassRule, this);
            timeTable.setFitness(this.m_RuleGrade);
        }
    }
}
