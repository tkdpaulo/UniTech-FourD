package com.unitechfourd.models;
import lombok.Data;
import java.util.Date;

@Data
public class Lesson {
    private String id;
    private String title;
    private String description;
    private Date expectedDate;
    private String teacherId;
}