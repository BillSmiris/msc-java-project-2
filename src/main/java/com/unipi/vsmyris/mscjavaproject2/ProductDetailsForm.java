package com.unipi.vsmyris.mscjavaproject2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class ProductDetailsForm extends JFrame{
    private JPanel mainPanel;
    private JButton priceHistoryBtn;
    private JButton backBtn;
    private JLabel entryNumber;
    private JLabel productCode;
    private JLabel timestamp;
    private JLabel price;
    private JLabel description;
    private JLabel category;
    private JLabel previousEntryNumber;
    private JLabel productTitle;
    private Product product;

    private DbProvider dbProvider;

    public ProductDetailsForm(Product product){
        this.product = product;
        dbProvider = DbProvider.getInstance();

        setContentPane(mainPanel);
        setTitle("Product Details");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        SetProductDetails(product);

        //btn event listeners
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Main();
            }
        });

        priceHistoryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new PriceHistoryForm(product);
            }
        });
    }

    private void SetProductDetails(Product product){
        entryNumber.setText("Entry Number: " + product.getNumber());
        productCode.setText("Product Code: " + product.getProductCode());
        productTitle.setText("Title: " + product.getTitle());
        timestamp.setText("Date Created: " + new Date(product.getTimestamp()));
        price.setText("Price: " + product.getPrice());
        description.setText("Description: " + product.getDescription());
        category.setText("Category: " + product.getCategory());
        previousEntryNumber.setText("Previous Entry Number: " + product.getPreviousEntryNumber());
    }
}
