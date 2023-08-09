import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from './app.component';
import {UserModule} from './user/user.module';
import {DonationsModule} from "./donations/donations.module";
import {AppRoutingModule} from './app-routing.module';
import {UserRoutingModule} from './user/user-routing.module';
import {DonationRoutingModule} from './donations/donation-routing.module';
import {UserRegisterComponent} from './user/components/user-register/user-register.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatMenuModule} from "@angular/material/menu";
import {MatButtonModule} from '@angular/material/button';
import {LoginComponent} from './login/components/login/login.component'
import {LoginModule} from "./login/login.module";
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from "@angular/common/http";
import {LoginService} from "./login/services/login.service";
import {Interceptor} from "./util/interceptors/interceptor";
import {RoleGuard} from "./util/Roleguard";
import {MatChipsModule} from "@angular/material/chips";
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  declarations: [
    AppComponent,
    UserRegisterComponent,
  ],
  imports: [
    BrowserModule,
    UserModule,
    DonationsModule,
    AppRoutingModule,
    UserRoutingModule,
    DonationRoutingModule,
    ReactiveFormsModule,
    MatMenuModule,
    BrowserAnimationsModule,
    MatButtonModule,
    LoginModule,
    HttpClientModule,
    FormsModule,
    MatChipsModule,
    MatIconModule,
  ],
  providers: [
    LoginService, {provide: HTTP_INTERCEPTORS, useClass: Interceptor, multi: true},RoleGuard
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
