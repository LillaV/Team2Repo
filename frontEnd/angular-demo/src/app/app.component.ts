import { Component } from '@angular/core';
import {User} from './user/models/user';
import {Donation} from "./donations/models/donation";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'untitled5';

  users: User[] = [
    new User(1, 'user1', 'user_email1@yahoo.com', '1234'),
    new User(2, 'user2', 'user_email2@yahoo.com', '1234'),
    new User(3, 'user3', 'user_email3@yahoo.com', '1234')
  ];

  donations: Donation[] = [
    new Donation(1,20,'Euro',this.users[0]),
    new Donation(2,80,'Euro',this.users[0]),
    new Donation(3,77,'Ron',this.users[0]),
  ];
}
