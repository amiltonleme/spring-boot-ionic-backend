package com.amiltonleme.cursomc.resources.exception;

import java.io.Serializable;

public class FieldMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String fieldname;
	private String fieldMessage;
	
	public FieldMessage () {
	}

	public FieldMessage(String fieldname, String fieldMessage) {
		super();
		this.fieldname = fieldname;
		this.fieldMessage = fieldMessage;
	}

	public String getFieldname() {
		return fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public String getFieldMessage() {
		return fieldMessage;
	}

	public void setFieldMessage(String fieldMessage) {
		this.fieldMessage = fieldMessage;
	}
}
