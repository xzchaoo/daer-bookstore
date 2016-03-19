package org.xzc.sshb.domain;

import java.io.Serializable;

public class Item implements Serializable {
	private String author;
	private Category category;
	private int id;
	private String imgPath;
	private String longDescription;
	private String name;
	private int page;
	private double price;
	private int remain;
	private String shortDescription;
	private State state;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Item other = (Item) obj;
		if (getId() != other.getId())
			return false;
		return true;
	}

	public String getAuthor() {
		return author;
	}

	public Category getCategory() {
		return category;
	}

	public int getId() {
		return id;
	}

	public String getImgPath() {
		return imgPath;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public String getName() {
		return name;
	}

	public int getPage() {
		return page;
	}

	public double getPrice() {
		return price;
	}

	public int getRemain() {
		return remain;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public State getState() {
		return state;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setRemain(int remain) {
		this.remain = remain;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public void setState(State state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", price=" + price + "]";
	}
}
