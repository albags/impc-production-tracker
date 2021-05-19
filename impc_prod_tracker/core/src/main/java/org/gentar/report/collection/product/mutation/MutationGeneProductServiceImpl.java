package org.gentar.report.collection.product.mutation;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MutationGeneProductServiceImpl
{
    private final MutationGeneProductRepository mutationGeneProductRepository;

    public MutationGeneProductServiceImpl(MutationGeneProductRepository mutationGeneProductRepository)
    {
        this.mutationGeneProductRepository = mutationGeneProductRepository;
    }


    public List<MutationGeneProductProjection> getSelectedMutationGeneProjections(List mutationIds){
        return mutationGeneProductRepository.findSelectedMutationGeneProjectionsForProductReport(mutationIds);
    }
}
