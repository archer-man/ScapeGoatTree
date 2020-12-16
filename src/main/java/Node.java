public class Node<T> {
    T value;
    Node<T> parent, right, left;

    Node(T value) {
        this.value = value;
    }
}