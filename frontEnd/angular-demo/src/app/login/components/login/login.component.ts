import { Component, OnInit } from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {LoginRequest} from "../../models/login-request";
import {LoginService} from "../../services/login.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required],

  })


  constructor(private fb: FormBuilder,
              private loginService: LoginService) { }

  ngOnInit(): void {
  }

  onLogin() {

    const username=this.loginForm.get('username')?.value;
    const password=this.loginForm.get('password')?.value;
    console.log(username);
    console.log(password);

    const loginRequest = new LoginRequest(username,password);

    this.loginService.login(loginRequest);

    localStorage.setItem('role',"admin")
  }

}
