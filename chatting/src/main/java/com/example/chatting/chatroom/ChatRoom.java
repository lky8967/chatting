package com.example.chatting.chatroom;

import com.example.chatting.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User requester;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User acceptor;

    @Column(nullable = false)
    private Boolean reqOut;

    @Column(nullable = false)
    private Boolean accOut;

    public void reqOut(Boolean bool) {
        this.reqOut = bool;
    }

    public void accOut(Boolean bool) {
        this.accOut = bool;
    }


    public static ChatRoom createOf(User requester, User acceptor) {

        ChatRoom room = new ChatRoom();
        room.requester = requester;
        room.acceptor = acceptor;
        room.reqOut = false;
        room.accOut = true;
//        room.accFixed = false;
//        room.reqFixed = false;
//        room.type = "START";
        return room;
    }


}
