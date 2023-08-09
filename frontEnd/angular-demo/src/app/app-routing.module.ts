import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {UserListComponent} from "./user/components/user-list/user-list.component";
import {UserDetailsComponent} from "./user/components/user-details/user-details.component";
import {DonationListComponent} from "./donations/components/donation-list/donation-list.component";
import {DonationComponent} from "./donations/components/donation/donation.component";
import {UserRoutingModule} from "./user/user-routing.module";
import {DonationRoutingModule} from "./donations/donation-routing.module";
import {LoginComponent} from "./login/components/login/login.component";


const routes: Routes = [
  { path: 'login', component: LoginComponent},
  { path: '', redirectTo: 'users', pathMatch: 'full'},
  { path: '**', redirectTo: 'users', pathMatch: 'full'}
];
@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    UserRoutingModule,
    DonationRoutingModule,
    RouterModule.forRoot(routes)
  ], exports: [
    RouterModule
  ]
})
export class AppRoutingModule { }
