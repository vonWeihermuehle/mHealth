package net.mbmedia.mHealth.backend.util;

import java.util.Locale;
import java.util.TreeMap;
import java.util.UUID;

import static java.lang.Math.min;

public class ValueProvider
{
    public static final String FIXED_NUMERIC_STRING = "12345678901234567890123456789012345678901234567890123456789012345678901234567890";
    private static final String ALL_NUMBERS = "1234567890";
    private static final String UPPERCASE_ISO_BASIC_LATIN_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE_ISO_BASIC_LATIN_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String ALL_ISO_BASIC_LATIN_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final String[] familiennamen =
            {
                    "Müller",
                    "Schmidt",
                    "Schneider",
                    "Fischer",
                    "Weber",
                    "Meyer",
                    "Wagner",
                    "Becker",
                    "Schulz",
                    "Hoffmann",
                    "Schäfer",
                    "Koch",
                    "Bauer",
                    "Richter",
                    "Klein",
                    "Wolf",
                    "Schröder (Schneider)",
                    "Neumann",
                    "Schwarz",
                    "Zimmermann"
            };

    private static final String[] vornamen =
            {
                    "Noah",
                    "Leon",
                    "Paul",
                    "Matteo",
                    "Ben",
                    "Elias",
                    "Finn",
                    "Felix",
                    "Henry",
                    "Louis"
            };

    private static final String[] emailHoster =
            {
                    "1und1.de",
                    "freenet.de",
                    "gmx.net",
                    "gmail.com",
                    "web.de",
                    "outlook.com",
                    "yahoo.de"
            };

    private static final String[] orte =
            {
                    "München",
                    "Nürnberg",
                    "Augsburg",
                    "Regensburg",
                    "Ingolstadt",
                    "Fürth",
                    "Würzburg",
                    "Erlangen",
                    "Bamberg",
                    "Bayreuth"
            };

    private static final String loremImpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam luctus non augue sit amet placerat. Nullam quis malesuada ante, id euismod ligula. In consequat tellus euismod orci porta, nec blandit massa dapibus. Nullam sagittis augue lectus, a gravida nisl rutrum vitae. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aenean eget lacinia tellus. Quisque non gravida nunc. Nulla pellentesque erat dolor, at porta nulla aliquam molestie. Cras vel erat auctor, ornare nibh at, sagittis nisi. Nunc vitae luctus turpis.\n" +
            "Nullam dapibus vitae tellus ac sodales. In a enim sit amet metus laoreet sagittis. Donec accumsan felis augue, vitae placerat nulla luctus a. Pellentesque porta felis tincidunt, vehicula dui id, volutpat ante. Nam sit amet ultricies sem. Nulla purus odio, fringilla in dignissim at, condimentum a est. Vivamus consequat ante ut lectus ullamcorper mattis. Duis in molestie est, et ullamcorper eros. Aliquam id libero sed libero consectetur consequat. Pellentesque rutrum lorem non lacinia accumsan. Nunc enim purus, condimentum quis velit laoreet, vulputate luctus neque. Nulla luctus, augue vel mollis laoreet, ligula velit varius erat, quis convallis nisl libero ac nisl. Duis placerat semper mauris luctus eleifend. Vestibulum commodo sollicitudin nisl et auctor. Morbi dapibus metus et nunc auctor, vitae tempor arcu cursus.\n" +
            "Nulla venenatis nulla lectus, eu viverra quam consequat et. Duis id nibh elementum magna dapibus cursus. In nec sapien at metus ultrices fringilla feugiat vel massa. Integer auctor, purus vitae auctor luctus, felis diam consectetur lectus, eget venenatis lorem ligula vitae lectus. Proin tortor metus, molestie a ante ultricies, placerat dictum sapien. Pellentesque vitae lacus condimentum, venenatis ipsum vel, volutpat est. Phasellus aliquet facilisis urna, vitae auctor arcu fermentum at. Morbi mattis ac nisi et lobortis. Curabitur tempus, ante ac ultricies elementum, tortor dolor posuere massa, sed viverra urna purus sed sapien. Ut laoreet aliquam nisl, ac tempus neque efficitur quis.\n" +
            "In ac ex eros. Integer dapibus, augue a iaculis cursus, mi nisi facilisis sem, eget efficitur sem tellus vel enim. Praesent eget leo vel est rutrum consequat sed quis odio. Vestibulum sagittis luctus orci, ac ullamcorper augue fermentum id. Nunc congue sit amet tellus sed tempor. Nulla bibendum orci ut euismod tristique. Morbi consequat bibendum velit. Nullam in purus ac mauris vulputate ultrices eget ut sem. Maecenas porttitor eleifend dolor non rhoncus. Ut vitae vestibulum dolor. Aenean fringilla mollis placerat. Donec fermentum et nibh quis vulputate. Morbi augue augue, varius vel dolor eu, rutrum placerat lectus. Vestibulum dignissim libero id felis imperdiet, eu dictum nisl condimentum.\n" +
            "Aliquam fermentum eros hendrerit metus maximus rutrum. Cras viverra mi sit amet ultricies eleifend. Vestibulum a tincidunt purus. Integer semper sagittis enim ac ullamcorper. Mauris ac lacus id diam semper dapibus in porta dui. Suspendisse a mauris in justo molestie efficitur eget non odio. Integer egestas nunc sem, sit amet fringilla eros dictum vitae. Morbi luctus mauris ut condimentum dignissim. Suspendisse vulputate tellus ac justo tempor lacinia. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nullam ac dolor imperdiet, elementum sapien et, tempor neque. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Vestibulum nibh velit, laoreet eget quam at, mattis blandit lorem. Cras eu nibh facilisis, elementum dolor a, scelerisque nibh. ";

    private final String prefix;
    private final String suffix;

    public static ValueProvider mitZufallswerten()
    {
        return new ValueProvider("prefix", "suffix");
    }

    private ValueProvider(String prefix, String suffix)
    {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String familienname()
    {
        return familiennamen[intNumber(0, familiennamen.length)];
    }

    public String vorname()
    {
        return vornamen[intNumber(0, vornamen.length)];
    }

    public Namen namenkombiniert()
    {
        return new Namen(familienname(), vorname());
    }

    public UUID uuid()
    {
        return UUID.randomUUID();
    }

    public String randomString(int digits)
    {
        return randomString(ALL_ISO_BASIC_LATIN_LETTERS, digits);
    }

    public String randomString(String validChars, int length)
    {
        return randomCharacters(validChars, length);
    }

    private String randomCharacters(String from, int length)
    {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
        {
            sb.append(from.charAt(intNumber(0, from.length())));
        }
        return sb.toString();
    }

    public String decoratedString(String base)
    {
        return prefix + base + suffix;
    }

    public String decoratedString(String base, int maxLength)
    {
        if (base.length() > maxLength)
        {
            return base.substring(0, maxLength);
        }

        if (decoratedString(base).length() < maxLength)
        {
            return decoratedString(base);
        }

        if ((prefix + base).length() >= maxLength)
        {
            return (prefix + base).substring((prefix + base).length() - maxLength);
        }

        return decoratedString(base).substring(0, maxLength);
    }


    public String telefonnummer()
    {
        int anzahlZiffernGesamtOhneNull = intNumber(8, 12);
        int anzahlZiffernVorwahlOhneNull = intNumber(2, 5);
        int anzahlZiffernTelefonnummer = anzahlZiffernGesamtOhneNull - anzahlZiffernVorwahlOhneNull;

        StringBuilder telefonNummerMitVorwahl = new StringBuilder("0");
        for (int i = 0; i < anzahlZiffernVorwahlOhneNull; i++)
        {
            telefonNummerMitVorwahl.append(intNumber(1, 10));
        }
        telefonNummerMitVorwahl.append("-");
        for (int i = 0; i < anzahlZiffernTelefonnummer; i++)
        {
            telefonNummerMitVorwahl.append(intNumber(1, 10));
        }

        return telefonNummerMitVorwahl.toString();
    }

    /**
     * Zahl zwischen {@code minInclusive} (inklusive) und {@code maxExclusive} (exklusive)
     */
    public int intNumber(int minInclusive, int maxExclusive)
    {
        return (int) (Math.random() * (maxExclusive - minInclusive)) + minInclusive;
    }

    /**
     * Zahl zwischen 0 (inklusive) und {@code Integer.MAX_VALUE} (exklusive)
     */
    public int intNumber()
    {
        return intNumber(0, Integer.MAX_VALUE);
    }

    /**
     * Zahl zwischen {@code Integer.MIN_VALUE} (inklusive) und 0 (exklusive)
     */
    public int negativeInt()
    {
        return intNumber(Integer.MIN_VALUE, 0);
    }

    public <T> T valueOrNull(T wert)
    {
        return booleanValue() ? wert : null;
    }

    public boolean booleanValue()
    {
        return intNumber() % 2 == 0;
    }

    public String emailAddress()
    {
        return String.format("%s@%s", decoratedString("mailName"), emailHost());
    }

    public String emailAddress(String name, String vorname)
    {
        return String.format("%s.%s@%s", name, vorname, emailHost());
    }

    public String emailHost()
    {
        int i = intNumber(0, emailHoster.length);
        return emailHoster[i];
    }

    public <T extends Enum<?>> T getRandomEnum(Class<T> e)
    {
        T[] enumConstants = e.getEnumConstants();
        int i = intNumber(0, enumConstants.length);
        return enumConstants[i];
    }

    public String ort()
    {
        int i = intNumber(0, orte.length);
        return orte[i];
    }

    public String loremIpsum(int maxLength)
    {
        int loremIpsumLength = loremImpsum.length();
        int startPosition = intNumber(0, loremIpsumLength - maxLength);
        int endePosition = min(startPosition + maxLength, loremIpsumLength);
        return loremImpsum.substring(startPosition, endePosition);
    }

    public String romanNumber()
    {
        int i = intNumber(0, 300);
        return toRoman(i);
    }

    private String toRoman(int number)
    {
        TreeMap<Integer, String> map = new TreeMap<>();
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

        int l = map.floorKey(number);
        if (number == l)
        {
            return map.get(number);
        }
        return map.get(l) + toRoman(number - l);
    }

    public static class Namen
    {
        private final String familienname;
        private final String vorname;

        public Namen(String familienname, String vorname)
        {
            this.familienname = familienname;
            this.vorname = vorname;
        }

        public String getFamilienname()
        {
            return familienname;
        }

        public String getVorname()
        {
            return vorname;
        }

        public String getUsername()
        {
            return (familienname + "." + vorname).toLowerCase(Locale.ROOT);
        }
    }

}
