package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

@Entity
public class Quip {
    
    @Id
    @GenericGenerator(name="incrementId",strategy="increment") 
	@GeneratedValue(generator="incrementId")
    public Long id;

    public Integer score;

    public String source;

    public Date sourceDate;
    
    public Date postedAt;
    
    @Column(length = 10000) //init with VARCHAR(1000)
    public String content;
    
    @ElementCollection(fetch=FetchType.EAGER)
    public List<Long> authorIds;
    
    public Quip() {}
    
    public Quip(User author, Date sourceDate, String content) {
        this.authorIds = Lists.newArrayList(author.id);
        this.sourceDate = sourceDate;
        this.content = content;
        this.postedAt = new Date();
    }
 
}