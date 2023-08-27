package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.donator.SimpleDonatorDto;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import de.msg.javatraining.donationmanager.service.DonatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/donators")
public class DonatorController {
    @Autowired
    private DonatorService donatorService;

    @GetMapping
    @PreAuthorize("hasAuthority(BENEF_MANAGEMENT)")
    public List<SimpleDonatorDto> getDonations(@RequestParam(name = "offset", required = false) Integer offset, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        if (offset != null && pageSize != null) {
            return donatorService.allDonatorsWithPagination(offset, pageSize);
        } else {
            return donatorService.getDonators();
        }
    }

    @GetMapping("/size")
    @PreAuthorize("hasAuthority(BENEF_MANAGEMENT)")
    public Long getSize() {
        return donatorService.getSize();
    }

    @PostMapping()
    @PreAuthorize("hasAuthority(BENEF_MANAGEMENT)")
    public TextResponse saveDonator(@RequestBody SimpleDonatorDto donator) {
        return donatorService.saveDonator(donator);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority(BENEF_MANAGEMENT)")
    public SimpleDonatorDto getDonatorById(@PathVariable(name = "id") Long id) {
        return donatorService.findById(id);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(BENEF_MANAGEMENT)")
    public TextResponse updateDonator(@RequestBody() SimpleDonatorDto donator, @PathVariable("id") Long id) {
        return donatorService.updateDonator(id, donator);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(BENEF_MANAGEMENT)")
    public TextResponse deleteDonatorById(@PathVariable("id") Long id) {
        return donatorService.deleteDonatorById(id);
    }
}
