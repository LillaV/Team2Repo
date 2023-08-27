package de.msg.javatraining.donationmanager.controller.app;

import com.opencsv.CSVWriter;
import de.msg.javatraining.donationmanager.config.exception.InvalidRequestException;
import de.msg.javatraining.donationmanager.persistence.dtos.donation.DonationFilterPair;
import de.msg.javatraining.donationmanager.persistence.dtos.donation.SimpleDonationDto;
import de.msg.javatraining.donationmanager.persistence.dtos.donation.UpdateDonationDto;
import de.msg.javatraining.donationmanager.persistence.dtos.mappers.DonationMapper;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import de.msg.javatraining.donationmanager.persistence.model.Donation;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.persistence.repository.UserRepository;
import de.msg.javatraining.donationmanager.service.DonationService;
import de.msg.javatraining.donationmanager.service.filter.DonationSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.util.ArrayList;
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
    @Autowired
    private DonationMapper donationMapper;


    @GetMapping("/currencies")
    public List<String> getCurrencies() {
        return donationService.getCurrencies();
    }

    @GetMapping("/{id}")
    public SimpleDonationDto findDonationById(@PathVariable(name = "id") Long id) {
        return donationMapper.donationToSimpleDonationDto(donationService.findById(id));
    }

    @GetMapping("/size")
    public long getSize() {
        return donationService.getSize();
    }

    @PostMapping()
    public TextResponse saveDonation(@RequestBody SimpleDonationDto donationDto) {
        try {
            donationService.saveDonation(donationDto);
            return new TextResponse("Donation added");
        } catch (Exception exception) {
            return new TextResponse(exception.getMessage());
        }
    }

    @PutMapping("/{id}")
    public TextResponse updateDonation(@RequestBody() UpdateDonationDto updateDonationDto, @PathVariable("id") Long id) {
        Donation donation = donationService.findById(id);
        if (!donation.getApproved()) {
            if (!donation.getApproved()) {
                donationService.updateDonation(id, updateDonationDto);
                return new TextResponse("Donation updated");
            } else {
                return new TextResponse("Donation is already approved, you cannot edit it anymore.");
            }
        }
        throw new InvalidRequestException("The donation you are trying to modify can't be updated");
    }

    @DeleteMapping("/{id}")
    public TextResponse deleteDonation(@PathVariable("id") Long id) {
        Donation donationDto = donationService.findById(id);
        if (!donationDto.getApproved()) {
            donationService.deleteDonation(id);
            return new TextResponse("Donation updated successfully!");
        } else {
            return new TextResponse("Donation is already approved, you cannot delete it anymore.");
        }
    }

    @PutMapping("/approve")
    public TextResponse approveDonation(@RequestParam(name = "donationId") Long donationId, @RequestParam(name = "approvedById") Long approvedById) {
        Donation donation = donationService.findById(donationId);
        Optional<User> approvedBy = userRepository.findById(approvedById);
        if (donation.getCreatedBy().getId() != approvedById && approvedBy.isPresent()) {
            donationService.approveDonation(donation, approvedBy.get());
            return new TextResponse("Donation approved successfully");
        } else {
            return new TextResponse("You cannot  approve your own donation!");
        }
    }

    @GetMapping("/filter")
    public DonationFilterPair filterDonations(
            @RequestParam(name = "offset") Integer offset,
            @RequestParam(name = "pageSize") Integer pageSize,
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

    @GetMapping("/export-csv")
    public ResponseEntity<ByteArrayResource> exportCsv(
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
            @RequestParam(name = "approvedDateEnd", required = false) LocalDate approvedDateEnd) {
        Specification<Donation> spec = donationSpecifications.filterDonations(
                minValue, maxValue, value, currency,
                campaignId, searchTerm, createdById,
                startDate, endDate,
                benefactorId, approved,
                approvedById, approvedDateStart, approvedDateEnd
        );
        List<Donation> filteredDonations = donationService.filterDonations(spec);

        byte[] csvData = generateCsvData(filteredDonations);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Donations.csv");

        ByteArrayResource resource = new ByteArrayResource(csvData);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(resource);
    }

    private byte[] generateCsvData(List<Donation> filteredDonations) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(outputStream))) {

            String[] header = {
                    "Amount", "Currency", "Campaign", "Creator", "Creation Date",
                    "Benefactor", "Approved", "Approved By", "Approval Date", "Notes"
            };
            csvWriter.writeNext(header);

            List<String[]> rows = new ArrayList<>();
            for (Donation donation : filteredDonations) {
                String creatorFullName = donation.getCreatedBy().getFirstName() + " " + donation.getCreatedBy().getLastName();
                String benefactorName = donation.getBenefactor() != null ?
                        donation.getBenefactor().getFirstName() + " " + donation.getBenefactor().getLastName() :
                        "Unknown";
                String approved = donation.getApproved() ? "Yes" : "No";
                String approvedByName = donation.getApprovedBy() != null ?
                        donation.getApprovedBy().getFirstName() + " " + donation.getApprovedBy().getLastName() : "";
                String approvalDate = donation.getApprovedDate() != null ?
                        String.valueOf(donation.getApprovedDate()) : "";
                String notes = donation.getNotes() != null ? donation.getNotes() : "";

                String[] row = {
                        String.valueOf(donation.getAmount()),
                        donation.getCurrency(),
                        donation.getCampaign().getName(),
                        creatorFullName,
                        String.valueOf(donation.getCreateDate()),
                        benefactorName,
                        approved,
                        approvedByName,
                        approvalDate,
                        notes
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

