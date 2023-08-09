import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {UserListComponent} from "./components/user-list/user-list.component";
import {UserDetailsComponent} from "./components/user-details/user-details.component";
import {DonationListComponent} from "../donations/components/donation-list/donation-list.component";
import {DonationComponent} from "../donations/components/donation/donation.component";
import {UserRegisterComponent} from "./components/user-register/user-register.component";
import {RoleGuard} from "../util/Roleguard";

const routes: Routes = [
// { path: 'users', component: UserListComponent},
  {path:'users',
    children:[
      {path:'', component:UserListComponent},
      {path:'details',component: UserDetailsComponent}
    ],
    canActivate: [RoleGuard],
    data:{role:'admin'}

  },
// { path: 'users/:id', component: UserDetailsComponent},
  { path: 'register', component: UserRegisterComponent},

]

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ], exports: [
    RouterModule
  ]
})
export class UserRoutingModule { }
