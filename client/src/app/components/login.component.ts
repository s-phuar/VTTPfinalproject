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
  private sub !: Subscription

  message = ''


  ngOnInit(): void {
    this.form = this.createAccountForm()
  }


  private createAccountForm():FormGroup{
    return this.fb.group({
      email: this.fb.control<string>('', [Validators.required, Validators.minLength(1)]),
      password: this.fb.control<string>('', [Validators.required, Validators.minLength(1)])
    })
  }

  login(){
    const details: Login = this.form.value
    console.info('>>> details: ', details)
    this.sub = this.loginSvc.login(details).subscribe({
      next: (resp) => {
        console.info('triggered login next')
        console.log('Token received:', resp.token); // Debug token from response
        this.loginSvc.setToken(resp.token) //overwrite existing token with fresh token
        console.log('Token stored:', this.loginSvc.getToken()); // Verify storage
        this.router.navigate(['/dashboard']) //not yet implemented ***************************
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
