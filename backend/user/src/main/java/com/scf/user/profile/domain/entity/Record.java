package com.scf.user.profile.domain.entity;

import com.scf.user.member.domain.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "record")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recordId;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Column(name = "ranking", nullable = false)
    private Integer ranking;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "part_cnt", nullable = false)
    private Integer partCnt;

    @Column(name = "game_type", nullable = false)
    private Integer gameType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;


}
