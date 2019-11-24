package com.venticas.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class ProductForm {

    private int id;
    @NotNull
    @Size(min = 5, max = 50)
    private String name;
    @NotNull
    @Size(min = 10, max = 500)
    private String description;
    @NotNull
    @Min(1000)
    private float price;
    @Min(1)
    @Max(200)
    private int unitsOnStock;
    @Min(1)
    @Max(200)
    private int category;
    private List<String> images;
    private List<Integer> families;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductForm() {
        images = new ArrayList<>();
        families = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getUnitsOnStock() {
        return unitsOnStock;
    }

    public void setUnitsOnStock(int unitsOnStock) {
        this.unitsOnStock = unitsOnStock;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<Integer> getFamilies() {
        return families;
    }

    public void setFamilies(List<Integer> families) {
        this.families = families;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
