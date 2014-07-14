package models;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

public class QuipDto {

    @Past
    public Date sourceDate;

    public Integer score;
    
    @Size(min = 5)
    public String content;
    
    public QuipDto() {}

}
