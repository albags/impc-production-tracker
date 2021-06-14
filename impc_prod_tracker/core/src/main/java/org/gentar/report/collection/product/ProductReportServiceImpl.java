package org.gentar.report.collection.product;

import org.gentar.report.ReportServiceImpl;
import org.gentar.report.ReportTypeName;
import org.gentar.report.collection.gene_interest.gene.GeneInterestReportGeneProjection;
import org.gentar.report.collection.product.colony.ColonyProductProjection;
import org.gentar.report.collection.product.colony.ColonyProductServiceImpl;
import org.gentar.report.collection.product.mutation.MutationGeneProductProjection;
import org.gentar.report.collection.product.mutation.MutationGeneProductServiceImpl;
import org.springframework.stereotype.Component;

import javax.persistence.TupleElement;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ProductReportServiceImpl implements ProductReportService
{
    private final ReportServiceImpl reportService;
    private final ColonyProductServiceImpl colonyProductService;
    private final MutationGeneProductServiceImpl mutationGeneProductService;
    private final ProductReportProjectionMergeHelperImpl projectionMergeHelper;

    private List<String> reportRows;
    private List<ColonyProductProjection> cpp;
    private List<MutationGeneProductProjection> mpp;
    private Map<String, List<String>> coloniesForGeneMutations;
    private List<List<String>> test;
    private Map<String, List<String>> test2;

    public ProductReportServiceImpl(ReportServiceImpl reportService,
                                    ColonyProductServiceImpl colonyProductService,
                                    MutationGeneProductServiceImpl mutationGeneProductService,
                                    ProductReportProjectionMergeHelperImpl projectionMergeHelper)
    {
        this.reportService = reportService;
        this.colonyProductService = colonyProductService;
        this.mutationGeneProductService = mutationGeneProductService;
        this.projectionMergeHelper = projectionMergeHelper;
    }

    @Override
    public void generateProductReport() {
        cpp = colonyProductService.getSelectedColonyProductProjections();
        List<String> filteredMutationIds = getFilteredMutationIds();
        mpp = mutationGeneProductService.getSelectedMutationGeneProjections(filteredMutationIds);

        Map<String, List<ColonyProductProjection>> cppColoniesByMutation = cpp.stream()
                .collect(Collectors.groupingBy(
                        ColonyProductProjection::getMutationMin));

        Map<String, List<MutationGeneProductProjection>> mppMutationByGene = mpp.stream()
                .collect(Collectors.groupingBy(
                        MutationGeneProductProjection::getMutationMin));


        Map<String, Set<String>> gpsProjectsByGene = mpp.stream()
                .collect(Collectors.groupingBy(
                        MutationGeneProductProjection::getMutationMin,
                        Collectors.mapping(MutationGeneProductProjection::getMutationCategorizationName, Collectors.toSet())));



        reportRows = prepareReport();

//        System.out.println(reportRows);

        saveReport();
    }

    private List<String> getFilteredMutationIds() {
        return cpp
                .stream()
                .map(e -> e.getMutationMin())
                .collect(Collectors.toList());
    }

    private List<String> prepareReport( ) {
//        List<ColonyProductProjection> sortedEntries =
//                cpp.stream()
//                        .filter(x -> filteredOutcomeMutationMap.containsKey(x.getOutcomeId()))
//                        .filter(x -> filteredMutationGeneMap.containsKey(filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationId()))
//                        .sorted(Comparator.comparing(x -> filteredMutationGeneMap.get(
//                                filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationId()).getSymbol()
//                                )
//                        )
//                        .collect(Collectors.toList());

        return cpp
                .stream()
                .map(x -> constructRow(x))
                .collect(Collectors.toList());
    }

    private String constructRow(ColonyProductProjection colonyProductProjection) {
//        Optional<MutationGeneProductProjection> mutationInfo = mpp.stream()
//                                .filter(x -> x.getMutationMin() == colonyProductProjection.getMutationMin())
//                                .findFirst();
//        String mutationSymbol = filteredOutcomeMutationMap.get(x.getOutcomeId()).getSymbol();
//        Gene g = filteredMutationGeneMap.get(filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationId());
//        String cohortProductionWorkUnit = x.getCohortProductionWorkUnit() == null ? x.getPhenotypingWorkUnit() : x.getCohortProductionWorkUnit();
        return  "IMPC" + "\t" +
                "marker_symbol" + "\t" +
                "\t" +
                "\t" +
                "mgi_accession_id" + "\t" +
                "\t" +
                "\t" +
                "\t" +
                "\t" +
                "\t" +
                colonyProductProjection.getColonyName() + "\t" +
                "allele_type" + "\t" + // mutation_categorization_name
                "allele_name" + "\t" + // mutation_table
                "\t" +
                "\t" +
                "mouse" + "\t" +
                "\t" +
                "\t" +
                colonyProductProjection.getPlanWorkUnitName() + "\t" +
                "\t" +
                "TRUE" + "\t" +
                "type_of_microinjection:Cas9/Crispr" + "\t" +
                colonyProductProjection.getColonyStatusName() + "\t" +
                colonyProductProjection.getColonyStatusDate().toString() + "\t" +
                "\t" +
                "\t" +
                "\t" +
                "\t" +
                "\t" +
                "\t" +
                "\t" +
                colonyProductProjection.getDistributionNetworkName() + "\t" +
                "order_links" + "\t" +
                colonyProductProjection.getDistributionWorkUnitName() + "\t" +
                "contact_links" + "\t" +
                "\t" +
                "\t" +
                "\t" +
                "\t" +
                "\t";
    }

    private void saveReport() {
        String report = assembleReport();
        reportService.saveReport(ReportTypeName.PRODUCTS, report);
    }

    private String assembleReport() {

        String report = reportRows
                .stream()
                .collect(Collectors.joining("\n"));

        String header = generateReportHeaders();

        return Arrays.asList(header, report).stream().collect(Collectors.joining("\n"));
    }

    private String generateReportHeaders()
    {
        List<String> headers = Arrays.asList(
            "allele_design_project", // "IMPC"
            "marker_symbol", // gene_table
            "product_id", // 'M' + attempt_id => empty
            "marker_mgi_accession_id", // empty
            "mgi_accession_id", // gene_table
            "marker_type", // empty
            "marker_name", // empty
            "marker_synonym", // empty
            "allele_mgi_accession_id", // empty
            "allele_symbol", // empty
            "name", // colony_name
            "allele_type", // mutation_categorization_name
            "allele_name", // mutation_table
            "allele_has_issue", // empty
            "allele_id", // empty
            "type", // "mouse" for CRISPR
            "allele_synonym", // empty
            "genetic_info", // empty
            "production_centre", // plan_work_unit
            "production_pipeline", // empty
            "production_completed", // "TRUE"
            "production_info", // "type_of_microinjection:Cas9/Crispr"
            "status", // only "Genotype Confirmed" colonies
            "status_date", // "Genotype confirmed" status date
            "qc_data", // empty
            "associated_product_colony_name", // empty
            "associated_product_es_cell_name", // empty
            "associated_product_vector_name", // empty
            "associated_products_colony_names", // empty
            "associated_products_es_cell_names", // empty
            "associated_products_vector_names", // empty
            "order_names", // network_distribution
            "order_links", // build link
            "contact_names", // plan_work_unit
            "contact_links", // build mailto
            "other_links", // empty
            "loa_assays", // empty
            "ikmc_project_id", // empty
            "design_id", // empty
            "cassette" // empty
        );

        String headerString =   headers
                .stream()
                .collect(Collectors.joining("\t"));

        return headerString;

    }
}
