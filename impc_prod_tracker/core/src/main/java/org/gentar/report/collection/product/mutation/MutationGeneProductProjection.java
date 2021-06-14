package org.gentar.report.collection.product.mutation;

import org.springframework.beans.factory.annotation.Value;

public interface MutationGeneProductProjection
{
    @Value("#{target.mutationMin}")
    String getMutationMin();

    @Value("#{target.mutationSymbol}")
    String getMutationSymbol();

    @Value("#{target.geneSymbol}")
    String getGeneSymbol();

    @Value("#{target.geneAccId}")
    String getGeneAccId();

    @Value("#{target.mutationCategorizationName}")
    String getMutationCategorizationName();
}
