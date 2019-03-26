package com.gzoom.leetcode;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.test_reverseBetween();
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
     * 1、超过时间限制，不知道为什么
     * */
    public ListNode reverseBetween(ListNode head, int m, int n) {
        ListNode mHead = head;
        ListNode last = null;
        int i = 1;
        List<ListNode> handle = new ArrayList<>();
        while (head != null) {
            if(handle.contains(head)) {
                break;
            }
            handle.add(head);
            if (i >= m && i <= n) {
                ListNode cur = head;
                head = head.next;
                cur.next = last;
                last = head;
            } else {
                last = head;
                head = head.next;
            }
            i++;
        }
        return mHead;
    }

    public void test_reverseBetween() {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);

        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        reverseBetween(node1,2,4);
    }
}
