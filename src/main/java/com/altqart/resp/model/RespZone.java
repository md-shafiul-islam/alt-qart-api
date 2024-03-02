package com.altqart.resp.model;

import java.util.List;

import com.altqart.model.City;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespZone {

	private int id;

	private String name;

	private RespCity city;

	private String value;

	private int pathaoCode;

	private List<RespArea> areas;
}
