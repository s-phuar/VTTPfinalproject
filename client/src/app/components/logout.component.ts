import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../login.service';
import { Subscription } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-logout',
  standalone: false,
  templateUrl: './logout.component.html',
  styleUrl: './logout.component.css'
})
export class LogoutComponent implements OnInit, OnDestroy{

  private router = inject(Router)
  private loginSvc = inject(LoginService)

  private sub !: Subscription

  message = ''

  ngOnInit(): void {
    console.log('Token before logout:', this.loginSvc.getToken()); // Debug token
    this.sub = this.loginSvc.logout().subscribe({
      next: (resp) =>{
        this.message = resp.message
        this.loginSvc.clearToken()
        this.router.navigate(['/'])
      },
      error: (resp) => {
        console.info('>>> oops')
        this.message = resp.message
      }
    })
  }

  ngOnDestroy(): void {
    if(this.sub){
      this.sub.unsubscribe()
    }
  }


}
