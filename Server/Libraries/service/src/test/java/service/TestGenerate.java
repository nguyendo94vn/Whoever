package service;

import java.util.Date;

import v.whoever.service.impl.GenerateIdImpl;
import vn.whoever.service.GenerateId;

public class TestGenerate {

	public static void main(String[] args) {
		GenerateId code = new GenerateIdImpl();
		System.out.println((new Date()).getTime());
		for(int i = 0 ; i < 1; ++i) {
			System.out.println(code.getIdUser());
			System.out.println(code.getIdStatus());
			System.out.println(code.getIdprofile());
			System.out.println(code.getIdPhoto());
			System.out.println(code.getIdContact());
			System.out.println(code.getIdGroup());
			System.out.println(code.getIdMessage());
			System.out.println(code.getIdNotify());
			System.out.println(code.getIdComment());
		}
		System.out.println((new Date()).getTime());
	}	
}
