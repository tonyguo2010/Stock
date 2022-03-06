package com.rbc;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class StockController {
	
	@Autowired
	StockService service;
	
	private StockPrice stringToPrice(String priceString){
		String[] units = priceString.split(",");
		StockPrice stockInfo = new StockPrice(
				Integer.parseInt(units[0]),  // int quarter;
				units[1],  // String stock;
				units[2],  // String date;
				units[3],  // String open;
				units[4],  // String high;
				units[5],  // String low;
				units[6],  // String close;
				parseLongWithDefault(units[7], 0), // long volume;
				parseFloatWithDefault(units[8], 0.0f),  // float percent_change_price;
				parseFloatWithDefault(units[9], 0.0f),  // float percent_change_volume_over_last_wk;
				parseFloatWithDefault(units[10], 0.0f), // float previous_weeks_volume;
				units[11], // String next_weeks_open;
				units[12], // String next_weeks_close;
				parseFloatWithDefault(units[13], 0.0f), // float percent_change_next_weeks_price;
				parseIntWithDefault(units[14], 0), // int days_to_next_dividend;
				parseFloatWithDefault(units[15], 0.0f)  // float percent_return_next_dividend;
		);
		return stockInfo;
	}

	private long parseLongWithDefault(String value, int i) {
		try {  
		     return Long.parseLong(value);  
		  } catch(NumberFormatException ex) {  
		      return i;
		  }  
	}

	private int parseIntWithDefault(String value, int i) {
		try {  
		     return Integer.parseInt(value);  
		  } catch(NumberFormatException ex) {  
		      return i;
		  }  
	}

	private float parseFloatWithDefault(String value, float f) {
		try {  
		     return Float.parseFloat(value);  
		  } catch(NumberFormatException ex) {  
		      return f;
		  }
	}

	@GetMapping(value = "/stock/{id}")
	public List<StockPrice> getStockHistoryByID(@PathVariable String id, HttpServletResponse response) {
		List<StockPrice> result = service.find(id);
		
		if (result.size() > 0) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		
		return result;
	}
	
	@PostMapping(value = "/stock")
	public String AddStockDetail(@RequestBody String stockPriceInfo, HttpServletResponse response) {
		try{
			StockPrice stockInfo = stringToPrice(stockPriceInfo);
			if (service.addStockPrice(stockInfo)){
				return "Same date existing in the system, skipped!";
			} else {
				return "Stock info set";
			}
			
		} catch (NumberFormatException ex){
			return "Please check the input format, NOTE: header is not needed";
		}
	}

	@PostMapping(value = "/stocks")
	public String AddStockDetails(@RequestBody String stockDetails, HttpServletResponse response) {
//		System.out.println(stockDetails);
		String[] lines = stockDetails.split("\\r\\n");
//		System.out.println(lines.length);
		for (int i = 1; i < lines.length; i++){ // skip header
			String line = lines[i];
			StockPrice stockInfo = stringToPrice(line);
			service.addStockPrice(stockInfo);
//			System.out.println();
		}

		return "Data loaded into the system.";
	}

}
