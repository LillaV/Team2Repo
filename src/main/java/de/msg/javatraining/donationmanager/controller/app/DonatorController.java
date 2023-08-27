package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.donator.SimpleDonatorDto;
import de.msg.javatraining.donationmanager.persistence.dtos.response.TextResponse;
import de.msg.javatraining.donationmanager.service.DonatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/donators")
public class DonatorController {
    @Autowired
    private DonatorService donatorService;

    @GetMapping
    public List<SimpleDonatorDto> getDonations(@RequestParam(name = "offset", required = false) Integer offset, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        if (offset != null && pageSize != null) {
            return donatorService.allDonatorsWithPagination(offset, pageSize);
        } else {
            return donatorService.getDonators();
        }
    }

    @GetMapping("/size")
    public Long getSize() {
        return donatorService.getSize();
    }

    @PostMapping()
    public TextResponse saveDonator(@RequestBody SimpleDonatorDto donator) {
        return donatorService.saveDonator(donator);
    }

    @GetMapping("/{id}")
    public SimpleDonatorDto getDonatorById(@PathVariable(name = "id") Long id) {
        return donatorService.findById(id);
    }


    @PutMapping("/{id}")
    public TextResponse updateDonator(@RequestBody() SimpleDonatorDto donator, @PathVariable("id") Long id) {
        return donatorService.updateDonator(id, donator);
    }

    @DeleteMapping("/{id}")
    public TextResponse deleteDonatorById(@PathVariable("id") Long id) {
        return donatorService.deleteDonatorById(id);
    }
}
