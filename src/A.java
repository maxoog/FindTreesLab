import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;


public class A {
    private final static BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    public static String next() {
        StringBuilder sb = new StringBuilder();
        try {
            while(true) {
                int n = bf.read();
                if (n == -1) {
                    return "OEF";
                }
                char charNum = (char) n;
                if (!Character.isWhitespace(charNum)) {
                    sb.append(charNum);
                } else {
                    if (sb.length() == 0) {
                        continue;
                    }
                    return sb.toString();
                }
            }
        } catch (IOException e) {
            System.out.println("IOException");
        }
        return "";
    }
    public static int nextInt() {
        StringBuilder sb = new StringBuilder();
        int num = 0;
        try {
            while(true) {
                int n = bf.read();
                char charNum = (char) n;
                if (!Character.isWhitespace(charNum)) {
                    sb.append(charNum);
                } else {
                    if (sb.length() == 0) {
                        continue;
                    }
                    num = Integer.parseInt(sb.toString());
                    sb.setLength(0);
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("IOException");
        }
        return num;
    }


    public static void main(String[] args) {
        Dekartach d = new Dekartach();

        while (true) {
            String command = next();
            if (command.equals("insert")) {
                d.insert(nextInt());
            } else if (command.equals("exists")) {
                System.out.println(d.exists(d.head, nextInt()));
            } else if (command.equals("prev")) {
                System.out.println(d.prev(nextInt()));
            } else if (command.equals("next")) {
                System.out.println(d.next(nextInt()));
            } else if (command.equals("delete")) {
                d.delete(nextInt());
            } else if (command.equals("+")) {
                d.insert(nextInt());
            } else if (command.equals("?")) {
                d.next(nextInt());
            } else if (command.equals("h")) {
                System.out.println(d.head);
            } else if (command.equals("split")) {
                Dekartach.pair r = d.split(d.head, nextInt());
                System.out.println(r.first);
                System.out.println(r.second);
            } else if (command.equals("s")) {
                d.toString(d.head);
            } else if (command.equals("hr")) {
                System.out.println(d.head.right);
            } else if (command.equals("hl")) {
                 System.out.println(d.head.left);
            } else {
                break;
            }
        }
    }

    public static class Dekartach {
        Random rand;
        node head;
        int size;

        public Dekartach() {
            this.head = null;
            this.rand = new Random();
            this.size = 0;
        }

        static class node {
            int x, y;
            node right, left;
            public node(int x, int y) {
                this.x = x;
                this.y = y;
            }
            public String toString() {
                return "(" + x + " " + y + ")";
            }
        }

        static class pair {
            node first, second;
            public pair(node first, node second) {
                this.first = first;
                this.second = second;
            }
        }

        public void insert(int x) {
            if (!exists(head, x)) {
                size++;
                head = insertIm(head, new node(x, rand.nextInt()));
            }
        }

        public node insertIm(node A, node node) {
            if (A == null) {
                return node;
            }
            if (node.y > A.y) {
                pair p = split(A, node.x);
                node.left = p.first;
                node.right = p.second;
                return node;
            }
            if (A.x <= node.x) {
                A.right = insertIm(A.right, node);
            } else {
                A.left = insertIm(A.left, node);
            }
            return A;
        }

        public int min(node node) {
            if (node.left == null) {
                return node.x;
            } else {
                return min(node.left);
            }
        }

        public void delete(int x) {
            size--;
            head = deleteIm(head, x);
        }

        public node deleteIm(node node, int x) {
            if (node == null) {
                return null;
            }
            if (node.x == x) {
                return merge(node.left, node.right);
            } else if (node.x < x) {
                node.right = deleteIm(node.right, x);
            } else {
                node.left = deleteIm(node.left, x);
            }
            return node;
        }

        public int max(node node) {
            if (node.right == null) {
                return node.x;
            } else {
                return max(node.right);
            }
        }

        public pair split(node head, int x) {
            if (head == null) {
                return new pair(null, null);
            }
            if (head.x < x) {
                pair splitted = split(head.right, x);
                head.right = splitted.first;
                return new pair(head, splitted.second);
            } else {
                pair splitted = split(head.left, x);
                head.left = splitted.second;
                return new pair(splitted.first, head);
            }
        }

        public boolean exists(node node, int x) {
            if (node == null) {
                return false;
            }
            if (node.x < x) {
                return exists(node.right, x);
            } else if (node.x > x) {
                return exists(node.left, x);
            } else {
                return true;
            }
        }

        public Object prev(int x) {
            pair splitted = split(head, x);
            if (splitted.first == null) {
                return "none";
            }
            int result = max(splitted.first);
            head = merge(splitted.first, splitted.second);
            return result;
        }

        public Object next(int x) {
            pair splitted = split(head, x + 1);
            if (splitted.second == null) {
                return "none";
            }
            int result = min(splitted.second);
            head = merge(splitted.first, splitted.second);
            return result;
        }

        public void toString(node node) {
            System.out.println(node);
            if (node.left != null) {
                toString(node.left);
            } else {
                System.out.println("null");
            }
            if (node.right != null) {
                toString(node.right);
            } else {
                System.out.println("null");
            }
        }

        public int size(node node) {
            if (node != null) {
                return 1 + size(node.left) + size(node.right);
            }
            return 0;
        }

        public node merge(node left, node right) {
            if (left == null) {
                return right;
            }
            if (right == null) {
                return left;
            }
            if (left.y > right.y) {
                left.right = merge(left.right, right);
                return left;
            } else {
                right.left = merge(left, right.left);
                return right;
            }
        }
    }
}
