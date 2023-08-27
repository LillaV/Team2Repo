package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.campaign.CampaignDto;
import de.msg.javatraining.donationmanager.persistence.dtos.campaign.CampaignFilterPair;
import de.msg.javatraining.donationmanager.persistence.model.Campaign;
import de.msg.javatraining.donationmanager.service.CampaignService;
import de.msg.javatraining.donationmanager.service.filter.CampaignSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaigns")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private CampaignSpecifications campaignSpecifications;

    @GetMapping
    @PreAuthorize("hasAuthority(CAMP_MANAGEMENT ) or hasAuthority(CAMP_REPORTING) or hasAuthority(CAMP_REPORT_RESTRICTED )")
    public List<CampaignDto> getCampaigns(@RequestParam(name = "offset", required = false) Integer offset, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        if (offset != null && pageSize != null) {
            return campaignService.allCampaignsWithPagination(offset, pageSize);
        } else {
            return campaignService.getCampaigns();
        }
    }

    @GetMapping("/size")
    @PreAuthorize("hasAuthority(CAMP_MANAGEMENT) or hasAuthority(CAMP_REPORTING or hasAuthority(CAMP_REPORT_RESTRICTED ))")
    public long getSize() {
        return campaignService.getSize();
    }

    @PostMapping()
    @PreAuthorize("hasAuthority(CAMP_MANAGEMENT)")
    public ResponseEntity<String> saveCampaign(@RequestBody CampaignDto campaignDto) {
        try {
            campaignService.saveCampaign(campaignDto);
            return new ResponseEntity<>("Campaign saved", HttpStatus.OK);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(CAMP_MANAGEMENT)")
    public ResponseEntity updateCampaign(@PathVariable("id") Long id, @RequestBody() CampaignDto campaign) {
        try {
            campaignService.updateCampaign(id, campaign);
            return new ResponseEntity<>("Campaign saved", HttpStatus.OK);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority(CAMP_MANAGEMENT )")
    public CampaignDto findCampaignById(@PathVariable(name = "id") Long id) {
        return campaignService.findById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(CAMP_MANAGEMENT )")
    public ResponseEntity deleteCampaignById(@PathVariable(name = "id") Long id) {
        try {
            campaignService.deleteCampaignById(id);
            return new ResponseEntity<>("Campaign deleted", HttpStatus.OK);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAuthority(CAMP_MANAGEMENT )")
    public CampaignFilterPair filterCampaigns(@RequestParam(name = "offset", required = false) int offset, @RequestParam(name = "pageSize", required = false) int pageSize, @RequestParam(name = "nameTerm", required = false) String nameTerm, @RequestParam(name = "purposeTerm", required = false) String purposeTerm) {
        Specification<Campaign> spec = campaignSpecifications.filterCampaigns(nameTerm, purposeTerm);

        return campaignService.filterCampaignsWithPaging(spec, PageRequest.of(offset, pageSize));
    }

    @GetMapping("/search/{text}")
    @PreAuthorize("hasAuthority(CAMP_MANAGEMENT )")
    public  List<CampaignDto> sercahCampaigns(@PathVariable(name = "text") String text){
        return this.campaignService.searchForCampaigns(text);
    }

}
