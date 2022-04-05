import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.ArrayList;


public class C {
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
        int n = nextInt();
        int m = nextInt();
        Dekartach d = new Dekartach(n + m);

        for (int i = 0; i < m; i++) {
            d.head = C.Dekartach.merge(d.head, new C.Dekartach.node(1, d.rand.nextInt(), 0, 1));
        }

        for (int i = 0; i < n; i++) {
            d.insert(nextInt(), i + 1);
        }

        d.str(d.head);
        int num = Dekartach.num;
        System.out.println(num);
        int p = num - 1;
        if (num != Dekartach.arr.size()) {
            throw new RuntimeException("слава что ты сделал");
        }
        while (true) {
            if (Dekartach.arr.get(p) == 0) {
                num--;
                p--;
            } else {
                break;
            }
        }
        for (int i = 0; i < Dekartach.num; i++) {
            System.out.print(Dekartach.arr.get(i) + " ");
        }
    }

    public static class Dekartach {
        public int size;
        public static ArrayList<Integer> arr = new ArrayList<>(0);
        Random rand;
        node head;

        public Dekartach(int size) {
            this.size = size;
            this.head = null;
            this.rand = new Random();
        }

        public node get(node node, int k) {
            if (node.left == null) {
                if (k == 1) {
                    return node;
                } else {
                    return get(node.right, k - 1);
                }
            }
            if (k - node.left.c == 1) {
                return node;
            }
            if (node.left.c >= k) {
                return get(node.left, k);
            } else {
                return get(node.right, k - node.left.c - 1);
            }
        }

        public static int num = 0;

        public void str(node node) {
            if (node.left != null) {
                str(node.left);
            }
//            System.out.print(node.value + " ");
            arr.add(num++, node.value);
//            System.out.print(node.value + " ");

            if (node.right != null) {
                str(node.right);
            }
        }

        static class node {
            int c, y, value, nulls;
            node right, left;
            public node(int c, int y, int value, int nulls) {
                this.c = c;
                this.y = y;
                this.value = value;
                this.nulls = nulls;
            }
            public String toString() {
                return "(" + c + " " + y + " " + value + ")";
            }
            public void toStrin() {
                System.out.println(this);
                if (this.left != null) {
                    this.left.toStrin();
                } else {
                    System.out.println("null");
                }
                if (this.right != null) {
                    this.right.toStrin();
                } else {
                    System.out.println("null");
                }
            }
            public static node destroy(node node) {
                return destroyIm(node);
            }
            public static node destroyIm(node node) {
                if (node == null) {
                    return null;
                }
                if (getNulls(node.left) != 0) {
                    node.left = destroyIm(node.left);
                } else if (node.value == 0) {
                    return merge(node.left, node.right);
                } else if (getNulls(node.right) != 0) {
                    node.right = destroyIm(node.right);
                }
                update(node);
                return node;
            }
            public static int getC(node node) {
                if (node == null) {
                    return 0;
                } else {
                    return node.c;
                }
            }

            public static int getNulls(node node) {
                if (node == null) {
                    return 0;
                } else {
                    return node.nulls;
                }
            }
        }

        static class pair {
            node first, second;
            public pair(node first, node second) {
                this.first = first;
                this.second = second;
            }
        }

        public void insert(int place, int value) {
            if (get(head, place).value == 0) {
                get(head, place).value = value;
                return;
            }
            node our = new node(1, rand.nextInt(), value, 0);
            pair splitted = split(head, place - 1);

            splitted.second = node.destroy(splitted.second);

            head = merge(splitted.first, our);
            head = merge(head, splitted.second);
        }

        public pair split(node node, int k) {
            if (node == null) {
                return new pair(null, null);
            }
            int l = Dekartach.node.getC(node.left);
            int r = Dekartach.node.getC(node.right);
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
        }

        public static void update(node node) {
            if (node == null) {
                return;
            }
            node.c = 1 + Dekartach.node.getC(node.left) + Dekartach.node.getC(node.right);
            node.nulls = 0;
            if (node.value == 0) {
                node.nulls++;
            }
            node.nulls += Dekartach.node.getNulls(node.left) + Dekartach.node.getNulls(node.right);
        }

        public static node merge(node left, node right) {
            if (left == null) {
                update(right);
                return right;
            }
            if (right == null) {
                update(left);
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
