import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;


public class D {
    private final static BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    public static String next() {
        StringBuilder sb = new StringBuilder();
        try {
            while(true) {
                long n = bf.read();
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
    public static long nextlong()
    {
        return Long.parseLong(next());
    }

    public static void main(String[] args) {
        Dekartach d = new Dekartach();
        long n = nextlong();
        long result = 0;
        boolean op = false;
        for (long i = 0; i < n; i++) {
            String command = next();
            switch (command) {
                case "+":
                    if (op) {
                        op = false;
                        d.insert((nextlong() + result) % 1000000000);
//                        d.toString(d.head);
                        break;
                    }
                    d.insert(nextlong());
                    op = false;
//                    d.toString(d.head);
                    break;
                case "?":
                    op = true;
                    result = d.sum(d.head, nextlong(), nextlong());
                    System.out.println(result);
                    break;
                case "s":
                    d.toString(d.head);
            }
        }
    }

    public static class Dekartach {
        Random rand;
        node head;

        public Dekartach() {
            this.head = null;
            this.rand = new Random();
        }

        static class node {
            long x, y, c, l, r;
            long sum;
            node right, left;
            public node(long x, long y) {
                this.x = x;
                this.y = y;
                this.c = 1;
                this.l = x;
                this.r = x;
                this.sum = x;
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

        public long sum(node node, long l, long r) {
            if (node == null) {
                return 0;
            }
            if (node.l >= l && node.r <= r) {
                return node.sum;
            }
            if (node.l > r || node.r < l) {
                return 0;
            }
            return ((node.x <= r && node.x >= l) ? node.x : 0) + sum(node.left, l, r) + sum(node.right, l, r);
        }

        public void insert(long x) {
            if (!exists(head, x)) {
                head = insertIm(head, new node(x, rand.nextInt()));
            }
        }

        public node insertIm(node A, node node) {
            pair p = split(A, node.x);
            node = merge(p.first, node);
            return merge(node, p.second);
        }

        public boolean exists(node node, long x) {
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

        public long getC(node node) {
            if (node == null) {
                return 0;
            }
            return node.c;
        }

        public long getSum(node node) {
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
            updateLeftRight(node);
            node.sum = node.x + getSum(node.left) + getSum(node.right);
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

        public pair split(node node, long x) {
            if (node == null) {
                return new pair(null, null);
            }
            if (node.x < x) {
                pair splitted = split(node.right, x);
                node.right = splitted.first;
                update(node);
                return new pair(node, splitted.second);
            } else {
                pair splitted = split(node.left, x);
                node.left = splitted.second;
                update(node);
                return new pair(splitted.first, node);
            }
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
                update(left);
                return left;
            } else {
                right.left = merge(left, right.left);
                update(right);
                return right;
            }
        }
    }
}
