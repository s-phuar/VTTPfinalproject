export interface Register{
    name: string
    email: string
    password: string
}

export interface Login{
    email: string
    password: string
}


export interface StockSlice{
    stocks: Stock[]
}

export interface Stock{
    mi: MiscItems
    ar: ActivityRatios
    lr: LiquidityRatios
    sr: SolvencyRatios
    pr: ProfitabilityRatios
    vr: ValuationRatios
}

export interface StockSummary{
    symbol: string
    companyName: string
}

export interface MiscItems{
    symbol: string
    companyName: string
    price: number
    dcf: number
    beta: number
    mktCap: number
    image: string
    description: string
    todayEpochMilli: string
}

export interface ActivityRatios{
    inventoryTurnover: number
    daysOfInventoryOnHand: number
    receivablesTurnover: number
    daysSalesOutstanding: number
    payablesTurnover:number
    daysPayablesOutstanding: number
    fixedAssetTurnoverTTM: number
    assetTurnoverTTM: number
}


export interface LiquidityRatios{
    currentRatioTTM: number
    quickRatioTTM: number
    cashRatioTTM: number
    cashConversionCycleTTM: number
}

export interface SolvencyRatios{
    debtRatioTTM: number
    debtEquityRatioTTM: number
    interestCoverageTTM: number
    cashFlowCoverageRatiosTTM: number
    shortTermCoverageRatiosTTM: number
    capitalExpenditureCoverageRatioTTM: number
    dividendPaidAndCapexCoverageRatioTTM: number
}

export interface ProfitabilityRatios{
    grossProfitMarginTTM: number
    operatingProfitMarginTTM: number
    pretaxProfitMarginTTM: number
    netProfitMarginTTM: number
    returnOnAssetsTTM: number
    returnOnEquityTTM: number
    returnOnCapitalEmployedTTM: number
    companyEquityMultiplierTTM: number
}

export interface ValuationRatios{
    peRatioTTM: number
    pegRatioTTM: number
    priceToBookRatioTTM: number
    priceToSalesRatioTTM: number
    priceEarningsRatioTTM: number
    priceFairValueTTM: number
    payoutRatioTTM: number
    retentionRate: number
    sustainableGrowthRate: number
}


