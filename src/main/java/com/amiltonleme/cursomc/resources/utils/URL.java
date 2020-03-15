package com.amiltonleme.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {
	
	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	
	public static List<Integer> decodeIntList(String s){
		String[] vet = s.split(",");
		List<Integer> list = new ArrayList<>();
		for (int i=0; i<vet.length; i++) {
			list.add(Integer.parseInt(vet[i]));
		}
		return list;
		//Esse método acima pode ser feito do jeito abaixo, com lâmbda
		//Arrays.asList converte o vetor s.split(",") para lista
		//map -- para cada elemento x na lista, coloca um parseInt no x
		//return Arrays.asList(s.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
	}

}
