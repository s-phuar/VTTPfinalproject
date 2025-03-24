import { HttpClient, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { firstValueFrom, map, Observable } from "rxjs";
import { Stock, StockSummary } from "./models";

@Injectable()
export class StockService{

    public http = inject(HttpClient)

    searchTicker(ticker: string): Observable<Stock>{
        return this.http.get<Stock>(`/api/search/${ticker}`)
    }

    pullPortfolio(email: string): Observable<StockSummary[]>{
        const params = new HttpParams()
            .set('email', email)

        return this.http.get<StockSummary[]>('/api/portfolio', {params})
    }

    save(stock: Stock, email: string){
        return this.http.post<any>(`/api/save/${email}`, stock)
    }
    

    delete(symbol: string, email:string): Observable<string>{
        const params = new HttpParams()
            .set('symbol', symbol)
            .set('email', email)
        return this.http.delete<string>('/api/delete', {params})
    }






}
