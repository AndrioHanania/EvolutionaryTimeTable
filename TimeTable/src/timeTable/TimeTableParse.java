package timeTable;

import engine.Engine;
import engine.Parse;
import engine.Solution;
import engine.crossover.Crossover;
import engine.mutation.Mutation;
import engine.selection.RouletteWheel;
import engine.selection.Selection;
import engine.selection.Tournament;
import engine.selection.Truncation;
import generated.*;
import timeTable.Mutation.Flipping;
import timeTable.Mutation.Sizer;
import timeTable.Rules.*;
import timeTable.crossover.AspectOriented;
import timeTable.crossover.DayTimeOriented;

import java.util.ArrayList;
import java.util.List;

public class TimeTableParse extends Parse
{


    @Override
    public Selection parseSelection(ETTSelection eTTSelection, int sizeOfFirstPopulation) throws Exception {
        Selection selection = null;

        switch(eTTSelection.getType())
        {
            case "Truncation":
                int topPercent = 0;
                try {
                    topPercent = Integer.parseInt(eTTSelection.getConfiguration().substring(11));
                }
                catch (NumberFormatException e)
                {
                    throw new Exception("The parameter -top percent- is not an integer in  Selection-Truncation");
                }
                selection = new Truncation(topPercent);
                break;

            case "RouletteWheel":
                selection = new RouletteWheel();
                break;

            case "Tournament":
                int pte = 0;
                try
                {
                    pte = Integer.parseInt(eTTSelection.getConfiguration().substring(4));
                }
                catch (NumberFormatException e)
                {
                    throw new Exception("The parameter -pte- is not an integer in  Selection-Tournament");
                }
                selection = new Tournament(pte);
                break;

            default:
                    throw new Exception("Error with type of selection");
        }
        selection.setConfiguration(eTTSelection.getConfiguration());
        if(eTTSelection.getETTElitism() == null)
        {
            selection.setSizeOfElitism(0);
        }
        else if(eTTSelection.getETTElitism() > sizeOfFirstPopulation)
        {
            throw new Exception("Error with size of elitism, is bigger than size of first population");
        }
        else if(eTTSelection.getETTElitism() < 0)
        {
            throw new Exception("Error with size of elitism, is zero");
        }
        else
        {
            selection.setSizeOfElitism(eTTSelection.getETTElitism());
        }


        return selection;
    }

    @Override
    public Crossover parseCrossover(ETTCrossover eTTCrossover) throws Exception {
        Crossover crossover = null;
        int cuttingPoint = eTTCrossover.getCuttingPoints();
        switch(eTTCrossover.getName())
        {
            case "AspectOriented":
                String[] str = eTTCrossover.getConfiguration().split("=");
                String Orientation = str[1];
                crossover = new AspectOriented(cuttingPoint);
                break;
            case "DayTimeOriented":
                crossover = new DayTimeOriented(cuttingPoint);
                break;
            default:
                throw new Exception("Error with type of crossover");
        }
        crossover.setConfiguration(eTTCrossover.getConfiguration());
        return crossover;
    }

    @Override
    public List<Mutation> parseMutation(ETTMutations eTTMutations) throws Exception {
        List<Mutation> mutations = new ArrayList<>(eTTMutations.getETTMutation().size());
        double probability;
        for(ETTMutation eTTMutation : eTTMutations.getETTMutation())
        {
            probability = eTTMutation.getProbability();
            String configuration = eTTMutation.getConfiguration();
            String tempStr;
            String[] tempStrArr;
            Mutation mutation=null;
            switch (eTTMutation.getName())
            {
                case "Flipping":
                    tempStr = configuration.substring(11);
                    tempStrArr = tempStr.split(",");
                    char component=0;
                    int maxTupples=0;
                    try{
                        maxTupples=Integer.parseInt(tempStrArr[0]);
                    }
                    catch (NumberFormatException e)
                    {
                        throw new Exception("The parameter -maxTupples- is not an integer in  Mutation-Flipping");
                    }
                    try
                    {
                        component=configuration.charAt(23);
                    }
                    catch (NumberFormatException e)
                    {
                        throw new Exception("Error in parameter -component- in Mutation-Flipping");
                    }
                    mutation = new Flipping(maxTupples, component, probability);
                    break;

                case "Sizer":
                    tempStr = configuration.substring(13);
                    int totalTupples=0;
                    try
                    {
                        totalTupples=Integer.parseInt(configuration.substring(13));
                    }
                    catch (NumberFormatException e)
                    {
                        throw new Exception("The parameter -totalTupples- is not an integer in  Mutation-Sizer");
                    }
                    mutation = new Sizer(totalTupples, probability);
                    break;

                default:
                    throw new Exception("Error with type of mutation");
            }
            mutations.add(mutation);
            mutation.setConfiguration(configuration);
        }
        return mutations;
    }

    public Rule parseRules(ETTRule ettRule) throws Exception {
        Rule rule = null;
        switch (ettRule.getETTRuleId())
        {
            case "Knowledgeable":
                rule = new Knowledgeable(ettRule);
                break;
            case "Satisfactory":
                rule = new Satisfactory(ettRule);
                break;
                case "Singularity":
                rule = new Singularity(ettRule);
                break;
            case "TeacherIsHuman":
                rule = new TeacherIsHuman(ettRule);
                break;
            case "DayOffTeacher":
                rule = new DayOffTeacher(ettRule);
                break;
            case "Sequentiality":
                rule = new Sequentiality(ettRule);
                break;
            default:
                throw new Exception("Error with type of rule");
        }
        return rule;
    }

    @Override
    public Engine parseEngine(ETTEvolutionEngine eTTEvolutionEngine) throws Exception {
        Engine engine=new Engine();
        engine.setSizeOfFirstPopulation(eTTEvolutionEngine.getETTInitialPopulation().getSize());
        engine.setSelection(parseSelection(eTTEvolutionEngine.getETTSelection(), engine.getSizeOfFirstPopulation()));
        engine.setCrossover(parseCrossover(eTTEvolutionEngine.getETTCrossover()));
        engine.setMutations(parseMutation(eTTEvolutionEngine.getETTMutations()));
        return engine;
    }

    @Override
    public Solution parseSolution(ETTTimeTable eTTTimeTable) throws Exception {
        TimeTable timeTable = new TimeTable();
        timeTable.setParse(this);
        timeTable.setDaysForStudy(eTTTimeTable.getDays());
        timeTable.setHourStudyForDay(eTTTimeTable.getHours());
        timeTable.initializeSubjects(eTTTimeTable);
        timeTable.initializeclasses(eTTTimeTable);
        timeTable.initializeTeachers(eTTTimeTable);
        timeTable.initializeRules(eTTTimeTable);
        return timeTable;
    }
}