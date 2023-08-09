import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {UserListComponent} from "../user/components/user-list/user-list.component";
import {DonationListComponent} from "./components/donation-list/donation-list.component";

const routes: Routes = [
  { path: 'donations', component: DonationListComponent},
  { path: 'donations', component: DonationListComponent},
];

@NgModule({
  declarations: [

  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class DonationRoutingModule { }
