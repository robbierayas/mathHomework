package mathHomework.controllers;

public class MainController {

    private String privateKey = "a2d43efac7e99b7e3cf4c07ebfebb3c349d8f2b5b0e1062d9cef93c170d22d4f";

    public static void main(String args[]){
        log("Starting Homework");

        log("Encode Private Key");
        log("Public Key");
        log("PKHash Step 1: Sha256");
        log("PKHash Step 2: RIPEMD160");
        log("CheckSum");
        log("Address");

        log("Decode Private Key");
        log("Address");
        log("PKHash");
    }

    private static void log(String message){
        System.out.println(message);
    }
}
