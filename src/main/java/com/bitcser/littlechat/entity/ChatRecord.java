package com.bitcser.littlechat.entity;

import java.sql.Timestamp;

public class ChatRecord {
    private Integer id;
    private Integer senderId;
    private Integer receiverId;
    private String lastMessage;
    private Timestamp updatedAt;
    private Integer unreadCount;
    private Integer isGroup;
    private User sender;

    public ChatRecord(Integer id, Integer senderId, Integer receiverId, String lastMessage, Timestamp updatedAt, Integer unreadCount, Integer isGroup) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.lastMessage = lastMessage;
        this.updatedAt = updatedAt;
        this.unreadCount = unreadCount;
        this.isGroup = isGroup;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

    public Integer getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(Integer isGroup) {
        this.isGroup = isGroup;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
