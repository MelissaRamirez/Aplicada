package com.venticas.controller;

import com.venticas.business.CategoryBusiness;
import com.venticas.business.FamilyBusiness;
import com.venticas.business.ProductBusiness;
import com.venticas.business.ProductImageBusiness;
import com.venticas.domain.Family;
import com.venticas.domain.Product;
import com.venticas.domain.ProductCategory;
import com.venticas.domain.ProductImage;
import com.venticas.form.ProductForm;
import com.venticas.form.ProductItemForm;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductBusiness productBusiness;

    @Autowired
    private CategoryBusiness categoryBusiness;

    @Autowired
    private FamilyBusiness familyBusiness;

    @Autowired
    private ProductImageBusiness imageBusiness;

    @RequestMapping(value = "/searchByFamily", method = RequestMethod.GET)
    public String searchByFamily(@RequestParam("familyID") int familyID, Model model) {
        List<Product> products = productBusiness.findByFamilyID(familyID);
        for (Product product : products) {
            product.setImagenes(imageBusiness.getProductImages(product.getId()));
        }
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryBusiness.findAll());
        model.addAttribute("families", familyBusiness.findAll());
        return "product/searchByFamilyView";
    }

    @RequestMapping(value = "/searchByCategory", method = RequestMethod.GET)
    public String searchByCategory(@RequestParam("categoryID") int categoryID, Model model) {
        List<Product> products = productBusiness.findByCategoryID(categoryID);
        for (Product product : products) {
            product.setImagenes(imageBusiness.getProductImages(product.getId()));
        }
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryBusiness.findAll());
        model.addAttribute("families", familyBusiness.findAll());
        return "product/searchByCategoryView";
    }

    @RequestMapping(value = "/productDetails", method = RequestMethod.GET)
    public String getProductDetails(@RequestParam("productID") int productID, Model model) {
    	ProductItemForm productItemForm = new ProductItemForm();
    	Product product = productBusiness.findProductById(productID);
    	BeanUtils.copyProperties(product, productItemForm);
    	model.addAttribute("product", product);
        model.addAttribute("productItemForm", productItemForm);
        model.addAttribute("product", productBusiness.findProductById(productID));
        model.addAttribute("categories", categoryBusiness.findAll());
        model.addAttribute("families", familyBusiness.findAll());
        return "product/productDetailsView";
    }

    @RequestMapping(value = "/searchByName", method = RequestMethod.POST)
    public String searchByName(@RequestParam("name") String name, Model model) {
        List<Product> products = productBusiness.findByName(name);
        for (Product product: products) {
            product.setImagenes(imageBusiness.getProductImages(product.getId()));
        }
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryBusiness.findAll());
        model.addAttribute("families", familyBusiness.findAll());

        return  "product/searchByNameView";
    }

    @RequestMapping(value = "/searchByName", method = RequestMethod.GET)
    public String searchByName(Model model) {
        model.addAttribute("products", new ArrayList<Product>());
        model.addAttribute("categories", categoryBusiness.findAll());
        model.addAttribute("families", familyBusiness.findAll());
        return "product/searchByNameView";
    }

    @RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
    public String updateProduct(@RequestParam("myFile") List<MultipartFile> images,
                                @Valid ProductForm productForm,
                                BindingResult br,
                                Model model) {

        model.addAttribute("productForm", productForm);
        Product product = new Product();

        if(br.hasErrors()) {
            return "product/insertProduct";
        }else {
            if (!images.isEmpty()) {
                for (MultipartFile image : images) {
                    String name = image.getOriginalFilename();
                    try {
                        //Se defina la ruta indicada para almacenar la imagen
                        byte[] bytes = image.getBytes();
                        Path path = FileSystems.getDefault().getPath("uploaded-img", name);
                        Files.write(path, bytes);

                        //Se guarda la ruta dentro del objeto productImage
                        ProductImage productImage = new ProductImage();
                        productImage.setFilePath(path.toString());
                        product.getImagenes().add(productImage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            //Se copian las propiedades simples del form al product mediante la clase BeanUtils
            BeanUtils.copyProperties(productForm, product);

            //Se guarda la categoria del producto
            ProductCategory category = new ProductCategory();
            category.setId(productForm.getCategory());
            product.setCategory(category);

            //Y se guardan las familias del producto
            List<Family> families = new ArrayList<>();
            for(int i = 0; i < productForm.getFamilies().size(); i++) {
                Family family = new Family();
                family.setId(productForm.getFamilies().get(i));
                families.add(family);
                product.setFamilies(families);
            }

           /* if(!productBusiness.updateProduct(product))
                return "error"; */
        }
        return "redirect:/findByName";
    }

    @RequestMapping(value = "/updateProduct", method = RequestMethod.GET)
    public String updateProduct(@RequestParam("productID") int productID, Model model) {
        model.addAttribute("product", productBusiness.findProductById(productID));
        model.addAttribute("productForm", new ProductForm());
        model.addAttribute("categories", categoryBusiness.findAll());
        model.addAttribute("allFamilies", familyBusiness.findAll());
        return "product/updateProductView";
    }

    @RequestMapping(value = "/deleteProduct", method = RequestMethod.GET)
    public String deleteProduct(@RequestParam("productID") int productID, Model model) {
        model.addAttribute("product", productBusiness.findProductById(productID));
        return "product/deleteProductView";
    }

    @RequestMapping(value = "/confirmDeleteProduct", method = RequestMethod.GET)
    public String deleteProduct(@RequestParam("productID") int productID) {
        productBusiness.deleteProduct(productID);
        return "redirect:/findByName";
    }

    @RequestMapping(value = "/findByName", method = RequestMethod.POST)
    public String findByName(@RequestParam("name") String name, Model model) {
        model.addAttribute("products", productBusiness.findByName(name));
        return  "product/listAllProducts";
    }

    @RequestMapping(value = "/findByName", method = RequestMethod.GET)
    public String findByName(Model model) {
        model.addAttribute("products", new ArrayList<Product>());
        return "product/listAllProducts";
    }

    @RequestMapping(value = "/insertProduct", method = RequestMethod.GET)
    public String insertProduct(Model model) {
        model.addAttribute("productForm", new ProductForm());
        model.addAttribute("categories", categoryBusiness.findAll());
        model.addAttribute("families", familyBusiness.findAll());
        return "product/insertProduct";
    }

    @RequestMapping(value = "/insertProduct", method = RequestMethod.POST)
    public String insertProduct(@RequestParam("myFile") List<MultipartFile> images,
                                @Valid ProductForm productForm,
                                BindingResult br,
                                Model model) {
        model.addAttribute("productForm", productForm);
        model.addAttribute("categories", categoryBusiness.findAll());
        model.addAttribute("families", familyBusiness.findAll());
        Product product = new Product();

        if(br.hasErrors()) {
            return "product/insertProduct";
        } else {
            // Si la lista de imagenes no está vacia guarda las imagenes en el server
            // y guarda el producto en la base de datos.
            if (!images.isEmpty()) {
                for (MultipartFile image: images) {
                    String name = image.getOriginalFilename();
                    try {
                        //Se defina la ruta indicada para almacenar la imagen
                        byte[] bytes = image.getBytes();
                        Path path = FileSystems.getDefault().getPath("uploaded-img", name);
                        Files.write(path, bytes);

                        //Se guarda la ruta dentro del objeto productImage
                        ProductImage productImage = new ProductImage();
                        productImage.setFilePath(path.toString());
                        product.getImagenes().add(productImage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //Se copian las propiedades simples del form al product mediante la clase BeanUtils
                BeanUtils.copyProperties(productForm, product);

                //Se guarda la categoria del producto
                ProductCategory category = new ProductCategory();
                category.setId(productForm.getCategory());
                product.setCategory(category);

                //Y se guardan las familias del producto
                List<Family> families = new ArrayList<>();
                for(int i = 0; i < productForm.getFamilies().size(); i++) {
                    Family family = new Family();
                    family.setId(productForm.getFamilies().get(i));
                    families.add(family);
                    product.setFamilies(families);
                }
                productBusiness.insertProduct(product);

                return "redirect:/insertProduct";
            }
        }
        return "product/error";
    }
}
