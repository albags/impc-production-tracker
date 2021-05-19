package org.gentar.report.collection.product.colony;

import org.gentar.report.collection.product.mutation.MutationGeneProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ColonyProductServiceImpl implements ColonyProductService
{
    private final ColonyProductRepository colonyProductRepository;

    public ColonyProductServiceImpl(ColonyProductRepository colonyProductRepository)
    {
        this.colonyProductRepository = colonyProductRepository;
    }

    @Override
    public List<ColonyProductProjection> getSelectedColonyProductProjections()
    {
        return colonyProductRepository.findColonyProductProjection();
    }
}
