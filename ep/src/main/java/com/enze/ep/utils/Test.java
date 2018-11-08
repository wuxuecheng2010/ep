package com.enze.ep.utils;

public class Test {

	public static void main(String[] args) {
		final IdGenerator ID_GENERATOR = IdGenerator.INSTANCE;
		String order_id = ID_GENERATOR.nextId();
		System.out.println(order_id);
	}

}
