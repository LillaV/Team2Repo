package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.donation.SimpleDonationDto;
import de.msg.javatraining.donationmanager.persistence.dtos.donation.UpdateDonationDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.UserMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import de.msg.javatraining.donationmanager.persistence.model.Donation;
import de.msg.javatraining.donationmanager.persistence.model.DonationFilterPair;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import de.msg.javatraining.donationmanager.service.DonationService;
import de.msg.javatraining.donationmanager.service.UserService;
import de.msg.javatraining.donationmanager.service.filter.DonationSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/donations")
public class DonationController {
    @Autowired
    private DonationService donationService;

    @Autowired
    private DonationSpecifications donationSpecifications;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/currencies")
    public List<String> getCurrencies(){
        return donationService.getCurrencies();
    }

    @GetMapping("/{id}")
    public Donation findDonationById(@PathVariable(name = "id") Long id){
        return donationService.findById(id);
    }

    @GetMapping("/size")
    public long getSize(){
        return donationService.getSize();
    }

    @PostMapping()
    public TextResponse saveDonation(@RequestBody SimpleDonationDto donationDto) {
        try{
            donationService.saveDonation(donationDto);
            return new TextResponse("Donation added");
        }
        catch (Exception exception){
            return new TextResponse(exception.getMessage());
        }
    }
    @PutMapping("/{id}")
    public TextResponse updateDonation(@RequestBody() UpdateDonationDto updateDonationDto, @PathVariable("id") Long id) {
        Donation donation = donationService.findById(id);
        if(!donation.isApproved()){
            donationService.updateDonation(id, updateDonationDto);
            return new TextResponse( "Donation updated");
        } else {
            return new TextResponse("Donation is already approved, you cannot edit it anymore.");
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDonation(@PathVariable("id") Long id) {
        Donation donationDto = donationService.findById(id);
        if(!donationDto.isApproved()){
            donationService.deleteDonation(id);
            return new ResponseEntity<>("Donation deleted successfully", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Donation is already approved, you cannot delete it anymore.", HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/approve")
    public ResponseEntity approveDonation(@RequestParam(name = "donationId") Long donationId,
                                          @RequestParam(name = "approvedById") Long approvedById){
        Donation donation = donationService.findById(donationId);
        Optional<User> approvedBy = userRepository.findById(approvedById);
        if (donation.getCreatedBy().getId() != approvedById && approvedBy.isPresent()){
            donationService.approveDonation(donation, approvedBy.get());
            return new ResponseEntity<>("Donation approved successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("4 Augen Prinzip is violated", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/filter")
    public DonationFilterPair filterDonations(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "pageSize") int pageSize,
            @RequestParam(name = "minAmount", required = false) Float minValue,
            @RequestParam(name = "maxAmount", required = false) Float maxValue,
            @RequestParam(name = "value", required = false) Float value,
            @RequestParam(name = "currency", required = false) String currency,
            @RequestParam(name = "campaignId", required = false) Long campaignId,
            @RequestParam(name = "searchTerm", required = false) String searchTerm,
            @RequestParam(name = "createdById", required = false) Long createdById,
            @RequestParam(name = "createDateStart", required = false) LocalDate startDate,
            @RequestParam(name = "createDateEnd", required = false) LocalDate endDate,
            @RequestParam(name = "benefactorId", required = false) Long benefactorId,
            @RequestParam(name = "approved", required = false) Boolean approved,
            @RequestParam(name = "approvedById", required = false) Long approvedById,
            @RequestParam(name = "approvedDateStart", required = false) LocalDate approvedDateStart,
            @RequestParam(name = "approvedDateEnd", required = false) LocalDate approvedDateEnd
    ) {
        Specification<Donation> spec = donationSpecifications.filterDonations(
                minValue, maxValue, value, currency,
                campaignId, searchTerm, createdById,
                startDate, endDate,
                benefactorId, approved,
                approvedById, approvedDateStart, approvedDateEnd
        );

        return donationService.filterDonationsWithPaging(
                spec,
                PageRequest.of(offset, pageSize)
        );
    }
}
