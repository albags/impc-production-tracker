package org.gentar.report.collection.product;

import org.gentar.report.collection.product.colony.ColonyProductProjection;
import org.gentar.report.collection.product.mutation.MutationGeneProductProjection;

import java.util.List;
import java.util.Map;

public interface ProductReportProjectionMergeHelper
{
    /* This helper provides methods to merge the information in two set of projections
     *
     *  colonies associated with a mutation.
     */

    /**
     *
     * @param cpp List of Spring GeneInterestReportProjectProjection database projections
     * @param mpp List of Spring GeneInterestReportGeneProjection database projections
     * @return a map containing:
     *              Key: the MGI gene accession ID
     *              Value: a list of Project TPN IDs associated with the gene
     */
    Map<String, List<String>> getMutationsByColony(List<ColonyProductProjection> cpp,
                                                List<MutationGeneProductProjection> mpp);
}
