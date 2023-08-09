import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, FormGroup, ValidatorFn, Validators} from "@angular/forms";

@Component({
  selector: 'app-user-register',
  templateUrl: './user-register.component.html',
  styleUrls: ['./user-register.component.css']
})
export class UserRegisterComponent implements OnInit {

  registerForm = this.fb.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    email: ['', Validators.email],
    phone: ['', Validators.pattern(/^(00407|07|\+407)\d{8}$/)],
    roles: ['', Validators.required],
  })


  onSave() {
    console.log(this.registerForm.value);
  }

  constructor(private fb: FormBuilder) {
  }

  // private validPhone():ValidatorFn{
  //   return (control: AbstractControl) => control.value !== null ? {invalidPhone: true}: undefined
  // }
  ngOnInit(): void {
  }

}
