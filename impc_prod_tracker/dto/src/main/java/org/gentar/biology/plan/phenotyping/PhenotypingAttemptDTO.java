package org.gentar.biology.plan.phenotyping;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.biology.outcome.OutcomeDTO;
import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
public class PhenotypingAttemptDTO
{
    private LocalDateTime phenotypingExperimentsStarted;
    private Boolean doNotCountTowardsCompleteness;
    private String phenotypingExternalRef;

    @JsonProperty("phenotypingBackgroundStrainName")
    private String strainName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("tissueDistribution")
    private List<TissueDistributionDTO> tissueDistributionCentreDTOs;

    @JsonProperty("outcomeDetails")
    private OutcomeDTO outcomeDTO;
}
