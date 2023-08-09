import {Component, Input, OnInit} from '@angular/core';
import {User} from '../../models/user';
import {UserService} from "../../services/user-service.service";
import {tap} from "rxjs";

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  userList: User[];

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.userService.loadUsers().subscribe();
    this.userService.getUsers().subscribe((users) => this.userList = users);
  }

  editUser(userToEdit: User) {
    this.userService.updateUser(userToEdit).subscribe( ()=> this.userService.loadUsers());
  }
}
