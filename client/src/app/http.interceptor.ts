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

    // Decode JWT payload (base64 URL decode to JSON)
    private decodeToken(token: string): any {
        try {
            const payload = token.split('.')[1]; // Get payload part
            const decoded = atob(payload.replace(/-/g, '+').replace(/_/g, '/')); // Base64 URL decode
            return JSON.parse(decoded);
        } catch (e) {
            console.error('Failed to decode token:', e);
            return null;
        }
    }

    // Check if token from server is expired
    private isTokenExpired(token: string): boolean {
        const payload = this.decodeToken(token);
        if (!payload || !payload.exp) return true; // No exp claim = treat as expired
        const nowInSeconds = Math.floor(Date.now() / 1000); // Current time in seconds
        // console.log('Token exp (seconds):', payload.exp, 'Now (seconds):', nowInSeconds);
        return payload.exp < nowInSeconds; //return true if expired
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const token = this.loginSvc.getToken();
        console.log('Interceptor triggered for:', req.url);
        console.log('Interceptor - Token:', token);
        if (token && !this.isTokenExpired(token)) {
                req = req.clone({
                setHeaders: { Authorization: `Bearer ${token}` }
            });
            console.log('Request headers:', req.headers.get('Authorization'));
        } else if (token && this.isTokenExpired(token)) {
            console.log('Token expired, clearing from localStorage');
            this.loginSvc.clearToken(); // Clear expired token
        } else {
            console.log('No token found in interceptor');
        }
        return next.handle(req);
    }
}