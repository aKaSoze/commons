package fractal.code.data.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by sorin.nica in September 2016
 */
public class TwoDimensionalList<T> {

    private final List<List<T>> backingLists = new ArrayList<>();

    public TwoDimensionalList() {
    }

    public TwoDimensionalList(T defaultElement, Integer rows, Integer columns) {
        for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
            List<T> row = new ArrayList<>(columns);
            backingLists.add(row);
            for (int colIndex = 0; colIndex < columns; colIndex++) row.add(defaultElement);
        }
    }

    public TwoDimensionalList(List<List<T>> backingLists) {
        Objects.requireNonNull(backingLists);
        this.backingLists.addAll(backingLists);
    }

    public void addRow(List<T> row) {
        Objects.requireNonNull(row);
        backingLists.add(new ArrayList<>(row));
    }

    public <U> TwoDimensionalList<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);

        TwoDimensionalList<U> result = new TwoDimensionalList<>();
        backingLists.forEach(row -> result.addRow(row.stream().map(mapper::apply).collect(Collectors.toList())));
        return result;
    }

    public Optional<T> reduce(BinaryOperator<T> columnsAccumulator, Function<T, T> columnValueDecorator, BinaryOperator<T> rowsAccumulator) {
        return backingLists.stream()
                .map(row -> row.stream().reduce(columnsAccumulator))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(columnValueDecorator::apply)
                .reduce(rowsAccumulator);
    }

    public void put(T element, Integer row, Integer column) {
        if (backingLists.size() <= row) {
            int oldSize = backingLists.size();
            for (int i = oldSize; i <= row; i++)
                backingLists.add(new ArrayList<>());
        }

        backingLists.get(row).remove(column.intValue());
        backingLists.get(row).add(column, element);
    }

    public T get(Integer row, Integer column) {
        return backingLists.get(row).get(column);
    }

    public Long count(T element) {
        long count = 0;
        for (int rowIndex = 0; rowIndex < backingLists.size(); rowIndex++) {
            List<T> row = backingLists.get(rowIndex);
            count += row.stream().filter(e -> e.equals(element)).count();
        }
        return count;
    }

    public Long countOnRow(Integer row, T element) {
        return backingLists.get(row).stream().filter(e -> e.equals(element)).count();
    }

    public Long countOnColumn(Integer column, T element) {
        long count = 0;
        for (int rowIndex = 0; rowIndex < backingLists.size(); rowIndex++) {
            List<T> row = backingLists.get(rowIndex);
            T e = row.get(column);
            if (e.equals(element)) count++;
        }
        return count;
    }

    public TwoDimensionalList<T> shallowClone() {
        return map(Function.identity());
    }

    public Tuple<Integer, Integer> size() {
        return new Tuple<>(backingLists.size(), backingLists.get(0).size());
    }

    public List<List<T>> getRows() {
        List<List<T>> rows = new ArrayList<>();
        backingLists.forEach(row -> rows.add(new ArrayList<>(row)));
        return rows;
    }

    public List<List<T>> getColumns() {
        List<List<T>> columns = new ArrayList<>();
        for (int colIndex = 0; colIndex < backingLists.get(0).size(); colIndex++)
            columns.add(new ArrayList<>());

        for (int row = 0; row < backingLists.size(); row++)
            for (int col = 0; col < backingLists.get(row).size(); col++)
                columns.get(col).add(get(row, col));

        return columns;
    }

    public List<List<T>> getDiagonals() {
        List<List<T>> sameSumDiagonals = new ArrayList<>();
        List<List<T>> increasingIndexesDiagonals = new ArrayList<>();

        for (int diagIndex = 0; diagIndex < (backingLists.size() * 2) + 1; diagIndex++) {
            sameSumDiagonals.add(new ArrayList<>());
            increasingIndexesDiagonals.add(new ArrayList<>());
        }

        for (int row = 0; row < backingLists.size(); row++)
            for (int col = 0; col < backingLists.get(row).size(); col++) {
                sameSumDiagonals.get((row + col)).add(get(row, col));
                increasingIndexesDiagonals.get((backingLists.size() - row + col)).add(get(row, col));
            }

        sameSumDiagonals.addAll(increasingIndexesDiagonals);
        return sameSumDiagonals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TwoDimensionalList<?> that = (TwoDimensionalList<?>) o;

        return backingLists != null ? backingLists.equals(that.backingLists) : that.backingLists == null;

    }

    @Override
    public int hashCode() {
        return backingLists != null ? backingLists.hashCode() : 0;
    }

    @Override
    public String toString() {
        return backingLists.toString();
    }
}
