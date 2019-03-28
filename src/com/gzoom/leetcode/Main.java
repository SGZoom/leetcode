package com.gzoom.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.test_numDecodings();
    }

    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    /**
     * 反转链表 II https://leetcode-cn.com/problems/reverse-linked-list-ii/
     * 反转从位置 m 到 n 的链表。请使用一趟扫描完成反转。
     *
     * 说明:
     * 1 ≤ m ≤ n ≤ 链表长度。
     *
     * 出现的问题：
     * 1、超过时间限制，不知道为什么:最后没有处理好，死循环了
     * 2、出现的极端情况：列表只有一个、列表从头开始倒序（你就不能用头）、m==n（那就不用处理了）
     * */
    public ListNode reverseBetween(ListNode head, int m, int n) {
        ListNode mHead = head;
        ListNode last = null;
        if (head == null || head.next == null) {
            return head;
        }
        if (m == n) {
            return head;
        }
        int i = 1;
        List<ListNode> handle = new ArrayList<>();
        ListNode listTop = null;
        ListNode oriTop = null;
        ListNode bottomNode = null;

        while (head != null) {
            if (handle.contains(head)) {
                break;
            }
            handle.add(head);
            if (i == m) {
                listTop = head;
                oriTop = last;
                last = head;
                head = head.next;
                listTop.next = null;

            } else if (i == n) {
                ListNode cur = head;
                head = head.next;
                cur.next = last;
                last = head;
                if (oriTop != null) {
                    oriTop.next = cur;
                }
                listTop.next = head;
                bottomNode = cur;
            } else if (i > m && i < n) {
                ListNode cur = head;
                head = head.next;
                cur.next = last;
                last = cur;
            } else {
                last = head;
                head = head.next;
            }
            i++;
        }
        if (m == 1) {
           return bottomNode;
        }
        return mHead;
    }

    public void test_reverseBetween() {
        ListNode node1 = new ListNode(3);
        ListNode node2 = new ListNode(5);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);

        node1.next = node2;
//        node2.next = node3;
//        node3.next = node4;
//        node4.next = node5;
        node1 = reverseBetween(node1,1,2);
        while (node1 != null) {
            System.out.println(node1.val);
            node1 = node1.next;
        }
    }

    /**
     * 解码方法 https://leetcode-cn.com/problems/decode-ways/
     *
     * 给定一个只包含数字的非空字符串，请计算解码方法的总数。
     *
     * 还是切割方式的演变，用迭代吧；总个数是当前解法的下顺
     *
     * 存在0 的情况，0不能单独匹配；
     * 最初的深度遍历超时了
     * */
    public int numDecodings(String s) {
        if (s.isEmpty()) {
            return 0;
        }
        if (s.startsWith("0")) {
            return 0;
        }
        if (s.length() == 1) {
            return 1;
        }

        int result = 1;
        result = result*muplitDecode(s.substring(1));
        if (Integer.valueOf(s.substring(0, 2)) < 27) {
            result += muplitDecode(s.substring(2));
        }
        return result;
    }

    private int muplitDecode(String source) {
        if (source.isEmpty() || (source.length() == 1 && !source.equals("0"))) {
            return 1;
        }
        if (source.startsWith("0")) {
            return 0;
        }
        int result = 1;
        result = result * muplitDecode(source.substring(1));
        if (Integer.valueOf(source.substring(0, 2)) < 27) {
            result += muplitDecode(source.substring(2));
        }
        return result;
    }

    public void test_numDecodings() {
        int val = numDecodings("1787897759966261825913315262377298132516969578441236833255596967132573482281598412163216914566534565");
        System.out.println(val);
    }
}
