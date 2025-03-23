import { Injectable } from "@angular/core";
import { ActivityRatios, LiquidityRatios, MiscItems, ProfitabilityRatios, SolvencyRatios, Stock, StockSlice, ValuationRatios } from "./models";
import { ComponentStore } from "@ngrx/component-store";
import { Observable } from "rxjs";

const INIT: StockSlice = {
    stocks: [] as Stock[]
  };
  

@Injectable()
export class StockStore extends ComponentStore<StockSlice>{

    //init store
    constructor(){
        super(INIT)
    }

    //add
    readonly addToStore = this.updater<Stock>( //update only takes in Stock object
        (store: StockSlice, newStock:Stock) => {
            const _newStore: StockSlice = {
                stocks: [... store.stocks]
            }

            //push to copied store
            _newStore.stocks.push(newStock)
            return _newStore
        }
    )


    // Selector to get a specific stock by symbol, return udefined if not found
    readonly getStockBySymbol = (symbol: string): Observable<Stock | undefined> => {
        return this.select<Stock | undefined>(
            (store: StockSlice) => store.stocks.find(
            stock => stock.mi.symbol === symbol
            )
    )}


    //get whole store for debugging
    readonly getAllStocks = this.select<Stock[]>(
        (store: StockSlice) =>{
            return store.stocks
        })



}
