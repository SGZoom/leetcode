package com.gzoom.leetcode;

import org.omg.PortableInterceptor.INACTIVE;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
//        int result = main.lengthOfLongestSubstring("tmmzuxt");
//        System.out.println(result);
        main.test_rotate();
//        System.out.println(2%5);
//        System.out.println(13%5);
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

    /**
     给定一个包含红色、白色和蓝色，一共 n 个元素的数组，原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。

     此题中，我们使用整数 0、 1 和 2 分别表示红色、白色和蓝色。

     注意:
     不能使用代码库中的排序函数来解决这道题。
     * */
    public void sortColors(int[] nums) {
        List<Integer> indexOfOne = new ArrayList<>();
        List<Integer> indexOfTwo = new ArrayList<>();
        List<Integer> indexOfZero = new ArrayList<>();
        int length = nums.length;
        for (int i = 0; i < length; i++) {
            if (nums[i] == 2) {
                indexOfTwo.add(i);
            }else if(nums[i] == 1) {
                //看一下前面有没有2
                if(!indexOfTwo.isEmpty() && indexOfTwo.get(0) < i) {
                    int tmpIndex = indexOfTwo.get(0);
                    nums[tmpIndex] = 1;
                    nums[i] = 2;
                    indexOfTwo.remove(0);
                    indexOfTwo.add(i);
                    indexOfOne.add(tmpIndex);
                } else {
                    indexOfOne.add(i);
                }
            } else {
                if(!indexOfOne.isEmpty() && indexOfOne.get(0) < i) {
                    int tmpIndex = indexOfOne.get(0);
                    nums[tmpIndex] = 0;
                    nums[i] = 1;
                    indexOfOne.remove(0);
                    indexOfOne.add(tmpIndex);
                }else if(!indexOfTwo.isEmpty() && indexOfTwo.get(0) < i) {
                    int tmpIndex = indexOfTwo.get(0);
                    nums[tmpIndex] = 0;
                    nums[i] = 2;
                    indexOfTwo.remove(0);
                    indexOfTwo.add(i);
                }
            }
        }
    }

    /**
     * 这是最简单的一个思路，还有一种，评论区指出：遇到0移动到头部，遇到2移到尾部
     *
     * 但是考虑到我们这是数组，可能移位反而麻烦
     * */
    public void sortColors2(int[] nums) {
        int numZero = 0;
        int numOne = 0;
        int numTwo = 0;
        int length = nums.length;
        for (int i = 0; i < length; i++) {
            if (nums[i] == 2) {
                numTwo ++;
            }else if(nums[i] == 1) {
                //看一下前面有没有2
                numOne ++;
            } else {
                numZero ++;
            }
        }
        int i = 0;
        for(i = 0;i<numZero;i++) {
            nums[i] = 0;
        }
        for(;i<numOne;i++) {
            nums[i] = 1;
        }
        for(;i<numTwo;i++) {
            nums[i] = 2;
        }
    }

    /**
     组合:https://leetcode-cn.com/problems/combinations/
     给定两个整数 n 和 k，返回 1 ... n 中所有可能的 k 个数的组合。

     熟悉的递归
     这里有个小坑，组合应该排重，[1,2]和[2,1]应该相同
     * */
    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        if (n < k || k == 0) {
            return result;
        }
        int[] flag = new int[n];
        combineEach(n, k, 0, flag, 0, new ArrayList<Integer>(), result);
        return result;
    }

    private void combineEach(int n, int k, int count, int[] flag, int index, List<Integer> list, List<List<Integer>> result) {
        if (count == k) {
            result.add(new ArrayList<>(list));
            return;
        }
        for (int i = index; i < n; i++) {
            if (flag[i] != 1) {
                flag[i] = 1;
                List<Integer> listNew = new ArrayList<>(list);
                listNew.add(i + 1);
                combineEach(n, k, count + 1, flag, i, listNew, result);
                flag[i] = 0;
            }
        }
    }

    /**
     子集:https://leetcode-cn.com/problems/subsets/

     给定一组不含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）。

     说明：解集不能包含重复的子集
     还是迭代吧，没有想到特别好的，这个比较方便的地方在于不含重复元素

     还是按照元素个数划分

     过了，效率还行
     start是没用的，一开始造成了干扰
     * */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        int length = nums.length;
        for (int i = 1; i <= length; i++) {
            subsetsPer(nums, 0, length, 0, result, new ArrayList<>(), i, 0);
        }
        return result;
    }

    private void subsetsPer(int[] nums, int start, int end, int index, List<List<Integer>> result, List<Integer> list, int count, int currentCount) {
        if (currentCount == count) {
            List<Integer> add = new ArrayList<Integer>(list);
//            add.add(nums[index]);
            result.add(add);
            return;
        }
        //已经不可能完成了
        if (end - index < (count - currentCount)) {
            return;
        }
        for (int i = index; i < end; i++) {
            List<Integer> each = new ArrayList<>(list);
            each.add(nums[i]);
            subsetsPer(nums, start, end, i + 1, result, each, count, currentCount + 1);
        }
    }

    /**
     单词搜索：https://leetcode-cn.com/problems/word-search/
     给定一个二维网格和一个单词，找出该单词是否存在于网格中。

     单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。同一个单元格内的字母不允许被重复使用。

     看到这个题，我就想到动态规划
     我想到的方法是从[0,0]往[x,y]遍历，每次找到起点都作为一个开始
     然后开始遍历周边，看能不能走到结尾

     1、超时了，递归超时
     2、有一处文字游戏：同一个单元格内的字母不允许被重复使用，但是可以被间接使用，也就是跳转一次然后就变质
     String key = i + " " + k + " " + indexOfString;
     if (helpMap.containsKey(key)) {
     return helpMap.get(key);
     }
     这个地方导致的，因为我是根据当前第几步、在哪里来保存解法的，这样不保险，因为步数一样走法可能不一样

     好奇怪啊，同样的代码，一开始过不了因为超时，后面就过了？？

     * */
    HashMap<String,Boolean> helpMap = new HashMap<>();
    public boolean exist(char[][] board, String word) {
        int deep = board.length;
        int width = board[0].length;
        if (deep == 0 && width == 0) {
            return false;
        }
        if (word.length() > width * deep) {
            return false;
        }
        for (int i = 0; i < deep; i++) {
            for (int k = 0; k < width; k++) {
                if (board[i][k] == word.charAt(0)) {
                    board[i][k] = '1';
                    boolean result = findStringInMap(i, k, deep, width, word, 1, board);
                    if (result) {
                        return true;
                    }
                    board[i][k]= word.charAt(0);
                }
            }
        }
        return false;
    }

    private boolean findStringInMap(int i, int k, int deep, int width, String word, int indexOfString, char[][] board) {
        if (indexOfString >= word.length()) {
            return true;
        }
        String key = i + " " + k + " " + indexOfString;
//        if (helpMap.containsKey(key)) {
//            return helpMap.get(key);
//        }
        //开始在四周寻找能够够到的
        char target = word.charAt(indexOfString);
        boolean result = false;
        if (i - 1 > -1 && board[i - 1][k] == target) {
            board[i-1][k] = '1';
            result = result || findStringInMap(i - 1, k, deep, width, word, indexOfString + 1, board);
            board[i-1][k] = target;
        }
        if (i + 1 < deep && board[i + 1][k] == target) {
            board[i+1][k] = '1';
            result = result || findStringInMap(i + 1, k, deep, width, word, indexOfString + 1, board);
            board[i+1][k] = target;
        }
        if (k - 1 > -1 && board[i][k - 1] == target ) {
            board[i][k-1] = '1';
            result = result || findStringInMap(i, k - 1, deep, width, word, indexOfString + 1, board);
            board[i][k-1] = target;
        }
        if (k + 1 < width && board[i][k + 1] == target ) {
            board[i][k+1] = '1';
            result = result || findStringInMap(i, k + 1, deep, width, word, indexOfString + 1, board);
            board[i][k+1] = target;
        }
//        helpMap.put(key, result);
        return result;
    }

    public void test_exist() {
        char[][] data = new char[][]{{'A','B','C','E'},{'S','F','E','S'},{'A','D','E','E'}};
        System.out.println(exist(data,"ABCESEEEFS"));
    }

    /**
     * 忘了这是哪道题了，找数组中第k大的数字，排序应该要自己写的
     * */
    public int findKthLargest(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length  - k];
    }

    /**
     删除排序数组中的重复项 II:https://leetcode-cn.com/problems/remove-duplicates-from-sorted-array-ii/
     给定一个排序数组，你需要在原地删除重复出现的元素，使得每个元素最多出现两次，返回移除后数组的新长度。

     不要使用额外的数组空间，你必须在原地修改输入数组并在使用 O(1) 额外空间的条件下完成。

     感觉亦或是一个方法，但是不知道如何具体操作
     还是试试排序？
     打扰了，已经排序了

     移位的时候注意，可能要一次性多移几位，不然不好遍历
     * */
    public int removeDuplicates(int[] nums) {
        if (nums == null || nums.length <= 0) {
            return 0;
        }
        int remove = 0;
        int count = 1;
        for(int i = 0;i<nums.length-1;i++) {
            if (nums[i] == nums[i+1]) {
                if (count < 2) {
                    count++;
                } else {
                    //到这里的时候要删掉后面所有跟这个相同的数字，然后返回删除的个数
                    remove += putNumsFrom(i+1, nums);
                    count = 1;
                }
            } else {
                count = 1;
            }
        }
        return nums.length - remove;
    }

    /**移动的时候要找到下一个不相同的数字
     * @param from 开始移动的位置
     *
     * */
    private int putNumsFrom(int from, int[] nums) {
        int index = -1;
        for (int i = from + 1; i < nums.length - 1; i++) {
            if (nums[i] != nums[from]) {
                index = i;
                break;
            }
        }
        //如果后面的数都跟from相同，那就没有移动的必要，直接放回删减长度即可
        if (index == -1) {
            return nums.length - from;
        }
        int remove = index - from;
        for (int i = from; i < nums.length - remove; i++) {
            nums[i] = nums[i + remove];
        }
        return remove;
    }

    public void test_removeDuplicates() {
//        int[] data = new int[]{1, 1, 1, 2, 2, 3};
        int[] data = new int[]{-50,-50,-49,-48,-47,-47,-47,-46,-45,-43,-42,-41,-40,-40,-40,-40,-40,-40,-39,-38,-38,-38,-38,-37,-36,-35,-34,-34,-34,-33,-32,-31,-30,-28,-27,-26,-26,-26,-25,-25,-24,-24,-24,-22,-22,-21,-21,-21,-21,-21,-20,-19,-18,-18,-18,-17,-17,-17,-17,-17,-16,-16,-15,-14,-14,-14,-13,-13,-12,-12,-10,-10,-9,-8,-8,-7,-7,-6,-5,-4,-4,-4,-3,-1,1,2,2,3,4,5,6,6,7,8,8,9,9,10,10,10,11,11,12,12,13,13,13,14,14,14,15,16,17,17,18,20,21,22,22,22,23,23,25,26,28,29,29,29,30,31,31,32,33,34,34,34,36,36,37,37,38,38,38,39,40,40,40,41,42,42,43,43,44,44,45,45,45,46,47,47,47,47,48,49,49,49,50};
        int result = removeDuplicates(data);
        for (int i = 0; i < result; i++) {
            System.out.print(data[i] + "  ");
        }
    }

    /**
     卧槽被一个超简单的代码打败了
     * */
    public int removeDuplicates_true(int[] nums) {
        int i = 0;
        for (int n : nums) {
            //前两个正确排列
            if (i < 2 || n > nums[i - 2]) {
                nums[i++] = n;
            }
        }
        return i;
    }

    /**
     搜索旋转排序数组 II :https://leetcode-cn.com/problems/search-in-rotated-sorted-array-ii/
     假设按照升序排序的数组在预先未知的某个点上进行了旋转。

     ( 例如，数组 [0,0,1,2,2,5,6] 可能变为 [2,5,6,0,0,1,2] )。

     编写一个函数来判断给定的目标值是否存在于数组中。若存在返回 true，否则返回 false。

    好像没有复杂度的要求？

     怎么写都能过的，网上都用的二分查找

     * */
    public boolean search(int[] nums, int target) {
        int length = nums.length;
        for (int i = 0; i < length; i++) {
            if (target == nums[i]) {
                return true;
            }
            if (i > 0 && i < length - 1) {
                int pre = nums[i-1];
                int next = nums[i+1];
                //说明到分界线了
                if (pre > next) {
                    if (target > nums[i - 1] || target < nums[i+1]) {
                       return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     删除排序链表中的重复元素 II:https://leetcode-cn.com/problems/remove-duplicates-from-sorted-list-ii/
     给定一个排序链表，删除所有含有重复数字的节点，只保留原始链表中 没有重复出现 的数字。

     错误：题意理解错了，所有相同的都要删掉，不能保留一个；这才稍稍有点难度嘛
     空指针[1,1]：在循环的时候没有对node判空
     错误[1,1,1,2,3]：没有针对根节点为相同的情况修改
     错误[1,1,2]:没有赋值最后一个给下一个
     这道题细节超级多，难怪提交率低
     * */
    public ListNode deleteDuplicates(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        int tmp = head.val;
        ListNode node = head;
        ListNode mHead = null;
        ListNode pre = null;
        while (node != null && node.next != null) {
            if (node.next.val != tmp) {
                //确定了node不会被删除
                pre = node;
                if (mHead == null) {
                    mHead = pre;
                }
            }
            node = node.next;
            while (node != null && node.val == tmp) {
                //删除掉中间的节点
                node = node.next;
            }
            if (pre != null) {
                pre.next = node;
            }
            if (node != null) {
                tmp = node.val;
            }
        }
        if (node != null && node.next == null && mHead == null) {
            mHead = node;
        }
        return mHead;
    }

    /**
     分隔链表:https://leetcode-cn.com/problems/partition-list/
     给定一个链表和一个特定值 x，对链表进行分隔，使得所有小于 x 的节点都在大于或等于 x 的节点之前。

     你应当保留两个分区中每个节点的初始相对位置。
     输入: head = 1->4->3->2->5->2, x = 3
     输出: 1->2->2->4->3->5

     这个相对位置就比较迷了
     我的想法是遍历，比他小的放一条，大的放一条

     错误：
     1、死循环：需要将最后一个置0
     2、顶部判空
     * */
    public ListNode partition(ListNode head, int x) {
        if (head == null) {
            return head;
        }
        Queue<ListNode> queueSmall = new LinkedList<>();
        Queue<ListNode> queueBig = new LinkedList<>();
        while (head != null) {
            if (head.val < x) {
                queueSmall.add(head);
            } else {
                queueBig.add(head);
            }
            head = head.next;
        }
        ListNode mHead = null;
        ListNode cur = null;
        if (queueSmall.size() > 0) {
            mHead = ((LinkedList<ListNode>) queueSmall).get(0);
            cur = mHead;
        }
        for (ListNode node : queueSmall) {
            cur.next = node;
            cur = cur.next;
        }

        if (mHead == null) {
            mHead = ((LinkedList<ListNode>) queueBig).get(0);
            cur = mHead;
        }
        for (ListNode node : queueBig) {
            cur.next = node;
            cur = cur.next;
        }
        cur.next = null;
        return mHead;
    }


    /**
     * 四数之和:https://leetcode-cn.com/problems/4sum/
     * 给定一个包含 n 个整数的数组 nums 和一个目标值 target，判断 nums 中是否存在四个元素 a，b，c 和 d ，使得 a + b + c + d 的值与 target 相等？找出所有满足条件且不重复的四元组。

    错误：1、没有区分开相同的数字
     2、[1,-2,-5,-4,-3,3,3,5] -11 负数的情况下，我的一个操作：当前和比target大就不继续往下走了；这个不行
     3、超时
     */
    List<FourList> containList = new ArrayList<>();
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums.length < 4) {
            return result;
        }
        Arrays.sort(nums);
        boolean[] use = new boolean[nums.length];
        handleFourList(nums, target, use, 0, result, new ArrayList<Integer>(), 0,0);
        return result;
    }

    private void handleFourList(int[] nums, int target, boolean[] use, int count, List<List<Integer>> result, List<Integer> list, int curAdd,int index) {
        if (count == 4) {
            if (curAdd == target) {
                FourList fourList = new FourList(list);
                if (containList.contains(fourList)) {
                    return;
                }
                containList.add(fourList);
                result.add(new ArrayList<>(list));
            }
            return;
        }
        int tmp = Integer.MIN_VALUE;
        for (int i = index; i < nums.length; i++) {
            if (tmp == nums[i] || use[i]) {
                continue;
            }
            tmp = nums[i];
            use[i] = true;
            List<Integer> newList = new ArrayList<>(list);
            newList.add(nums[i]);
            handleFourList(nums, target, use, count + 1, result, newList, curAdd + nums[i],index+1);
            use[i] = false;
        }
    }

    class FourList extends Object{
        public FourList(List<Integer> data) {
            setData(data);
        }
        int[] value;
        public void setData(List<Integer> data) {
            int length = data.size();
            value = new int[length];
            for (int i = 0; i < length; i++) {
                value[i] = data.get(i);
            }
            Arrays.sort(value);
        }

        @Override
        public boolean equals(Object obj) {
            FourList fourList = (FourList) obj;
            if (value.length != fourList.value.length) {
                return false;
            }
            for (int i = 0; i < value.length; i++) {
                if (value[i] != fourList.value[i]) {
                    return false;
                }
            }
            return true;
        }
    }
    /**
     所以这个算法时间复杂度是？
     外层ij 就n^2
     他优化的应该是第三层第四层，否则像我那样直接递归会造成n^4

     * */
    //Todo:四数之和的答案
    public List<List<Integer>> fourSum_true(int[] nums, int target) {
        Arrays.sort(nums);
        List<List<Integer>> ls = new ArrayList<>();
        for (int i = 0; i < nums.length - 3; i++) {
            if (i == 0 || (nums[i] != nums[i - 1])) {
                for (int j = i + 1; j < nums.length - 2; j++) {
                    if (j == i + 1 || (j > i + 1 && nums[j] != nums[j - 1])) {
                        int l = j + 1, r = nums.length - 1, sum = target - nums[j] - nums[i];
                        while (l < r) {
                            if (nums[l] + nums[r] == sum) {
                                ls.add(Arrays.asList(nums[i], nums[j], nums[l], nums[r]));
                                while (l < r && nums[l] == nums[l + 1]) {
                                    l++;
                                }
                                while (l < r && nums[r] == nums[r - 1]) {
                                    r--;
                                }
                                l++;
                                r--;
                            } else if (nums[l] + nums[r] < sum) {
                                while (l < r && nums[l] == nums[l + 1]) {
                                    l++;
                                }
                                l++;
                            } else {
                                while (l < r && nums[r] == nums[r - 1]) {
                                    r--;
                                }
                                r--;
                            }
                        }
                    }
                }
            }
        }
        return ls;
    }

    /**
     三数之和:https://leetcode-cn.com/problems/3sum/
     给定一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？找出所有满足条件且不重复的三元组。

     注意：答案中不可以包含重复的三元组。

     思路：先找到第一个数，然后分别在他的左右找和为-c的数字

     * */
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums.length < 3) {
            return result;
        }
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; i++) {
            if (i == 0 || nums[i] != nums[i - 1]) {
                int sum = 0 - nums[i];
                int left = i + 1;
                int right = nums.length - 1;
                while (left < right) {
                    if (nums[left] + nums[right] == sum) {
                        List<Integer> list = new ArrayList<>();
                        list.add(nums[i]);
                        list.add(nums[left]);
                        list.add(nums[right]);
                        result.add(list);
                        left++;
                        while (left < right && nums[left] == nums[left - 1]) {
                            left++;
                        }
                        right --;
                        while (right > left && nums[right] == nums[right + 1]) {
                            right--;
                        }
                    } else if (nums[left] + nums[right] < sum) {
                        left++;
                    } else {
                        right--;
                    }
                }
            }
        }
        return result;
    }

    /**
     岛屿数量:https://leetcode-cn.com/problems/number-of-islands/
     给定一个由 '1'（陆地）和 '0'（水）组成的的二维网格，计算岛屿的数量。一个岛被水包围，并且它是通过水平方向或垂直方向上相邻的陆地连接而成的。你可以假设网格的四个边均被水包围。

     也就是四面都要是水
     一个点属于岛屿标准是四面要么都是水，要么都属于岛屿
     首先初始化一次，初始化四条边的值
     首先遍历一遍，找到所有能作为岛屿的点
     然后再遍历一遍

     想到另外一个方法，就是整体从左上开始往右下走，保存当前状态是否在遍历岛屿
     应该还有一个：就是找到1的节点的时候，将所有与他相连的都变为0，最后总数加1
     * */
//    int result = 0;
//    public int numIslands(char[][] grid) {
//        int width = grid.length;
//        int deep = grid[0].length;
//        if (width == 0 && deep == 0) {
//            return 0;
//        }
//        updateIsland(0, 0, grid, 0,width,deep);
//    }
//
//    private int updateIsland(int i, int k, char[][] grid, int cur, int width, int deep) {
//        int result = 0;
//        if (grid[i][k] == '1') {
//            //先看看是否是结束
//            if (findIsIson(i, k, grid, width, deep)) {
//                result += 1;
//            } else {
//                if (i + 1 < width && grid[i + 1][k] == '1') {
//                    result = updateIsland(i + 1, k, grid, cur, width, deep) > 0 ? 1 : result;
//                }
//                if (k + 1 < width && grid[i][k + 1] == '1') {
//                    result = updateIsland(i, k + 1, grid, cur, width, deep) > 0 ? 1 : result;
//                }
//            }
//            grid[i][k] = '0';
//        } else {
//
//        }
//    }
    /**
     出错： int deep = grid[0].length;可能为空
     * */
    public int numIslands(char[][] grid) {
        int width = grid.length;
        if (width == 0) {
            return 0;
        }
        int deep = grid[0].length;
        int result = 0;
        for (int i = 0; i < width; i++) {
            for (int k = 0; k < deep; k++) {
                if (grid[i][k] == '1') {
                    result++;
                    searchOneAndDismiss(i,k,grid,width,deep);
                }
            }
        }
        return result;
    }

    private void searchOneAndDismiss(int i, int k, char[][] grid, int width, int deep) {
        if (i < 0 || i >= width || k < 0 || k >= deep) {
            return;
        }
        if (grid[i][k] == '1') {
            grid[i][k] = '0';
            searchOneAndDismiss(i - 1, k, grid, width, deep);
            searchOneAndDismiss(i + 1, k, grid, width, deep);
            searchOneAndDismiss(i, k - 1, grid, width, deep);
            searchOneAndDismiss(i, k + 1, grid, width, deep);
        }
    }


//    public int numIslands(char[][] grid) {
//        if (grid.length == 0 && grid[0].length == 0) {
//            return 0;
//        }
//        int lie = grid.length;
//        int deep = grid[0].length;
//        boolean[][] map = new boolean[lie][deep];
//        int[][] result = new int[lie][deep];
//        for (int i = 0; i < lie; i++) {
//            for (int k = 0; k < deep; k++) {
//                map[i][k] = findIsIson(map, i, k, grid, lie, deep);
//                if (map[i][k]) {
//
//                }
//            }
//        }
//
//    }

    private boolean findIsIson(int i, int k, char[][] grid, int lie, int deep) {
        boolean result = true;
        if (i + 1 < lie) {
            if (grid[i + 1][k] == '1') {
                return false;
            }
        }
        if (k + 1 < deep && grid[i][k + 1] == '1') {
            return false;
        }
        return true;
    }

//    private boolean findIsIson(int i, int k, char[][] grid, int lie, int deep) {
//        boolean result = false;
//        if (i - 1 >= 0 && grid[i - 1][k] == '1') {
//            return true;
//        }
//        if (i + 1 < lie && grid[i + 1][k] != '1') {
//            return false;
//        }
//        if (k - 1 >= 0 && grid[i][k - 1] != '1') {
//            return false;
//        }
//        if (k + 1 < deep && grid[i][k + 1] != '1') {
//            return false;
//        }
//        return true;
//    }


    /**
     数字范围按位与:https://leetcode-cn.com/problems/bitwise-and-of-numbers-range/
     给定范围 [m, n]，其中 0 <= m <= n <= 2147483647，返回此范围内所有数字的按位与（包含 m, n 两端点）。

     错误[1,2]:没有《=
     错误[0,2147483647] :超时
     [2000,2147483647]超时or错误
     2147483646
     2147483647
     妈呀，最大的不行呀

     2147483647
     2147483647

     这个最大的陷阱是int.max

     网上的方法：【笔记】当一个数+1时，总会有这么一个规律“某一位后的数字，全部被置为相反数”。举个例子：

     010111 + 1 = 011000，则010111 & 011000 = 010000。那么，x & (x+1) 后几位相反数的“与操作”，结果总为0。
     所以，当(m,m+1,...n-1,n)进行连续“与操作”时，会按照上述规律被抵消很大一部分，而只剩下n的前缀部分，最后只需将n归位。举个例子：

     m = 5(0101), n = 7 (0111)。不停右移，得到n前缀部分为01，最后归位前缀得res=0100=4
     int rangeBitwiseAnd(int m, int n) {
     int offset = 0;
     for (; m != n; ++offset) {
     m >>= 1;
     n >>= 1;
     }
     return n << offset;
     }
     * */
    public int rangeBitwiseAnd(int m, int n) {
        if (m == 0 || n == 0) {
            return 0;
        }
        if (m == n) {
            return m;
        }
        if (m < Integer.MAX_VALUE / 2 && n >= 2 * m) {
            return 0;
        }
        int result = m;
        if (n == Integer.MAX_VALUE) {
            n = n - 1;
        }
        for (int i = m + 1; i <= n; i++) {
            result = result & i;
        }
        return result;
    }

    /**
     无重复字符的最长子串 https://leetcode-cn.com/problems/longest-substring-without-repeating-characters/
     给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。

     lastIndex应该包括
     错误："aab" 1->2
     "nfpdmpi" 4—>5 最后结尾的判断错了
     tmmzuxt 4—>5  忽略了一个情况，遍历值已经在index之前
     * */
    public int lengthOfLongestSubstring(String s) {
        if (s.length() <= 1) {
            return s.length();
        }
        HashMap<Character, Integer> map = new HashMap<>();
        int result = 0;
        int lastIndex = 0;
        for (int i = 0; i < s.length(); i++) {
            char tmp = s.charAt(i);
            if (map.containsKey(tmp) && map.get(tmp) >= lastIndex) {
                int add = i - lastIndex;
                result = Math.max(add, result);
                lastIndex = map.get(tmp) + 1;
                map.put(tmp, i);
            } else if(i == s.length() - 1) {
                result = Math.max(result,i + 1 -lastIndex);
            } else {
                map.put(tmp, i);
            }
        }
        if (result == 0) {
            return s.length();
        }
        return result;
    }

    /**
     删除链表的倒数第N个节点 https://leetcode-cn.com/problems/remove-nth-node-from-end-of-list/
     给定一个链表，删除链表的倒数第 n 个节点，并且返回链表的头结点。

     使用双指针？

     [1,2]
     2
     错误[1,2]

     这里一个亮点就是使用了虚拟头部，避免了头部会被删除的问题
     * */
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode virtulHead = new ListNode(0);
        virtulHead.next = head;
        ListNode slow = virtulHead;
        ListNode fast = virtulHead;
        if (n == 0) {
            return head;
        }
        if (head.next == null) {
            return null;
        }
        for (int i = 0; i < n + 1; i++) {
            fast = fast.next;
        }
        if (fast == null) {
            slow.next = slow.next.next;
            return virtulHead.next;
        }
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        slow.next = slow.next.next;
        return virtulHead.next;
    }

    public void test_removeNthFromEnd() {
        ListNode n1 = new ListNode(1);
        ListNode n2 = new ListNode(2);
        n1.next = n2;
        n2.next = null;
        ListNode result = removeNthFromEnd(n1,2);
        while (result != null) {
            System.out.println(result.val+" ");
            result = result.next;
        }
    }

    /**
     搜索旋转排序数组:https://leetcode-cn.com/problems/search-in-rotated-sorted-array/
     假设按照升序排序的数组在预先未知的某个点上进行了旋转。
     ( 例如，数组 [0,1,2,4,5,6,7] 可能变为 [4,5,6,7,0,1,2] )。
     搜索一个给定的目标值，如果数组中存在这个目标值，则返回它的索引，否则返回 -1 。
     你可以假设数组中不存在重复的元素。
     你的算法时间复杂度必须是 O(log n) 级别。

     我能想到的还是使用二分查找，只不过在查找的时候需要判断下当前分段是否是排序分段
     * */
//    public int search(int[] nums, int target) {
//        if (nums.length < 1) {
//            return -1;
//        }
//        int length = nums.length;
//        int index = length/2;
//        if(nums[index] == target) {
//            return index;
//        }
//        int result = findTargetBinary(nums,0,index-1,target);
//        if (result == -1) {
//            return findTargetBinary(nums, index + 1, length - 1, target);
//        } else {
//            return result;
//        }
//    }

    private int findTargetBinary(int[] nums, int start, int end, int target) {
        if (start > end) {
            return -1;
        } else if (start == end) {
            if (nums[start] == target) {
                return start;
            }
            return -1;
        }
        int index = (start + end) / 2;
        if (nums[index] == target) {
            return index;
        }
        //顺序数组
        if (nums[start] < nums[end]) {
            if (nums[index] < target) {
                return findTargetBinary(nums, index + 1, end, target);
            } else {
                return findTargetBinary(nums, start, index - 1, target);
            }
        } else {
            int result = findTargetBinary(nums, start, index - 1, target);
            if (result != -1) {
                return result;
            } else {
                return findTargetBinary(nums, index + 1, end, target);
            }
        }
    }

    /**
     最大正方形:https://leetcode-cn.com/problems/maximal-square/
     在一个由 0 和 1 组成的二维矩阵内，找到只包含 1 的最大正方形，并返回其面积。

     想到之前做的另外一个岛屿的题，可以仿造？
     不对，这是正方形，得规定下

     这个算法的问题在于我是用深度优先的，应该用广度优先的
     或许可以换个思路：
     找到一个点的时候，先跳跃到他的对角点，然后匹配对角点和起点的距离横竖是否全为1，是则置空0并计数加1

     出错的点：遍历后置0：这样容易吧别的正方形弄掉了
     * */

    public int maximalSquare(char[][] matrix) {
        int result = 0;
        int width = matrix.length;
        if(width == 0) {
            return result;
        }
        int deep = matrix[0].length;
        for (int i = 0; i < width; i++) {
            for (int k = 0; k < deep; k++) {
                if (matrix[i][k] == '1') {
                    result = Math.max(result,findMaxQuare(matrix,i,k,width,deep));
                }
            }
        }
        return result;
    }

    private int findMaxQuare(char[][] matrix, int i, int k, int width, int deep) {
        //最大边长
        int maxLine = Math.min(width-i,deep-k);
        int result = 1;
        for (int line = 1; line < maxLine; line++) {
            if (checkSquare(matrix, i, k, i + line, k + line)) {
                result = (line + 1) * (line + 1);
                matrix[i][k] = '0';
            } else {
                break;
            }
        }
        return result;
    }

    /**
     验证从curI到curK是否都为'1'
     * */
    private boolean checkSquare(char[][] matrix, int i, int k, int curI, int curK) {
        for (int indexI = curI; indexI >= i; indexI--) {
            if(matrix[indexI][curK] != '1') {
                return false;
            }
        }
        for (int indexK = curK; indexK >= k; indexK--) {
            if(matrix[curI][indexK] != '1') {
                return false;
            }
        }
//        for (int indexI = curI; indexI >=i; indexI--) {
//            matrix[indexI][curK] = '0';
//        }
//        for (int indexK = curK; indexK >= k; indexK--) {
//            matrix[curI][indexK] = '0';
//        }
        return true;
    }

//    /**
//     i,k是起点
//     * */
//    private int findMaxQuare(char[][] matrix, int i, int k, int width, int deep) {
//        //最大边长
//        int maxLine = Math.min(width-i,deep-k);
//        for (int line = maxLine - 1; line >= 0; line--) {
//            if (checkAndDismissOne(i, k, i + line, k + line, matrix)) {
//                return (line + 1) * (line + 1);
//            }
//        }
//        return 1;
//    }
//
//    private boolean checkAndDismissOne(int i, int k, int targetI, int targetK, char[][] matrix) {
//        if (i > targetI || k > targetK) {
//            return true;
//        }
//        if (matrix[i][k] != '1') {
//            return false;
//        }
//        if (i == targetI && k == targetK) {
////            matrix[i][k] = '0';
//            return true;
//        }
//        //广度优先
//
//        if (checkAndDismissOne(i + 1, k, targetI, targetK, matrix) && checkAndDismissOne(i, k + 1, targetI, targetK, matrix)) {
//            matrix[i][k] = '0';
//            return true;
//        } else {
//            return false;
//        }
//    }

    public void test_maximalSquare() {
        char[][] tmp = new char[][]{{'1','0','1','0','0'},{'1','0','1','1','1'},{'1','1','1','1','1'},{'1','0','0','1','0'}};
        System.out.println(maximalSquare(tmp));
    }


    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    /**
     二叉树的最近公共祖先 https://leetcode-cn.com/problems/lowest-common-ancestor-of-a-binary-tree/
     给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。

     百度百科中最近公共祖先的定义为：“对于有根树 T 的两个结点 p、q，最近公共祖先表示为一个结点 x，满足 x 是 p、q 的祖先且 x 的深度尽可能大（一个节点也可以是它自己的祖先）。

     我的想法是深度优先算法，找到其中一个,然后从这个节点开始往上溯源，找到另外一个节点

     使用广度优先的话，很容易找到同级的，导致错误

     我好像把它复杂化了，可以直接迭代呀

     哥哥还是深度优先把，我在栈的写法上面有问题

     说明:

     所有节点的值都是唯一的。
     p、q 为不同节点且均存在于给定的二叉树中


     * */

//    Stack<TreeNode> stack = new Stack<>();
//    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
//        stack.push(root);
//        return lowestCommonAncestor(p,q);
//    }
//
//    private TreeNode lowestCommonAncestor(TreeNode p, TreeNode q) {
//        TreeNode root = stack.pop();
//        if (root.val == p.val) {
//            if (findTreeNode(root, q)) {
//                return root;
//            }
//            if (root.right != null) {
//                stack.push(root.right);
//            }
//            if(root.left != null) {
//                stack.push(root.left);
//            }
//            return lowestCommonAncestor(p,q);
//        }
//    }

    TreeNode result = null;
    TreeNode findOne = null;
    public TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
        //处理下根节点相同时的逻辑
        TreeNode next = null;
        if (root.val == p.val) {
            next = q;
            if (findTreeNode(root, next)) {
                return root;
            }
        } else if (root.val == q.val) {
            next = p;
            if (findTreeNode(root, next)) {
                return root;
            }
        }
        int req = -1;
        //然后左子树开始查找
        if (root.left != null) {
            req = findTreeNodeByCode(root.left, p, q);
            if (req == 0) {
                return result;
            } else if (req == 1) {
                if (root.right == null) {
                    return null;
                }
                return root;
            }
            if (root.right == null) {
                return null;
            }
            if (findTreeNodeByCode(root.right, p, q) != 0) {
                return null;
            }
            return result;
        }

        if (root.right != null) {
            req = findTreeNodeByCode(root.right, p, q);
            if (req == 0) {
                return result;
            }
            return null;
        }
        return null;
    }

    private int findTreeNodeByCode(TreeNode root, TreeNode p, TreeNode q) {
        if (root.val == p.val) {
            if (findTreeNode(root, q)) {
                //两个都找到了
                result = root;
                return 0;
            }
            findOne = q;
            //找到一个
            return 1;
        }
        if(root.val == q.val) {
            if (findTreeNode(root, p)) {
                result = root;
                return 0;
            }
            findOne = p;
            return 1;
        }
        if (root.left != null) {
            int req = findTreeNodeByCode(root.left, p, q);
            if (req == 0) {
                return 0;
            } else if (req == 1) {
                //找到一个,那我们在右子树看看有没有
                if (root.right == null) {
                    return 1;
                } else {
                    if (findTreeNode(root.right, findOne)) {
                        result = root;
                        return 0;
                    }
                    return 1;
                }
            } else {
                //左子树没找到
                if (root.right == null) {
                    return -1;
                } else {
                    return findTreeNodeByCode(root.right, p, q);
                }
            }
        }
        if (root.right != null) {
            int req = findTreeNodeByCode(root.right, p, q);
            if (req == 0) {
                return 0;
            } else if (req == 1) {
                //找到一个,那我们在右子树看看有没有
                if (root.left == null) {
                    return 1;
                } else {
                    if (findTreeNode(root.left, findOne)) {
                        result = root;
                        return 0;
                    }
                    return 1;
                }
            } else {
                //右子树没找到
                if (root.left == null) {
                    return -1;
                } else {
                    return findTreeNodeByCode(root.left, p, q);
                }
            }
        }
        return -1;
    }

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        TreeNode result = null;
        TreeNode next = null;
        Stack<TreeNode> stack = new Stack<>();
        Stack<TreeNode> useFind = new Stack<>();
        useFind.push(root);
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode cur = stack.pop();
            if (cur.val == p.val) {
                next = q;
                break;
            } else if (cur.val == q.val) {
                next = p;
                break;
            }
            if (cur.left != null) {
                stack.push(cur.left);
                useFind.push(cur.left);
            }

            if (cur.right != null) {
                stack.push(cur.right);
                useFind.push(cur.right);
            }

        }
        if (next == null) {
            return result;
        }
        //首先查找到的这个点的左右节点
        while (!useFind.isEmpty()) {
            TreeNode cur = useFind.pop();
            if (findTreeNode(cur, next)) {
                return cur;
            }
        }
        return result;
    }
//
    private boolean findTreeNode(TreeNode cur, TreeNode next) {
        if (cur == null) {
            return false;
        }
        if (cur.val == next.val) {
            return true;
        }
        return findTreeNode(cur.left,next) || findTreeNode(cur.right,next);
    }
//
    public void test_lowestCommonAncestor() {
        TreeNode root = new TreeNode(3);
        TreeNode left = new TreeNode(5);
        TreeNode right = new TreeNode(1);
        root.left = left;
        root.right = right;
        left.left = null;
        left.right = null;
        right.left = null;
        right.right = null;
        System.out.println(lowestCommonAncestor2(root,left,right).val);
    }

    /**
     我上面的写法比较繁杂，其实这算是理想题，没有不存在的情况，那么我们可以稍微简化一点

     * */
    public TreeNode lowestCommonAncestor_true(TreeNode root, TreeNode p, TreeNode q) {
        // LCA 问题
        if (root == null) {
            return root;
        }
        if (root == p || root == q) {
            return root;
        }
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if (left != null && right != null) {
            return root;
        } else if (left != null) {
            return left;
        } else if (right != null) {
            return right;
        }
        return null;
    }


    /**
     螺旋矩阵 https://leetcode-cn.com/problems/spiral-matrix/
     给定一个包含 m x n 个元素的矩阵（m 行, n 列），请按照顺时针螺旋顺序，返回矩阵中的所有元素。

     不如把这道题简化成一圈一圈的解析

     错误：
     1、width和deep弄混了
     2、
     * */
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        int deep = matrix.length;
        if (deep <= 0) {
            return result;
        }
        int width = matrix[0].length;
        //能够遍历多少层
        int goMax = Math.min(width - 1, deep - 1);
        int curX = 0;
        int curY = 0;
        //一开始的四个点
        int top = 0;
        int left = 0;
        int right = width - 1;
        int bottom = deep - 1;
        while (true) {
            //提供上下左右四个点
            recordPoint(left, top, right, bottom, result, matrix);
            left++;
            right--;
            top++;
            bottom--;
            if (left > right || top > bottom) {
                break;
            }
        }

        return result;
    }

    private void recordPoint(int left, int top, int right, int bottom, List<Integer> result, int[][] matrix) {
        if (left == right) {
            if (top == bottom) {
                result.add(matrix[left][top]);
                return;
            }
            for (int i = top; i <= bottom; i++) {
                result.add(matrix[i][left]);
            }
            return;
        } else if (top == bottom) {
            for (int i = left; i <= right; i++) {
                result.add(matrix[bottom][i]);
            }
            return;
        } else {
            for (int i = left; i <= right; i++) {
                result.add(matrix[top][i]);
            }
            for (int k = top + 1; k <= bottom; k++) {
                result.add(matrix[k][right]);
            }
            for (int i = right - 1; i >= left; i--) {
                result.add(matrix[bottom][i]);
            }
            for (int k = bottom - 1; k > top; k--) {
                result.add(matrix[k][left]);
            }
        }
    }

    public void test_spiralOrder() {
//        int[][] data = new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
        int[][] data = new int[][]{{1}};
        List<Integer> result = spiralOrder(data);
        for (int a : result) {
            System.out.print(a+" ");
        }
    }

    /**
      螺旋矩阵 2 https://leetcode-cn.com/problems/spiral-matrix-ii/
     给定一个正整数 n，生成一个包含 1 到 n2 所有元素，且元素按顺时针顺序螺旋排列的正方形矩阵。

     就是上面那个的逆向思路，从外层填充到中间
     * */
    public int[][] generateMatrix(int n) {
        if (n == 0) {
            return null;
        }
        int[][] result = new int[n][n];
        int top = 0;
        int left = 0;
        int right = n - 1;
        int bottom = n - 1;
        int cur = 1;
        while (true) {
            //提供上下左右四个点
            cur = inputPoint(left, top, right, bottom, result, cur);
            left++;
            right--;
            top++;
            bottom--;
            if (left > right || top > bottom) {
                break;
            }
        }
        return result;

    }

    private int inputPoint(int left, int top, int right, int bottom, int[][] matrix, int cur) {
        if (left == right) {
            if (top == bottom) {
                matrix[left][top] = cur;
                cur++;
                return cur;
            }
            for (int i = top; i <= bottom; i++) {
                matrix[i][left] = cur;
                cur++;
            }
            return cur;
        } else if (top == bottom) {
            for (int i = left; i <= right; i++) {
                matrix[bottom][i] = cur;
                cur++;
            }
            return cur;
        } else {
            for (int i = left; i <= right; i++) {
                matrix[top][i] = cur;
                cur++;
            }
            for (int k = top + 1; k <= bottom; k++) {
                matrix[k][right] = cur++;
            }
            for (int i = right - 1; i >= left; i--) {
                matrix[bottom][i] = cur++;
            }
            for (int k = bottom - 1; k > top; k--) {
                matrix[k][left] = cur++;
            }
            return cur;
        }
    }

    /**
     旋转链表 https://leetcode-cn.com/problems/rotate-list/
     给定一个链表，旋转链表，将链表每个节点向右移动 k 个位置，其中 k 是非负数。

    我有个想法，使用k进行循环定位，保留尾节点，然后找到目标节点，保留目标节点的上一个节点（这里可能为空）
     然后进行断的操作


     错误：[1,2] 1 我的输出是2,1
     我的思路错了，从头开始数是向左移动
     还有一种做法,使用快慢节点
     还有一种比较好理解，就是使用队列
    反正也没有限制，还是用链表吧

     过了
     但是还有一种思路，就是遍历，然后得到长度
     然后头尾相连，然后整个环形移动
     虽然不觉得这种做法比我好到哪里

     * */
    public ListNode rotateRightV2(ListNode head, int k) {
        if (k == 0 || head == null || head.next == null) {
            return head;
        }
        List<ListNode> list = new ArrayList<>();
        ListNode cur = head;
        while (cur != null) {
            list.add(cur);
            cur = cur.next;
        }
        int length = list.size();
        int move = k % length;
        if (move == 0) {
            return head;
        }
        ListNode newHead = list.get(length - move);
        list.get(length-1).next = list.get(0);
        list.get(length - move -1).next = null;
        return newHead;
    }

    public ListNode rotateRight(ListNode head, int k) {
        if (k == 0 || head == null || head.next == null) {
            return head;
        }
        ListNode cur = head;
        ListNode tail = null;
        ListNode last = null;
        while (k > 0) {
            last = cur;
            cur = cur.next;
            if (cur == null) {
                //找到尾巴
                tail = last;
                cur = head;
            }
            k--;
        }
        ListNode newHead = cur;
        if (tail == null) {
            while (cur != null) {
                if (cur.next == null) {
                    tail = cur;
                    break;
                }
                cur = cur.next;
            }
        }
        //找到的还是原来的
        if (newHead == head) {
            return newHead;
        } else {
            last.next = null;
            tail.next = head;
            return newHead;
        }

    }



    /**
     除自身以外数组的乘积:https://leetcode-cn.com/problems/product-of-array-except-self/
     给定长度为 n 的整数数组 nums，其中 n > 1，返回输出数组 output ，其中 output[i] 等于 nums 中除 nums[i] 之外其余各元素的乘积。
     说明: 请不要使用除法，且在 O(n) 时间复杂度内完成此题。

     这道题比较麻烦的是在常数时间，还不能使用除法？要上天
     想到一个方法，a[i]表示i-1前面的乘机，b[i]表示i+1之后的乘机
     结果c[i] = a[i]*b[i]
     这里我多了一个循环，其实可以不要的，最后从右往左循环的时候再来一次就行了

     
     * */
    public int[] productExceptSelf(int[] nums) {
        int length = nums.length;
        int a[] = new int[length];
        int b[] = new int[length];
        a[0] = 1;
        for (int i = 1; i < length; i++) {
            a[i] = a[i-1] * nums[i-1];
        }
        b[length-1] = 1;
        for (int i = length - 2; i >= 0; i--) {
            b[i] = b[i+1] * nums[i+1];
        }
        for (int i = 0; i < length; i++) {
            nums[i] = b[i] * a[i];
        }
        return nums;
    }

    public void test_productExceptSelf() {
        int[] res = productExceptSelf(new int[]{1,2,3,4});
        for(int a : res) {
            System.out.println(a);
        }
    }


    /**
     旋转图像 https://leetcode-cn.com/problems/rotate-image/
     给定一个 n × n 的二维矩阵表示一个图像。

     将图像顺时针旋转 90 度。

     说明：

     你必须在原地旋转图像，这意味着你需要直接修改输入的二维矩阵。请不要使用另一个矩阵来旋转图

     来源：力扣（LeetCode）
     链接：https://leetcode-cn.com/problems/rotate-image

     需要原地旋转，又不给我创造矩阵，那就一个个单元转呗
     突然脑子转不过来，不知道怎么做四点旋转

     错误[[5,1,9,11],[2,4,8,10],[13,3,6,7],[15,14,12,16]]

     突然觉得自己秀逗了，直接打印不好吗？不好，就是不能直接打印
     * */
    public void rotate(int[][] matrix) {
        int n = matrix.length;
        if (n == 0 || n == 1) {
            return;
        }
        int left = 0,right = n-1,top = 0,bottom = n-1;
        while (left <= right) {
            handleMatrixRotate(matrix,left,top,right,bottom);
            left++;
            right--;
            top++;
            bottom--;
        }
    }

    private void handleMatrixRotate(int[][] matrix, int left, int top, int right, int bottom) {
        int tmp;
        //应该是只走一步的时候有问题
        for (int i = 0; i < right-left; i++) {
            tmp = matrix[top + i][right];
            matrix[top + i][right] = matrix[top][left+i];
            matrix[top][left + i] = matrix[bottom-i][left];
            matrix[bottom-i][left] = matrix[bottom][right-i];
            matrix[bottom][right-i] = tmp;
        }
    }

    public void test_rotate() {
        int[][] data = new int[][]{{5, 1, 9, 11}, {2, 4, 8, 10}, {13, 3, 6, 7}, {15, 14, 12, 16}};
        rotate(data);
        for (int i = 0; i < 4; i++) {
            for (int k = 0; k < 4; k++) {
                System.out.print(data[i][k]+" ");
            }
            System.out.println();
        }
    }

    /**
     长度最小的子数组
     给定一个含有 n 个正整数的数组和一个正整数 s ，找出该数组中满足其和 ≥ s 的长度最小的连续子数组。如果不存在符合条件的连续子数组，返回 0。
      https://leetcode-cn.com/problems/minimum-size-subarray-sum/


     一开始看题目还感觉有点绕

     维护一个start和end，end进行叠加，如果和大于s的话就尝试将start回退

     这道题不难解，问题是如何优化
     这个有空在看吧，不想玩
     * */
    public int minSubArrayLen(int s, int[] nums) {
        int start = 0,end = 0;
        int length = nums.length;
        int maxLength = length + 1;
        int curSum = 0;
        for (; end < length; end++) {
            curSum += nums[end];
            if (curSum >= s) {
                maxLength = Math.min((end-start+1),maxLength);
            }
            //可以前推的情况
            while (curSum >= s && curSum-nums[start] >= s && start <= end) {
                curSum -= nums[start];
                start++;
                maxLength = Math.min((end-start+1),maxLength);
            }
        }
        if (maxLength == length + 1) {
            return 0;
        }
        return maxLength;
    }


    /**
     搜索二维矩阵 II
     https://leetcode-cn.com/problems/search-a-2d-matrix-ii/

     编写一个高效的算法来搜索 m x n 矩阵 matrix 中的一个目标值 target。该矩阵具有以下特性：

     每行的元素从左到右升序排列。
     每列的元素从上到下升序排列。

     这道题貌似做过咧，从右上角开始就行了
     如果大，那么就没必要往下走了，往左走就行了
     * */
    public boolean searchMatrix(int[][] matrix, int target) {
        int deep = matrix.length;
        if (deep == 0) {
            return false;
        }
        int width = matrix[0].length;
        int x = 0;
        int y = width - 1;
        while (x < deep && y >= 0) {
            //一开始的时候没有考虑越界
            if (matrix[x][y] == target) {
                return true;
            }
            if (matrix[x][y] > target) {
                if (y == 0) {
                    return false;
                }
                y--;
            } else {
                if (x == deep - 1) {
                    return false;
                }
                x++;
            }
        }
        return false;
    }
}