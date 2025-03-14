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


  ngOnInit(): void {
    this.form = this.createAccountForm()
  }


  private createAccountForm():FormGroup{
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required, Validators.minLength(1)]),
      email: this.fb.control<string>('', [Validators.required, Validators.minLength(1)]),
      password: this.fb.control<string>('', [Validators.required, Validators.minLength(1)]),
      password2: this.fb.control<string>('', [Validators.required, Validators.minLength(1)])
    })
  }

  clear(){
    this.form = this.createAccountForm()
    this.message = ''
  }

  register(){

    if(this.form.value['password'] == this.form.value['password2']){
      const details: Register = this.form.value
      console.info('>>> details: ', details)
      this.sub = this.loginSvc.create(details).subscribe({
        next: (resp) => this.message = resp.message,
        error: () => this.message = 'Account already exists'
      })
      console.info('message: ', this.message)
    }else{
      //passwords dont match
      this.message = "Passwords do not match"
    }


  }


  ngOnDestroy(): void {
    if(this.sub){
      this.sub.unsubscribe()
    }
  }

  
}
