package com.rbc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class StockRepo {
	private ConcurrentHashMap<String, ArrayList<StockPrice>> stocks = new ConcurrentHashMap<String, ArrayList<StockPrice>>();

	public List<StockPrice> getStockByID(String id) {
		return stocks.get(id);
	}

	public boolean addStock(StockPrice stockInfo) {
		boolean found = false;
		if (stocks.get(stockInfo.getStock()) == null){
			stocks.put(stockInfo.getStock(), new ArrayList<StockPrice>());
		}
		
		ArrayList<StockPrice> history = stocks.get(stockInfo.getStock());
		for (StockPrice price : history){
			if (price.getDate().equals(stockInfo.getDate())){
				found = true;
				break;
			}
		}
		
		if (found == false){
			stocks.get(stockInfo.getStock()).add(stockInfo);
		}
//		System.out.println(stocks.size());
		return found;
	}

}
