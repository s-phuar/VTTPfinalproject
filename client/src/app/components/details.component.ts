import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription, switchMap } from 'rxjs';
import { StockService } from '../stock.service';
import { Stock } from '../models';
import { StockStore } from '../stock.store';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-details',
  standalone: false,
  templateUrl: './details.component.html',
  styleUrl: './details.component.css'
})
export class DetailsComponent implements OnInit, OnDestroy{

  private urlSub !: Subscription
  protected form !:FormGroup
  ticker : string = ''
  searchResults !: Stock
  errorMessage !: string

  private activatedRoute = inject(ActivatedRoute)
  private stockSvc = inject(StockService)
  private stockStore = inject(StockStore)
  private loginSvc = inject(LoginService)
  private fb = inject(FormBuilder)
  private router = inject(Router)


  ngOnInit(): void {
    if (!this.loginSvc.isLoggedIn()) {
      alert('Please login first');
      this.router.navigate(['/login']);
      return;
  }

    this.form = this.createAccountForm()

    this.urlSub = this.activatedRoute.queryParams
      .pipe(
        switchMap(params => {
          this.ticker = (params['ticker'] || '').toUpperCase()
          if(this.ticker){
            this.loadStockDetails(this.ticker)
          }
          //else
          return[]
        })
      ).subscribe()
  }


  private createAccountForm():FormGroup{
    return this.fb.group({
      ticker: this.fb.control<string>('', [Validators.required, Validators.minLength(1)])
    })
  }


  search(){
    this.router.navigate(['/details'], {queryParams: {ticker: this.form.controls['ticker'].value.trim()}})
  }



  //grab data
  //1. memory/component store
  //2. database/apicall
    //a. on api pull, immediately store in component store (persists until browser refresh)
    //b. display the data
  loadStockDetails(ticker: string){

    this.stockStore.getStockBySymbol(ticker).subscribe(stock => {
      if(stock) { //found inside store
        console.info('stock found in store: ', stock)
        this.searchResults = stock
      }else{  //not found in store
        console.info('stock NOT found in store,, fetching from server')
        this.stockSvc.searchTicker(ticker).subscribe({
          next: (resp) =>{
            console.info('triggered search next')
            this.searchResults = resp
            console.info('message: ', this.searchResults)
            this.stockStore.addToStore(resp) //update store with recent call to server(database/api call)
          },
          error: (err) => {
            console.info('triggered search error')
            this.errorMessage = err.error.message
            console.info(">>> search error:", this.errorMessage)
          } 
        })
      }
    }) 
  }



  //add method
  //3. add button after displaying data
    //a. stores in couchbase (for 24 hours, keep things fresh)

  addStock(){
    const email = localStorage.getItem('email')!;
    this.stockSvc.save(this.searchResults, email).subscribe({
      next: (resp) =>{
        console.info('triggered save next')
        console.info(resp)
      },
      error: (err) => {
        console.info('triggered save error')
        this.errorMessage = err.error.message
        console.info(">>> search error:", this.errorMessage)
      }
    })
  }



  ngOnDestroy(): void {
    if(this.urlSub){
      this.urlSub.unsubscribe()
    }
  }



}
