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
        int productListSize =  products.size();
        for(int i = 0; i < productListSize; i++) {
            product = products.get(i);
            for (int j = i - 1; j > -1; j--) { //check for occurrence in new block
                if (product.getProductCode().equals(products.get(j).getProductCode())) {
                    product.setPreviousEntryNumber(products.get(j).getNumber());
                    break;
                }
            }
        }
    }

    public static void searchOccurrencesInBlockchain(List<Product> initialBlockProducts, String initialBlockPreviousHash, Connection connection, List<Product> products, Gson gson){
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Blocks WHERE hash = ?")){
            int productListSize = products.size();
            int done = 0;
            List<Product> currentBlockProducts = initialBlockProducts;
            String currentBlockPreviousHash = initialBlockPreviousHash;
            ResultSet rs = null;

            while(done < productListSize){
                for(Product product : products){
                    if(product.getPreviousEntryNumber() == 0) {
                        List<Product> occurrencesInBlock = currentBlockProducts.stream().filter(p -> p.getProductCode().equals(product.getProductCode())).toList();
                        if(!occurrencesInBlock.isEmpty()){
                            product.setPreviousEntryNumber(occurrencesInBlock.get(0).getNumber());
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
                    break;
                }
            }
        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }
}
