import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { LoginService } from '../login.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Login } from '../models';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit, OnDestroy{

  private loginSvc = inject(LoginService)
  private router = inject(Router)
  private fb = inject(FormBuilder)

  protected form !:FormGroup
  protected sub !: Subscription

  protected message = ''

  private emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
  private passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/


  ngOnInit(): void {
    this.form = this.createAccountForm()
  }


  private createAccountForm():FormGroup{
    return this.fb.group({
      email: this.fb.control<string>('', [Validators.required, Validators.pattern(this.emailPattern)]),
      password: this.fb.control<string>('', [Validators.required, Validators.pattern(this.passwordPattern)])
    })
  }

  login(){
    const details: Login = this.form.value
    console.info('>>> details: ', details)
    this.sub = this.loginSvc.login(details).subscribe({
      next: (resp) => {
        console.info('triggered login next')
        this.loginSvc.setToken(resp.token) //overwrite existing token with fresh token every login
        this.loginSvc.setEmail(details.email)
        this.router.navigate(['/dashboard'])
      },
      error: (err) => {
        console.info('triggered login error')
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
