package org.gentar.biology.mutation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class MutationCommonDTO
{
    private Boolean mgiAlleleSymbolRequiresConstruction;
    private String geneticMutationTypeName;
    private String molecularMutationTypeName;
    private Boolean alleleConfirmed;

    @JsonProperty("mutationQcResults")
    private List<MutationQCResultDTO> mutationQCResultDTOs;

    @JsonProperty("mutationSequences")
    private List<MutationSequenceDTO> mutationSequenceDTOS;

    @JsonProperty("mutationCategorizations")
    private List<MutationCategorizationDTO> mutationCategorizationDTOS;
}
