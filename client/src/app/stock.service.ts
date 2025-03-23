import { HttpClient, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { firstValueFrom, Observable } from "rxjs";
import { Stock } from "./models";

@Injectable()
export class StockService{

    public http = inject(HttpClient)

    searchTicker(ticker: string): Observable<Stock>{
        return this.http.get<Stock>(`/api/search/${ticker}`)
    }

    save(stock: Stock){
        return this.http.post<any>('/api/save', stock)
    }
    

    delete(ticker: string){
        
    }






}
