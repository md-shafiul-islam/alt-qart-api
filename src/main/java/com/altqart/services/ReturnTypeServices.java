package com.altqart.services;

import java.util.List;

import com.altqart.model.ReturnType;
import com.altqart.resp.model.RespReturnType;

public interface ReturnTypeServices {

	public List<RespReturnType> getAllReturnType();

	public ReturnType getReturnTypeById(int returnType);

}
