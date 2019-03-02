package fractal.code.data.structures;

/**
 * Created by sorin.nica in September 2016
 */
public class Table<T> {

//    private final List<List<T>> rows = new ArrayList<>();
//    private final List<List<T>> columns = new ArrayList<>();
//
//    public static <T> Table<T> ofColumns(List<List<T>> backingLists) {
//
//    }
//
//    public Table(List<List<T>> backingLists) {
//        Objects.requireNonNull(backingLists);
//        this.backingLists.addAll(backingLists);
//    }
//
//    public void addRow(List<T> row) {
//        Objects.requireNonNull(row);
//        backingLists.add(new ArrayList<>(row));
//    }
//
//    public <U> Table<U> map(Function<? super T, ? extends U> mapper) {
//        Objects.requireNonNull(mapper);
//
//        Table<U> result = new Table<>();
//        backingLists.forEach(ts -> result.addRow(ts.stream().map(mapper::apply).collect(Collectors.toList())));
//        return result;
//    }
//
//    public Optional<T> reduce(BinaryOperator<T> columnsAccumulator, Function<T, T> columnValueDecorator, BinaryOperator<T> rowsAccumulator) {
//        return backingLists.stream()
//                .map(row -> row.stream().reduce(columnsAccumulator))
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .map(columnValueDecorator::apply)
//                .reduce(rowsAccumulator);
//    }

}
