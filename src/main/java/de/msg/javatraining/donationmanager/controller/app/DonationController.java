package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.donation.SimpleDonationDto;
import de.msg.javatraining.donationmanager.persistence.dtos.donation.UpdateDonationDto;
import de.msg.javatraining.donationmanager.persistence.model.Donation;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.service.DonationService;
import de.msg.javatraining.donationmanager.service.UserService;
import de.msg.javatraining.donationmanager.service.utils.DonationSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/donations")
public class DonationController {
    @Autowired
    private DonationService donationService;

    @Autowired
    private  UserService userService;

//    @GetMapping("/{offset}/{pageSize}")
//    public List<SimpleDonationDto> getPage(@PathVariable(name = "offset") int offset, @PathVariable(name = "pageSize") int pageSize) {
//        return donationService.allDonationsWithPagination(offset, pageSize);
//    }

    @GetMapping()
    public List<SimpleDonationDto> getPage(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "pageSize") int pageSize) {
        return donationService.allDonationsWithPagination(offset, pageSize);
    }


    @PostMapping()
    public ResponseEntity<String> saveDonation(@RequestBody SimpleDonationDto donationDto) {
        try{
            donationService.saveDonation(donationDto);
            return new ResponseEntity<>("Donation saved", HttpStatus.OK);
        }
        catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity updateDonation(@RequestBody() UpdateDonationDto updateDonationDto, @PathVariable("id") Long id) {
        Donation donation = donationService.findById(id);
        if(!donation.isApproved()){
            donationService.updateDonation(id, updateDonationDto);
            return new ResponseEntity<>( "Donation updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Donation is already approved, you cannot edit it anymore.", HttpStatus.FORBIDDEN);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteDonation(@PathVariable("id") Long id) {
        Donation donationDto = donationService.findById(id);
        if(!donationDto.isApproved()){
            donationService.deleteDonation(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Donation is already approved, you cannot delete it anymore.", HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/approve/{donationId}/{approvedById}")
    public ResponseEntity approveDonation(@PathVariable Long donationId, @PathVariable Long approvedById){
        Donation donation = donationService.findById(donationId);
        User approvedBy = userService.findById(approvedById);
        if (donation.getCreatedBy().getId() != approvedById){
            donationService.approveDonation(donation, approvedBy);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity("4 Augen Prinzip is violated", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/filter")
    public List<SimpleDonationDto> filterDonations(
            @RequestParam(name = "minValue", required = false) Float minValue,
//            @RequestParam(name = "minValue", defaultValue = "0.0") Float minValue,
            @RequestParam(name = "maxValue", required = false) Float maxValue,
            @RequestParam(name = "value", required = false) Float value,
            @RequestParam(name = "currency", required = false) String currency,
            @RequestParam(name = "campaignId", required = false) Long campaignId,
            @RequestParam(name = "searchTerm", required = false) String searchTerm,
            @RequestParam(name = "createdById", required = false) Long createdById,
            @RequestParam(name = "createDate", required = false) LocalDate createDate,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate,
            @RequestParam(name = "benefactorId", required = false) Long benefactorId,
            @RequestParam(name = "approved", required = false) Boolean approved,
            @RequestParam(name = "approvedById", required = false) Long approvedById,
            @RequestParam(name = "approvedDateStart", required = false) LocalDate approvedDateStart,
            @RequestParam(name = "approvedDateEnd", required = false) LocalDate approvedDateEnd,
            Pageable pageable
    ) {
        Specification<Donation> spec = DonationSpecifications.filterDonations(
                minValue, maxValue, value, currency,
                campaignId, searchTerm, createdById,
                createDate, startDate, endDate,
                benefactorId, approved,
                approvedById, approvedDateStart, approvedDateEnd
        );

        return donationService.filterDonationsWithPaging(
                spec,
                pageable
        );
    }
}