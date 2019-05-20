package com.gzoom.leetcode;

import org.omg.PortableInterceptor.INACTIVE;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.test_uniquePathsWithObstacles();
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

    /**
     <<<<<<< HEAD
     *  最小路径和 https://leetcode-cn.com/problems/minimum-path-sum/
     *  这不就是地杰斯特拉算法吗= =
     * */
    public int minPathSum(int[][] grid) {
        int row = grid.length;
        int vol = grid[0].length;
        int dp[][] = new int[row][vol];
        int hang = 0;
        for (int i = 0; i < vol; i++) {
            dp[0][i] = hang + grid[0][i];
            hang = dp[0][i];
        }
        int lie = 0;
        for (int j = 0; j < row; j++) {
            dp[j][0] = lie + grid[j][0];
            lie = dp[j][0];
        }

        for (int i = 1; i < row; i++) {
            for (int j = 1; j < vol; j++) {
                dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
            }
        }

//        for(int i = 0;i<row;i++) {
//            for(int j=0;j<vol;j++) {
//                System.out.print(dp[i][j] + " ");
//            }
//            System.out.println();
//        }

        return dp[row - 1][vol - 1];
    }

    public void test_minPathSum() {
        int[][] grid = new int[][]{{1,3,1},{1,5,1},{4,2,1}};
        minPathSum(grid);

    }


    /**
     * 不同路径 https://leetcode-cn.com/problems/unique-paths/
     *
     * 秒过，都不用怎么改的
     * */

    public int uniquePaths(int m, int n) {
        int row = m;
        int vol = n;
        int dp[][] = new int[row][vol];
        for (int i = 0; i < vol; i++) {
            dp[0][i] = 1;
        }
        for (int j = 0; j < row; j++) {
            dp[j][0] = 1;
        }

        for (int i = 1; i < row; i++) {
            for (int j = 1; j < vol; j++) {
                dp[i][j] = dp[i-1][j] + dp[i][j-1];
            }
        }

//        for(int i = 0;i<row;i++) {
//            for(int j=0;j<vol;j++) {
//                System.out.print(dp[i][j] + " ");
//            }
//            System.out.println();
//        }

        return dp[row - 1][vol - 1];
    }


    public class Interval {
        int start;
        int end;

        Interval() {
            start = 0;
            end = 0;
        }

        Interval(int s, int e) {
            start = s;
            end = e;
        }
    }


    /*
     * 跳跃游戏 https://leetcode-cn.com/problems/jump-game/
     *  给定各个地方的最大距离，看能否跳到最后一个
     *
     *  我在想能往前跳吗？不然最后一个为什么要赋值
     *
     *  想到的法子：从0开始，收集能跳到的地方，作为一个列表，然后深度遍历下去
     *  复杂度最差是n^2
     *  第一版失败，时间超时;尝试缩减，失败
     *
     * */
    public boolean canJump(int[] nums) {
        if (nums.length == 0) {
            return false;
        }
        if (nums.length == 1) {
            return true;
        }
        int target = nums.length - 1;
        List<Integer> list = new ArrayList<>();
        if (nums[0] == 0) {
            return false;
        }
        if (nums[0] >= target) {
            return true;
        }
        for (int i = 1; i <= nums[0]; i++) {
            list.add(i);
        }
        while (!list.isEmpty()) {
            int index = list.get(0);
            if (index > target) {
                list.remove(0);
                break;
            }
            if (index == target) {
                return true;
            }
            for (int i = 1; i <= nums[index]; i++) {
                int val = index + i;
                if (val >= target) {
                    return true;
                }
                if (!list.contains(val)) {
                    list.add(val);
                }
            }
            list.remove(0);
        }
        return false;
    }

    /**
     * 按照我昨天的思路
     *
     * 通过！！！！！！
     * */
    public boolean canJump_v2(int[] nums) {
        int index = 0;
        //从后往前数
        for (int i = nums.length - 1; i >= 0; i--) {
            if (nums[i] >= index) {
                index = 1;
            } else {
                index++;
            }
        }
        if (index == 1) {
            return true;
        }
        return false;
    }

    public void test_canJump() {
        int test[] = new int[]{
                3,2,1,0,4};
        System.out.println(canJump_v2(test));
    }

    /**
     * https://leetcode-cn.com/problems/merge-intervals/
     * 合并区间
     * */
    public List<Interval> merge(List<Interval> intervals) {
        if (intervals.isEmpty() || intervals.size() == 1) {
            return intervals;
        }
        //先排序
        Collections.sort(intervals, new Comparator<Interval>() {
            @Override
            public int compare(Interval o1, Interval o2) {
                if (o1.start < o2.start) {
                    return -1;
                } else if (o1.start > o2.start) {
                    return 1;
                } else if (o1.end < o2.end) {
                    return -1;
                } else if (o1.end > o2.end) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        Interval last = intervals.get(0);

        Iterator<Interval> iterator = intervals.iterator();
        List<Interval> intervalList = new ArrayList<>();
        iterator.next();
        while (iterator.hasNext()) {
            Interval per = iterator.next();
            if (canBindOne(per, last)) {
                last = bindOne(per,last);
                iterator.remove();
            }else {
                intervalList.add(last);
                last = per;
            }
        }
        intervalList.add(last);


        return intervalList;
    }

    private Interval bindOne(Interval per, Interval last) {
        per.start = per.start < last.start ? per.start : last.start;
        per.end = per.end > last.end ? per.end : last.end;
        return per;
    }

    private boolean canBindOne(Interval per, Interval last) {
        //不可合并的情况
        if (per.end < last.start || last.end < per.start) {
            return false;
        }
        return true;
    }

    public void test_merge() {
        Interval a1 = new Interval();
        a1.start = 1;
        a1.end = 3;
        Interval a2 = new Interval();
        a2.start = 2;
        a2.end = 4;
        List<Interval> intervals = new ArrayList<>();
        intervals.add(a1);
        intervals.add(a2);
        intervals = merge(intervals);
        for(Interval interval : intervals) {
            System.out.println("["+interval.start+","+interval.end+"]");
        }
    }

    /**
     * 第k个排列  https://leetcode-cn.com/problems/permutation-sequence/
     *
     * 逻辑有点乱，比如[2,2]，有点麻烦
     * 通过-卡住的点在于index的确定
     * 因为我们数数的时候是1开始，但是datas定位是从0开始
     * 打个比方，1,2,3分别去除以2，答案是0,1,1
     * 但是2的时候还是停留在上一个答案，所以我们需要将被除数减一
     * */
    public String getPermutation(int n, int k) {
        //每一位每一位找，然后维护一个链表，在链表中慢慢取
        StringBuilder stringBuilder = new StringBuilder("");
        List<Integer> datas = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            datas.add(i + 1);
        }
        for (int i = 0; i < n-1; i++) {
            int result = getCircleIndex(n - 1 - i);
            //这个index比较麻烦，因为
            int index = (k-1)/ result;
            if (index < 0) {
                index = 0;
            } else if (index >= datas.size()) {
                index = index % (datas.size() - 1);
            }
            stringBuilder.append(datas.get(index));
            datas.remove(index);
            k = k - (result) * index;
        }
        stringBuilder.append(datas.get(0));

        return stringBuilder.toString();
    }

    public void test_getPermutation() {
        System.out.println(getPermutation(3, 3));
        //[3,3] w:123 t:213
        System.out.println(getPermutation(3, 2));
        //[3,2] w:213 t:132
        System.out.println(getPermutation(2, 2));
        //[2,2] w:12 t:21
        System.out.println(getPermutation(3, 4));
        //[3,4] w:132 t:231
    }

    private Map<Integer, Integer> mJieMap = new HashMap<>();

    private int getCircleIndex(int n) {
        if (mJieMap.containsKey(n)) {
            return mJieMap.get(n);
        }
        int result = 1;
        for (int i = 2; i <= n; i++) {
            result = result * i;
        }
        mJieMap.put(n, result);
        return result;
    }

//
//    /**
//     * 字母异位词分组 https://leetcode-cn.com/problems/group-anagrams/
//     *
//     *
//     * */
//    public List<List<String>> groupAnagrams(String[] strs) {
//
//    }

    /**
     不同路径 II:https://leetcode-cn.com/problems/unique-paths-ii/submissions/
     一个机器人位于一个 m x n 网格的左上角 （起始点在下图中标记为“Start” ）。

     机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角（在下图中标记为“Finish”）。

     现在考虑网格中有障碍物。那么从左上角到右下角将会有多少条不同的路径？
     网格中的障碍物和空位置分别用 1 和 0 来表示。

     说明：m 和 n 的值均不超过 100。

     想使用深度遍历，函数每次返回该节点能够到达终点的分支数目

     出现错误：
     1、终点值不对，不应该是i==length，而是length-1
     2、不是很懂，终点起点一起的时候为0/因为为1
     3、超时了，尝试使用hashMap?成功
     * */


    HashMap<String,Integer> map = new HashMap<>();
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int length = obstacleGrid[0].length;
        int deep = obstacleGrid.length;
        if (deep == 1 && deep == 1 && obstacleGrid[0][0] == 1) {
            return 0;
        }
        if (obstacleGrid[deep - 1][length - 1] == 1) {
            return 0;
        }
        int result = handleRoute(obstacleGrid, deep, length, 0, 0, 0);
        return result;
    }

    private int handleRoute(int[][] obstacleGrid, int length, int deep, int i, int k, int count) {
        if (i == length-1 && k == deep-1) {
            return 1;
        }

        if (obstacleGrid[i][k] == 1) {
            return 0;
        }
        String key = i+","+k;
        if (map.containsKey(key)) {
            return map.get(key);
        }
        int add = 0;
        if (i + 1 < length) {
            add += handleRoute(obstacleGrid, length, deep, i + 1, k, count);
        }
        if (k + 1 < deep) {
            add += handleRoute(obstacleGrid, length, deep, i, k + 1, count);
        }
        map.put(key,add);
        return add;
    }

    public void  test_uniquePathsWithObstacles() {
        int[][] data = new int[][]{{0,0,0},{0,1,0},{0,0,0}};
        System.out.println(uniquePathsWithObstacles(data));
    }

}