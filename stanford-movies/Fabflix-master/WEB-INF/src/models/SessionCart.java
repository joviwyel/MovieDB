package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionCart {

    private Map<Integer, CartItem> cartItems; // Maps movie IDs to CartItem
    
    public SessionCart() {
        cartItems = new HashMap<Integer, CartItem>();
    }

    /**
     * Adds the movie to the cart with the given quantity.
     * If the movie is already in the cart, increments the
     * total number in the cart by quantity.
     * @return Updated CartItem corresponding to movie
     */
    public CartItem addItemToCart(Movie movie, int quantity) {
        if (cartItems.containsKey(movie.getId())) {
            CartItem existingItem = cartItems.get(movie.getId());
            existingItem.addQuantity(quantity);
            return existingItem;
        } else {
            CartItem cartItem = new CartItem(movie, quantity);
            cartItems.put(movie.getId(), cartItem);
            return cartItem;
        }
    }
    
    public void updateQuantityOfItemInCart(CartItem item, int quantity) {
        if (cartItems.containsKey(item.getMovieId())) {
            CartItem existingItem = cartItems.get(item.getMovieId());
            existingItem.setQuantity(quantity);
        }
    }
    
    public void updateQuantityOfItemInCart(Movie movie, int quantity) {
        if (cartItems.containsKey(movie.getId())) {
            CartItem existingItem = cartItems.get(movie.getId());
            existingItem.setQuantity(quantity);
        }
    }
    
    public void removeItemFromCart(CartItem item) {
        if (cartItems.containsKey(item.getMovieId())) {
            cartItems.remove(item.getMovieId());
        }
    }
    
    public void removeItemFromCart(Movie movie) {
        if (cartItems.containsKey(movie.getId())) {
            cartItems.remove(movie.getId());
        }
    }

    public void updateQuantityOfItemInCart(Integer movieId, Integer quantity, Boolean addToCart, String movieTitle) {

        if (movieId != null && quantity != null) { 

            if (cartItems.containsKey(movieId)) {

                if (quantity > 0) {
                    CartItem existingItem = cartItems.get(movieId);
                    existingItem.setQuantity(quantity);
                } else if (quantity == 0) {
                    removeItemFromCart(movieId);
                }
                       } else if (addToCart != null && addToCart == true) {
                            if (movieTitle != null) {
                                    Movie movie = new Movie(movieId, movieTitle);
                                    addItemToCart(movie, quantity);
                            }
                       }
        }
    }
    public void removeItemFromCart(Integer id) {
        if (id != null && cartItems.containsKey(id)) {
            cartItems.remove(id);
        }
    }
    
    public void removeAllItemsFromCart() {
        cartItems.clear();
    }
    
    public boolean containsItemForMovie(Movie movie) {
        return (cartItems.containsKey(movie.getId()));
    }
    
    public List<CartItem> getCartItems() {
                return new ArrayList<CartItem>(cartItems.values());
    }
}

