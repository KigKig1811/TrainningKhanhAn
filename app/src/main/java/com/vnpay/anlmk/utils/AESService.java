
package com.vnpay.anlmk.utils;


import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AESService {
   private static AESService instance;
   public final int IV_LENGTH = 16;


   public static AESService getInstance() {
       if (instance == null)
           instance = new AESService();
       return instance;
   }

   public byte[] generateIV() {
       byte[] iv = new byte[IV_LENGTH];
       SecureRandom prng = new SecureRandom();
       prng.nextBytes(iv);
       return iv;
   }

   public byte[] encrypt(String strSecretKey, String text, byte[] iv) throws Exception {
       byte[] encodedKey = strSecretKey.getBytes();
       SecretKey secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
       Cipher aesCipherForEncryption = Cipher.getInstance("AES/CTR/NoPadding");
       aesCipherForEncryption.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
       byte[] byteDataToEncrypt = text.getBytes("UTF-8");
       byte[] byteCipherText = aesCipherForEncryption
               .doFinal(byteDataToEncrypt);
       return byteCipherText;

   }


   public byte[] encryptByte(String strSecretKey, String byteDataToEncrypt, byte[] iv) throws Exception {
       byte[] encodedKey = strSecretKey.getBytes();
       SecretKey secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
       Cipher aesCipherForEncryption = Cipher.getInstance("AES/CTR/NoPadding");
       aesCipherForEncryption.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
       byte[] byteCipherText = aesCipherForEncryption
               .doFinal(byteDataToEncrypt.getBytes("UTF-8"));
       return byteCipherText;

   }

   public byte[] encryptByte(String strSecretKey, byte[] byteDataToEncrypt, byte[] iv) throws Exception {
       byte[] encodedKey = strSecretKey.getBytes();
       SecretKey secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
       Cipher aesCipherForEncryption = Cipher.getInstance("AES/CTR/NoPadding");
       aesCipherForEncryption.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

       byte[] byteCipherText = aesCipherForEncryption
               .doFinal(byteDataToEncrypt);
       return byteCipherText;

   }
    public  String encrypt(String strSecretKey, String data){
        try {
            byte[] iv = AESService.getInstance().generateIV();
            byte[] encodedKey = strSecretKey.getBytes();
            SecretKey secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
            Cipher aesCipherForEncryption = Cipher.getInstance("AES/CTR/NoPadding");
            aesCipherForEncryption.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
            byte[] dataEncrypted = aesCipherForEncryption.doFinal(data.getBytes());
            byte[] source = new byte[iv.length + data.length()];
            System.arraycopy(iv, 0, source, 0, iv.length);
            System.arraycopy(dataEncrypted, 0, source, iv.length, dataEncrypted.length);
            return Base64.encodeToString(source, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public  String decrypt(String key, String str) {
        try {
            byte[] allEncryptByte = Base64.decode(str, Base64.NO_WRAP);
            byte[] iv = new byte[16];
            System.arraycopy(allEncryptByte, 0, iv, 0, iv.length);
            byte[] dataByte = new byte[allEncryptByte.length - 16];
            System.arraycopy(allEncryptByte, iv.length, dataByte, 0, dataByte.length);

            byte[] encodedKey = key.getBytes();
            SecretKey secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
            Cipher aesCipherForDecryption = Cipher.getInstance("AES/CTR/NoPadding");
            aesCipherForDecryption.init(2, secretKey, new IvParameterSpec(iv));
            byte[] byteCipherText = aesCipherForDecryption.doFinal(dataByte);
            return new String(byteCipherText, "UTF-8");
        } catch (Exception var6) {
        }
        return "{}";
    }


   public String decrypt(String strSecretKey, byte[] textByte, byte[] iv) throws Exception {
       byte[] encodedKey = strSecretKey.getBytes();
       SecretKey secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
       Cipher aesCipherForDecryption = Cipher.getInstance("AES/CTR/NoPadding");

       aesCipherForDecryption.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

       byte[] byteCipherText = aesCipherForDecryption
               .doFinal(textByte);
       String strCipherText = new String(byteCipherText, "UTF-8");
       return strCipherText;


   }

   public boolean checkCRC(byte[] aResponseData, byte[] arr) {
       byte[] serverCRC = new byte[2];
       System.arraycopy(aResponseData, 0, arr, 0, arr.length);
       System.arraycopy(aResponseData, arr.length, serverCRC, 0, serverCRC.length);
       byte[] ourCRC = getCRC(arr);
       for (int i = 0; i < ourCRC.length; i++) {
           if (ourCRC[i] != serverCRC[i]) {
               return true;
           }
       }
       return false;
   }

   public byte[] getCRC(byte[] bytes) {
       int crc2 = -1;
       try {
           short crc = (short) 0xFFFF;
           for (int j = 0; j < bytes.length; j++) {
               byte c = bytes[j];
               for (int i = 7; i >= 0; i--) {
                   boolean c15 = ((crc >> 15 & 1) == 1);
                   boolean bit = ((c >> (7 - i) & 1) == 1);
                   crc <<= 1;
                   if (c15 ^ bit) {
                       crc ^= 0x1021; // 0001 0000 0010 0001 (0, 5, 12)
                   }
               }
           }
           crc2 = crc - 0xffff0000;
       } catch (Exception ex) {

       } finally {
           byte[] result = new byte[2];
           result[0] = (byte) (crc2 % 256);
           result[1] = (byte) (crc2 / 256);
           return result;
       }
   }


   private static final String ALGORITHM = "HmacSHA256";


   public byte[] createMac(byte[] bData, byte[] keybyte) throws NoSuchAlgorithmException, InvalidKeyException {

       Mac mac = Mac.getInstance(ALGORITHM);
       SecretKey key = new SecretKeySpec(keybyte, ALGORITHM);
       mac.init(key);
       mac.update(bData);
       return mac.doFinal();
   }



}
