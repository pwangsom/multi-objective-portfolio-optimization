package com.amonthep.portfolio.stock;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class StockInitilizer {
	
	@Getter
	private static List<Stock> stockList = new ArrayList<Stock>();
	
	static {
		Stock stock0 = new Stock();
		stock0.setReturnProfit(100.0);
		stock0.setRisk(10.0);
		stock0.setCompanyPerformance(100.0);

		Stock stock1 = new Stock();
		stock1.setReturnProfit(200.0);
		stock1.setRisk(20.0);
		stock1.setCompanyPerformance(200.0);
		
		Stock stock2 = new Stock();
		stock2.setReturnProfit(300.0);
		stock2.setRisk(30.0);
		stock2.setCompanyPerformance(300.0);
		
		stockList.add(stock0);
		stockList.add(stock1);
		stockList.add(stock2);		
	}
	
}
