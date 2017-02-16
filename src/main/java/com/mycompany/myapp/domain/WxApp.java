package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * 小程序实体.
 * @author Bruce true hipster
 */
@ApiModel(description = "小程序实体. @author Bruce true hipster")
@Entity
@Table(name = "wx_app")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "wxapp")
public class WxApp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "icon")
    private String icon;

    /**
     * name
     */
    @NotNull
    @ApiModelProperty(value = "name", required = true)
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * description
     */
    @ApiModelProperty(value = "description")
    @Column(name = "description")
    private String description;

    /**
     * 发布日期
     */
    @ApiModelProperty(value = "发布日期")
    @Column(name = "publication_date")
    private LocalDate publicationDate;

    /**
     * 截图
     */
    @ApiModelProperty(value = "截图")
    @Column(name = "screenshot")
    private String screenshot;

    @NotNull
    @Column(name = "qcode", nullable = false)
    private String qcode;

    @Column(name = "score", precision=10, scale=2)
    private BigDecimal score;

    @Column(name = "views")
    private Long views;

    @OneToMany(mappedBy = "wxapp")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "wxApp")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "wxApp")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public WxApp icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public WxApp name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public WxApp description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public WxApp publicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public WxApp screenshot(String screenshot) {
        this.screenshot = screenshot;
        return this;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    public String getQcode() {
        return qcode;
    }

    public WxApp qcode(String qcode) {
        this.qcode = qcode;
        return this;
    }

    public void setQcode(String qcode) {
        this.qcode = qcode;
    }

    public BigDecimal getScore() {
        return score;
    }

    public WxApp score(BigDecimal score) {
        this.score = score;
        return this;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Long getViews() {
        return views;
    }

    public WxApp views(Long views) {
        this.views = views;
        return this;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public WxApp comments(Set<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public WxApp addComment(Comment comment) {
        this.comments.add(comment);
        comment.setWxapp(this);
        return this;
    }

    public WxApp removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setWxapp(null);
        return this;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public WxApp categories(Set<Category> categories) {
        this.categories = categories;
        return this;
    }

    public WxApp addCategory(Category category) {
        this.categories.add(category);
        category.setWxApp(this);
        return this;
    }

    public WxApp removeCategory(Category category) {
        this.categories.remove(category);
        category.setWxApp(null);
        return this;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public WxApp tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public WxApp addTag(Tag tag) {
        this.tags.add(tag);
        tag.setWxApp(this);
        return this;
    }

    public WxApp removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.setWxApp(null);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public User getUser() {
        return user;
    }

    public WxApp user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WxApp wxApp = (WxApp) o;
        if (wxApp.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, wxApp.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WxApp{" +
            "id=" + id +
            ", icon='" + icon + "'" +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", publicationDate='" + publicationDate + "'" +
            ", screenshot='" + screenshot + "'" +
            ", qcode='" + qcode + "'" +
            ", score='" + score + "'" +
            ", views='" + views + "'" +
            '}';
    }
}
