import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { LoginService } from "./login.service";
import { Observable } from "rxjs";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    private loginSvc = inject(LoginService);

    constructor() {
        console.log('AuthInterceptor instantiated');
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const token = this.loginSvc.getToken();
        console.log('Interceptor triggered for:', req.url);
        console.log('Interceptor - Token:', token);
        if (token) {
            req = req.clone({
                setHeaders: { Authorization: `Bearer ${token}` }
            });
            console.log('Request headers:', req.headers.get('Authorization'));
        } else {
            console.log('No token found in interceptor');
        }
        return next.handle(req);
    }
}