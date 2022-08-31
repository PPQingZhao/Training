package com.pp.media.callback;

public interface BackPressedHandler {
    /**
     * 是否处理 backPressed 事件
     *
     * @return
     */
    boolean isHandle();

    /**
     * @return true表示backPressed事件不在继续分发
     */
    boolean handleBackPressed();
}
