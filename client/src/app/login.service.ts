import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Login, Register } from "./models";
import { Observable } from "rxjs";

@Injectable()
export class LoginService{

    public http = inject(HttpClient)

    create(details: Register): Observable<any>{
        return this.http.post<any>('/api/creation', details)
    }

    login(details: Login): Observable<any>{
        return this.http.post('/api/login', details)
    }

    logout():Observable<any>{
        return this.http.get('/api/logout')
    }

    setEmail(email: string): void{
        localStorage.setItem('email', email)
    }

    getEmail(): string | null{
        return localStorage.getItem('email')
    }

    setToken(token: string): void{
        localStorage.setItem('token', token)
    }

    getToken(): string | null{
        return localStorage.getItem('token')
    }

    clearToken(): void{
        localStorage.removeItem('token')
    }

    isLoggedIn(): boolean{
        return !!this.getToken()
    }




}