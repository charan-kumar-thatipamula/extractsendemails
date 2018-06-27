package com.charan.util.email;

import com.charan.util.DateUtil;

import java.util.*;

//Circular buffer implementation using HashMap and Queue
public class SentMailCountHandler {
    int waitTimeBetweenEmails;
    int emailLimit;
    Queue<Count> countQueue;

    public int getWaitTimeBetweenEmails() {
        return waitTimeBetweenEmails;
    }

    public void setWaitTimeBetweenEmails(int waitTimeBetweenEmails) {
        this.waitTimeBetweenEmails = waitTimeBetweenEmails;
    }

    public int getEmailLimit() {
        return emailLimit;
    }

    public void setEmailLimit(int emailLimit) {
        this.emailLimit = emailLimit;
    }

    Map<Long, Count> map;

    public SentMailCountHandler() {
        this.countQueue = new LinkedList<Count>();
        this.map = new HashMap<>();
    }
    public SentMailCountHandler(int waitTimeBetweenEmails, int emailLimit) {
        this.waitTimeBetweenEmails = waitTimeBetweenEmails;
        this.emailLimit = emailLimit;
        this.countQueue = new LinkedList<Count>();
        map = new HashMap<>();
    }

    public boolean countReached(long timeInMinutes) {
        int totalCount = getTotalCount(timeInMinutes);
        return totalCount >= emailLimit;
    }

    public boolean sentMailsLimitReached() {
//        long currentTimeInMinutes = DateUtil.getTimeInMinutes();
        long currentTime = DateUtil.getCurrentTime();
        int totalCount = getTotalCount(currentTime);
        return totalCount >= emailLimit;
    }

    public int getTotalCount(long currentTime) {
        int totalCount = 0;
        for (int i=0;i<waitTimeBetweenEmails;i++) {
            if (map.get(currentTime-i) != null) {
                totalCount += map.get(currentTime-i).getCount();
            }
        }
        return totalCount;
    }

    public void updateSentMailCount() {
//        long currentTimeInMinutes = DateUtil.getTimeInMinutes();
        long currentTime = DateUtil.getCurrentTime();
        if (map.get(currentTime) != null) {
            map.get(currentTime).incrementCount();
            System.out.println(currentTime + " : " + map.get(currentTime).getCount());
        } else {
            if (countQueue.size() == waitTimeBetweenEmails) {
                Count count = countQueue.poll();
                map.remove(count.getTime());
                System.out.println("    Removed time: " + count.getTime());
            }
            Count count = new Count(1);
            count.setTime(currentTime);
            countQueue.add(count);
            map.put(currentTime, count);
            System.out.println(currentTime + " : " + count.getCount());
        }
    }
}


class Count {
    int count;

    long time;

    Count() {

    }

    Count(int count) {
        this.count = count;
    }
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    void incrementCount() {
        count++;
    }

    int getCount() {
        return count;
    }
}
