import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { StockService } from '../stock.service';
import { Observable } from 'rxjs';
import { StockSummary } from '../models';
import { LoginService } from '../login.service';
import { HttpClient } from '@angular/common/http';

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
  private stockSvc = inject(StockService)
  private loginSvc = inject(LoginService)
  private http = inject(HttpClient)
  errorMessage !: string
  stockSum !: StockSummary[]
  metrics: string | null = null; // Store metrics to display

  ngOnInit(): void {
    if (!this.loginSvc.isLoggedIn()) {
      alert('Please login first');
      this.router.navigate(['/login']);
      return;
    }

    this.form = this.createAccountForm()

    //need to pull portfolio from database everytime i come here, consider storing portfolio data in component store
    if(localStorage.getItem('email')!){
      this.stockSvc.pullPortfolio(localStorage.getItem('email')!).subscribe({
        next: (resp) =>{
          console.info('triggered dashboard next')
          this.stockSum = resp
        },
        error: (err) => {
          console.info('triggered dashboard error')
          this.errorMessage = err.error.message
          console.info(">>> dashboard error:", this.errorMessage)
        }
      })
    }
  }

  private createAccountForm():FormGroup{
    return this.fb.group({
      ticker: this.fb.control<string>('', [Validators.required, Validators.minLength(1)])
    })
  }


  search(){
    this.router.navigate(['/details'], {queryParams: {ticker: this.form.controls['ticker'].value.trim()}})
  }

  delete(symbol: string){
    const email = localStorage.getItem('email')!
    this.stockSvc.delete(symbol, email).subscribe({
      next: (resp) => {
        console.info(resp);
        this.stockSum = this.stockSum.filter(stock => stock.symbol !== symbol);
      },
      error: (err) => {
        console.info(err.error.message);
      }
    })
  }

  donate(){
    this.router.navigate(['/donation']);
  }


  // Check if the logged-in user is admin
  isAdminUser(): boolean {
    const email = localStorage.getItem('email')
    return email === 'samuel.phuar@gmail.com'
  }

  // Open Prometheus in a new tab
  openPrometheus() {
    const prometheusUrl = 'http://localhost:8080/actuator/prometheus'
    window.open(prometheusUrl, '_blank')
  }
  

}
