package org.phelim.model;

public class ContentRange {
	private long fullsize;
	private long startPosition;
	private long endPosition;

	public ContentRange(String contentrange) {
		contentrange = contentrange.replace("bytes", "").trim();
		this.fullsize = Long.parseLong(contentrange.substring(
				contentrange.indexOf("/") + 1, contentrange.length()));
		this.startPosition = Long.parseLong(contentrange.substring(0,
				contentrange.indexOf("-")));
		this.endPosition = Long.parseLong(contentrange.substring(
				contentrange.indexOf("-") + 1, contentrange.indexOf("/")));
	}

	public long getFullsize() {
		return fullsize;
	}

	public void setFullsize(long fullsize) {
		this.fullsize = fullsize;
	}

	public long getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(long startPosition) {
		this.startPosition = startPosition;
	}

	public long getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(long endPosition) {
		this.endPosition = endPosition;
	}
	

}
