package com.zigzag.share.mail_box;

import com.zigzag.share.GuardSuspensionV3;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qlk
 */
@Slf4j(topic = "c.Postman")
public class Postman extends Thread {

    private int id;
    private String mail;

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        GuardSuspensionV3 guard = MailBoxes.getGuardSuspensionV3(id);
        log.debug("送信 id: {}, 内容: {}", id, mail);
        guard.complete(mail);
    }
}
