package mathHomework.models;

public class Address {
    EncryptionKey encryptionKey;
    String address;

    public Address(EncryptionKey encryptionKey){
        this.encryptionKey=encryptionKey;
        this.address=hashAddressFromPublicKey();
    }
    //TODO
    private String hashAddressFromPublicKey() {
        String pkHash = pkHash();
        byte[] checkSumBytes = getCheckSumBytes(pkHash);
        String address = base58("00"+pkHash+new String(checkSumBytes));
        return address;
    }

    private String pkHash() {
        return "";
    }

    private byte[] getCheckSumBytes(String pkHash) {
        return new byte[]{};
    }

    private String base58(String s) {
        return "";
    }

}
