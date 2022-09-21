package com.example.chatting.chatroom.sse;

import com.example.chatting.user.User;
import com.example.chatting.utils.TimeStamp;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter @NoArgsConstructor
public class Notification extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    //읽었는지에 대한 여부
    @Column(nullable = false)
    private Boolean isRead;


    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User receiver;

    @Builder
    public Notification(User receiver, Boolean isRead) {
        this.receiver = receiver;
        this.isRead = isRead;
    }

    public void read() {
        this.isRead = true;
    }


}
