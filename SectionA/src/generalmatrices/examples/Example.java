package generalmatrices.examples;

import generalmatrices.matrix.Matrix;
import generalmatrices.pair.PairWithOperators;
import java.util.List;

public class Example {

  public static Matrix<PairWithOperators> multiplyPairMatrices(List<Matrix<PairWithOperators>> matrices) {
    Matrix<PairWithOperators> initial = matrices.get(0);
    for (int i = 1; i < matrices.size(); i++) {
      initial = initial.product(matrices.get(i), PairWithOperators::sum, PairWithOperators::product);
    }
    return initial;
  }

}
