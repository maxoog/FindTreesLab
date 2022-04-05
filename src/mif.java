import java.lang.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class mif {
    public static class Node {
        Node left;
        Node right;
        Node parent;
        int x;
        int y;
        int size;
        boolean cycle;

        public Node(int x) {
            this.left = null;
            this.right = null;
            this.parent = null;
            this.size = 1;
            this.y = (new Random().nextInt() << 16) + new Random().nextInt();
            this.x = x;
            this.cycle = false;
        }
    }

    public static class Pair {
        Node first;
        Node second;

        private Pair(Node first, Node second) {
            this.first = first;
            this.second = second;
        }
    }

    public ArrayList<Node> nodes = new ArrayList<>();

    public int get_size(Node tree) {
        if (tree != null) {
            return tree.size;
        } else {
            return 0;
        }
    }

    public void update(Node tree) {
        tree.size = get_size(tree.left) + get_size(tree.right) + 1;
    }

    public Pair split(Node tree, int key) {
        if (tree == null) {
            return new Pair(null, null);
        }
        if (get_size(tree.left) >= key) {
            Pair p = split(tree.left, key);
            tree.left = p.second;
            if (p.second != null) {
                p.second.parent = tree;
            }
            update(tree);
            return new Pair(p.first, tree);
        }
        Pair p = split(tree.right, key - get_size(tree.left) - 1);
        tree.right = p.first;
        if (p.first != null) {
            p.first.parent = tree;
        }
        update(tree);
        return new Pair(tree, p.second);
    }

    public Node merge(Node left, Node right) {
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }
        if (left.y > right.y) {
            Node temp = merge(left.right, right);
            left.right = temp;
            temp.parent = left;
            left.size = get_size(left.left) + get_size(left.right) + 1;
            return left;
        }
        Node temp = merge(left, right.left);
        right.left = temp;
        temp.parent = right;
        right.size = get_size(right.left) + get_size(right.right) + 1;
        return right;
    }

    public void turn_back(Node tree) {
        if (tree == null) {
            return;
        }
        Node temp = tree.left;
        tree.left = tree.right;
        tree.right = temp;
        turn_back(tree.left);
        turn_back(tree.right);
    }

    public Node get_root(Node tree) {
        while (tree.parent != null)
            tree = tree.parent;
        return tree;
    }

    public Pair get_ends(Node tree) {
        Node node1;
        Node node2;
        node2 = node1 = tree;
        while (node1.left != null) {
            node1 = node1.left;
        }
        while (node2.right != null) {
            node2 = node2.right;
        }
        return new Pair(node1, node2);
    }

    public void insert(int left, int right) {
        Node first = nodes.get(left);
        Node second = nodes.get(right);
        Node left_root = get_root(first);
        Node right_root = get_root(second);
        if (left_root == right_root) {
            Pair ends = get_ends(left_root);
            if ((ends.first == first && ends.second == second) || (ends.first == second && ends.second == first)) {
                left_root.cycle = true;
            }
            return;
        }
        Pair left_ends = get_ends(left_root);
        Pair right_ends = get_ends(right_root);
        if (left_ends.second == first && right_ends.first == second) {
            merge(left_root, right_root);
        } else if (left_ends.first == first && right_ends.second == second) {
            merge(right_root, left_root);
        } else if (left_ends.first == first && right_ends.first == second) {
            turn_back(left_root);
            merge(left_root, right_root);

        } else if (left_ends.second == first && right_ends.second == second) {
            turn_back(right_root);
            merge(left_root, right_root);
        }
    }

    public int exists(Node tree) {
        int result = get_size(tree.left);
        while (tree.parent != null) {
            if (tree == tree.parent.right) {
                result += get_size(tree.parent.left) + 1;
            }
            tree = tree.parent;
        }
        return result;
    }

    public void gen(Node tree, int key, int left, int right) {
        Pair p = split(tree, key);
        p.first.parent = p.second.parent = null;
        Node i_root = get_root(nodes.get(left));
        Node j_root = get_root(nodes.get(right));
        turn_back(i_root);
        turn_back(j_root);
        merge(i_root, j_root);
    }

    public void del(int left, int right) {
        Node first = nodes.get(left);
        Node second = nodes.get(right);
        Node left_root = get_root(first);
        Pair right_ends = get_ends(left_root);
        if (left_root.cycle) {
            left_root.cycle = false;
            if ((right_ends.first == first && right_ends.second == second) || (right_ends.second == first && right_ends.first == second)) {
                return;
            }
            int left_num = exists(first);
            int right_num = exists(second);
            if (left_num + 1 == right_num) {
                gen(left_root, right_num, left, right);
            } else {
                gen(left_root, left_num, left, right);
            }
            get_root(nodes.get(left)).cycle = get_root(nodes.get(right)).cycle = false;
            return;
        }
        int left_num = exists(first);
        int right_num = exists(second);
        Pair p;
        if (left_num + 1 == right_num) {
            p = split(left_root, right_num);
        } else {
            p = split(left_root, left_num);
        }
        p.first.parent = p.second.parent = null;
    }

    public int query(int left, int right) {
        if (left == right) {
            return 0;
        }
        Node first = nodes.get(left);
        Node second = nodes.get(right);
        Node left_root = get_root(first);
        Node right_root = get_root(second);
        if (left_root != right_root) {
            return -1;
        }
        int left_num = exists(first);
        int right_num = exists(second);
        if (!left_root.cycle) {
            return Math.abs(right_num - left_num) - 1;
        }
        Node temp = left_root;
        while (temp.right != null) {
            temp = temp.right;
        }
        int maximum = exists(temp);
        if (right_num > left_num) {
            return Math.min(maximum - right_num + left_num, right_num - left_num - 1);
        } else {
            return Math.min(maximum - left_num + right_num, left_num - right_num - 1);
        }
    }

    public void solve() {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        int q = in.nextInt();
        for (int j = 0; j <= n; j++) {
            nodes.add(new Node(j));
        }
        for (int k = 0; k < m; k++) {
            int i = in.nextInt();
            int j = in.nextInt();
            insert(i, j);
        }
        for (int k = 0; k < q; k++) {
            String c = in.next();
            int i = in.nextInt();
            int j = in.nextInt();
            switch (c) {
                case "+":
                    insert(i, j);
                    break;
                case "-":
                    del(i, j);
                    break;
                case "?":
                    System.out.println(query(i, j));
                    break;
            }
        }
    }

    public static void main(String[] args) {
        new mif().solve();
    }
}
