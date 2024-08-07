package com.shop.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;
	    private int memberId;
	    private int itemId;
	    private int quantity;

	    // Getter와 Setter
	    public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    public int getMemberId() {
	        return memberId;
	    }

	    public void setMemberId(int memberId) {
	        this.memberId = memberId;
	    }

	    public int getItemId() {
	        return itemId;
	    }

	    public void setItemId(int itemId) {
	        this.itemId = itemId;
	    }

	    public int getQuantity() {
	        return quantity;
	    }

	    public void setQuantity(int quantity) {
	        this.quantity = quantity;
	    }
	    
}
