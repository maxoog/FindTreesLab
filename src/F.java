import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;


public class F {
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
    public static long nextLong() {
        return Long.parseLong(next());
    }


    public static void main(String[] args) {
        Dekartach d = new Dekartach();
        long n = nextLong();
        long m = nextLong();

        for (int i = 0; i < n; i++) {
            d.addAfter(i, nextLong());
        }

        for (long i = 0; i < m; i++) {
            String command = next();
            switch (command) {
                case "add":
                    d.addAfter(nextLong(), nextLong());
                    break;
                case "del":
                    d.deleteByIndex(nextLong());
                    break;
                case "s":
                    d.toString(d.head);
            }
        }
        if (d.head == null) {
            System.out.println(0);
            return;
        }

        d.str(d.head);
        long num = d.size;
        System.out.println(num);
        for (int i = 0; i < num; i++) {
            System.out.print(Dekartach.arr.get(i) + " ");
        }
    }

    public static class Dekartach {
        public static ArrayList<Long> arr = new ArrayList<>(0);
        Random rand;
        node head;
        long size;

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

        public void str(node node) {
            if (node.left != null) {
                str(node.left);
            }
            arr.add(arr.size(), node.x);
            if (node.right != null) {
                str(node.right);
            }
        }

        public void addAfter(long k, long x) {
            size++;
            pair splitted = split(head, k);
            head = merge(splitted.first, new node(x, rand.nextInt()));
            head = merge(head, splitted.second);
        }

        public void deleteByIndex(long i) {
            size--;
            pair splitted = split(head, i);
            splitted.first = deleteMax(splitted.first);
            head = merge(splitted.first, splitted.second);
        }

        public node deleteMax(node node) {
            if (node.right == null) {
                return node.left;
            } else {
                node.right = deleteMax(node.right);
                update(node);
                return node;
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

        public pair split(node node, long k) {
            if (node == null) {
                return new pair(null, null);
            }
            long l = getC(node.left);
            long r = getC(node.right);
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
        } // ???????????????? K ?????????????????? ??????????

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
