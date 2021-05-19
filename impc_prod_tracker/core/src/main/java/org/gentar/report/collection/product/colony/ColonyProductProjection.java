package org.gentar.report.collection.product.colony;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface ColonyProductProjection
{
    @Value("#{target.outcomeTpo}")
    String getOutcomeTpo();

    @Value("#{target.mutationMin}")
    String getMutationMin();

    @Value("#{target.colonyName}")
    String getColonyName();

    @Value("#{target.planWorkUnitName}")
    String getPlanWorkUnitName();

    @Value("#{target.colonyStatusName}")
    String getColonyStatusName();

    @Value("#{target.colonyStatusDate}")
    LocalDateTime getColonyStatusDate();

    @Value("#{target.distributionWorkUnitName}")
    String getDistributionWorkUnitName();

    @Value("#{target.distributionNetworkName}")
    String getDistributionNetworkName();
}
