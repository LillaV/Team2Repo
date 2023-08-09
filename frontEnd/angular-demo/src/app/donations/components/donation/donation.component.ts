import {Component, Input, OnInit} from '@angular/core';
import {Donation} from "../../models/donation";

@Component({
  selector: 'app-donation',
  templateUrl: './donation.component.html',
  styleUrls: ['./donation.component.css']
})
export class DonationComponent implements OnInit {
  @Input() donation: Donation;

  constructor() { }

  ngOnInit(): void {
  }

}
