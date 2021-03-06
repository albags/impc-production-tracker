/******************************************************************************
 Copyright 2019 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package org.gentar.biology.project.mappers;

import org.gentar.biology.project.search.ProjectSearcherService;
import org.gentar.biology.project.search.Search;
import org.gentar.helpers.SearchCsvRecord;
import org.gentar.biology.project.search.SearchReport;
import org.gentar.biology.project.search.SearchReportDTO;
import org.gentar.helpers.CsvReader;
import org.gentar.helpers.CsvWriter;
import org.gentar.util.TextUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.gentar.biology.project.search.filter.ProjectFilter;
import org.gentar.biology.project.search.filter.ProjectFilterBuilder;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the endpoints for the Search functionality in GenTaR.
 * The basic functionality is , given an input (like a gene), to return the projects associated
 * to it, if any.
 */
@RestController
@RequestMapping("/api/projects/search")
@CrossOrigin(origins = "*")
public class ProjectSearcherController
{
    private final ProjectSearcherService projectSearcherService;
    private final SearchReportMapper searchReportMapper;
    private final CsvReader csvReader;
    private final CsvWriter<SearchCsvRecord> csvWriter;
    private final SearchCsvRecordMapper searchCsvRecordMapper;

    public ProjectSearcherController(
        ProjectSearcherService projectSearcherService,
        SearchReportMapper searchReportMapper,
        CsvReader csvReader,
        CsvWriter<SearchCsvRecord> csvWriter,
        SearchCsvRecordMapper searchCsvRecordMapper)
    {
        this.projectSearcherService = projectSearcherService;
        this.searchReportMapper = searchReportMapper;
        this.csvReader = csvReader;
        this.csvWriter = csvWriter;
        this.searchCsvRecordMapper = searchCsvRecordMapper;
    }

    /**
     * Endpoint to search projects based on an list of inputs (genes for example).
     * @param pageable Information about pagination.
     * @param searchTypeName Type of search. Currently only supported search by gene.
     * @param inputs The input to the search.
     * @param tpns List of TPN identifying projects to use as filter.
     * @param intentionTypeNames List of intentions to use as filter.
     * @param privacies List of privacies to use as filter.
     * @param workUnitsNames List of work units to use as filter.
     * @param workGroupNames List of work groups to use as filter.
     * @param consortiaNames List of consortia to use as filter.
     * @param summaryStatusNames List of summary status to use as filter.
     * @return a {@link SearchReportDTO} object with the search result.
     */
    @GetMapping
    public ResponseEntity search(
        Pageable pageable,
        @RequestParam(value = "searchTypeName", required = false) String searchTypeName,
        @RequestParam(value = "input", required = false) List<String> inputs,
        @RequestParam(value = "tpn", required = false) List<String> tpns,
        @RequestParam(value = "intentionTypeName", required = false) List<String> intentionTypeNames,
        @RequestParam(value = "privacyName", required = false) List<String> privacies,
        @RequestParam(value = "workUnitName", required = false) List<String> workUnitsNames,
        @RequestParam(value = "workGroupName", required = false) List<String> workGroupNames,
        @RequestParam(value = "consortiumName", required = false) List<String> consortiaNames,
        @RequestParam(value = "summaryStatusName", required = false) List<String> summaryStatusNames)
    {
        Search search =
            buildSearch(
                searchTypeName,
                inputs,
                tpns,
                intentionTypeNames,
                privacies,
                workUnitsNames,
                workGroupNames,
                consortiaNames,
                summaryStatusNames);
        SearchReport searchReport = projectSearcherService.executeSearch(search, pageable);
        SearchReportDTO searchReportDTO = searchReportMapper.toDto(searchReport);

        return new ResponseEntity<>(searchReportDTO, HttpStatus.OK);
    }

    /**
     * Endpoint to search projects based on an file that contains a list of inputs (genes for example).
     * @param pageable Information about pagination.
     * @param searchTypeName Type of search. Currently only supported search by gene.
     * @param file File with the input.
     * @param tpns List of TPN identifying projects to use as filter.
     * @param intentionTypeNames List of intentions to use as filter.
     * @param privacies List of privacies to use as filter.
     * @param workUnitsNames List of work units to use as filter.
     * @param workGroupNames List of work groups to use as filter.
     * @param consortiaNames List of consortia to use as filter.
     * @param summaryStatusNames List of summary status to use as filter.
     * @return a {@link SearchReportDTO} object with the search result.
     */
    @PostMapping
    public ResponseEntity searchByFile(
        Pageable pageable,
        @RequestParam(value = "searchTypeName", required = false) String searchTypeName,
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "tpn", required = false) List<String> tpns,
        @RequestParam(value = "intentionTypeName", required = false) List<String> intentionTypeNames,
        @RequestParam(value = "privacyName", required = false) List<String> privacies,
        @RequestParam(value = "workUnitName", required = false) List<String> workUnitsNames,
        @RequestParam(value = "workGroupName", required = false) List<String> workGroupNames,
        @RequestParam(value = "consortiumName", required = false) List<String> consortiaNames,
        @RequestParam(value = "summaryStatusName", required = false) List<String> summaryStatusNames)
    {
        List<String> inputs = getInputByFile(file);
        Search search =
            buildSearch(
                searchTypeName,
                inputs,
                tpns,
                intentionTypeNames,
                privacies,
                workUnitsNames,
                workGroupNames,
                consortiaNames,
                summaryStatusNames);
        SearchReport searchReport = projectSearcherService.executeSearch(search, pageable);
        SearchReportDTO searchReportDTO = searchReportMapper.toDto(searchReport);

        return new ResponseEntity<>(searchReportDTO, HttpStatus.OK);
    }

    List<String> getInputByFile(MultipartFile file)
    {
        List<List<String>> csvContent = csvReader.getCsvContentFromMultipartFile(file);
        List<String> inputs = new ArrayList<>();
        csvContent.forEach(x -> inputs.add(getCleanText(String.join(",", x))));
        return inputs;
    }

    /**
     * Executes a search with all the given filters and exports the results in a cvs file. To be
     * used when the input is set in the url as a comma separated strings containing the search elements.
     * @param response Http response needed to handle the file.
     * @param searchTypeName Type of search. Currently only supported search by gene.
     * @param inputs The inputs to search for.
     * @param tpns List of TPN identifying projects to use as filter.
     * @param intentionTypeNames List of intentions to use as filter.
     * @param privacies List of privacies to use as filter.
     * @param workUnitsNames List of work units to use as filter.
     * @param workGroupNames List of work groups to use as filter.
     * @param consortiaNames List of consortia to use as filter.
     * @param summaryStatusNames List of summary status to use as filter.
     * @throws Exception
     */
    @GetMapping("/exportSearch")
    public void exportCSV(
        HttpServletResponse response,
        @RequestParam(value = "searchTypeName", required = false) String searchTypeName,
        @RequestParam(value = "input", required = false) List<String> inputs,
        @RequestParam(value = "tpn", required = false) List<String> tpns,
        @RequestParam(value = "intentionTypeName", required = false) List<String> intentionTypeNames,
        @RequestParam(value = "privacyName", required = false) List<String> privacies,
        @RequestParam(value = "workUnitName", required = false) List<String> workUnitsNames,
        @RequestParam(value = "workGroupName", required = false) List<String> workGroupNames,
        @RequestParam(value = "consortiaName", required = false) List<String> consortiaNames,
        @RequestParam(value = "summaryStatusName", required = false) List<String> summaryStatusNames)
        throws Exception
    {
        exportCsv(
            response,
            searchTypeName,
            inputs,
            tpns,
            intentionTypeNames,
            privacies,
            workUnitsNames,
            workGroupNames,
            consortiaNames,
            summaryStatusNames);
    }

    /**
     * Executes a search with all the given filters and exports the results in a cvs file. To be
       used when the input given in a file containing in each line a search element.
     * @param response Http response needed to handle the file.
     * @param searchTypeName Type of search. Currently only supported search by gene.
     * @param file File with the input.
     * @param tpns List of TPN identifying projects to use as filter.
     * @param intentionTypeNames List of intentions to use as filter.
     * @param privacies List of privacies to use as filter.
     * @param workUnitsNames List of work units to use as filter.
     * @param workGroupNames List of work groups to use as filter.
     * @param consortiaNames List of consortia to use as filter.
     * @param summaryStatusNames List of summary status to use as filter.
     * @throws Exception
     */
    @PostMapping("/exportSearchByFile")
    public void exportCSVByFile(
        HttpServletResponse response,
        @RequestParam(value = "searchTypeName", required = false) String searchTypeName,
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "tpns", required = false) List<String> tpns,
        @RequestParam(value = "intentionTypeNames", required = false) List<String> intentionTypeNames,
        @RequestParam(value = "privacyNames", required = false) List<String> privacies,
        @RequestParam(value = "workUnitNames", required = false) List<String> workUnitsNames,
        @RequestParam(value = "workGroupNames", required = false) List<String> workGroupNames,
        @RequestParam(value = "consortiaNames", required = false) List<String> consortiaNames,
        @RequestParam(value = "summaryStatusNames", required = false) List<String> summaryStatusNames) throws Exception
    {

        List<String> inputs = getInputByFile(file);
        exportCsv(
            response,
            searchTypeName,
            inputs,
            tpns,
            intentionTypeNames,
            privacies,
            workUnitsNames,
            workGroupNames,
            consortiaNames,
            summaryStatusNames);
    }

    private void exportCsv(
        HttpServletResponse response,
        String searchTypeName,
        List<String> inputs,
        List<String> tpns,
        List<String> intentionTypeNames,
        List<String> privacies,
        List<String> workUnitsNames,
        List<String> workGroupNames,
        List<String> consortiaNames,
        List<String> summaryStatusNames) throws IOException
    {
        String filename = "download.csv";
        response.setContentType("text/csv");
        response.setHeader(
            HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        Search search =
            buildSearch(
                searchTypeName,
                inputs,
                tpns,
                intentionTypeNames,
                privacies,
                workUnitsNames,
                workGroupNames,
                consortiaNames,
                summaryStatusNames);
        SearchReport searchReport = projectSearcherService.executeSearch(search);
        var searchCsvRecords = searchCsvRecordMapper.toDtos(searchReport.getResults());
        csvWriter.writeListToCsv(response.getWriter(), searchCsvRecords, SearchCsvRecord.HEADERS);
    }

    private String getCleanText(String text)
    {
        return TextUtil.cleanTextContent(text);
    }

    private Search buildSearch(
        String searchTypeName,
        List<String> inputs,
        List<String> tpns,
        List<String> intentionTypeNames,
        List<String> privacies,
        List<String> workUnitsNames,
        List<String> workGroupNames,
        List<String> consortiaNames,
        List<String> summaryStatusNames)
    {
        ProjectFilter projectFilter = ProjectFilterBuilder.getInstance()
            .withTpns(tpns)
            .withIntentions(intentionTypeNames)
            .withWorkUnitNames(workUnitsNames)
            .withWorkGroupNames(workGroupNames)
            .withPrivacies(privacies)
            .withConsortiaNames(consortiaNames)
            .withSummaryStatusNames(summaryStatusNames)
            .build();
        return new Search(searchTypeName, inputs, projectFilter);
    }
}
