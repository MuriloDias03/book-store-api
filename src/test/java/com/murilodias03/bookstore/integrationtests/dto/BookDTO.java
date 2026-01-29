package com.murilodias03.bookstore.integrationtests.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@XmlRootElement(name = "Book")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonPropertyOrder({"id", "author", "launchDate", "price", "title"})
public class BookDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @JsonProperty("author")
    @XmlElement(name = "author")
    private String author;

    @JsonProperty("launch_date")
    @XmlElement(name = "launch_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private Date launchDate;

    @JsonProperty("price")
    @XmlElement(name = "price")
    private Double price;

    @JsonProperty("title")
    @XmlElement(name = "title")
    private String title;

    public BookDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Date getLaunchDate() { return launchDate; }
    public void setLaunchDate(Date launchDate) { this.launchDate = launchDate; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDTO bookDTO = (BookDTO) o;
        return Objects.equals(id, bookDTO.id) && Objects.equals(author, bookDTO.author) && Objects.equals(launchDate, bookDTO.launchDate) && Objects.equals(price, bookDTO.price) && Objects.equals(title, bookDTO.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, launchDate, price, title);
    }
}