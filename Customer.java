package com.ecommerce;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer in the SmartShop e-commerce system.
 */
public class Customer {
    private String customerID;
    private String name;
    private List<Product> shoppingCart;
    private double cartTotal;

    public Customer(String customerID, String name) {
        this.customerID = customerID;
        this.name = name;
        this.shoppingCart = new ArrayList<>();
        this.cartTotal = 0.0;
    }

    // Getters
    public String getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public List<Product> getShoppingCart() {
        return new ArrayList<>(shoppingCart); // Return a copy for encapsulation
    }

    public double getCartTotal() {
        return cartTotal;
    }

    /**
     * Adds a product to the shopping cart
     * @param product the product to add
     * @param quantity the quantity to add
     * @throws IllegalArgumentException if product is null or quantity is invalid
     */
    public void addToCart(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (quantity > product.getStockQuantity()) {
            throw new IllegalArgumentException("Insufficient stock for " + product.getName());
        }

        // Add the product to cart (simplified - in real system we'd track quantities)
        for (int i = 0; i < quantity; i++) {
            shoppingCart.add(product);
        }
        cartTotal += product.getPrice() * quantity;
    }

    /**
     * Removes a product from the shopping cart
     * @param product the product to remove
     * @param quantity the quantity to remove
     * @throws IllegalArgumentException if product is not in cart or quantity is invalid
     */
    public void removeFromCart(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        int count = 0;
        for (Product p : shoppingCart) {
            if (p.getProductID().equals(product.getProductID())) {
                count++;
            }
        }

        if (count < quantity) {
            throw new IllegalArgumentException("Not enough items in cart");
        }

        // Remove the specified quantity
        int removed = 0;
        for (int i = 0; i < shoppingCart.size() && removed < quantity; i++) {
            if (shoppingCart.get(i).getProductID().equals(product.getProductID())) {
                shoppingCart.remove(i);
                i--; // Adjust index after removal
                removed++;
                cartTotal -= product.getPrice();
            }
        }
    }

    /**
     * Clears the shopping cart
     */
    public void clearCart() {
        shoppingCart.clear();
        cartTotal = 0.0;
    }

    /**
     * Places an order based on the current shopping cart
     * @return the created Order object
     * @throws IllegalStateException if cart is empty
     */
    public com.ecommerce.orders.Order placeOrder() {
        if (shoppingCart.isEmpty()) {
            throw new IllegalStateException("Cannot place order with empty cart");
        }

        // Create a copy of the cart for the order
        List<Product> orderProducts = new ArrayList<>(shoppingCart);
        com.ecommerce.orders.Order order = new com.ecommerce.orders.Order(
            "ORD-" + System.currentTimeMillis(), 
            this, 
            orderProducts, 
            cartTotal
        );

        // Update product stock quantities
        for (Product product : orderProducts) {
            product.reduceStock(1); // Simplified - assumes 1 per product
        }

        clearCart();
        return order;
    }

    @Override
    public String toString() {
        return String.format("Customer[ID=%s, Name=%s, CartTotal=UGX %.2f]", 
            customerID, name, cartTotal);
    }
}
