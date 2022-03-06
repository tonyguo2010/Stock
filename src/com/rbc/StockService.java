package com.rbc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {

	@Autowired
	StockRepo repo;
	
	
	public List<StockPrice> find(String id) {
		List<StockPrice> result = repo.getStockByID(id);
		
		if (result == null){
			result = new ArrayList<StockPrice>();
		}
		return result;
	}


	public boolean addStockPrice(StockPrice stockInfo) {
//		System.out.println(stockInfo + " added");
		return repo.addStock(stockInfo);
	}

}
