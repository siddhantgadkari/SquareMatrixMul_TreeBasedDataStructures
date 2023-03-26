package generalmatrices.matrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BinaryOperator;

public final class Matrix<T> {

//  public static void main(String[] args) {
//    Matrix<Integer> intMatrix = new Matrix(List.of(1,2,3,4));
//    System.out.println(intMatrix.toString());
//  }
  private final int order;
  private final T[][] matrix;

  public Matrix(List<T> elements) {
    double dim = Math.sqrt(elements.size());
    boolean orderIsIntegral = dim - Math.floor(dim) == 0;
    if (elements.isEmpty()) {
      throw new IllegalArgumentException("Cannot have empty list");
    }
    if (!orderIsIntegral) {
      throw new IllegalArgumentException("Dimension is not square");
    }
    order = (int) dim;
    T[][] matrix = (T[][]) new Object[order][order];
    for (int i = 0; i < elements.size(); i++) {
      matrix[i/order][i%order] = elements.get(i);
    }
    this.matrix = matrix;
  }

  public T get(int row, int col) {
    return matrix[row][col];
  }

  private T[] getCol(int col) {
    T[] column = (T[]) new Object[order];
    for (int i = 0; i < order; i++) {
      column[i] = matrix[i][col];
    }
    return column;
  }
  public int getOrder() {
    return order;
  }

  private List<T> toList(Matrix<T> matrix) {
    List<T> list = new ArrayList<>();
    for (int i = 0; i < order; i++) {
      for (int j = 0; j < order; j++) {
        list.add(matrix.get(i,j));
      }
    }
    return list;
  }

  @Override
  public String toString() {
    String matrixString = "[";
    for (int i = 0; i < order; i++) {
      String rowString = "[";
      for (int j = 0; j < order; j++) {
        rowString += matrix[i][j].toString() + ' ';
      }
      rowString = rowString.substring(0, rowString.length()-1);
      rowString += ']';
      matrixString += rowString;
    }
    return matrixString + ']';
  }

  public Matrix<T> sum(Matrix<T> other, BinaryOperator<T> elementSum) {
    Matrix<T> computed = new Matrix<>(toList(this));
    for (int i = 0; i < order; i++) {
      for (int j = 0; j < order; j++) {
        computed.matrix[i][j] = elementSum.apply(matrix[i][j], other.matrix[i][j]);
      }
    }
    return computed;
  }

  public Matrix<T> product(Matrix<T> other, BinaryOperator<T> elementSum, BinaryOperator<T> elementProduct) {
    Matrix<T> computed = new Matrix<>(toList(this));
    for (int i = 0; i < order; i++) {
      for (int j = 0; j < order; j++) {
        T[] thisRow = matrix[i];
        T[] otherCol = other.getCol(j);
        //This is to initialise a value to which we can keep 'adding'
        T total  = elementProduct.apply(thisRow[0] ,otherCol[0]);
        //This check needs to be here otherwise for a 1x1 matrix the index would be out of bounds
        if (thisRow.length > 1) {
          for (int k = 1; k < thisRow.length; k++) {
            T product = elementProduct.apply(thisRow[k], otherCol[k]);
            total = elementSum.apply(total, product);
          }
        }
        computed.matrix[i][j] = total;
      }
    }
    return computed;
  }
}
