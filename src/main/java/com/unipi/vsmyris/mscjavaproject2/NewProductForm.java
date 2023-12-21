package com.unipi.vsmyris.mscjavaproject2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class NewProductForm extends JFrame{
    private JTextField productCodeField;
    private JTextField titleField;
    private JTextField priceField;
    private JTextField descriptionField;
    private JTextField categoryField;
    private JButton cancelBtn;
    private JButton previousBtn;
    private JButton nextOrCreateBtn;
    private JPanel mainPanel;
    private int numberOfEntries;
    private int caret;
    private int maxCaret;
    private List<Product> newProductList;
    private DbProvider dbProvider;

    public NewProductForm(int numberOfEntries){
        dbProvider = DbProvider.getInstance();
        newProductList = new ArrayList<>();
        this.numberOfEntries = numberOfEntries;
        caret = 1;
        maxCaret = 0;

        setContentPane(mainPanel);
        setTitle("Add New Products");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        if(numberOfEntries == 1){
            nextOrCreateBtn.setText("Create");
        }

        //btn click listeners

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Main();
            }
        });

        previousBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                caret--;
                populateForm(newProductList.get(caret - 1));
                nextOrCreateBtn.setText("Next");
                if(caret == 1){
                    previousBtn.setEnabled(false);
                }
            }
        });

        nextOrCreateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previousBtn.setEnabled(true);

                if(caret <= maxCaret){
                    newProductList.set(caret - 1, newProductFromForm());
                }
                else{
                    newProductList.add(newProductFromForm());
                    maxCaret++;
                }

                if(caret == numberOfEntries){

                    dbProvider.insertNewBlock(newProductList);
                    dispose();
                    new Main();
                }

                caret++;
                if(caret == numberOfEntries){
                    nextOrCreateBtn.setText("Create");
                }
                if(caret > maxCaret){
                    clearForm();
                }
                else{
                    populateForm(newProductList.get(caret - 1));
                }
            }
        });
    }

    private void populateForm(Product product){
        productCodeField.setText(product.getProductCode());
        titleField.setText(product.getTitle());
        priceField.setText(String.valueOf(product.getPrice()));
        descriptionField.setText(product.getDescription());
        categoryField.setText(product.getCategory());
    }

    private void clearForm(){
        productCodeField.setText("");
        titleField.setText("");
        priceField.setText("");
        descriptionField.setText("");
        categoryField.setText("");
    }

    private Product newProductFromForm(){
        String productCode = productCodeField.getText();
        String title = titleField.getText();
        float price = Float.parseFloat(priceField.getText());
        String description = descriptionField.getText();
        String category = categoryField.getText();

        return new Product(productCode, title, price, description, category);
    }
}
