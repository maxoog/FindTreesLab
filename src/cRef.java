import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;


public class cRef {
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
            d.head = cRef.Dekartach.merge(d.head, new cRef.Dekartach.node(1, d.rand.nextInt(), 0));
        }
//        d.toString(d.head);
//        d.str(d.head);
        for (int i = 0; i < n; i++) {
            d.insert(nextInt(), i + 1);
            System.out.println();
            d.strIm(d.head);
//            d.toString(d.head);
        }
        d.str(d.head);
        /*if (!d.valid(d.head)) {
            throw new AssertionError("пам пам");
        }*/
        int num = Dekartach.num;
        /*int i = num - 1;
        while (true) {
            if (Dekartach.arr[i] == 0) {
                num--;
                i--;
            } else {
                break;
            }
        }*/
        System.out.println(num);
        for (int i = 0; i < Dekartach.num; i++) {
            System.out.print(Dekartach.arr[i] + " ");
        }
    }

    public static class Dekartach {
        public int size;
        public static int[] arr;
        Random rand;
        node head;
        static int nulls;

        public Dekartach(int size) {
            arr = new int[size * 3];
            this.size = size;
            this.head = null;
            this.rand = new Random();
        }

        public boolean validFlag = false;
        public boolean valid(node node) {
            return validIm(node);
        }

        public boolean validIm(node node) {
            if (node.right == null) {
                if (node.value == 0 && !validFlag) {
                    return false;
                } else {
                    validFlag = true;
                    return true;
                }
            }
            return validIm(node.right);
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
            arr[num++] = node.value;
//            System.out.print(node.value + " ");

            if (node.right != null) {
                str(node.right);
            }
        }

        public void strIm(node node) {
            if (node.left != null) {
                strIm(node.left);
            }
//            System.out.print(node.value + " ");
            System.out.print(node.value + " ");

            if (node.right != null) {
                strIm(node.right);
            }
        }

        static class node {
            int c, y, value;
            node right, left;
            public node(int c, int y, int value) {
                this.c = c;
                this.y = y;
                this.value = value;
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
            public static boolean flag = false;
            public static node destroy(node node) {
                flag = false;
                return destroyIm(node);
            }
            public static node destroyIm(node node) {
                if (node == null) {
                    return null;
                }
                node.left = destroy(node.left);
                if (node.value == 0) {
                    if (!flag) {
//                        System.out.println("destroyed " + node);
                        flag = true;
                        return Dekartach.merge(node.left, node.right);
                    }
                }
                if (!flag) {
                    node.right = destroy(node.right);
                }
                node.c = getC(node.left) + getC(node.right) + 1;
                return node;
            }
            public static int getC(node node) {
                if (node == null) {
                    return 0;
                } else {
                    return node.c;
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
//                System.out.println(get(head, place));
                get(head, place).value = value;
                return;
            }
            node our = new node(1, rand.nextInt(), value);
            pair splitted = split(head, place - 1);
//            System.out.println("________ first");
            /*if (splitted.first != null) {
                splitted.first.toStrin();
                System.out.println("_______");
            }*/
//            System.out.println("________ second");
            /*if (splitted.second != null){
                splitted.second.toStrin();
                System.out.println("________");
            }*/
            splitted.second = node.destroy(splitted.second);
//            splitted.second.toStrin();
//            System.out.println("place " + place);
            head = merge(splitted.first, our);
            head = merge(head, splitted.second);
        }

        public pair split(node node, int k) {
            if (node == null || k == 0) {
                return new pair(null, node);
            }
            if (node.left != null && node.left.c >= k) {
                pair p = split(node.left, k);
                node.left = p.second;
                update(node);
                return new pair(p.first, node);
            } else {
                int n = 0;
                if (node.left == null) {
                } else {
                    n = node.left.c;
                }
                pair p = split(node.right, k - n - 1);
                node.right = p.first;
                update(node);
                return new pair(node, p.second);
            }
        }

        public static void update(node node) {
            node.c = 1;
            if (node.left != null) {
                node.c += node.left.c;
            }
            if (node.right != null) {
                node.c += node.right.c;
            }
        }

        public void toString(node node) {
            if (node == null) {
                System.out.println("null");
                return;
            }
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

        public static node merge(node left, node right) {
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

//        public void toString(A.Dekartach.node node) {
//            System.out.println(node);
//            if (node.left != null) {
//                toString(node.left);
//            } else {
//                System.out.println("null");
//            }
//            if (node.right != null) {
//                toString(node.right);
//            } else {
//                System.out.println("null");
//            }
//        }
    }
}
