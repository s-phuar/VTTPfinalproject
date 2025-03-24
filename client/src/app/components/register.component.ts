import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginService } from '../login.service';
import { Register } from '../models';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit, OnDestroy{

  private loginSvc = inject(LoginService)
  private fb = inject(FormBuilder)

  protected form !:FormGroup
  private sub !: Subscription

  message = ''

  private emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  private passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

  ngOnInit(): void {
    this.form = this.createAccountForm()
  }

  private createAccountForm():FormGroup{
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required, Validators.minLength(1)]),
      email: this.fb.control<string>('', [Validators.required, Validators.pattern(this.emailPattern)]),
      password: this.fb.control<string>('', [Validators.required, Validators.pattern(this.passwordPattern)]),
      password2: this.fb.control<string>('', [Validators.required, Validators.minLength(1)])
    })
  }

  clear(){
    this.form = this.createAccountForm()
    this.message = ''
  }

  register(){
    if (this.form.get('name')?.invalid) {
      this.message = 'Name is required';
      return;
    }

    if (this.form.get('email')?.invalid) {
      if (this.form.get('email')?.errors?.['required']) {
        this.message = 'Email is required';
      } else if (this.form.get('email')?.errors?.['pattern']) {
        this.message = 'Please enter a valid email address';
      }
      return;
    }

    if (this.form.get('password')?.invalid) {
      if (this.form.get('password')?.errors?.['required']) {
        this.message = 'Password is required';
      } else if (this.form.get('password')?.errors?.['pattern']) {
        this.message = 'Password must be at least 8 characters with uppercase, lowercase, number, and special character';
      }
      return;
    }

    if (this.form.get('password2')?.invalid) {
      this.message = 'Please confirm your password';
      return;
    }

    if (this.form.value['password'] !== this.form.value['password2']) {
      this.message = 'Passwords do not match';
      return;
    }

    const details: Register = this.form.value
    console.info('>>> details: ', details)
    this.sub = this.loginSvc.create(details).subscribe({
      next: (resp) => {
        console.info('triggered register next')
        this.message = resp.message
      },
      error: (err) => {
        console.info('triggered register error')
        this.message = err.error.message
      }
    })
    
  }


  ngOnDestroy(): void {
    if(this.sub){
      this.sub.unsubscribe()
    }
  }

  
}
