package com.unipi.vsmyris.mscjavaproject2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.net.ProxySelector;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DbProvider {

    private static void insertNewBlock(List<Product> newBlockProducts) {
        try {
            Connection connection = connect();
            //get last block based on timestamp
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT hash, data, previousHash FROM Blocks ORDER BY timestamp DESC LIMIT 1");
            statement.close();
            String previousHash = "0";

            if(rs.next()){//if there is at least one block in the blockchain(we found the last block)
                previousHash = rs.getString("hash"); //save its hash as the previousHash of the new block

                Gson gson = new Gson();
                List<Product> retrievedBlockProducts = gson.fromJson(rs.getString("data"), new TypeToken<ArrayList<Product>>() {}.getType()); //get data of current block
                String retrievedBlockPreviousHash = rs.getString("previousHash"); //get previous hash of current block

                //assign AA in the new product entries
                Utilities.setProductNumbers(newBlockProducts, retrievedBlockProducts.getLast().getNumber());
                //set last occurrences of new product entries
                Utilities.searchOccurrencesInSameBlock(newBlockProducts); //search in the same block
                //search in the blockchain
                Utilities.searchOccurrencesInBlockchain(retrievedBlockProducts, retrievedBlockPreviousHash, connection, newBlockProducts);
            }
            else { //if we are adding the first block
                //assign AA in the new product entries
                Utilities.setProductNumbers(newBlockProducts, 0);
                Utilities.searchOccurrencesInSameBlock(newBlockProducts);
                //set last occurrences of new product entries
            }

            Block newBlock = new Block(previousHash, newBlockProducts);

            newBlock.mineBlock(5);

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Blocks VALUES(?,?,?,?,?)");
            preparedStatement.setString(1, newBlock.getHash());
            preparedStatement.setString(2, newBlock.getPreviousHash());
            preparedStatement.setString(3, newBlock.getData());
            preparedStatement.setLong(4, newBlock.getTimestamp());
            preparedStatement.setInt(5, newBlock.getNonce());
            int count = preparedStatement.executeUpdate();
            if(count>0){
                System.out.println(count + " record updated");
            }

            preparedStatement.close();
            connection.close();
            System.out.println("Done!");
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static List<Product> selectAll(){
        try {
            Connection connection = connect();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT hash, data, previousHash FROM Blocks ORDER BY timestamp DESC LIMIT 1");

            Gson gson = new Gson();
            TypeToken<ArrayList<Product>> typeToken = new TypeToken<>() {};
            String currentPreviousHash;
            List<Product> currentProducts;

            List<Product> allProducts = new ArrayList<>();

            if(rs.next()){
                currentPreviousHash = rs.getString("previousHash");
                currentProducts = gson.fromJson(rs.getString("data"), typeToken.getType());

                allProducts.addAll(currentProducts);

                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Blocks WHERE hash = ?");

                while(!currentPreviousHash.equals("0")){
                    preparedStatement.setString(1, currentPreviousHash);
                    rs = preparedStatement.executeQuery();
                    if(rs.next()){
                        currentPreviousHash = rs.getString("previousHash");
                        currentProducts = gson.fromJson(rs.getString("data"), typeToken.getType());
                        allProducts.addAll(currentProducts);
                    }
                }

                preparedStatement.close();
            }

            statement.close();
            connection.close();
            System.out.println("Done!");

            return new ArrayList<>(allProducts
                    .stream()
                    .collect(Collectors.toMap(Product::getProductCode, p -> p, (p1, p2) -> p1))
                    .values());

        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Collections.emptyList();
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
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Connection connect(){
        String connectionString = "jdbc:sqlite:mscjavaproject2.db";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }
}
