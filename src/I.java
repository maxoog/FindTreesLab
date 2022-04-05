import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;


public class I {
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
    public static int nextInt() {
        return Integer.parseInt(next());
    }


    public static void main(String[] args) {
        Dekartach d = new Dekartach();
        int n = nextInt();
        int m = nextInt();
        int q = nextInt();
        for (int i = 0; i <= n; i++) {
            d.addToArray(i);
        }
        for (int j = 0; j < m; j++) {
            d.insert(nextInt(), nextInt());
        }
        for (int k = 0; k < q; k++) {
            String command = next();
            switch (command) {
                case "-"  :
                    d.delete(nextInt(), nextInt());
                    break;
                case "+":
                    d.insert(nextInt(), nextInt());
                    break;
                case "?":
                    System.out.println(d.find(nextInt(), nextInt()));
                    break;
            }
        }
    }

    public static class Dekartach {
        public static ArrayList<node> arr = new ArrayList<>(0);
        static Random rand;
        node head;

        public Dekartach() {
            this.head = null;
            rand = new Random();
        }

        public void addToArray(int x) {
            arr.add(new node(x));
        }

        public void delete(int left, int right) {
            node second = arr.get(right);
            node first = arr.get(left);
            node rootA = findRoot(first);
            pair r_endings = down(rootA);
            if (rootA.loop) {
                rootA.loop = false;
                if ((r_endings.second == first && r_endings.first == second) || (r_endings.first == first && r_endings.second == second)) {
                    return;
                }
                int left_size = size(first);
                int right_size = size(second);
                if (left_size == right_size - 1) {
                    parentfirst(rootA, left, right, right_size);
                } else {
                    parentsecond(rootA, left, right, left_size);
                }

                findRoot(arr.get(left)).loop = findRoot(arr.get(right)).loop = false;
                return;
            }
            int right_size = size(second);
            int left_size = size(first);
            pair p;
            if (left_size != right_size - 1) {
                p = split(rootA, left_size);
            } else {
                p = split(rootA, right_size);
            }
            p.first.prev = null;
            p.second.prev = null;
        }

        public int find(int left, int right) {
            if (left == right) {
                return 0;
            }
            node second = arr.get(right);
            node first = arr.get(left);
            int left_size = size(first);
            int right_size = size(second);
            node rootB = findRoot(second);
            node rootA = findRoot(first);

            if (rootA != rootB) {
                return -1;
            }
            node tool = rootA;

            if (!tool.loop) {
                return Math.abs(right_size - left_size) - 1;
            }

            tool = right(tool);
            int max = size(tool);
            if (right_size <= left_size) {
                return min(left_size - right_size - 1, max - left_size + right_size);
            } else {
                return min(right_size - left_size - 1, max - right_size + left_size);
            }
        }

        public int min(int a, int b) {
            return Math.min(a, b);
        }

        public node right(node node) {
            if (node.right == null) {
                return node;
            }
            return right(node.right);
        }

        public node left(node node) {
            if (node.left == null) {
                return node;
            }
            return left(node.left);
        }

        public void parentfirst(node node, int left, int right, int k) {
            pair p = split(node, k);
            p.first.prev = p.second.prev = null;
            node A = findRoot(arr.get(left));
            node B = findRoot(arr.get(right));
            A = Return(A);
            B = Return(B);
            merge(A, B);
        }

        public void parentsecond(node node, int left, int right, int k) {
            pair p = split(node, k);
            p.first.prev = null;
            p.second.prev = null;
            node A = findRoot(arr.get(left));
            node B = findRoot(arr.get(right));
            A = Return(A);
            B = Return(B);
            merge(B, A);
        }
        
        public int size(node node) {
            int c = getC(node.left);
            return sizeImpl(node, c);
        }
        
        public int sizeImpl(node node, int c) {
            if (node.prev == null) {
                return c;
            }
            if (node == node.prev.right) {
                c += getC(node.prev.left) + 1;
            }
            return sizeImpl(node.prev, c);
        }

        public void insert(int one, int second) {
            node rootA = findRoot(arr.get(one));
            node rootB = findRoot(arr.get(second));
            node A = arr.get(one);
            node B = arr.get(second);
            if (rootA == rootB) {
                pair p = down(rootA);
                if ((p.first == B && p.second == A) || (p.first == A && p.second == B)) {
                    rootA.loop = true;
                }
                return;
            }
            pair left = down(rootA);
            pair right = down(rootB);
            if (left.second == A && right.first == B) {
                merge(rootA, rootB);
            } else if (left.first == A && right.second == B) {
                merge(rootB, rootA);
            } else if (left.first == A && right.first == B) {
                rootA = Return(rootA);
                merge(rootA, rootB);
            } else if (left.second == A && right.second == B) {
                rootB = Return(rootB);
                merge(rootA, rootB);
            }
//            merge(rootA, rootB); // todo: косячокс
        }
        
        public node Return(node node) {
            if (node == null) {
                return null;
            }
            node t = node.right;
            node.right = node.left;
            node.left = t;
            node.left = Return(node.left);
            node.right = Return(node.right);
            return node;
        }
        
        public pair down(node node) {
            node two, one;
            one = two = node;
            one = left(one);
            two = right(two);
            return new pair(one, two);
        }

        public node findRoot(node node) {
            if (node.prev == null) {
                return node;
            } else {
                return findRoot(node.prev);
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
                node t = merge(left.right, right);
                left.right = t;
                t.prev = left;
                update(left);
                return left;
            }
            node temp = merge(left, right.left);
            right.left = temp;
            temp.prev = right;
            update(right);
            return right;
        }

        public pair split(node node, int k) {
            if (node == null) {
                return new pair(null, null);
            }
            int l = getC(node.left);
            int r = getC(node.right);
            if (l >= k) {
                pair p = split(node.left, k);
                node.left = p.second;
                if (p.second != null) {
                    p.second.prev = node;
                }
                update(node);
                return new pair(p.first, node);
            } else {
                pair p = split(node.right, k - 1 - l);
                node.right = p.first;
                if (p.first != null) {
                    p.first.prev = node;
                }
                update(node);
                return new pair(node, p.second);
            }
        } // обычный split на отрубание k элементов

        public static int getC(node node) {
            if (node == null) {
                return 0;
            } else {
                return node.c;
            }
        }

        public static void update(node node) {
            if (node == null) {
                return;
            }
            node.c = 1 + getC(node.right) + getC(node.left);
        }

        static class node {
            int x, y, c;
            boolean loop;
            node right, left, prev;
            public node(int x) {
                this.x = x;
                this.y = rand.nextInt();
                this.c = 1;
                this.loop = false;
            }
        }

        static class pair {
            node first, second;
            public pair(node first, node second) {
                this.first = first;
                this.second = second;
            }
        }

    }
}

