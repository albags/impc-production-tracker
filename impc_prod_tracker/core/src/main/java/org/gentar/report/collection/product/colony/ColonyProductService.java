package org.gentar.report.collection.product.colony;

import java.util.List;

public interface ColonyProductService {
    /**
     * @return a list of ColonyProductProjection Spring database projections containing
     *         colony distribution information
     */
    List<ColonyProductProjection> getSelectedColonyProductProjections();
}
