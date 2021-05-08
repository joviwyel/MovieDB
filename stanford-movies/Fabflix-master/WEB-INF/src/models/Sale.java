
package models;

import java.sql.Date;

public class Sale {
    private int id;
    private int customerId;
    private int movieId;
    private Date saleDate;

    public Sale() {

    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getMovieId() {
        return movieId;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }
}