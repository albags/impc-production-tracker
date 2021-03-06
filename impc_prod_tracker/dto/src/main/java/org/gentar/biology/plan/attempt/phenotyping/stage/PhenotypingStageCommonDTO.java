package org.gentar.biology.plan.attempt.phenotyping.stage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PhenotypingStageCommonDTO
{
    private LocalDate phenotypingExperimentsStarted;
    private LocalDate initialDataReleaseDate;

    @JsonProperty("tissueDistributions")
    private List<TissueDistributionDTO> tissueDistributionCentreDTOs;

}
