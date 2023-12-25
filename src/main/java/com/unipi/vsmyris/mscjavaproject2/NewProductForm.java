package com.unipi.vsmyris.mscjavaproject2;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

    public NewProductForm(int numberOfEntries){
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
                nextOrCreateBtn.setEnabled(true);
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
//                    Main.miningExecutorService.execute(() -> {
//                        Main.dbProvider.insertNewBlock(newProductList);
//                        JOptionPane.showMessageDialog(null, "Block created!");
//                    });
                    Main.miningTaskList.add(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
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

                nextOrCreateBtn.setEnabled(validateForm());
            }
        });

        //textbox change listeners
        productCodeField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                nextOrCreateBtn.setEnabled(validateForm());
            }
            public void removeUpdate(DocumentEvent e) {
                nextOrCreateBtn.setEnabled(validateForm());
            }
            public void insertUpdate(DocumentEvent e) {
                nextOrCreateBtn.setEnabled(validateForm());
            }
        });
        titleField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                nextOrCreateBtn.setEnabled(validateForm());
            }
            public void removeUpdate(DocumentEvent e) {
                nextOrCreateBtn.setEnabled(validateForm());
            }
            public void insertUpdate(DocumentEvent e) {
                nextOrCreateBtn.setEnabled(validateForm());
            }
        });
        priceField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                nextOrCreateBtn.setEnabled(validateForm());
            }
            public void removeUpdate(DocumentEvent e) {
                nextOrCreateBtn.setEnabled(validateForm());
            }
            public void insertUpdate(DocumentEvent e) {
                nextOrCreateBtn.setEnabled(validateForm());
            }
        });
        descriptionField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                nextOrCreateBtn.setEnabled(validateForm());
            }
            public void removeUpdate(DocumentEvent e) {
                nextOrCreateBtn.setEnabled(validateForm());
            }
            public void insertUpdate(DocumentEvent e) {
                nextOrCreateBtn.setEnabled(validateForm());
            }
        });
        categoryField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                nextOrCreateBtn.setEnabled(validateForm());
            }
            public void removeUpdate(DocumentEvent e) {
                nextOrCreateBtn.setEnabled(validateForm());
            }
            public void insertUpdate(DocumentEvent e) {
                nextOrCreateBtn.setEnabled(validateForm());
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (e.getSource() instanceof JFrame) {
                    // User clicked the close button
                    Main.cleanup();
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
        String productCode = productCodeField.getText().trim();
        String title = titleField.getText().trim();
        float price = Float.parseFloat(priceField.getText().trim());
        String description = descriptionField.getText().trim();
        String category = categoryField.getText().trim();

        return new Product(productCode, title, price, description, category);
    }

    private boolean validateForm(){
        boolean productCodeValid = !productCodeField.getText().trim().isEmpty();
        boolean titleValid = !titleField.getText().trim().isEmpty();
        boolean priceValid = !priceField.getText().trim().isEmpty();
        try{
            Float.parseFloat(priceField.getText().trim());
        }
        catch (Exception e){
            priceValid = false;
        }
        boolean descriptionValid = !descriptionField.getText().trim().isEmpty();
        boolean categoryValid = !categoryField.getText().trim().isEmpty();

        return productCodeValid && titleValid && priceValid && descriptionValid && categoryValid;
    }
}
