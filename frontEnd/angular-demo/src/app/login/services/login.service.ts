import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {User} from "../../user/models/user";
import {LoginResponse} from "../models/login-response";
import {LoginRequest} from "../models/login-request";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  url: string = "http://localhost:8080/auth/login";

  // @ts-ignore
  loginResponse: BehaviorSubject<LoginResponse> = new BehaviorSubject<LoginResponse>([]);

  constructor(
    private http: HttpClient
  ) {
  }

  login(loginRequest: LoginRequest): void {
    this.http.post<LoginResponse>(this.url, loginRequest).subscribe((loginResponse: LoginResponse) =>
      localStorage.setItem("token", loginResponse.accessToken)
    );
  }


}
