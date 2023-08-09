import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {DonationComponent} from "./components/donation/donation.component";
import {DonationListComponent} from "./components/donation-list/donation-list.component";



@NgModule({
  declarations: [
    DonationComponent,
    DonationListComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    DonationListComponent
  ]
})
export class DonationsModule { }
