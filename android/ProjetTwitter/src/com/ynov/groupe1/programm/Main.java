package com.ynov.groupe1.programm;

import com.ynov.groupe1.gui.Home;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpException;
import org.apache.http.ParseException;
import org.json.JSONException;

public class Main {
	public static void main(String[] args) throws JSONException, IOException, KeyManagementException, ParseException, NoSuchAlgorithmException, HttpException {

	   new Home().setVisible(true);
	
	}
}
