package com.gzoom.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.test_subsetsWithDup();
    }

    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    /**
     * 反转链表 II https://leetcode-cn.com/problems/reverse-linked-list-ii/ 反转从位置 m 到 n
     * 的链表。请使用一趟扫描完成反转。
     *
     * 说明: 1 ≤ m ≤ n ≤ 链表长度。
     *
     * 出现的问题： 1、超过时间限制，不知道为什么:最后没有处理好，死循环了
     * 2、出现的极端情况：列表只有一个、列表从头开始倒序（你就不能用头）、m==n（那就不用处理了）
     */
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
        // node2.next = node3;
        // node3.next = node4;
        // node4.next = node5;
        node1 = reverseBetween(node1, 1, 2);
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
     * 存在0 的情况，0不能单独匹配； 最初的深度遍历超时了
     */
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
        result = result * muplitDecode(s.substring(1));
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
    //1787897759966261825913315262377298132516969578441236833255596967132573482281598412163216914566534565
    //10
    public void test_numDecodings() {
        int val = numDecodings_v2(
                "12");
        System.out.println(val);
    }

    /**
     * 应该使用dp，原理就是dp[i] = dp[i-1] + dp[i-2]
     * dp[i-1] 可取 条件是 s[i-1] != 0 这里i-1具有迷惑性，因为我们的dp是超出一位的，所以是指本来这一位
     *      本来这一位不为0那么可以单独切分，不影响前面的切分，所以可以继承上一个dp
     * dp[i-2] 可取 条件是 上一位加上遍历这一位可以单独成二位数，符合要求（0<x<27）
     *
     * 我发现自己在边界值的取值上很容易迷糊
     *
     */
    public int numDecodings_v2(String s) {
        if (s.isEmpty()) {
            return 0;
        }
        if (s.startsWith("0")) {
            return 0;
        }
        if (s.length() == 1) {
            return 1;
        }

        int dp[] = new int[s.length() + 1];
        char sc[] = s.toCharArray();
        dp[0] = 1;
        int length = s.length();
        for (int i = 1; i <= length; i++) {
            dp[i] = (sc[i - 1] != '0') ? dp[i - 1] : 0;
            if (i > 1 && (sc[i - 2] == '1' || sc[i - 2] == '2' && sc[i - 1] <= '6')) {
                dp[i] += dp[i - 2];

            }
        }
        return dp[length];
    }

    public int numDecodings_v3(String s) {
        int cnt = 0;
        if(s.length() == 0 || (s.length() == 1 && s.charAt(0) == '0')) return 0;
        if(s.length() == 1) return 1;
//        vector<int> dp(s.size() + 1, 0);
        int dp[] = new int[s.length() +1];
        dp[0] = 1;
        for(int i = 0; i < s.length(); ++i){
            dp[i+1] = s.charAt(i) == '0' ? 0 : dp[i];
            if(i > 0 && (s.charAt(i-1) == '1' || (s.charAt(i-1) == '2' && s.charAt(i) <= '6'))){
                dp[i+1] += dp[i-1];
            }
        }
        return dp[s.length()];
    }

    /**
     * 子集 II https://leetcode-cn.com/problems/subsets-ii/
     * 一开始的思路就错了，子集不需要连续，所以不是通过链表
     *  我的想法是通过动态规划，f[i] = f[i-1] + f[i-1] add i
     *  f[i]表示i个数字的时候的结果集
     * */
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        if (nums.length == 0) {
            return result;
        }
        result.add(new ArrayList<>());
        if (nums.length == 1) {
            List<Integer> add = new ArrayList<>();
            add.add(nums[0]);
            result.add(add);
            return result;
        }
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            int value = nums[i];
            List<Integer> list = new ArrayList<>();
            list.add(nums[i]);
            if (!values.contains(nums[i])) {
                values.add(value);
                List<Integer> add = new ArrayList<>(list);
                result.add(add);
            }
            for(int j = i+1;j<nums.length;j++) {
                value = value *10 + nums[j];
                if(!values.contains(value)) {
                    values.add(value);
                    list.add(nums[j]);
                    List<Integer> add = new ArrayList<>(list);
                    result.add(add);
                }

            }
        }
        return result;
    }
    /**
     * 一开始用普通的ArrayList会报错：java.util.ConcurrentModificationException，在遍历的时候
     * 在遍历的时候不能操作List,所以应该单独陵出来
     *
     * 还是出现重复的情况，需要想方法去除
     *
     * 最终通过，匹配的时候还是太麻烦了
     * */
    public List<List<Integer>> subsetsWithDup_v2(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums.length == 0) {
            return result;
        }
        Arrays.sort(nums);
        List<String> values = new ArrayList<>();
        result.add(new ArrayList<>());
        for(int i = 0;i<nums.length;i++) {
            List<List<Integer>> newList = new ArrayList<>();
            boolean quitSame = false;
            if (i >= 1 && nums[i] == nums[i - 1]) {
                quitSame = true;
            }
            for(List<Integer> list : result) {
                //如果只有0个和只有1个，那么就没必要加了
                if ((list.isEmpty() || (list.size() == 1 && list.get(0) != nums[i])) && quitSame) {
                    continue;
                }
                List<Integer> newOne = new ArrayList<>(list);
                newOne.add(nums[i]);
                if (!caculateListIn(newOne, values)) {
                    newList.add(newOne);
                }
            }
            result.addAll(newList);
        }
        return result;
    }
    /**
     * 妈的有0还有负数，采用字符串匹配试试？
     * */
    private boolean caculateListIn(List<Integer> list, List<String> contain) {
        StringBuilder value = new StringBuilder();
        //这里是为了计算出0
        for (int a : list) {
            value.append(a);
        }
        String result = value.toString();
        if (contain.contains(result)) {
            return true;
        }
        contain.add(result);
        return false;
    }

    public void test_subsetsWithDup() {
        System.out.println(subsetsWithDup_v2(new int[]{-1,1,2}));
    }
}