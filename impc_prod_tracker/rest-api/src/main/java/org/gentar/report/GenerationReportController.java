package org.gentar.report;

import org.gentar.report.collection.gene_interest.GeneInterestReportServiceImpl;
import org.gentar.report.collection.mgi_crispr_allele.MgiCrisprAlleleReportServiceImpl;
import org.gentar.report.collection.mgi_phenotyping_colony.MgiPhenotypingColonyReportServiceImpl;
import org.gentar.report.collection.phenotyping_colony.PhenotypingColonyReportServiceImpl;
import org.gentar.report.collection.product.ProductReportServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/tracking-api/reports/generate")
public class GenerationReportController
{
    private final GeneInterestReportServiceImpl geneInterestReportService;
    private final PhenotypingColonyReportServiceImpl phenotypingColonyReportService;
    private final ProductReportServiceImpl productReportService;
    private final MgiCrisprAlleleReportServiceImpl mgiCrisprAlleleService;
    private final MgiPhenotypingColonyReportServiceImpl mgiPhenotypingColonyReportService;

    public GenerationReportController(GeneInterestReportServiceImpl geneInterestReportService,
                                      PhenotypingColonyReportServiceImpl phenotypingColonyReportService,
                                      ProductReportServiceImpl productReportService,
                                      MgiCrisprAlleleReportServiceImpl mgiCrisprAlleleService,
                                      MgiPhenotypingColonyReportServiceImpl mgiPhenotypingColonyReportService)
    {
        this.geneInterestReportService = geneInterestReportService;
        this.phenotypingColonyReportService = phenotypingColonyReportService;
        this.productReportService = productReportService;
        this.mgiCrisprAlleleService = mgiCrisprAlleleService;
        this.mgiPhenotypingColonyReportService = mgiPhenotypingColonyReportService;
    }

    @GetMapping("/gene_interest")
    public void exportGeneInterest( HttpServletResponse response) throws IOException
    {
        geneInterestReportService.generateGeneInterestReport();
    }

    @GetMapping("/phenotyping_colonies")
    public void exportPhenotypingColonies(HttpServletResponse response) throws IOException
    {
        phenotypingColonyReportService.generatePhenotypingColonyReport();
    }

    @GetMapping("/products")
    public void exportProducts(HttpServletResponse response) throws IOException
    {
        productReportService.generateProductReport();
    }

    @GetMapping("/mgi/phenotyping_colonies")
    public void exportMgiPhenotypingColonies(HttpServletResponse response) throws IOException
    {
        mgiPhenotypingColonyReportService.generateMgiPhenotypingColonyReport();
    }


    @GetMapping("/mgi/crispr_alleles")
    public void exportCrisprAlleles(HttpServletResponse response) throws IOException
    {
        mgiCrisprAlleleService.generateMgiCrisprAlleleReport();
    }
}
