package com.eduardo.projarq.t1.sales.config;

import com.eduardo.projarq.t1.sales.models.Product;
import com.eduardo.projarq.t1.sales.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final ProductRepository productRepository;
    
    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            initializeProducts();
        }
    }
    
    private void initializeProducts() {
        Product arroz = new Product();
        arroz.setCode("PROD001");
        arroz.setName("Arroz Integral");
        arroz.setDescription("Arroz integral orgânico 1kg");
        arroz.setPrice(8.50);
        arroz.setStock(100);
        arroz.setEssential(true);
        productRepository.save(arroz);
        
        Product feijao = new Product();
        feijao.setCode("PROD002");
        feijao.setName("Feijão Preto");
        feijao.setDescription("Feijão preto 1kg");
        feijao.setPrice(6.80);
        feijao.setStock(80);
        feijao.setEssential(true);
        productRepository.save(feijao);
        
        Product leite = new Product();
        leite.setCode("PROD003");
        leite.setName("Leite Integral");
        leite.setDescription("Leite integral 1L");
        leite.setPrice(4.20);
        leite.setStock(50);
        leite.setEssential(true);
        productRepository.save(leite);
        
        Product notebook = new Product();
        notebook.setCode("PROD004");
        notebook.setName("Notebook Dell");
        notebook.setDescription("Notebook Dell Inspiron 15 polegadas");
        notebook.setPrice(2500.00);
        notebook.setStock(10);
        notebook.setEssential(false);
        productRepository.save(notebook);
        
        Product smartphone = new Product();
        smartphone.setCode("PROD005");
        smartphone.setName("Smartphone Samsung");
        smartphone.setDescription("Smartphone Samsung Galaxy A54");
        smartphone.setPrice(1200.00);
        smartphone.setStock(15);
        smartphone.setEssential(false);
        productRepository.save(smartphone);
        
        Product headphone = new Product();
        headphone.setCode("PROD006");
        headphone.setName("Fone de Ouvido Bluetooth");
        headphone.setDescription("Fone de ouvido sem fio com cancelamento de ruído");
        headphone.setPrice(150.00);
        headphone.setStock(30);
        headphone.setEssential(false);
        productRepository.save(headphone);
        
        System.out.println("Sample products initialized successfully!");
    }
} 