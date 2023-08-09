import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../../user/models/user";
import {Donation} from "../../models/donation";

@Component({
  selector: 'app-donation-list',
  templateUrl: './donation-list.component.html',
  styleUrls: ['./donation-list.component.css']
})
export class DonationListComponent implements OnInit {

  constructor() { }


  users: User[] = [
    new User(1, 'user1', 'user_email1@yahoo.com', '1234'),
    new User(2, 'user2', 'user_email2@yahoo.com', '1234'),
    new User(3, 'user3', 'user_email3@yahoo.com', '1234')
  ];

  donationList: Donation[] = [
    new Donation(1,20,'Euro',this.users[0]),
    new Donation(2,80,'Euro',this.users[0]),
    new Donation(3,77,'Ron',this.users[0]),
  ];


  donations: Donation[] = [

  ];

  ngOnInit(): void {
  }

}
