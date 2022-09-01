/*
 * Copyright 2021 Cloudera, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudera.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Protect {


//    KeyGenerator keyGenerator = null;
    String key = null;
    SecretKeySpec keySpec = null;
//    SecretKey secretKey = null;
    Cipher cipher = null;
    final String initialVector = "ae280ckq";

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    // Converts byte array to hex string
    // From: http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public Protect(String key) {
        this.key = key;
        try {
            /*
            Create key spec seeded with a user defined key.
             */
            keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "Blowfish");

            /**
             * Create an instance of cipher mentioning the name of algorithm
             *     - Blowfish
             */
            cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
        } catch (NoSuchPaddingException ex) {
            System.out.println(ex);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex);
        }

    }

    /**
     * @param plainText
     * @return cipherBytes
     */
    public String encrypt(String plainText) {

        String rtn = null;
        byte[] encoding = new byte[0];
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new javax.crypto.spec.IvParameterSpec(initialVector.getBytes()));
            encoding = cipher.doFinal(plainText.getBytes());
        } catch (InvalidKeyException|InvalidAlgorithmParameterException|IllegalBlockSizeException|BadPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        rtn = DatatypeConverter.printBase64Binary(encoding);

//        System.out.println("-- Encrypted -----------");
//        System.out.println("Base64:\t " + rtn);
//        System.out.println("HEX:\t " + bytesToHex(encoding));

        return rtn;
    }

    /**
     * @param text
     * @return plainText
     */
    public String decrypt(String text) throws RuntimeException {
        String plainText = null;
        byte[] ciphertext = DatatypeConverter.parseBase64Binary(text);

        // Decrypt
        try {
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new javax.crypto.spec.IvParameterSpec(initialVector.getBytes()));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        byte[] message = new byte[0];
        try {
            message = cipher.doFinal(ciphertext);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
        plainText = new String(message);

//        System.out.println("-- Decrypted -----------");
//        System.out.println("HEX:\t " + bytesToHex(message));
//        System.out.println("PLAIN:\t " + plainText);

        return plainText;
    }

    public static void main(String[] args) {
        Protect blowfishAlgorithm = new Protect("hello2");
        String textToEncrypt = "Blowfish Algorithm";
        System.out.println("Text before Encryption: " + textToEncrypt);
        String cipherText = blowfishAlgorithm.encrypt(textToEncrypt);
        System.out.println("Cipher Text: " + cipherText);
        System.out.println("Text after Decryption: " + blowfishAlgorithm.decrypt(cipherText));
    }

}

