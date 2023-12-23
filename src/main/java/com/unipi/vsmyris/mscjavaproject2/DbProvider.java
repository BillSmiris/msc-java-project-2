package com.unipi.vsmyris.mscjavaproject2;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.LinkedHashMap;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DbProvider implements AutoCloseable{

    private final Connection connection;
    private final Gson gson;
    private final TypeToken<ArrayList<Product>> typeToken;

    private static final DbProvider instance  = new DbProvider();
    private DbProvider(){
        if(instance != null){
            throw new IllegalStateException("Instance already created!");
        }
        System.out.println("\nDbProvider: Initializing db provider...");
        connection = connect();
        gson = new Gson();
        typeToken = new TypeToken<ArrayList<Product>>() {};
        createTableAndData();
        System.out.println("DbProvider: Db provider initialized!\n");
    }
    public static DbProvider getInstance(){
        return instance;
    }


    public void insertNewBlock(List<Product> newBlockProducts) {
        try (Statement statement = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Blocks VALUES(?,?,?,?,?)");){

            System.out.println("\nDbProvider: Creating new block...");
            //get last block based on timestamp
            System.out.println("DbProvider: Retrieving last block of the blockchain...");
            ResultSet rs = statement.executeQuery("SELECT hash, data, previousHash FROM Blocks ORDER BY timestamp DESC FETCH FIRST 1 ROW ONLY");
            String previousHash = "0";

            if(rs.next()){//if there is at least one block in the blockchain(we found the last block)
                previousHash = rs.getString("hash"); //save its hash as the previousHash of the new block
                System.out.println("DbProvider: Last block retrieved with hash " + previousHash);
                List<Product> retrievedBlockProducts = gson.fromJson(rs.getString("data"), typeToken.getType());
                String retrievedBlockPreviousHash = rs.getString("previousHash"); //get previous hash of current block

                //assign AA in the new product entries
                System.out.println("DbProvider: Setting new entry numbers for new product entries...");
                Utilities.setProductNumbers(newBlockProducts, retrievedBlockProducts.getFirst().getNumber());
                System.out.println("DbProvider: Done!");
                //set last occurrences of new product entries
                System.out.println("DbProvider: Checking for double entries in new block data...");
                Utilities.searchOccurrencesInSameBlock(newBlockProducts); //search in the same block
                System.out.println("DbProvider: Done!");
                //search in the blockchain
                System.out.println("DbProvider: Checking for previous entries of the products in the blockchain...");
                Utilities.searchOccurrencesInBlockchain(retrievedBlockProducts, retrievedBlockPreviousHash, connection, newBlockProducts, gson);
                System.out.println("DbProvider: Done!");
            }
            else { //if we are adding the first block
                System.out.println("DbProvider: No block found. The blockchain is currently empty.");
                //assign AA in the new product entries
                System.out.println("DbProvider: Setting new entry numbers for new product entries...");
                Utilities.setProductNumbers(newBlockProducts, 0);
                System.out.println("DbProvider: Done!");
                System.out.println("DbProvider: Checking for double entries in new block data...");
                Utilities.searchOccurrencesInSameBlock(newBlockProducts);
                System.out.println("DbProvider: Done!");
                //set last occurrences of new product entries
            }

            Block newBlock = new Block(previousHash, newBlockProducts);

            newBlock.mineBlock(6);

            preparedStatement.setString(1, newBlock.getHash());
            preparedStatement.setString(2, newBlock.getPreviousHash());
            preparedStatement.setString(3, newBlock.getData());
            preparedStatement.setLong(4, newBlock.getTimestamp());
            preparedStatement.setInt(5, newBlock.getNonce());
            int count = preparedStatement.executeUpdate();
            if(count>0){
                System.out.println(count + " record updated");
            }

            System.out.println("Done!");
        } catch (SQLException ex) {
            Logger.getLogger(Mainc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Product> selectAll(){
        try(Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Blocks WHERE hash = ?");){

            ResultSet rs = statement.executeQuery("SELECT hash, data, previousHash FROM Blocks ORDER BY timestamp DESC FETCH FIRST 1 ROW ONLY");

            String currentPreviousHash;
            List<Product> currentProducts;

            List<Product> allProducts = new ArrayList<>();

            while(rs.next()){
                currentPreviousHash = rs.getString("previousHash");
                currentProducts = gson.fromJson(rs.getString("data"), typeToken.getType());

                allProducts.addAll(currentProducts);

                preparedStatement.setString(1, currentPreviousHash);
                rs = preparedStatement.executeQuery();
            }

            System.out.println("Done!");

            return new ArrayList<>(allProducts
                    .stream()
                    .collect(Collectors.toMap(Product::getProductCode, p -> p, (p1, p2) -> p1, LinkedHashMap::new))
                    .values());

        } catch (SQLException ex) {
            Logger.getLogger(Mainc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Collections.emptyList();
    }

    public Product searchProduct(String productCode, Boolean lastOccurrence){
        Product productToReturn = null;

        try(Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Blocks WHERE hash = ?");) {

            ResultSet rs = statement.executeQuery("SELECT hash, data, previousHash FROM Blocks ORDER BY timestamp DESC FETCH FIRST 1 ROW ONLY");

            String currentPreviousHash;
            List<Product> currentProducts;

            outer:while(rs.next()){
                currentPreviousHash = rs.getString("previousHash");
                currentProducts = gson.fromJson(rs.getString("data"), typeToken.getType());
                for(Product currentProductsItem : currentProducts){
                    if(currentProductsItem.getProductCode().equals(productCode)){
                        productToReturn = currentProductsItem;
                        if(lastOccurrence){
                            break outer;
                        }
                    }
                }

                preparedStatement.setString(1, currentPreviousHash);
                rs = preparedStatement.executeQuery();
            }

            System.out.println("Done!");
        } catch (SQLException ex) {
            Logger.getLogger(Mainc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return productToReturn;
    }

    public List<Product> getPriceHistory(String productCode){
        List<Product> productHistory = new ArrayList<>();

        try(Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Blocks WHERE hash = ?");){

            ResultSet rs = statement.executeQuery("SELECT hash, data, previousHash FROM Blocks ORDER BY timestamp DESC FETCH FIRST 1 ROW ONLY");

            String currentPreviousHash;
            List<Product> currentProducts;

            while (rs.next()){
                currentPreviousHash = rs.getString("previousHash");
                currentProducts = gson.fromJson(rs.getString("data"), typeToken.getType());

                productHistory.addAll(currentProducts.stream().filter(p -> p.getProductCode().equals(productCode)).toList());

                preparedStatement.setString(1, currentPreviousHash);
                rs = preparedStatement.executeQuery();
            }

        } catch (SQLException ex){
            Logger.getLogger(Mainc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return productHistory;
    }

    private static void selectBlock(){
        try {
            Connection connection = connect();
            Statement statement = connection.createStatement();
            String selectSQL = "select * from D_USER";
            ResultSet resultSet = statement.executeQuery(selectSQL);
            while(resultSet.next()){
                System.out.println(resultSet.getString("USERNAME")+","+resultSet.getString("PASSWORD"));
            }
            statement.close();
            connection.close();
            System.out.println("Done!");
        } catch (SQLException ex) {
            Logger.getLogger(Mainc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Connection connect(){
        String connectionString = "jdbc:derby:mscjavaproject2;create=true";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            Logger.getLogger(Mainc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

    private void createTableAndData(){
        try (Connection connection = connect();
             Statement statement = connection.createStatement()){
            System.out.println("DbProvider: Creating database tables!");
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "BLOCKS", null);

            if(!tables.next()){
                System.out.println(tables.toString());
                String createTableSQL = "CREATE TABLE BLOCKS"
                        + "(hash VARCHAR(64) NOT NULL PRIMARY KEY,"
                        + "previousHash VARCHAR(64),"
                        + "data CLOB,"
                        + "timestamp BIGINT,"
                        + "nonce INT)";
                statement.executeUpdate(createTableSQL);
                System.out.println("DbProvider: Blocks table created successfully!");
            }
            else{
                System.out.println("DbProvider: Blocks table already exists! Skipping creation...");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Mainc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void close(){
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
