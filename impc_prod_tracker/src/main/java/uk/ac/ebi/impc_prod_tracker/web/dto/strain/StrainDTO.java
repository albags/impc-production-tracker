package uk.ac.ebi.impc_prod_tracker.web.dto.strain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class StrainDTO
{

    private String name;

    @JsonProperty("mgi_strain_acc_id")
    private String mgiStrainAccId;
}
