package org.gentar.report.collection.product.colony;

import org.gentar.biology.colony.Colony;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface ColonyProductRepository extends Repository<Colony, Long>
{
    @Query("SELECT " +
            "o.tpo as outcomeTpo, " +
            "m.min as mutationMin, " +
            "c.name as colonyName, " +
            "wup.name as planWorkUnitName, " +
            "s.name as colonyStatusName, " +
            "st.date as colonyStatusDate, " +
            "wuc.name as distributionWorkUnitName, " +
            "dn.name as distributionNetworkName " +
            "FROM " +
            "Colony c " +
            "LEFT OUTER JOIN Status s ON c.status = s " +
            "LEFT OUTER JOIN ColonyStatusStamp st ON st.colony = c AND s = st.status " +
            "INNER JOIN Outcome o ON o = c.outcome " +
            "LEFT OUTER JOIN Plan p ON o.plan = p " +
            "LEFT OUTER JOIN WorkUnit wup on p.workUnit = wup " +
            "LEFT OUTER JOIN o.mutations m " +
            "LEFT OUTER JOIN DistributionProduct dp on c = dp.colony " +
            "LEFT OUTER JOIN WorkUnit wuc on dp.distributionCentre = wuc " +
            "LEFT OUTER JOIN DistributionNetwork dn on dp.distributionNetwork = dn " +
            "WHERE s.name = 'Genotype Confirmed' ")
    List<ColonyProductProjection> findColonyProductProjection();
}
