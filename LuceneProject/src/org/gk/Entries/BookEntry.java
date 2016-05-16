package org.gk.Entries;

public class BookEntry {
	
	public static final String DBTABLE_STRING="books";
	public static final String NAME_STRING = "bookName";
	
	private String bookName;
	
	public BookEntry(String bookName) {
		super();
		this.bookName = bookName;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	@Override
	public String toString() {
		return "BookEntry [bookName=" + bookName + "]";
	}
	
	
	
}
