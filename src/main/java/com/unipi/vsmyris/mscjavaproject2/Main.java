package com.unipi.vsmyris.mscjavaproject2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Main extends JFrame {
    private JTextField searchTermField;
    private JPanel mainPanel;
    private JButton searchProductBtn;
    private JButton addProductBtn;
    private JButton resetProductsBtn;
    private JButton addManyProductsButton;
    private JScrollPane productScrollPane;
    private JRadioButton lastEntryRadioButton;
    private JRadioButton firstEntryRadioButton;
    private JPanel productListPanel;
    private List<Product> products;
    private boolean lastEntry;
    private ButtonGroup buttonGroup;
    public static DbProvider dbProvider = DbProvider.getInstance();
    public static List<Runnable> miningTaskList = new ArrayList<>();
    public static Thread miningThread = new Thread(Main::miningThreadJob);
    public static Thread retrievalThread = null;
    public static final Object monitor = new Object();
    public static ReentrantLock miningLock = new ReentrantLock();
    public static ReentrantLock retrievalLock = new ReentrantLock();

    public Main(){
        setContentPane(mainPanel);
        setTitle("Product Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        lastEntry = true;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(lastEntryRadioButton);
        buttonGroup.add(firstEntryRadioButton);
        lastEntryRadioButton.setSelected(true);

        //populate scrollpane

        productListPanel = new JPanel();
        productListPanel.setLayout(new BoxLayout(productListPanel, BoxLayout.Y_AXIS));
        productListPanel.add(new JLabel("Loading..."));
        productScrollPane.setViewportView(productListPanel);

        startRetrievalThread(new Runnable() {
            @Override
            public void run() {
                products = dbProvider.selectAll();
                productListPanel.removeAll();
                for(Product product : products){
                    productListPanel.add(createProductPanel(product, false));
                }
                productListPanel.revalidate();
                productListPanel.repaint();
            }
        });

        //btn events
        searchProductBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                interruptRetrievalThread();
                startRetrievalThread(new Runnable() {
                    @Override
                    public void run() {
                        Product result = dbProvider.searchProduct(searchTermField.getText(), lastEntry);
                        productListPanel.removeAll();
                        if(result != null){
                            productListPanel.add(createProductPanel(result, true));
                        }
                        else {
                            productListPanel.add(new JLabel("No results"));
                        }

                        productListPanel.revalidate();
                        productListPanel.repaint();
                    }
                });
            }
        });

        resetProductsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                interruptRetrievalThread();
                startRetrievalThread(new Runnable() {
                    @Override
                    public void run() {
                        productListPanel.removeAll();
                        products = dbProvider.selectAll();
                        for(Product product : products){
                            productListPanel.add(createProductPanel(product, false));
                        }

                        productListPanel.revalidate();
                        productListPanel.repaint();

                        searchTermField.setText("");
                    }
                });
            }
        });

        addProductBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                interruptRetrievalThread();
                dispose();
                new NewProductForm(1);
            }
        });

        addManyProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                interruptRetrievalThread();
                dispose();
                new NumberOfEntriesForm();
            }
        });

        //radio button event listeners
        lastEntryRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastEntry = true;
            }
        });

        firstEntryRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastEntry = false;
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (e.getSource() instanceof JFrame) {
                    // User clicked the close button
                    cleanup();
                }
            }
        });
    }

    private JPanel createProductPanel(Product product, boolean search){
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(product.getTitle());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16));

        JLabel codeLabel = new JLabel("Product code: " + product.getProductCode());
        JLabel priceLabel = new JLabel("Price: " + product.getPrice());
        JLabel categoryLabel = new JLabel("Category: " + product.getCategory());

        JButton button = new JButton();
        button.setText("View");

        if(search){
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new ProductDetailsForm(product, lastEntry);
                }
            });
        }
        else{
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new ProductDetailsForm(product, true);
                }
            });
        }

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

    public static void cleanup(){
        dbProvider.close();
    }

    public static void main(String[] args) {
        new Main();
        miningThread.start();
    }

    private static void miningThreadJob(){
        miningLock.lock();
        while (true) {
            try {
                if (miningTaskList.isEmpty()) {
                    synchronized (monitor){
                        monitor.wait();
                    }
                }
                Runnable r = miningTaskList.remove(0);
                r.run();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void interruptRetrievalThread(){
        if(retrievalThread != null){
            retrievalThread.interrupt();
        }
    }

    public static void startRetrievalThread(Runnable runnable){
        retrievalThread = new Thread(() -> {
            retrievalLock.lock();
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                return;
            }

            runnable.run();

            retrievalLock.unlock();
            retrievalThread = null;
        });
        retrievalThread.start();
    }
}
