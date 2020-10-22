package com.happy3w.lotterycalculator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LotteryInfo {
    private String code;
    private int[] reds;
    private int[] blues;

    public String getDesc() {
        StringBuilder builder = new StringBuilder();
        fillNums(reds, builder);
        fillNums(blues, builder);

        builder.setLength(builder.length() - 1);
        return builder.toString();
    }

    private void fillNums(int[] nums, StringBuilder builder) {
        for (int v : nums) {
            builder.append(v)
                    .append(',');
        }
    }

    public String winPrice(LotteryInfo other) {
        int redCount = compareNums(reds, other.reds);
        int blueCount = compareNums(blues, other.blues);
        if (redCount == 6 && blueCount == 1) {
            return "一等奖";
        } else if (redCount == 6) {
            return "二等奖";
        } else if (redCount == 5 && blueCount == 1) {
            return  "三等奖";
        } else if (redCount == 5 ||
                redCount == 4 && blueCount == 1) {
            return  "四等奖";
        } else if (redCount == 4 ||
                redCount == 3 && blueCount == 1) {
            return  "五等奖";
        } else if (blueCount == 1) {
            return  "六等奖";
        } else {
            return "";
        }
    }

    private int compareNums(int[] nums1, int[] nums2) {
        int count = 0;

        for (int index1 = 0, index2 = 0; index1 < nums1.length && index2 < nums2.length;) {
            int v1 = nums1[index1];
            int v2 = nums2[index2];
            if (v1 == v2) {
                count++;
                index1++;
                index2++;
            } else if (v1 < v2) {
                index1++;
            } else {
                index2++;
            }
        }

        return count;
    }

    public static LotteryInfo decode(String line) {
        String[] items = line.split(",");

        int[] reds = new int[items.length - 2];
        for (int i = 0; i < reds.length; i++) {
            reds[i] = Integer.parseInt(items[ i + 1]);
        }
        return new LotteryInfo(items[0], reds, new int[]{Integer.parseInt(items[items.length - 1])});
    }
}
