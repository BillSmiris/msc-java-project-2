package com.unipi.vsmyris.mscjavaproject2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Block {
    private String hash;
    private String previousHash;
    private String data;
    private long timestamp;
    private int nonce;

    public Block(String previousHash, List<Product> data) {
        this.previousHash = previousHash;
        this.data = new GsonBuilder().setPrettyPrinting().create().toJson(data.reversed());
        this.timestamp = new Date().getTime();
        this.hash = calculateBlockHash(nonce);
    }

    public String calculateBlockHash(int nonce){
        String dataToHash = previousHash+String.valueOf(timestamp)
                +data+String.valueOf(nonce);
        MessageDigest digest = null;
        byte[] bytes = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes){
            builder.append(String.format("%02x",b));
        }
        return builder.toString();
    }

    public String mineBlock(int prefix){
        String prefixString = new String(new char[prefix]).replace('\0','0');
        int numOfThreads = 6;
        boolean hashIsValid = hash.substring(0,prefix).equals(prefixString);
        String[] threadHashes = new String[numOfThreads];
        int[] threadNonces = new int[numOfThreads];
        int noncesPerThread = 1000;
        int startingNonce = 0;
        try(ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads)){
            while(!hashIsValid){
                CountDownLatch countDownLatch = new CountDownLatch(numOfThreads);
                for(int i = 0; i < numOfThreads; i++){
                    int finalI = i;
                    int finalStartingNonce = startingNonce;
                    executorService.execute(() -> {
                        int localNonce = finalStartingNonce;
                        String currentHash;
                        boolean found = false;
                        int limit = finalStartingNonce + noncesPerThread;

                        for(; localNonce < limit; localNonce++){
                            currentHash = calculateBlockHash(localNonce);
                            threadNonces[finalI] = localNonce;
                            if(currentHash.substring(0,prefix).equals(prefixString)){
                                threadHashes[finalI] = currentHash;
                                threadNonces[finalI] = localNonce;
                                found = true;
                                break;
                            }
                        }
                        if(!found){
                            threadNonces[finalI] = -1;
                        }
                        countDownLatch.countDown();
                    });
                    startingNonce += noncesPerThread;
                }
                countDownLatch.await();
                for(int i = 0; i < numOfThreads; i++){
                    if(threadNonces[i] > -1){
                        nonce = threadNonces[i];
                        hash = threadHashes[i];
                        hashIsValid = true;
                        break;
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return hash;
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getNonce() {
        return nonce;
    }

    public ArrayList<Product> getDeserializedData(){
        return new Gson().fromJson(this.data, ArrayList.class);
    }
}
