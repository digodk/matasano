package set1;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * The hex encoded string:
 * 
 * 1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736
 * ... has been XOR'd against a single character. Find the key, decrypt the message.
 * You can do this by hand. But don't: write code to do it for you.
 * How? Devise some method for "scoring" a piece of English plaintext.
 * Character frequency is a good metric. Evaluate each output and choose
 * the one with the best score.
 */
public class Challenge3 {
  // Class for many time pad cipher decoder

  public static void main(String[] args) {
    String encodedString = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";
    mtpDecoder decoder = new mtpDecoder();
    decoder.decodeHex(encodedString, 1);

  }

}

class mtpDecoder {
  // Auxiliary class for listing letter frequencies
  class Letter {
    String name;
    double frequency;
    int counter = 0;

    Letter(char name, double frequency) {
      this.name = Character.toString(name);
      this.frequency = frequency;
    }

    // Counter for how many times the letter appears in a string
    void count() {
      counter++;
    }
  }

  String chars = "etaoinshrdlcumwfgypbvkjxqz";
  Letter[] stdCharsFrequencies = new Letter[26];
  Letter[] emptyCharsFrequencies = new Letter[26];
  double[] frequencies = { 12.702, 9.056, 8.167, 7.507, 6.966, 6.749, 6.327, 6.094, 5.987, 4.253,
      4.025, 2.782, 2.758, 2.406, 2.36, 2.228, 2.015, 1.974, 1.929, 1.492, 0.978, 0.772, 0.153,
      0.15, 0.095, 0.074 };

  mtpDecoder() {
    initLetterTables();
  }

  // Initializes the letter frequency table
  void initLetterTables() {
    for (int ix = 0; ix < chars.length(); ix++) {
      stdCharsFrequencies[ix] = new Letter(chars.charAt(ix), frequencies[ix]);
      emptyCharsFrequencies[ix] = new Letter(chars.charAt(ix), 0);
    }
  }

  // Lists letter frequencies for a given string
  Letter[] freqList(String s) {
    Letter[] list = Arrays.copyOf(emptyCharsFrequencies, emptyCharsFrequencies.length);
    int position = 0;
    int totalChar = 0;
    for (Character c : s.toLowerCase().toCharArray()) {
      position = chars.indexOf(c);
      if (position >= 0) {
        list[position].count();
        totalChar++;
      }
    }
    for (Letter letter : list) {
      letter.frequency = letter.counter / ((double) totalChar) * 100;
    }
    return list;
  }

  // Parses hex strings to byte arrays. Odd sized strings receive a leading 0;
  public byte[] hexStringToByteArray(String s) {
    int len = s.length();
    if (len % 2 != 0) {
      s = "0" + s;
      len++;
    }
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
              + Character.digit(s.charAt(i + 1), 16));
    }
    return data;
  }

  // Converts byte data to Hex strings
  String bytesToHex(byte[] bytes) {
    final char[] hexArray = "0123456789ABCDEF".toCharArray();
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
  }

  // Converts byte arrays to a string
  String byteToChars(byte[] bytes) {
    String s = "";
    for (byte b : bytes) {
      s += (char) b;
    }
    return s;
  }

  // XORs a byte array against a given key
  byte[] singleKeyXOR(byte[] message, byte[] key) {
    byte[] cipher = new byte[message.length];
    for (int ix = 0; ix < message.length; ix++) {
      cipher[ix] = (byte) (message[ix] ^ key[ix % key.length]);
    }
    return cipher;
  }

  // determines the average word length of a string
  double avgWordLen(String s) {
    int wordCount = 0;
    int wordLen = 0;
    double lenSum = 0;
    String[] words = s.split("[\\s\u00A0]+");

    wordCount = words.length;
    if (wordCount == 0) {
      return 0;
    }
    for (String word : words) {
      wordLen = word.length();
      lenSum += wordLen;
    }
    return lenSum / wordCount;
  }

  // Evaluates how probable that a given string is a valid english text.
  // Ratings range from 0 (least likely) to 100 (most likely)
  // Measures both letter frequency and word length.
  double evaluator(String s) {
    final double stdWrdLen = 5.1;
    Letter[] frequencies = freqList(s);
    double rating = 100, avgLen = 0;
    avgLen = avgWordLen(s);
    for (int ix = 0; ix < frequencies.length; ix++) {
      rating -= (Math.abs(frequencies[ix].frequency - stdCharsFrequencies[ix].frequency)
              / stdCharsFrequencies[ix].frequency) * 100 / stdCharsFrequencies.length;
    }
    rating *= (1 - Math.abs(stdWrdLen - avgLen) / stdWrdLen);
    return rating;
  }

  void decodeHex(String encodedString, int keyLen) {
    ArrayList<Integer> evaluatedKeys = new ArrayList<>();
    ArrayList<Double> ratings = new ArrayList<>();
    double grade = 0;
    int keyRange = (int) Math.pow(2, 8 * keyLen);
    String xordString;
    byte[] message = hexStringToByteArray(encodedString);
    byte[] key = new byte[keyLen];
    byte[] xordBytes;
    System.out.println("Decoding \n" + encodedString + "\nfor a key range of " + keyRange);
    // Evaluates all possible keys in the key range
    for (int ix = 0; ix < keyRange; ix++) {
      key = ByteBuffer.allocate(keyLen).put((byte) ix).array();
      xordBytes = singleKeyXOR(message, key);
      xordString = byteToChars(xordBytes);
      grade = evaluator(xordString);
      System.out.println(
              "for key: " + ix + ", final string was: " + xordString + ", grade: " + grade);
      if (ix == 120) {
        Letter[] freq = freqList(xordString);
        System.out.println("avg wrd len: " + avgWordLen(xordString));
        for (Letter l : freq) {
          System.out.println(l.name + " - " + l.frequency);
        }
      }
      evaluatedKeys.add(ix);
      ratings.add(grade);
    }
    // Bubble sorting, cos I ain't got time for this shit.
    Integer tempKey;
    Double tempGrade;
    int pos = 0;
    int tempPos;
    while (pos < ratings.size() - 1) {
      tempKey = evaluatedKeys.get(pos);
      tempGrade = ratings.get(pos);
      tempPos = 0;
      for (int index = pos + 1; index < ratings.size(); index++) {
        if (ratings.get(index) > tempGrade) {
          tempGrade = ratings.get(index);
          tempKey = evaluatedKeys.get(index);
          tempPos = index;
        }
      }
      if (tempPos > 0) {
        ratings.add(pos, tempGrade);
        evaluatedKeys.add(pos, tempKey);
        ratings.remove(tempPos);
        evaluatedKeys.remove(tempPos);
      }
      pos++;
    }
    String result = "Most likely key is: " + Integer.toHexString(evaluatedKeys.get(0));
    result += "\nMost likely decrypted message is: " + new String(singleKeyXOR(message, key));
    System.out.println(result);
  }

  void decodeHex(String encodedString) {
    decodeHex(encodedString, 1);
  }
}