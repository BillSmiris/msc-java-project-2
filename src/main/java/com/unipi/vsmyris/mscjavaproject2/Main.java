package com.unipi.vsmyris.mscjavaproject2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Main extends JFrame {
    private JTextField searchTermField;
    private JPanel mainPanel;
    private JButton searchProductBtn;
    private JButton addProductBtn;
    private JButton resetProductsBtn;
    private JButton addManyProductsButton;
    private JScrollPane productScrollPane;
    private JPanel productListPanel;
    private DbProvider dbProvider;
    private List<Product> products;

    public Main(){
        setContentPane(mainPanel);
        setTitle("Product Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        //populate scrollpane
        dbProvider = DbProvider.getInstance();
        products = dbProvider.selectAll();

        productListPanel = new JPanel();
        productListPanel.setLayout(new BoxLayout(productListPanel, BoxLayout.Y_AXIS));

        for(Product product : products){
            productListPanel.add(createProductPanel(product));
        }

        productScrollPane.setViewportView(productListPanel);

        //btn events
        searchProductBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Product result = dbProvider.searchProduct(searchTermField.getText(), true);
                productListPanel.removeAll();
                if(result != null){
                    productListPanel.add(createProductPanel(result));
                }
                else {
                    productListPanel.add(new JLabel("No results"));
                }

                productListPanel.revalidate();
                productListPanel.repaint();
            }
        });

        resetProductsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productListPanel.removeAll();
                products = dbProvider.selectAll();
                for(Product product : products){
                    productListPanel.add(createProductPanel(product));
                }

                productListPanel.revalidate();
                productListPanel.repaint();

                searchTermField.setText("");
            }
        });

        addProductBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new NewProductForm(1);
            }
        });

        addManyProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new NumberOfEntriesForm();
            }
        });
    }

    private JPanel createProductPanel(Product product){
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(product.getTitle());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16));

        JLabel codeLabel = new JLabel("Product code: " + product.getProductCode());
        JLabel priceLabel = new JLabel("Price: " + product.getPrice());
        JLabel categoryLabel = new JLabel("Category: " + product.getCategory());

        JButton button = new JButton();
        button.setText("View");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(null, product.getProductCode());
                dispose();
                new ProductDetailsForm(product);
            }
        });

        productPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 125));
        productPanel.add(Box.createHorizontalStrut(5));
        productPanel.add(Box.createVerticalStrut(10));
        productPanel.add(titleLabel);
        productPanel.add(Box.createVerticalStrut(5));
        productPanel.add(codeLabel);
        productPanel.add(priceLabel);
        productPanel.add(categoryLabel);
        productPanel.add(Box.createVerticalStrut(5));
        productPanel.add(button);
        productPanel.add(Box.createVerticalStrut(10));
        productPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        return productPanel;
    }

    public static void main(String[] args) {
        Main main = new Main();

        //System.out.println("dddd");
        //main.dbProvider.close();
    }
}
