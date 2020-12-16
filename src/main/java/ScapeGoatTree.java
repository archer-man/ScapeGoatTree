import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class ScapeGoatTree<T extends Comparable<T>> extends AbstractSet<T> implements Set<T> {

    private int n = 0;
    private int q = 0;
    private Node<T> root = null;

    @Override
    public int size() {
        return n;
    }

    public int size(Node<T> node) throws NullPointerException {
        if (node == null) {
            return 0;
        } else {
            return (1 + size(node.left) + size(node.right));
        }
    }

    public int height(Node<T> node) {
        if (root == null) {
            return -1;
        } else {
            return (1 + Math.max(height(node.left), height(node.right)));
        }
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    public boolean contains(T value) {
        if (root == null) return false;
        Node<T> result = findNodeItself(root, value);
        return (result != null && result.value == value);
    }

    private Node<T> findNodeItself(Node<T> currentNode, T value) {
        while (currentNode != null) {
            int compareResult = value.compareTo(currentNode.value);
            if (compareResult < 0) {
                currentNode = currentNode.left;
            } else if (compareResult > 0) {
                currentNode = currentNode.right;
            } else {
                return currentNode;
            }
        }
        return null;
    }

    @Override
    public boolean add(T t) throws NullPointerException {
        Node<T> x = new Node<>(t);
        int d = addWithDepth(x);
        double number = (Math.log(q) / Math.log(3.0 / 2.0));
        if (d > number) {
            Node<T> z = x.parent;
            while (3 * size(z) <= 2 * size(z.parent)) {
                z = z.parent;
            }
            rebuild(z.parent);
        }
        return d >= 0;
    }

    private void rebuild(Node<T> k) {
        int size = size(k);
        Node<T> parent = k.parent;
        ArrayList<Node<T>> array = new ArrayList<>();
        packArray(k, array, 0);
        if (parent == null) {
            root = buildBalancedNodes(array, 0, size);
            root.parent = null;
        } else if (parent.right == k) {
            parent.right = buildBalancedNodes(array, 0, size);
            parent.right.parent = parent;
        } else {
            parent.left = buildBalancedNodes(array, 0, size);
            parent.left.parent = parent;
        }
    }

    private int packArray(Node<T> k, ArrayList<Node<T>> array, int i) {
        if (k == null) {
            return i;
        }
        i = packArray(k.left, array, i);
        array.add(i++, k);
        return packArray(k.right, array, i);
    }

    private Node<T> buildBalancedNodes(ArrayList<Node<T>> array, int i, int size) {
        if (size == 0) {
            return null;
        }
        int m = size / 2;
        array.get(i + m).left = buildBalancedNodes(array, i, m);
        if (array.get(i + m).left != null) {
            array.get(i + m).left.parent = array.get(i + m);
        }
        array.get(i + m).right = buildBalancedNodes(array, i + m + 1, size - m - 1);
        if (array.get(i + m).right != null) {
            array.get(i + m).right.parent = array.get(i + m);
        }
        return array.get(i + m);
    }

    private int addWithDepth(Node<T> x) throws NullPointerException {
        Node<T> st = root;
        if (st == null) {
            root = x;
            n++;
            q++;
            return 0;
        }
        boolean added = false;
        int depth = 0;
        int compareResult = x.value.compareTo(st.value);
        do {
            if (compareResult < 0) {
                if (st.left == null) {
                    st.left = x;
                    x.parent = st;
                    added = true;
                } else {
                    st = st.left;
                }
            } else if (compareResult > 0) {
                if (st.right == null) {
                    st.right = x;
                    x.parent = st;
                    added = true;
                } else {
                    st = st.right;
                }
            } else {
                return -1;
            }
            depth++;
        } while (!added);
        n++;
        q++;
        return depth;
    }

    public boolean remove(T x) {
        boolean result = remove(root, x);
        if (root == null) return false;
        if (2 * n < q) {
            rebuild(root);
            q = n;
        }
        return result;
    }

    private boolean remove(Node<T> root, T val) {
        Node<T> nodeToDelete = findNodeItself(root, val);
        if (nodeToDelete == null) return false;
        if (nodeToDelete.left == null || nodeToDelete.right == null) {
            splice(nodeToDelete);
        } else {
            Node<T> smallestR = nodeToDelete.right;
            while (smallestR.left != null) {
                smallestR = smallestR.left;
            }
            nodeToDelete.value = smallestR.value;
            splice(smallestR);
        }
        return true;
    }

    private void splice(Node<T> node) {
        Node<T> parent;
        Node<T> temp;
        if (node.left != null) {
            temp = node.left;
        } else {
            temp = node.right;
        }
        if (node == root) {
            root = temp;
            parent = null;
        } else {
            parent = node.parent;
            if (parent.left == node) {
                parent.left = temp;
            } else {
                parent.right = temp;
            }
        }
        if (temp != null) {
            temp.parent = parent;
        }
        n--;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }
}
