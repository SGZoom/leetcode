package com.gzoom.leetcode;
/**
 * 用于做二叉树相关的题，因为有些名字和链表冲突了
 * */
public class AbTree {



    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    /**
     二叉搜索树中第K小的元素 https://leetcode-cn.com/problems/kth-smallest-element-in-a-bst/
     给定一个二叉搜索树，编写一个函数 kthSmallest 来查找其中第 k 个最小的元素。

     我的想法是，到达一个节点的时候进行计算，他的大小是左子树+1

     过了嘻嘻
     * */
    int cur = 0;
    int target = 0;

    public int kthSmallest(TreeNode root, int k) {
        if (root == null) {
            return target;
        }
        kthSmallest(root.left, k);
        if (cur >= k) {
            return target;
        }
        cur++;
        if (cur == k) {
            target = root.val;
            return target;
        }
        return kthSmallest(root.right, k);

    }
}
