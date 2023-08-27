package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.donator.SimpleDonatorDto;
import de.msg.javatraining.donationmanager.service.DonatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/donators")
public class DonatorController {
    @Autowired
    private DonatorService donatorService;

    @GetMapping
    public List<SimpleDonatorDto> getDonators(
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        if (offset != null && pageSize != null) {
            return donatorService.allDonatorsWithPagination(offset, pageSize);
        } else {
            return donatorService.getDonators();
        }
    }

    @GetMapping("/size")
    public long getSize() { return donatorService.getSize();}

    @PostMapping()
    public ResponseEntity<String> saveDonator(@RequestBody SimpleDonatorDto donator) {
        try{
            donatorService.saveDonator(donator);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/{id}")
    public SimpleDonatorDto getDonatorById(@PathVariable(name = "id") Long id){
        return  donatorService.findById(id);
    }


    @PutMapping("/{id}")
    public ResponseEntity updateDonator(@RequestBody() SimpleDonatorDto donator, @PathVariable("id") Long id) {
        donatorService.updateDonator(id, donator);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDonatorById(@PathVariable("id") Long id) {
        donatorService.deleteDonatorById(id);
        return new ResponseEntity<>("Donator deleted", HttpStatus.OK);
    }
}
