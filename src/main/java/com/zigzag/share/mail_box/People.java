package com.zigzag.share.mail_box;

import com.zigzag.share.GuardSuspensionV3;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qlk
 */
@Slf4j(topic = "c.People")
public class People extends Thread {

    @Override
    public void run() {
        // 收信
        GuardSuspensionV3 guard = MailBoxes.createGuardSuspensionV3();
        log.debug("开始收信 id: {}", guard.getId());
        Object mail = guard.get(5000);
        log.debug("收信成功 id: {}, 内容为: {}", guard.getId(), mail);

    }
}
