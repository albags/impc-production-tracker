package org.gentar.biology.plan.phenotyping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.biology.colony.ColonyDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
public class PhenotypingAttemptDTO
{
    @JsonIgnore
    private Long planId;
    @JsonIgnore
    private Long imitsPhenotypeAttemptId;
    @JsonIgnore
    private Long imitsPhenotypingProductionId;
    private LocalDateTime experimentStartedOn;
    private Boolean phenotypingStarted;
    private Boolean phenotypingComplete;
    private Boolean doNotCountTowardsCompleteness;
    private String phenotypingExternalRef;

    @JsonProperty("colonyDetails")
    private ColonyDTO colonyDTO;

    // TODO Add specimenDetails and outcomeType? - so the wranglers can understand phenotypingExternalRef
    // @JsonProperty("specimenDetails")
    // private SpecimenDTO specimenDTO;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("tissueDistributionCentresAttributes")
    private List<TissueDistributionCentreDTO> tissueDistributionCentreDTOs;
}
