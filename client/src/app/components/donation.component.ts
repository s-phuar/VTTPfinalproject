import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { loadStripe } from '@stripe/stripe-js';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-donation',
  standalone: false,
  templateUrl: './donation.component.html',
  styleUrl: './donation.component.css'
})
export class DonationComponent implements OnInit{
  //loads stripe.js 
  private stripePromise = loadStripe('pk_test_51R65TvDv7osjVqPq1j8Ado5os5hqLk3S5efjPeinwVuCN6t7iwdp1SxQ06B5Sy55foAcCwU3X1o0xi75kbPGLr6d00vGMaTRz0')
  private http = inject(HttpClient)
  private router = inject(Router)
  private loginSvc = inject(LoginService)
  private card: any

  ngOnInit(): void {
    if (!this.loginSvc.isLoggedIn()) {
      alert('Please login first');
      this.router.navigate(['/login']);
      return;
    }

    this.initializeStripe()
  }

  private async initializeStripe() {
    const stripe = await this.stripePromise
    if (!stripe) {
      alert('failed to load Stripe')
      return;
    }
    const elements = stripe.elements()
    this.card = elements.create('card')
    this.card.mount('#card-element')
  }


  async donate(){

    //create and mount card input
    const stripe = await this.stripePromise
    if(!stripe || !this.card){
      alert('Stripe/card not yet initialised')
      return
    }

    // generate a token from card inputs
    const result = await stripe.createToken(this.card)
    if(result.error){
      alert(result.error.message)
      return
    }

    const token = result.token.id
    const donationAmount = 500 //cents

    this.http.post('/api/donate', null, { params: { token, amount: donationAmount.toString() } }).subscribe({
      next: (resp: any) => {
          alert(resp.message)
          this.router.navigate(['/dashboard'])
      },
      error: (err) => {
          alert(err.error.message)
          }
      })
    }


}






