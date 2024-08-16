package com.scf.single.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "content")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private int contentId;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "title")
    private String title;

    @Column(name = "content_exp")
    private int contentExp;

    @OneToMany(mappedBy = "content")
    private List<Script> scripts;

    @OneToMany(mappedBy = "content")
    private List<ContentCheckUser> contentCheckUsers;

}
