package com.kenlhk.notekeeper.model.source;

import com.kenlhk.notekeeper.model.Source;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("podcast")
@Data
public class Podcast extends Source {
    @Column(name = "channel")
    private String channel;

    @Column(name = "url")
    private String url;
}
