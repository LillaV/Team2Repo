package de.msg.javatraining.donationmanager.controller.app;

import com.opencsv.CSVWriter;
import de.msg.javatraining.donationmanager.persistence.dtos.campaign.CampaignDto;
import de.msg.javatraining.donationmanager.persistence.dtos.campaign.CampaignFilterPair;
import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.service.CampaignService;
import de.msg.javatraining.donationmanager.service.filter.CampaignSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/campaigns")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private CampaignSpecifications campaignSpecifications;

    @GetMapping
    @PreAuthorize("hasAuthority('CAMP_MANAGEMENT' ) or hasAuthority('CAMP_REPORTING') or hasAuthority('CAMP_REPORT_RESTRICTED' )")
    public List<CampaignDto> getCampaigns(@RequestParam(name = "offset", required = false) Integer offset, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        if (offset != null && pageSize != null) {
            return campaignService.allCampaignsWithPagination(offset, pageSize);
        } else {
            return campaignService.getCampaigns();
        }
    }

    @GetMapping("/size")
    @PreAuthorize("hasAuthority('CAMP_MANAGEMENT') or hasAuthority('CAMP_REPORTING' or hasAuthority('CAMP_REPORT_RESTRICTED' ))")
    public long getSize() {
        return campaignService.getSize();
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('CAMP_MANAGEMENT')")
    public ResponseEntity<String> saveCampaign(@RequestBody CampaignDto campaignDto) {
        try {
            campaignService.saveCampaign(campaignDto);
            return new ResponseEntity<>("Campaign saved", HttpStatus.OK);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CAMP_MANAGEMENT')")
    public ResponseEntity updateCampaign(@PathVariable("id") Long id, @RequestBody() CampaignDto campaign) {
        try {
            campaignService.updateCampaign(id, campaign);
            return new ResponseEntity<>("Campaign saved", HttpStatus.OK);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CAMP_MANAGEMENT' )")
    public CampaignDto findCampaignById(@PathVariable(name = "id") Long id) {
        return campaignService.findById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CAMP_MANAGEMENT' )")
    public ResponseEntity deleteCampaignById(@PathVariable(name = "id") Long id) {
        try {
            campaignService.deleteCampaignById(id);
            return new ResponseEntity<>("Campaign deleted", HttpStatus.OK);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('CAMP_MANAGEMENT' )")
    public CampaignFilterPair filterCampaigns(@RequestParam(name = "offset", required = false) int offset, @RequestParam(name = "pageSize", required = false) int pageSize, @RequestParam(name = "nameTerm", required = false) String nameTerm, @RequestParam(name = "purposeTerm", required = false) String purposeTerm) {
        Specification<Campaign> spec = campaignSpecifications.filterCampaigns(nameTerm, purposeTerm);

        return campaignService.filterCampaignsWithPaging(spec, PageRequest.of(offset, pageSize));
    }

    @GetMapping("/search/{text}")
    @PreAuthorize("hasAuthority('CAMP_MANAGEMENT' )")
    public  List<CampaignDto> sercahCampaigns(@PathVariable(name = "text") String text){
        return this.campaignService.searchForCampaigns(text);
    }


    @GetMapping("/export-csv")
    public ResponseEntity<ByteArrayResource> exportCsv(
            @RequestParam(name = "nameTerm", required = false) String nameTerm,
            @RequestParam(name = "purposeTerm", required = false) String purposeTerm
    ){
        Specification<Campaign> spec = campaignSpecifications.filterCampaigns(
                nameTerm, purposeTerm
        );

        List<Campaign> campaigns = campaignService.filterCampaigns(spec);

        byte[] csvData = generateCsvData(campaigns);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Campaigns.csv");

        ByteArrayResource resource = new ByteArrayResource(csvData);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(resource);
    }

    private byte[] generateCsvData(List<Campaign> campaigns)  {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(outputStream))) {
            String[] header = {
                    "Name", "Purpose", "Donators"
            };
            csvWriter.writeNext(header);

            List<String[]> rows = new ArrayList<>();
            for (Campaign campaign: campaigns){
                    String donatorsInfo = campaignService.getDistinctBenefactorsByCampaignId(campaign.getId()).stream()
                            .map(donator -> {
                                if (donator != null) {
                                    StringBuilder donatorInfoBuilder = new StringBuilder(donator.getFirstName());
                                    if (donator.getAdditionalName() != null) {
                                        donatorInfoBuilder.append(" ").append(donator.getAdditionalName());
                                    }
                                    if (donator.getMaidenName() != null) {
                                        donatorInfoBuilder.append(" (Maiden: ").append(donator.getMaidenName()).append(")");
                                    }
                                    donatorInfoBuilder.append(" ").append(donator.getLastName());
                                    return donatorInfoBuilder.toString();
                                }
                                return "";
                            })
                            .collect(Collectors.joining(", "));

                String[] row = {
                        campaign.getName(),
                        campaign.getPurpose(),
                        donatorsInfo
                };

                rows.add(row);
            }

            csvWriter.writeAll(rows);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return outputStream.toByteArray();
    }
}
