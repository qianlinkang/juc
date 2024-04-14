package com.zigzag.share.mail_box;

import com.zigzag.share.GuardSuspensionV3;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 中间解耦类
 *
 * @author qlk
 * @see GuardSuspensionV3
 */
public class MailBoxes {

    // 线程安全-hashtable
    private static Map<Integer, GuardSuspensionV3> boxes = new Hashtable<>();

    private static int id = 1;

    // 产生唯一id,线程安全-synchronized
    public static synchronized int generateId() {
        return id++;
    }

    public static GuardSuspensionV3 getGuardSuspensionV3(int id) {
        return boxes.remove(id);
    }

    public static GuardSuspensionV3 createGuardSuspensionV3() {
        GuardSuspensionV3 guard = new GuardSuspensionV3(generateId());
        boxes.put(guard.getId(), guard);
        return guard;
    }

    public static Set<Integer> getIds() {
        return boxes.keySet();
    }


    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new People().start();
        }

        TimeUnit.SECONDS.sleep(1);

        // 一一对应,一个居民对应一个邮递员
        for (Integer id : MailBoxes.getIds()) {
            new Postman(id, "内容" + id).start();
        }
    }

}
