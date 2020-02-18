package org.gentar.biology.colony.engine.processors;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.Processor;
import org.springframework.stereotype.Component;

@Component
public class ColonyProcessor implements Processor
{
    private StatusService statusService;

    public ColonyProcessor(StatusService statusService)
    {
        this.statusService = statusService;
    }

    @Override
    public ProcessData process(ProcessData data)
    {
        ProcessEvent processEvent = data.getEvent();
        String statusName = processEvent.getEndState().getInternalName();
        Status newStatus = statusService.getStatusByName(statusName);
        ((Colony)data).setStatus(newStatus);
        return data;
    }
}