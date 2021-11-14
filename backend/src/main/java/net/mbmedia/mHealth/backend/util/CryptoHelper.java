package net.mbmedia.mHealth.backend.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class CryptoHelper
{
    private static final String SECRET_KEY = "super_secret_key";
    private static final String SALT = "super_salty_salt";


    public static String encrypt(String s)
    {
        try
        {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(s.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e);
        }
        return null;
    }

    public static String decrypt(String strToDecrypt)
    {
        try
        {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e);
        }
        return null;
    }

    public static String hash(String tohash)
    {
        MessageDigest digest = null;
        try
        {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        assert digest != null;
        byte[] encodedhash = digest.digest(tohash.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
        for (byte b : encodedhash)
        {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
            {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String generateInitialPassword(int length)
    {
        String CHAR_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
        String CHAR_UPPERCASE = CHAR_LOWERCASE.toUpperCase();
        String DIGIT = "0123456789";
        String OTHER_SYMBOL = "$!";

        String PASSWORD_ALLOW = CHAR_LOWERCASE + CHAR_UPPERCASE + DIGIT + OTHER_SYMBOL;

        String result = generateRandomString(PASSWORD_ALLOW, length);

        return shuffleString(result);
    }

    private static String generateRandomString(String input, int size)
    {
        SecureRandom random = new SecureRandom();

        if (input == null || input.length() <= 0)
            throw new IllegalArgumentException("Invalid input.");
        if (size < 1) throw new IllegalArgumentException("Invalid size.");

        StringBuilder result = new StringBuilder(size);
        for (int i = 0; i < size; i++)
        {
            int index = random.nextInt(input.length());
            result.append(input.charAt(index));
        }
        return result.toString();
    }

    private static String shuffleString(String input)
    {
        List<String> result = Arrays.asList(input.split(""));
        Collections.shuffle(result);
        return String.join("", result);
    }
}
