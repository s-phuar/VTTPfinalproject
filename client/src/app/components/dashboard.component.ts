import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit{

  private router = inject(Router)
  private fb = inject(FormBuilder)
  protected form !:FormGroup


  ngOnInit(): void {
    this.form = this.createAccountForm()

    //need to pull portfolio from database everytime i come here, consider storing portfolio data in component store
    
  }

  private createAccountForm():FormGroup{
    return this.fb.group({
      ticker: this.fb.control<string>('', [Validators.required, Validators.minLength(1)])
    })
  }


  search(){
    this.router.navigate(['/details'], {queryParams: {ticker: this.form.controls['ticker'].value.trim()}})
  }


  

}
