package com.altqart.req.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemReq {
	
	private int id;
	
	private double qty;
	
	private double salePrice;
	
	private String product;
	
    private double maxPrice;
    
    private double minPrice;
    
    private double stkQty;
        
    private double itemSubTotal;
    
    private String purchaseBarCode;    
    
	
	
}
