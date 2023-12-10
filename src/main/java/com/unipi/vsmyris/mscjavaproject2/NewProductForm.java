package com.unipi.vsmyris.mscjavaproject2;

import javax.swing.*;
import java.awt.*;

public class NewProductForm extends JFrame{
    private JPanel mainPanel;
    private JTextField productCodeField;
    private JTextField titleField;
    private JTextField priceField;
    private JTextField descriptionField;
    private JTextField categoryField;
    private JLabel productCodeLabel;
    private JButton createProductBtn;
    private JLabel titleLabel;
    private JLabel priceLabel;
    private JLabel descriptionLabel;
    private JLabel categoryLabel;

    public NewProductForm() {
        setTitle("New Product");
        setPreferredSize(new Dimension(420, 650));
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
