package org.gentar.report.collection.product;

public interface ProductReportService {
    /**
     * Persists a Product Report to the database, which is used by the CDA in the products section of the gene page.
     */
    void generateProductReport();
}
