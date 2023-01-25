package com.notificationman.library.config

import com.notificationman.library.model.NotificationImportanceLevel

class NotificationManChannelConfig(
    var channelId: String? = null,
    var channelName: String? = null,
    var importanceLevel: NotificationImportanceLevel = NotificationImportanceLevel.DEFAULT,
    var showBadge: Boolean = true
) {
    /**
     * @param channelId A string value for custom notification channel id. (optional)
     * @param channelName A string value for custom notification channel name. (optional)
     * @param importanceLevel Importance level of the notification. (optional)
     * @param showBadge Visibility settings of the notification badge. (optional)
     */
    data class Builder(
        var channelId: String? = null,
        var channelName: String? = null,
        var importanceLevel: NotificationImportanceLevel = NotificationImportanceLevel.DEFAULT,
        var showBadge: Boolean = true
    ) {
        fun setChannelId(id: String) = apply {
            this.channelId = id
        }
        fun setChannelName(name: String) = apply {
            this.channelName = name
        }
        fun setImportanceLevel(level: NotificationImportanceLevel) = apply {
            this.importanceLevel = level
        }
        fun setShowBadge(shouldShow: Boolean) = apply {
            this.showBadge = shouldShow
        }
        fun build(): NotificationManChannelConfig =
            NotificationManChannelConfig(
                channelId = channelId,
                channelName = channelName,
                importanceLevel = importanceLevel,
                showBadge = showBadge,
            )
    }
}
