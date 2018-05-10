package set1;

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

  static public Letter[] stdCharsFrequencies = new Letter[26];
  static public Letter[] emptyCharsFrequencies = new Letter[26];
  static public String chars = "etaoinshrdlcumwfgypbvkjxqz";
  static public double[] frequencies = { 12.702, 9.056, 8.167, 7.507, 6.966, 6.749, 6.327, 6.094,
      5.987, 4.253, 4.025, 2.782, 2.758, 2.406, 2.36, 2.228, 2.015, 1.974, 1.929, 1.492, 0.978,
      0.772, 0.153, 0.15, 0.095, 0.074 };

  class Letter {
    String name;
    double frequency;
    int counter = 0;

    Letter(char name, double frequency) {
      this.name = Character.toString(name);
      this.frequency = frequency;
    }

    void count() {
      counter++;
    }
  }

  public static void main(String[] args) {

  }

  void initLetterTables() {
    for (int ix = 0; ix < chars.length(); ix++) {
      stdCharsFrequencies[ix] = new Letter(chars.charAt(ix), frequencies[ix]);
      emptyCharsFrequencies[ix] = new Letter(chars.charAt(ix), 0);
    }
  }

  // Evaluates how probable a given is to be a valid english text. Measures both
  // letter frequency and also word length.
  static double evaluator(String s) {

    return 0;
  }

  Letter[] freqList(String s) {
    Letter[] list = Arrays.copyOf(emptyCharsFrequencies, emptyCharsFrequencies.length);
    int position = 0;
    for (Character c : s.toCharArray()) {
      position = chars.indexOf(c);
      list[position].count();
    }
    for (Letter letter : list) {
      letter.frequency = letter.counter / ((double) s.length()) * 100;
    }
    return list;
  }

  String hexStrToStr(String hex) {

    return null;
  }

}
