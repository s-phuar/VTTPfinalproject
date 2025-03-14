import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable()
export class StockService{

    public http = inject(HttpClient)

    searchTicker(ticker: string): Observable<any>{
        return this.http.post<any>('/api/search', ticker)
    }

    save(ticker: string){

    }

    delete(ticker: string){
        
    }






}
