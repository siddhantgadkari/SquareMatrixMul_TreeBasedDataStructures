package collections;

import collections.exceptions.InvalidWordException;
import java.util.List;
import java.util.Optional;

public interface CompactWordsSet {

  static void checkIfWordIsValid(String word) throws InvalidWordException {
    if (word == null) {
      throw new InvalidWordException("The word is null");
    }
    if (word.isEmpty()) {
      throw new InvalidWordException("The word is empty");
    }
    if (word.chars().min().getAsInt() < (int) 'a' || word.chars().max().getAsInt() > (int) 'z') {
      throw new InvalidWordException("Invalid characters");
    }
  }

  boolean add(String word) throws InvalidWordException;

  boolean remove(String word) throws InvalidWordException;

  boolean contains(String word) throws InvalidWordException;

  int size();

  List<String> uniqueWordsInAlphabeticOrder();

}
