package com.unipi.vsmyris.mscjavaproject2;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Utilities {
    public static void setProductNumbers(List<Product> products, int seed){
        for(Product product : products){
            product.setNumber(++seed);
        }
    }

    public static void searchOccurrencesInSameBlock(List<Product> products){
        Product product = null;
        for(int i = 0; i < products.size(); i++) {
            product = products.get(i);
            for (int j = i; j > 0; j--) { //check for occurrence in new block
                if (product.getProductCode().equals(products.get(j - 1).getProductCode())) {
                    product.setPreviousEntryNumber(products.get(j - 1).getNumber());
                    break;
                }
            }
        }
    }

    public static void searchOccurrencesInBlockchain(List<Product> initialBlockProducts, String initialBlockPreviousHash, Connection connection, List<Product> products){
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Blocks WHERE hash = ?")){
            Gson gson = new Gson();
            int productListSize = products.size();
            int done = 0;
            List<Product> currentBlockProducts = initialBlockProducts;
            String currentBlockPreviousHash = initialBlockPreviousHash;
            ResultSet rs = null;

            while(done < productListSize){
                for(Product product : products){
                    if(product.getPreviousEntryNumber() == 0) {
                        Product lastOccurrenceInBlock = currentBlockProducts.stream().filter(p -> p.getProductCode().equals(product.getProductCode())).toList().getLast();
                        if(lastOccurrenceInBlock != null){
                            product.setPreviousEntryNumber(lastOccurrenceInBlock.getPreviousEntryNumber());
                            done++;
                        }
                    }
                    else{
                        done++;
                    }
                }
                preparedStatement.setString(1, currentBlockPreviousHash);
                rs = preparedStatement.executeQuery();
                if(rs.next()){
                    currentBlockProducts = gson.fromJson(rs.getString("data"), new TypeToken<ArrayList<Product>>() {}.getType());
                    currentBlockPreviousHash = rs.getString("previousHash");
                }
                else{
                    return;
                }
            }
        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }
}
