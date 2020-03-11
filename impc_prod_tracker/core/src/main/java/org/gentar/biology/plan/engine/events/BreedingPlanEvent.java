package org.gentar.biology.plan.engine.events;

import org.gentar.biology.plan.engine.PlanProcessor;
import org.gentar.biology.plan.engine.processors.BreedingPlanAbortProcessor;
import org.gentar.biology.plan.engine.processors.BreedingPlanAbortReverserProcessor;
import org.gentar.biology.plan.engine.state.BreedingPlanState;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;

import java.util.Arrays;
import java.util.List;

public enum BreedingPlanEvent implements ProcessEvent 
{
    abortWhenBreedingStarted(
            "Abort a breeding plan that has been started.",
            BreedingPlanState.BreedingStarted,
            BreedingPlanState.BreedingAborted,
            true,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return BreedingPlanAbortProcessor.class;
                }
            },
    breedingComplete(
            "Breeding plan is complete.",
            BreedingPlanState.BreedingStarted,
            BreedingPlanState.BreedingComplete,
            true,
            null),
    reverseAbortion(
            "Reverse abortion",
            BreedingPlanState.BreedingAborted,
            BreedingPlanState.BreedingStarted,
            true,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return BreedingPlanAbortReverserProcessor.class;
                }
            },
    abortWhenBreedingComplete(
            "Abort a breeding plan that is complete",
            BreedingPlanState.BreedingComplete,
            BreedingPlanState.BreedingAborted,
            true,
            null)
            {
                @Override
                public Class<? extends Processor> getNextStepProcessor()
                {
                    return BreedingPlanAbortProcessor.class;
                }
            };

    BreedingPlanEvent(
            String description,
            ProcessState initialState,
            ProcessState endState,
            boolean triggeredByUser,
            String triggerNote)
    {
        this.description = description;
        this.initialState = initialState;
        this.endState = endState;
        this.triggeredByUser = triggeredByUser;
        this.triggerNote = triggerNote;
    }

    public static BreedingPlanEvent getEventByName(String name)
    {
        BreedingPlanEvent[] BreedingPlanEvents = BreedingPlanEvent.values();
        for (BreedingPlanEvent BreedingPlanEvent : BreedingPlanEvents)
        {
            if (BreedingPlanEvent.name().equalsIgnoreCase(name))
                return BreedingPlanEvent;
        }
        return null;
    }

    @Override
    public ProcessState getInitialState()
    {
        return initialState;
    }

    @Override
    public ProcessState getEndState()
    {
        return endState;
    }

    @Override
    public boolean isTriggeredByUser()
    {
        return triggeredByUser;
    }

    @Override
    public String getTriggerNote()
    {
        return triggerNote;
    }

    private String description;
    private ProcessState initialState;
    private ProcessState endState;
    private boolean triggeredByUser;
    private String triggerNote;

    @Override
    public Class<? extends Processor> getNextStepProcessor()
    {
        return PlanProcessor.class;
    }

    @Override
    public String getName()
    {
        return name();
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    public static List<ProcessEvent> getAllEvents()
    {
        return Arrays.asList(BreedingPlanEvent.values());
    }

}