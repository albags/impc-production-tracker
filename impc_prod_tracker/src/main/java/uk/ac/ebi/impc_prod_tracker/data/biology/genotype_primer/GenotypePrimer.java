package uk.ac.ebi.impc_prod_tracker.data.biology.genotype_primer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.mouse_allele.MouseAllele;
import uk.ac.ebi.impc_prod_tracker.data.biology.mutagenesis_attempt.MutagenesisAttempt;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class GenotypePrimer extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "genotypePrimerSeq", sequenceName = "GENOTYPE_PRIMER_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genotypePrimerSeq")
    private Long id;

    private String name;

    private String sequence;

    private String chromosome;

    private Integer start;

    private Integer stop;

    private String strand;

    private String genomeBuild;

    @ManyToOne(targetEntity = MutagenesisAttempt.class)
    private MutagenesisAttempt mutagenesisAttempt;

    @ManyToOne(targetEntity = MouseAllele.class)
    private MouseAllele mouseAllele;
}