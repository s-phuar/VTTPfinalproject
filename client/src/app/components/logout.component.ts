import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../login.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-logout',
  standalone: false,
  templateUrl: './logout.component.html',
  styleUrl: './logout.component.css'
})
export class LogoutComponent implements OnInit, OnDestroy{

  private router = inject(Router)
  private loginSvc = inject(LoginService)
  protected sub !: Subscription

  protected message = ''

  ngOnInit(): void {
    console.info('triggered logout method')
    this.sub = this.loginSvc.logout().subscribe({
      next: (resp) =>{
        console.info('triggered logout next')
        this.loginSvc.clearToken()
        this.router.navigate(['/'])
      },
      error: (err) => {
        console.info('triggered logout error')
        this.message = err.error.message
        alert(this.message)
        this.router.navigate(['/'])
      }
    })
  }

  ngOnDestroy(): void {
    if(this.sub){
      this.sub.unsubscribe()
    }
  }


}
