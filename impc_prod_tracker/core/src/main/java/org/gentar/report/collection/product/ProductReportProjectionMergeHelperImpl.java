package org.gentar.report.collection.product;

import org.gentar.report.collection.product.colony.ColonyProductProjection;
import org.gentar.report.collection.product.mutation.MutationGeneProductProjection;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ProductReportProjectionMergeHelperImpl implements ProductReportProjectionMergeHelper
{
    @Override
    public Map<String, List<String>> getMutationsByColony(List<ColonyProductProjection> cpp,
                                                       List<MutationGeneProductProjection> mpp)
    {
        Map<String, List<ColonyProductProjection>> cppColoniesByMutation = cpp.stream()
                .collect(Collectors.groupingBy(
                        ColonyProductProjection::getMutationMin));

        Map<String, List<MutationGeneProductProjection>> mppMutationByGene = mpp.stream()
                .collect(Collectors.groupingBy(
                        MutationGeneProductProjection::getMutationMin));

        System.out.println(cppColoniesByMutation.get("MIN:000000003530").stream().collect(Collectors.toList()));
        System.out.println(mppMutationByGene.get("MIN:000000003530").stream().collect(Collectors.toList()));

        Map<Object, Object> mutationsByColony = Stream.concat(
                cppColoniesByMutation.entrySet().stream(), mppMutationByGene.entrySet().stream() )
                .filter(x -> x.getKey() == "MIN:000000003530")
                .collect(Collectors.toMap(
                        map -> map.getKey(),
                        map -> List.copyOf(map.getValue()) ));
//                        (set1,set2) -> List.copyOf(Stream.concat(set1., set2).collect(Collectors.toSet()))));

        return null;
    }
}
