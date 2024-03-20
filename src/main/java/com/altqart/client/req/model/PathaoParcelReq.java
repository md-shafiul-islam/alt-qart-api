package com.altqart.client.req.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PathaoParcelReq {

    @JsonProperty("store_id")
    private int storeId;
    
    @JsonProperty("merchant_order_id")
    private String merchantOrderId;
    
    @JsonProperty("sender_name")
    private String senderName;
    
    @JsonProperty("sender_phone")
    private String senderPhone;
   
    @JsonProperty("recipient_name")
    private String recipientName;
    
    @JsonProperty("recipient_phone")
    private String recipientPhone;
    
    @JsonProperty("recipient_address")
    private String recipientAddress;
    
    @JsonProperty("recipient_city")
    private int recipientCity;
    
    @JsonProperty("recipient_zone")
    private int recipientZone;
    
    @JsonProperty("recipient_area")
    private int recipientArea;
    
    @JsonProperty("delivery_type")
    private int deliveryType= 48;
    
    @JsonProperty("item_type")
    private int itemType= 2;
    
    @JsonProperty("special_instruction")
    private String specialInstruction;
    
    @JsonProperty("item_quantity")
    private int itemQuantity;
    
    @JsonProperty("item_weight")
    private double itemWeight;
    
    @JsonProperty("amount_to_collect")
    private int amountToCollect;
    
    @JsonProperty("item_description")
    private String itemDescription;
}
