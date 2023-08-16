package de.msg.javatraining.donationmanager.controller.app;

import de.msg.javatraining.donationmanager.persistence.dtos.donator.SimpleDonatorDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.CreateUserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.UpdateUserDto;
import de.msg.javatraining.donationmanager.persistence.dtos.user.UserDto;
import de.msg.javatraining.donationmanager.persistence.model.Donator;
import de.msg.javatraining.donationmanager.persistence.model.User;
import de.msg.javatraining.donationmanager.service.DonatorService;
import de.msg.javatraining.donationmanager.service.UserService;
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

    @GetMapping("/{offset}/{pageSize}")
    public List<SimpleDonatorDto> getPage(@PathVariable(name = "offset") int offset, @PathVariable(name = "pageSize") int pageSize) {
        return donatorService.allDonatorsWithPagination(offset, pageSize);
    }

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
