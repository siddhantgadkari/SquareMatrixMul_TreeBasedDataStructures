package collections;

import collections.exceptions.InvalidWordException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleCompactWordTreeFineGrain implements CompactWordsSet {
  private SimpleCompactWordNode root = new SimpleCompactWordNode(' ');
  private int wordCount = 0;
  private Lock lock = new ReentrantLock();

  public static void main(String[] args) throws InvalidWordException {
    String test = "qwertyuiopasdfghjklzxcvbnm";
    if (test == null || test.isEmpty()) {
      System.out.println("empty");
    }
    if (test.chars().min().getAsInt() < (int) 'a' || test.chars().max().getAsInt() > (int) 'z') {
      System.out.println("chars invalid");
    }
    else {
      System.out.println("valid");
    }
    int t = "asdf".charAt(0);
    System.out.println(t - 'a');

    char t2 = (char) 25+97;
    System.out.println(t2);

    String t3 = "string" + 'c';
    System.out.println("here: " + "a".substring(1));

    SimpleCompactWordTreeFineGrain tree = new SimpleCompactWordTreeFineGrain();
//    tree.add("aa");
//    System.out.println(tree.contains("a"));
//    System.out.println(tree.contains("aa"));
//    System.out.println(tree.contains("aaa"));

    List<String> t4 = List.of("aab", "bba", "ab", "aaa", "b", "a", "aa");
    System.out.println("list words");
    for (String s : t4) {
      tree.add(s);
    }
    for (String s : t4) {
      System.out.println(tree.contains(s));
    }
    System.out.println(tree.uniqueWordsInAlphabeticOrder());
  }

  private int getIndex (char c) {
    return (int) c - 'a';
  }
  @Override
  public boolean add(String word) throws InvalidWordException {
    CompactWordsSet.checkIfWordIsValid(word);
    SimpleCompactWordNode next = root;
    root.lock.lock();
    SimpleCompactWordNode curr = next;
    while (!word.isEmpty()) {
      int charIndex = word.charAt(0) - 'a';
      if (curr.children[charIndex] == null) {
        curr.children[charIndex] = new SimpleCompactWordNode(word.charAt(0));

      }
      next = curr.children[charIndex];
      next.lock.lock();
      curr.lock.unlock();
      curr = next;
      word = word.substring(1);
    }
    try {
      if (curr.isWord) {
        return false;
      } else {
        wordCount += 1;
        curr.isWord = true;
        return true;
      }
    }
    finally {
      curr.lock.unlock();
    }
  }
//"aab", "bba", "ab", "aaa"
  @Override
  public boolean remove(String word) throws InvalidWordException {
    CompactWordsSet.checkIfWordIsValid(word);
    if (!this.contains(word)) {
      return false;
    }
    SimpleCompactWordNode next = root;
    next.lock.lock();
    SimpleCompactWordNode curr = next;
    while (!word.isEmpty()) {
      int charIndex = word.charAt(0) - 'a';
      next = curr.children[charIndex];
      next.lock.lock();
      curr.lock.unlock();
      curr = next;
      word = word.substring(1);
    }
    try {
      curr.isWord = false;
      wordCount -= 1;
      return true;
    }
    finally {
      curr.lock.unlock();
    }
  }

  @Override
  public boolean contains(String word) throws InvalidWordException {
    CompactWordsSet.checkIfWordIsValid(word);
    SimpleCompactWordNode next = root;
    next.lock.lock();
    SimpleCompactWordNode curr = next;
    while (!word.isEmpty()) {
      int charIndex = word.charAt(0) - 'a';
      if (curr.children[charIndex] == null) {
        return false;
      }
      next = curr.children[charIndex];
      next.lock.lock();
      curr.lock.unlock();
      curr = next;
      word = word.substring(1);
    }
    curr.lock.unlock();
    return curr.isWord;
  }

  @Override
  public int size() {
    lock.lock();
    try {
      return wordCount;
    }
    finally {
      lock.unlock();
    }
  }

  @Override
  public List<String> uniqueWordsInAlphabeticOrder() {
    List<String> wordList = new ArrayList<>();
    getWords(root, "", wordList);
    return wordList;
  }
//"aab", "bba", "ab", "aaa", "b", "a", "aa"
  private void getWords(SimpleCompactWordNode node, String currStr, List<String> words) {
    for (SimpleCompactWordNode child : node.children) {
      if (child != null) {
        String temp = currStr + child.character;
        if (child.isWord) {
          words.add(temp);
        }
        getWords(child, temp, words);
      }
    }
  }
  private class SimpleCompactWordNode {
    private char character;
    private boolean isWord;
    private SimpleCompactWordNode[] children;

    private Lock lock = new ReentrantLock();
    public SimpleCompactWordNode(char character) {
      this.character = character;
      isWord = false;
      children = new SimpleCompactWordNode[26];
    }
  }
}
