package com.unipi.vsmyris.mscjavaproject2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.List;

public class PriceHistoryForm extends JFrame{
    private JPanel mainPanel;
    private JScrollPane priceHisotryScrollPane;
    private JButton backBtn;
    private Product product;
    private JTable priceHistoryTable;
    private boolean lastEntry;

    public PriceHistoryForm(Product product, boolean lastEntry){
        this.product = product;
        this.lastEntry = lastEntry;

        setContentPane(mainPanel);
        setTitle("Price History");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        priceHisotryScrollPane.setViewportView(new JLabel("Loading..."));
        List<Product> priceHistory = Main.dbProvider.getPriceHistory(product.getProductCode());

        priceHistoryTable = new JTable(priceHistory.size(), 2);
        priceHistoryTable.getColumnModel().getColumn(0).setHeaderValue("Date");
        priceHistoryTable.getColumnModel().getColumn(1).setHeaderValue("Price");

        int i = 0;
        for(Product p : priceHistory){
            priceHistoryTable.setValueAt((new Date(p.getTimestamp())).toString(), i, 0);
            priceHistoryTable.setValueAt(String.valueOf(p.getPrice()), i, 1);
            i++;
        }

        priceHisotryScrollPane.setViewportView(priceHistoryTable);

        priceHisotryScrollPane.revalidate();
        priceHisotryScrollPane.repaint();

        //btn event listeners
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ProductDetailsForm(product, lastEntry);
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
}
