package com.recursivechaos.boredgames.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "game")
@NamedQueries({
    @NamedQuery(
        name = "com.recursivechaos.boredgames.core.Game.findAll",
        query = "SELECT g FROM Game g"
    ),
    @NamedQuery(
        name = "com.recursivechaos.boredgames.core.Game.findByGid",
        query = "SELECT g FROM Game g WHERE g.gid = :gid"
    )
})
public class Game {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long gid;
	
	@Column(name = "title", nullable = false)
    private String title;

    @Column(name = "desc", nullable = false)
    private String desc;
    
    @Column(name = "imgUrl", nullable = false)
    private String imgUrl;

	public long getGid() {
		return gid;
	}

	public void setGid(long gid) {
		this.gid = gid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

}
