package org.gentar.report.collection.product.mutation;

import org.gentar.biology.mutation.Mutation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface MutationGeneProductRepository extends Repository<Mutation, Long>
{
    @Query("SELECT " +
            "m.min as mutationMin, " +
            "m.symbol as mutationSymbol, " +
            "g.symbol as geneSymbol, " +
            "g.accId as geneAccId, " +
            "mc.name as mutationCategorizationName " +
            "FROM " +
            "Mutation m " +
            "LEFT OUTER JOIN m.genes g " +
            "LEFT OUTER JOIN m.mutationCategorizations mc " +
            "WHERE " +
            "m.min IN :min")
    List<MutationGeneProductProjection> findSelectedMutationGeneProjectionsForProductReport(@Param("min") List mutationIds );
}
