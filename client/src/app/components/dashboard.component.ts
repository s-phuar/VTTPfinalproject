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
  }

  private createAccountForm():FormGroup{
    return this.fb.group({
      ticker: this.fb.control<string>('', [Validators.required, Validators.minLength(1)])
    })
  }

  //search for stock ticker
  //set jwt filter to access this page
  // 1. search indexdb component store for the data (expires in 15)
  // 2. search call to check couchbase for the data (expires in 15)
  // 3. search call to api for fresh data
  //route to details page to display stock details

  search(){
    
  }


  

}
