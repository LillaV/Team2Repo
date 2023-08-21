package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.campaign.CampaignDto;
import de.msg.javatraining.donationmanager.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/campaigns")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;
    @GetMapping()
    public List<CampaignDto> getCampaigns(){
        //return campaignService.getCampaigns();
        return null;
    }

    @PostMapping()
    public ResponseEntity<String> saveCampaign(@RequestBody CampaignDto campaignDto) {
        try{
            campaignService.saveCampaign(campaignDto);
            return new ResponseEntity<>("Campaign saved", HttpStatus.OK);
        }
        catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity updateCampaign(@PathVariable("id") Long id,@RequestBody() CampaignDto campaign) {
        try{
            campaignService.updateCampaign(id, campaign);
            return new ResponseEntity<>("Campaign saved", HttpStatus.OK);
        }
        catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("{id}")
    public CampaignDto findCampaignById(@PathVariable(name = "id") Long id){
        // return campaignService.findById(id);
        return null;
    }
}
