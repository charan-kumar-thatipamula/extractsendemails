package com.charan.util.email;

import java.util.*;

//Circular buffer implementation using HashMap and Queue
public class SentMailCountHandler {
    int waitTimeBetweenEmails;
    int emailLimit;
    Queue<Count> countQueue;
    Map<Long, Count> map;
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

    public int getTotalCount(long timeInMinutes) {
        int totalCount = 0;
        for (int i=0;i<=waitTimeBetweenEmails;i++) {
            if (map.get(timeInMinutes-i) != null) {
                totalCount += map.get(timeInMinutes-i).getCount();
            }
        }
        return totalCount;
    }

    public void updateCount(long timeInMinutes) {
        if (map.get(timeInMinutes) != null) {
            map.get(timeInMinutes).incrementCount();
        } else {
            if (countQueue.size() == waitTimeBetweenEmails) {
                Count count = countQueue.poll();
                map.remove(count.getTimeInMinutes());
            }
            Count count = new Count();
            countQueue.add(count);
            map.put(timeInMinutes, count);
        }
    }
}


class Count {
    int count;
    long timeInMinutes;

    Count() {
        count = 1;
    }

    public long getTimeInMinutes() {
        return timeInMinutes;
    }

    public void setTimeInMinutes(long timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }


    void incrementCount() {
        count++;
    }

    int getCount() {
        return count;
    }
}
