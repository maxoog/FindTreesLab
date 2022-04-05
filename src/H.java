import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;


public class H {
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
            System.out.print("IOException");
        }
        return "";
    }
    public static int nextint() {
        return Integer.parseInt(next());
    }


    public static void main(String[] args) {
        Dekartach d = new Dekartach();
        int n = nextint();
        int m = nextint();
        for (int i = 0; i < n; i++) {
            d.addAfter(i, i + 1);
        }
        for (int i = 0; i < m; i++) {
            d.reverse(nextint(), nextint());
        }
        d.str(d.head);
        for (int i = 0; i < n; i++) {
            System.out.print(Dekartach.arr.get(i) + " ");
        }
    }

    public static class Dekartach {
        public static ArrayList<Integer> arr = new ArrayList<>(0);
        Random rand;
        node head;
        int size;

        public void reverse(int l, int r) {
            pair A_BC = split(head, l - 1);
            pair B_C = split(A_BC.second, r - l + 1);
            node B = B_C.first;
            B.reversed ^= true;
            head = merge(A_BC.first, B);
            head = merge(head, B_C.second);
        }

        public void push(node node) {
            if (node == null) {
                return;
            }
            if (!node.reversed) {
                return;
            }
            node.reversed = false;
            node tmp = node.right;
            node.right = node.left;
            node.left = tmp;
            updateReverses(node.left, node.right);
        }

        public void updateReverses(node left, node right) {
            if (left != null) {
                left.reversed ^= true;
            }
            if (right != null) {
                right.reversed ^= true;
            }
        }

        public Dekartach() {
            this.head = null;
            this.rand = new Random();
        }

        static class node {
            int x, y, c, l, r;
            int sum;
            boolean reversed;
            node right, left;
            public node(int x, int y) {
                this.x = x;
                this.y = y;
                this.c = 1;
                this.l = x;
                this.r = x;
                this.sum = x;
                this.reversed = false;
            }
            public String toString() {
                return "(" + x + " " + c + " " + l + " " + r + " " + sum + ")";
            }
        }

        static class pair {
            node first, second;
            public pair(node first, node second) {
                this.first = first;
                this.second = second;
            }
        }

        public static int num = 0;

        public void str(node node) {
            push(node);
            if (node.left != null) {
                str(node.left);
            }
            arr.add(num++, node.x);
            if (node.right != null) {
                str(node.right);
            }
        }

        public void addAfter(int k, int x) {
            size++;
            pair splitted = split(head, k);
            head = merge(splitted.first, new node(x, rand.nextInt()));
            head = merge(head, splitted.second);
        }

        public int getC(node node) {
            if (node == null) {
                return 0;
            }
            return node.c;
        }

        public int getSum(node node) {
            if (node == null) {
                return 0;
            } else {
                return node.sum;
            }
        }

        public void updateLeftRight(node node) {
            if (node.left == null) {
                node.l = node.x;
            } else {
                node.l = node.left.l;
            }
            if (node.right == null) {
                node.r = node.x;
            } else {
                node.r = node.right.r;
            }
        }

        public void update (node node) {
            node.c = 1 + getC(node.left) + getC(node.right);
//            updateLeftRight(node);
//            node.sum = node.x + getSum(node.left) + getSum(node.right);
        }

        public pair split(node node, int k) {
            if (node == null) {
                return new pair(null, null);
            }
            push(node);
            int l = getC(node.left);
            int r = getC(node.right);
            if (l >= k) {
                pair p = split(node.left, k);
                node.left = p.second;
                update(node);
                return new pair(p.first, node);
            } else {
                pair p = split(node.right, k - 1 - l);
                node.right = p.first;
                update(node);
                return new pair(node, p.second);
            }
        } // отрубаем к элементов слева (upgraded для H)

        public node merge(node left, node right) {
            if (left == null) {
                return right;
            }
            if (right == null) {
                return left;
            }
            if (left.y > right.y) {
                push(left);
                left.right = merge(left.right, right);
                update(left);
                return left;
            } else {
                push(right);
                right.left = merge(left, right.left);
                update(right);
                return right;
            }
        }
    }
}
