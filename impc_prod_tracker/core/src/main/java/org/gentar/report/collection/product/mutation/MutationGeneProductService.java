package org.gentar.report.collection.product.mutation;

import java.util.List;

public interface MutationGeneProductService
{
    /**
     *
     * @param mutationIds
     * @return a list of MutationGeneProductProjection Spring database projections containing
     *         an Mutation Id and the associated Gene Id and Gene Symbol and Mutation Categorization
     */
    List<MutationGeneProductProjection> getSelectedMutationGeneProjections(List mutationIds);
}
