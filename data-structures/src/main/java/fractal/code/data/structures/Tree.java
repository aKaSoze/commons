package fractal.code.data.structures;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * Created by sorin.nica in May 2016
 */
public class Tree<T> {

    private final T nodeValue;

    private final List<Tree<T>> children = new LinkedList<>();

    public Tree(T value) {
        this.nodeValue = value;
    }

    public void addChild(Tree<T> node) {
        children.add(node);
    }

    public Tree<T> addChild(T value) {
        Tree<T> treeNode = new Tree<>(value);
        children.add(treeNode);
        return treeNode;
    }

    public Tree<T> getOrAddChild(T value) {
        return children.stream()
                .filter(child -> child.nodeValue.equals(value))
                .findFirst()
                .orElseGet(() -> addChild(value));
    }

    public void addChain(List<T> valueChain) {
        if (!valueChain.isEmpty()) {
            T head = valueChain.remove(0);
            getOrAddChild(head).addChain(valueChain);
        }
    }

    public Optional<Tree<T>> searchNode(T value) {
        if (this.nodeValue.equals(value)) return Optional.of(this);

        for (Tree<T> child : children) {
            Optional<Tree<T>> candidate = child.searchNode(value);
            if (candidate.isPresent()) return candidate;
        }

        return Optional.empty();
    }

    public List<T> verticeReduce(BinaryOperator<T> combinator) {
        if (children.isEmpty()) {
            List<T> wrapper = new LinkedList<>();
            wrapper.add(nodeValue);
            return wrapper;
        } else return children.stream()
                .flatMap(child -> child.verticeReduce(combinator).stream())
                .map(t -> combinator.apply(nodeValue, t))
                .collect(Collectors.toList());
    }

    public List<List<T>> toVertices() {
        if (children.isEmpty()) {
            List<T> wrapper = new LinkedList<>();
            List<List<T>> topLevelWrapper = new LinkedList<>();
            wrapper.add(nodeValue);
            topLevelWrapper.add(wrapper);
            return topLevelWrapper;
        } else return children.stream()
                .flatMap(child -> child.toVertices().stream())
                .map(vertice -> {
                    vertice.add(0, nodeValue);
                    return vertice;
                })
                .collect(Collectors.toList());
    }

    public class Printer {

        private class StringConstructor {

            private final StringBuilder sb = new StringBuilder();

            public StringConstructor append(String str) {
                sb.append(str);
                return this;
            }

            public StringConstructor append(String str, Integer multiplier) {
                for (int i = 0; i < multiplier; i++) append(str);
                return this;
            }

            @Override
            public String toString() {
                return sb.toString();
            }

        }

        public <T> void print(Tree<T> tree) {
            print(tree, 0);
        }

        private <T> void print(Tree<T> tree, int level) {
            StringConstructor sb = new StringConstructor();
            sb.append(" ", level).append(tree.nodeValue.toString());
            System.out.println(sb);
            tree.children.forEach(child -> print(child, level + 1));
        }
    }

    public static void main(String[] args) {
        Tree<String> root = new Tree<>("S3n:");
        root.addChain(new ArrayList<>(Arrays.asList("documents", "programming", "java", "project.md")));
        root.addChain(new ArrayList<>(Arrays.asList("documents", "programming", "ruby", "project.md")));
        root.addChain(new ArrayList<>(Arrays.asList("documents", "movies", "heat", "heat.avi")));
        root.addChain(new ArrayList<>(Arrays.asList("documents", "movies", "romantic", "titanic", "titanic.avi")));
        root.addChain(new ArrayList<>(Arrays.asList("docs", "movies", "romantic", "titanic", "titanic.avi")));

        Tree.Printer printer = root.new Printer();
        printer.print(root);

        root.verticeReduce((String s1, String s2) -> s1 + "/" + s2).forEach(System.out::println);
        root.toVertices().stream()
                .map(strings -> strings.stream()
                        .reduce((String s1, String s2) -> s1 + "/" + s2)
                        .orElse(""))
                .forEach(System.out::println);
    }

}
